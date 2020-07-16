@file:Suppress("UNCHECKED_CAST")

package com.monkeydp.tools.ext.kotlin

import com.fasterxml.jackson.module.kotlin.convertValue
import com.monkeydp.tools.exception.ierror
import com.monkeydp.tools.ext.kotlin.KPropertyFilter.FilterConfig
import com.monkeydp.tools.ext.reflections.reflections
import com.monkeydp.tools.global.objectMapper
import com.monkeydp.tools.util.FieldUtil
import com.monkeydp.tools.util.FieldUtil.GetValueConfig
import com.monkeydp.tools.util.FieldUtil.SetValueConfig
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.*
import kotlin.reflect.jvm.javaField

/**
 * @author iPotato
 * @date 2019/10/29
 */
// ==== Class X ====

val <T : Any> T.classX: Class<T>
    get() =
        when (this) {
            is Class<*> -> this
            is KClass<*> -> this.java
            else -> this.javaClass
        } as Class<T>

val <T : Any> T.kClassX: KClass<T>
    get() = classX.kotlin


// ==== Properties ====

fun Any.toProperties(): Properties {
    val properties = Properties()
    this.javaClass.kotlin.memberProperties.forEach { properties[it.name] = it.get(this) }
    return properties
}

fun Any.toDeclaredProperties(): Properties {
    val properties = Properties()
    this.javaClass.kotlin.declaredMemberProperties.forEach { properties[it.name] = it.get(this) }
    return properties
}


// ==== Props ====
private fun <T : Any> T.filterProps(
        props: Iterable<KProperty1<T, *>>,
        config: (FilterConfig.() -> Unit)? = null
): List<KProperty1<T, *>> = KProperty1Filter.filterProps(this, props, config)

fun <T : Any> T.toProps(
        config: (FilterConfig.() -> Unit)? = null
): List<KProperty1<T, *>> = javaClass.kotlin.memberProperties.run { filterProps(this, config) }

inline fun <T : Any, reified R> T.toPropsX(
        noinline config: (FilterConfig.() -> Unit)? = null
): List<KProperty1<T, R>> = toProps(config).filterValueType<T, R>()

fun <T : Any> T.toDeclaredProps(
        config: (FilterConfig.() -> Unit)? = null
): List<KProperty1<T, *>> = javaClass.kotlin.declaredMemberProperties.run { filterProps(this, config) }

inline fun <T : Any, reified R> T.toDeclaredPropsX(
        noinline config: (FilterConfig.() -> Unit)? = null
): List<KProperty1<T, R>> = toDeclaredProps(config).filterValueType<T, R>()


// ==== Prop Values ====

fun Any.toPropValues(
        config: (FilterConfig.() -> Unit)? = null
): List<Any?> = toProps(config).map { it.get(this) }

fun <V : Any> Any.toPropValues(
        kClass: KClass<V>,
        config: (FilterConfig.() -> Unit)? = null
): List<V> = toPropValues(config).filterIsInstance(kClass)

inline fun <reified V> Any.toPropValuesX(
        noinline config: (FilterConfig.() -> Unit)? = null
): List<V> = toPropValues(config).filterIsInstance<V>()

fun Any.toDeclaredPropValues(
        config: (FilterConfig.() -> Unit)? = null
): List<Any?> = toDeclaredProps(config).map { it.get(this) }

fun <V : Any> Any.toDeclaredPropValues(
        kClass: KClass<V>,
        config: (FilterConfig.() -> Unit)? = null
): List<V> = toDeclaredPropValues(config).filterIsInstance(kClass)

inline fun <reified V> Any.toDeclaredPropValuesX(
        noinline config: (FilterConfig.() -> Unit)? = null
): List<V> = toDeclaredPropValues(config).filterIsInstance<V>()


// ==== Prop Map ====

inline fun <K, reified V> Any.toPropMap(
        key: (KProperty1<Any, V>) -> K = { it as K },
        noinline config: (FilterConfig.() -> Unit)? = null
): Map<K, V> = toPropsX<Any, V>(config).map { key(it) to it.get(this) }.toMap()

inline fun <reified K, reified V> Any.buildPropMap(props: Iterable<KProperty1<Any, *>>): Map<K, V> =
        K::class.let { kClass ->
            when {
                kClass.isSubclassOf(String::class) -> props.map { it.name to it.get(this) }.toMap() as Map<K, V>
                kClass.isSubclassOf(KProperty1::class) -> props.map { it to it.get(this) }.toMap() as Map<K, V>
                else -> ierror("Unsupported type: $kClass!")
            }
        }

inline fun <reified K, reified V> Any.toPropMapX(
        noinline config: (FilterConfig.() -> Unit)? = null
): Map<K, V> = toPropsX<Any, V>(config).run(::buildPropMap)

inline fun <K, reified V> Any.toDeclaredPropMap(
        key: (KProperty1<Any, V>) -> K = { it as K },
        noinline config: (FilterConfig.() -> Unit)? = null
): Map<K, V> = toDeclaredPropsX<Any, V>(config).map { key(it) to it.get(this) }.toMap()

inline fun <reified K, reified V> Any.toDeclaredPropMapX(
        noinline config: (FilterConfig.() -> Unit)? = null
): Map<K, V> = toDeclaredPropsX<Any, V>(config).run(::buildPropMap)


// ==== Copy Prop Values ====

fun <T : Any, S : Any> T.copyPropValuesFrom(
        source: S,
        vararg props: KProperty1<S, *>,
        config: (FilterConfig.() -> Unit)? = null
) {
    val sourceProps =
            (if (props.isEmpty()) source::class.memberProperties.toList() else props.toList())
                    .let {
                        KProperty1Filter.filterProps(source, it, config)
                    }

    this::class.memberProperties.filterIsInstance<KMutableProperty<*>>().also { mutableProps ->
        mutableProps.forEach { targetProp ->
            sourceProps.find { sourceProp ->
                sourceProp.name == targetProp.name &&
                        targetProp.returnType.isSupertypeOf(sourceProp.returnType)
            }?.let {
                targetProp.setter.call(this, it.getter.call(source))
            }
        }
    }
}

fun <T : Any> T.copyPropValuesFrom(map: Map<String, Any?>) {
    this::class.memberProperties.filterIsInstance<KMutableProperty<*>>().also { mutableProps ->
        mutableProps.forEach { targetProp ->
            val key = targetProp.name
            if (!map.containsKey(key)) return@forEach
            val anyOrNull = map[key]
            if (anyOrNull != null && !targetProp.returnType.isSupertypeOf(anyOrNull::class.createType())) return@forEach
            targetProp.setter.call(this, anyOrNull)
        }
    }
}

// ==== Compare Prop Values ====

/**
 * Example:
 *  object Dog {
 *      val name = "Tom"
 *      val age = 5
 *      val color = "Grey"
 *  }

 *  object Cat {
 *      val age = 5
 *      val color = "Grey"
 *  }

 *  object Mouse {
 *      val name = "Bob"
 *      val age = 5
 *      val color = "Grey"
 *  }
 *
 *  Dog has the same `age` and `color` as Cat, Dog.sameNamePropsEqual(Cat) return true
 *  Cat does't have `name`, Cat.sameNamePropsEqual(Dog) return false
 *  Dog has the diff `name` as Mouse, Dog.sameNamePropsEqual(Mouse) return false, vice versa
 */
fun Any.sameNamePropsEqual(any: Any): Boolean {
    val propMap = toPropMapX<String, Any?>()
    any.toPropMapX<String, Any?>().forEach {
        if (!propMap.containsKey(it.key)) return false
        if (propMap.getValue(it.key) != it.value) return false
    }
    return true
}

// ==== Field ====

fun Any.getField(fieldName: String): Field =
        FieldUtil.getField(this, fieldName)

fun Any.getFields(): List<Field> =
        FieldUtil.getFields(this)

fun <T> Any.getFieldValue(
        fieldName: String,
        configInit: (GetValueConfig.() -> Unit)? = null
) = FieldUtil.getValue<T>(this, fieldName, configInit)

fun <T> Any.getFieldValue(
        field: Field,
        configInit: (GetValueConfig.() -> Unit)? = null
) = FieldUtil.getValue<T>(this, field, configInit)

fun Any.setFieldValue(
        fieldName: String,
        value: Any,
        configInit: (SetValueConfig.() -> Unit)? = null
) = FieldUtil.setValue(this, fieldName, value, configInit)

@Suppress("UNCHECKED_CAST")
fun <T> Any.getFieldValueByPath(
        path: String,
        configInit: (GetValueConfig.() -> Unit)? = null
): T {
    val parts = path.split(".")
    if (parts.isEmpty()) return this as T
    else if (parts.size == 1) return getFieldValue(path, configInit)

    var value: Any = this
    parts.forEach {
        value = value.getFieldValue<Any>(it, configInit)
    }
    return value as T
}

fun Any.setFieldValueByPath(
        path: String,
        value: Any,
        configInit: (SetValueConfig.() -> Unit)? = null
) {
    val parts = path.split(".")
    if (parts.size == 1) {
        setFieldValue(path, value, configInit)
        return
    }
    val pathWithoutLast = parts.toMutableList().removeLast().joinToString(".")
    val any = getFieldValueByPath<Any>(pathWithoutLast) { forceAccess = true }
    any.setFieldValue(parts.last(), value, configInit)
}

fun Any.getAnnotatedField(annotClass: KClass<out Annotation>) =
        kClassX.getAnnotatedField(annotClass)

inline fun <reified A : Annotation> Any.getAnnotatedField() =
        getAnnotatedField(A::class)

fun Any.getAnnotatedFields(annotClass: KClass<out Annotation>) =
        kClassX.getAnnotatedFields(annotClass)

inline fun <reified A : Annotation> Any.getAnnotatedFields() =
        getAnnotatedFields(A::class)

// ==== Copy Field Values ====

fun <T : Any, S : Any> T.copyFieldValuesFromX(
        source: S,
        vararg ignoreProps: KProperty<T>,
        configInit: (SetValueConfig.() -> Unit)? = null
) = copyFieldValuesFrom(source, *ignoreProps.map { it.javaField!! }.toTypedArray(), configInit = configInit)

fun <T : Any, S : Any> T.copyFieldValuesFrom(
        source: S,
        vararg ignoreFields: Field,
        configInit: (SetValueConfig.() -> Unit)? = null
): Unit =
        FieldUtil.getFields(source).forEach { sourceField ->
            val field = FieldUtil.getField(this, sourceField.name)
            if (ignoreFields.contains(field)) return
            val sourceValue = sourceField.get(source)
            if (sourceField.type.kotlin.isSubclassOf(field.type.kotlin))
                FieldUtil.setValue(this, field, sourceValue, configInit = configInit)
        }

fun <T : Any, R : Any> T.copyFieldValuesFrom(
        vararg pairs: Pair<KProperty1<T, R>, R>,
        configInit: (SetValueConfig.() -> Unit)? = null
): Unit =
        pairs.forEach { FieldUtil.setValue(this, it.first.name, it.second, configInit = configInit) }


// ==== Json ====

fun <T : Any> T.toJson() =
        objectMapper.writeValueAsString(this)

inline fun <reified T> Any.convertValue() =
        objectMapper.convertValue<T>(this)

fun <T> Any.convertValue(clazz: Class<T>) =
        objectMapper.convertValue(this, clazz)

fun <T : Any> Any.convertValue(kClass: KClass<T>) =
        objectMapper.convertValue(this, kClass.java)

// ==== Reflections ====

fun <T : Any> T.getReflections() =
        reflections(this::class)

fun <T : Any> T.getPropValue(propName: String) =
        this::class.memberProperties.first { it.name == propName }.getter.call(this)

fun <T : Any, U : Any> T.getPropValueX(propName: String) =
        getPropValue(propName) as U


// ==== Generic ====

fun Any.getActualTypeInSuperclass(index: Int = 0) =
        (javaClass.genericSuperclass as? ParameterizedType ?: ierror("Generic superclass must be parameterized type!"))
                .actualTypeArguments[index]

fun <T> Any.getActualTypeInSuperclassX(index: Int = 0) =
        getActualTypeInSuperclass(index) as T
