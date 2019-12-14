package com.monkeydp.tools.util

import com.monkeydp.tools.ext.java.hasSuperclass
import com.monkeydp.tools.ext.kotlin.classX
import java.lang.reflect.Field

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
     * Whether to force access not public field
     */
    private const val DEFAULT_FORCE_ACCESS = false
    
    /**
     * Whether to override same name field in superclass
     *
     * true: the field in current class will override same name field in superclass
     */
    private const val DEFAULT_OVERRIDE_SAME_NAME_FIELD = true;
    
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
            override: Boolean = DEFAULT_OVERRIDE_SAME_NAME_FIELD
    ): List<Field> {
        
        val clazz = any.classX
        val fields = getDeclaredFields(clazz).toMutableList()
        val fieldNames = mutableListOf<String>()
        
        fields.forEach { field -> fieldNames.add(field.name) }
        
        if (clazz.hasSuperclass(except = Object::class.java)) {
            getFields(clazz.superclass, override).apply {
                if (override)
                    forEach {
                        if (fieldNames.contains(it.name)) return@forEach
                        fields.add(it)
                        fieldNames.add(it.name)
                    }
                else fields.addAll(this)
            }
        }
        return fields
    }
    
    /**
     * Get field value
     */
    fun <T> getValue(
            any: Any,
            field: Field,
            forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): T = field.run {
        if (forceAccess) isAccessible = true
        get(any) as T
    }
    
    /**
     * Get field value
     */
    fun <T> getValue(
            any: Any,
            fieldName: String,
            forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): T = getField(any, fieldName).run { getValue(any, this, forceAccess) }
    
    /**
     * Get field value or null
     */
    fun <T> getValueOrNull(
            any: Any,
            field: Field,
            forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): T? =
            try {
                getValue(any, field, forceAccess)
            } catch (e: Exception) {
                null
            }
    
    /**
     * Get field value or null
     */
    fun <T> getValueOrNull(
            any: Any,
            fieldName: String,
            forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): T? = getValueOrNull(any, getField(any, fieldName), forceAccess)
    
    /**
     * Get field values
     */
    fun <T> getValues(
            any: Any,
            forceAccess: Boolean = DEFAULT_FORCE_ACCESS,
            override: Boolean = DEFAULT_OVERRIDE_SAME_NAME_FIELD
    ): List<T> {
        val list = mutableListOf<T>()
        getFields(any, override = override).forEach {
            getValue<T>(any, it, forceAccess).apply { list.add(this) }
        }
        return list.toList()
    }
    
    /**
     * Get nullable field values
     */
    fun <T> getNullableValues(
            any: Any,
            forceAccess: Boolean = DEFAULT_FORCE_ACCESS,
            override: Boolean = DEFAULT_OVERRIDE_SAME_NAME_FIELD
    ): List<T?> {
        val list = mutableListOf<T?>()
        getFields(any, override = override).forEach {
            getValueOrNull<T>(any, it, forceAccess).apply { list.add(this) }
        }
        return list.toList()
    }
    
    /**
     * Set field value
     */
    fun setValue(
            any: Any,
            fieldName: String,
            value: Any,
            forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ) = setValue(any, getField(any, fieldName), value, forceAccess)
    
    /**
     * Set field value
     */
    fun setValue(
            any: Any,
            field: Field,
            value: Any,
            forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ) = setNullableValue(any, field, value, forceAccess)
    
    /**
     * Set nullable field value
     */
    fun setNullableValue(
            any: Any,
            fieldName: String,
            value: Any?,
            forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ) = setNullableValue(any, getField(any, fieldName), value, forceAccess)
    
    /**
     * Set nullable field value
     */
    fun setNullableValue(
            any: Any,
            field: Field,
            value: Any?,
            forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): Unit = field.run {
        if (forceAccess) isAccessible = true
        set(any, value)
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
    fun getDeclaredFields(any: Any): List<Field> = any.classX.declaredFields.toList()
    
    /**
     * Get declared field value
     */
    fun <T> getDeclaredValue(
            any: Any,
            fieldName: String,
            forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): T = getDeclaredField(any, fieldName).run {
        getValue(any, this, forceAccess)
    }
    
    /**
     * Get declared field value or null
     */
    fun <T> getDeclaredValueOrNull(
            any: Any,
            fieldName: String,
            forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): T? = getDeclaredFieldOrNull(any, fieldName)?.run {
        getValueOrNull(any, this, forceAccess)
    }
    
    /**
     * Get declared field values
     */
    fun <T> getDeclaredValues(
            any: Any,
            forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): List<T> = getDeclaredFields(any).map { getValue<T>(any, it, forceAccess) }
    
    /**
     * Get declared nullable field values
     */
    fun <T> getDeclaredNullableValues(
            any: Any,
            forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): List<T?> = getDeclaredFields(any).map { getValueOrNull<T>(any, it, forceAccess) }
    
    /**
     * Set declared field value
     */
    fun setDeclaredValue(
            any: Any,
            fieldName: String,
            value: Any,
            forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ) = setDeclaredNullableValue(any, fieldName, value, forceAccess)
    
    /**
     * Set declared nullable field value
     */
    fun setDeclaredNullableValue(
            any: Any,
            fieldName: String,
            value: Any?,
            forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): Unit = getDeclaredField(any, fieldName).run {
        setNullableValue(any, this, value, forceAccess)
    }
}