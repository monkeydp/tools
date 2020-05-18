package com.monkeydp.tools.util

import com.monkeydp.tools.ext.java.hasSuperclass
import com.monkeydp.tools.ext.kotlin.classX
import java.lang.reflect.Field
import kotlin.reflect.KProperty1

/**
 * !!!! Using properties first, reflection destroys encapsulation !!!!
 *
 * Declared: only include current class, exclude superclass
 *
 * @author iPotato
 * @date 2019/10/15
 */
@Suppress("UNCHECKED_CAST")
object FieldUtil {

    /**
     * Get field
     */
    fun getField(any: Any, fieldName: String): Field = getFieldOrNull(any, fieldName) ?: throw NoSuchFieldException()

    /**
     * Get field or null
     */
    tailrec fun getFieldOrNull(any: Any, fieldName: String): Field? {
        val clazz = any.classX
        var fieldOrNull = getDeclaredFieldOrNull(clazz, fieldName)
        if (fieldOrNull != null) return fieldOrNull

        val superclass = clazz.superclass
        return if (clazz.hasSuperclass(except = Object::class.java))
            getFieldOrNull(superclass, fieldName)
        else null
    }

    /**
     * Raw method to get field
     *
     * Can only get "public static field"
     *
     * @return
     */
    fun rawGetField(any: Any, fieldName: String): Field = any.classX.getField(fieldName)

    /**
     * Raw method to get field or null
     */
    fun rawGetFieldOrNull(any: Any, fieldName: String) =
            try {
                any.classX.getField(fieldName)
            } catch (e: NoSuchFieldException) {
                null
            }

    /**
     * Get fields
     */
    fun getFields(
            any: Any,
            configInit: (GetFieldsConfig.() -> Unit)? = null
    ): List<Field> =
            GetFieldsConfig().run {
                if (configInit != null) configInit(this)
                getFields(any, this)
            }

    /**
     * Get fields
     */
    private fun getFields(
            any: Any,
            config: GetFieldsConfig
    ): List<Field> =
            config.run {
                val clazz = any.classX
                val fields = getDeclaredFields(clazz).toMutableList()
                val fieldNames = mutableListOf<String>()

                fields.forEach { field -> fieldNames.add(field.name) }

                if (clazz.hasSuperclass(except = Object::class.java)) {
                    getFields(clazz.superclass, config).apply {
                        if (overrideSameNameField)
                            forEach {
                                if (fieldNames.contains(it.name)) return@forEach
                                fields.add(it)
                                fieldNames.add(it.name)
                            }
                        else fields.addAll(this)
                    }
                }
                fields
            }

    /**
     * Get field value
     */
    fun <T> getValue(
            any: Any,
            field: Field,
            configInit: (GetValueConfig.() -> Unit)? = null
    ): T =
            GetValueConfig().run {
                if (configInit != null) configInit(this)
                if (forceAccess) field.isAccessible = true
                field.get(any) as T
            }

    /**
     * Get field value
     */
    fun <T> getValue(
            any: Any,
            fieldName: String,
            configInit: (GetValueConfig.() -> Unit)? = null
    ): T = getField(any, fieldName).run { getValue(any, this, configInit) }

    /**
     * Get field value
     */
    fun <T, R> getValue(
            any: Any,
            prop: KProperty1<T, R>,
            configInit: (GetValueConfig.() -> Unit)? = null
    ): R = getValue(any, prop.name, configInit)

    /**
     * Get field value or null
     */
    fun <T> getValueOrNull(
            any: Any,
            field: Field,
            configInit: (GetValueConfig.() -> Unit)? = null
    ): T? =
            try {
                getValue(any, field, configInit)
            } catch (e: Exception) {
                null
            }

    /**
     * Get field value or null
     */
    fun <T> getValueOrNull(
            any: Any,
            fieldName: String,
            configInit: (GetValueConfig.() -> Unit)? = null
    ): T? = getValueOrNull(any, getField(any, fieldName), configInit)

    /**
     * Get field values
     */
    fun <T> getValues(
            any: Any,
            configInit: (GetValuesConfig.() -> Unit)? = null
    ): List<T> =
            GetValuesConfig().let { config ->
                if (configInit != null) configInit(config)
                val list = mutableListOf<T>()
                getFields(any) {
                    overrideSameNameField = config.overrideSameNameField
                }.forEach {
                    getValue<T>(any, it) {
                        forceAccess = config.forceAccess
                    }.apply { list.add(this) }
                }
                list.toList()
            }

    /**
     * Get nullable field values
     */
    fun <T> getNullableValues(
            any: Any,
            configInit: (GetValuesConfig.() -> Unit)? = null
    ): List<T?> =
            GetValuesConfig().let { config ->
                if (configInit != null) configInit(config)
                val list = mutableListOf<T?>()
                getFields(any) {
                    overrideSameNameField = config.overrideSameNameField
                }.forEach {
                    getValueOrNull<T>(any, it) {
                        forceAccess = config.forceAccess
                    }.apply { list.add(this) }
                }
                list.toList()
            }

    /**
     * Set field value
     */
    fun setValue(
            any: Any,
            fieldName: String,
            value: Any,
            configInit: (SetValueConfig.() -> Unit)? = null
    ) = setValue(any, getField(any, fieldName), value, configInit)

    /**
     * Set field value
     */
    fun setValue(
            any: Any,
            field: Field,
            value: Any,
            configInit: (SetValueConfig.() -> Unit)? = null
    ) = setNullableValue(any, field, value, configInit)

    /**
     * Set nullable field value
     */
    fun setNullableValue(
            any: Any,
            fieldName: String,
            value: Any?,
            configInit: (SetValueConfig.() -> Unit)? = null
    ) = setNullableValue(any, getField(any, fieldName), value, configInit)

    /**
     * Set nullable field value
     */
    fun setNullableValue(
            any: Any,
            field: Field,
            value: Any?,
            configInit: (SetValueConfig.() -> Unit)? = null
    ): Unit =
            SetValueConfig().run {
                if (configInit != null) configInit(this)
                if (forceAccess) field.isAccessible = true
                field.set(any, value)
            }

    /**
     * Get declared field
     */
    fun getDeclaredField(any: Any, fieldName: String): Field = any.classX.getDeclaredField(fieldName)

    /**
     * Get declared field or null
     */
    fun getDeclaredFieldOrNull(any: Any, fieldName: String
    ) = try {
        getDeclaredField(any, fieldName)
    } catch (e: NoSuchFieldException) {
        null
    }

    /**
     * Get declared fields
     */
    fun getDeclaredFields(any: Any): List<Field> =
            any.classX.declaredFields.filter { it.name != "Companion" }.toList()

    /**
     * Get declared field value
     */
    fun <T> getDeclaredValue(
            any: Any,
            fieldName: String,
            configInit: (GetValueConfig.() -> Unit)? = null
    ): T =
            getDeclaredField(any, fieldName).run {
                getValue(any, this, configInit)
            }

    /**
     * Get declared field value or null
     */
    fun <T> getDeclaredValueOrNull(
            any: Any,
            fieldName: String,
            configInit: (GetValueConfig.() -> Unit)? = null
    ): T? =
            getDeclaredFieldOrNull(any, fieldName)?.run {
                getValueOrNull(any, this, configInit)
            }

    /**
     * Get declared field values
     */
    fun <T> getDeclaredValues(
            any: Any,
            configInit: (GetValueConfig.() -> Unit)? = null
    ): List<T> = getDeclaredFields(any).map { getValue<T>(any, it, configInit) }

    /**
     * Get declared nullable field values
     */
    fun <T> getDeclaredNullableValues(
            any: Any,
            configInit: (GetValueConfig.() -> Unit)? = null
    ): List<T?> = getDeclaredFields(any).map { getValueOrNull<T>(any, it, configInit) }

    /**
     * Set declared field value
     */
    fun setDeclaredValue(
            any: Any,
            fieldName: String,
            value: Any,
            configInit: (SetValueConfig.() -> Unit)? = null
    ) = setDeclaredNullableValue(any, fieldName, value, configInit)

    /**
     * Set declared nullable field value
     */
    fun setDeclaredNullableValue(
            any: Any,
            fieldName: String,
            value: Any?,
            configInit: (SetValueConfig.() -> Unit)? = null
    ): Unit = getDeclaredField(any, fieldName).run { setNullableValue(any, this, value, configInit) }


    data class GetFieldsConfig(
            /**
             * Whether to override same name field in superclass
             *
             * true: the field in current class will override same name field in superclass
             */
            var overrideSameNameField: Boolean = true
    )

    data class GetValueConfig(
            /**
             * Whether to force access non-public field
             */
            var forceAccess: Boolean = false
    )

    data class GetValuesConfig(
            /**
             * Whether to force access non-public field
             */
            var forceAccess: Boolean = false,
            /**
             * Whether to override same name field in superclass
             *
             * true: the field in current class will override same name field in superclass
             */
            var overrideSameNameField: Boolean = true
    )

    data class SetValueConfig(
            /**
             * Whether to force access non-public field
             */
            var forceAccess: Boolean = false
    )
}