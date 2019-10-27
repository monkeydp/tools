package com.monkeydp.tools.util

import java.lang.reflect.Field

/**
 * Declared: only include current class, exclude superclass
 *
 * @author iPotato
 * @date 2019/10/15
 */
@Suppress("UNCHECKED_CAST")
object FieldUtil {
    
    /**
     * Whether to ignore the exception when the field not found
     */
    private const val DEFAULT_IGNORE_NOT_FOUND = false
    
    // ==== Get field ====
    
    fun getField(any: Any, fieldName: String) = getField(any.javaClass, fieldName, false)!!
    
    fun getField(any: Any, fieldName: String,
                 ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND): Field? {
        return getField(any.javaClass, fieldName, ignoreNotFound)
    }
    
    fun getField(clazz: Class<*>, fieldName: String) = getField(clazz, fieldName, false)!!
    
    fun getField(clazz: Class<*>, fieldName: String,
                 ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND): Field? {
        var field: Field? = null
        try {
            // get field from current class
            field = getDeclaredField(clazz, fieldName, false)
        } catch (e: NoSuchFieldException) {
            // ignore
        }
        
        if (field != null) return field
        
        val superclass = clazz.superclass
        if (null != superclass && !Class::class.java.isAssignableFrom(superclass))
            return getField(superclass, fieldName, ignoreNotFound)
    
        if (!ignoreNotFound && null == superclass) throw NoSuchFieldException()
        
        return null
    }
    
    fun rawGetField(clazz: Class<*>, fieldName: String) = rawGetField(clazz, fieldName, false)!!
    
    /**
     * Raw method to get field
     *
     * Can only use to get public static field
     *
     * @return
     */
    fun rawGetField(clazz: Class<*>, fieldName: String,
                    ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND): Field? {
        return try {
            clazz.getField(fieldName)
        } catch (e: NoSuchFieldException) {
            if (!ignoreNotFound) throw e
            null
        }
    }
    
    
    // ==== Get fields ====
    // ==== The field in current class overrides same name field in superclass ====
    
    fun getFields(any: Any): List<Field> {
        return getFields(any.javaClass)
    }
    
    fun getFields(clazz: Class<*>): List<Field> {
        
        val fields = getDeclaredFields(clazz).toMutableList()
        val fieldNames: MutableList<String> = mutableListOf()
        
        fields.forEach { field -> fieldNames.add(field.name) }
        
        val superclass = clazz.superclass
        if (null != superclass && !Class::class.java.isAssignableFrom(superclass)) {
            val superFields = getFields(superclass)
            superFields.forEach { superField ->
                if (fieldNames.contains(superField.name)) return@forEach
                fields.add(superField)
                fieldNames.add(superField.name)
            }
        }
        
        return fields
    }
    
    // ==== Get field value ====
    
    fun <T> getValue(any: Any, fieldName: String,
                     ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND): T? {
        val field = getField(any, fieldName, ignoreNotFound) ?: return null
        return getValue<T>(any, field)
    }
    
    fun <T> getValue(any: Any, field: Field): T? {
        field.isAccessible = true
        return field.get(any) as T
    }
    
    fun <T> getNotnullValue(any: Any, fieldName: String): T = getValue<T>(any, fieldName, false)!!
    
    fun <T> getNotnullValue(any: Any, field: Field): T = getValue<T>(any, field)!!
    
    // ==== Get fields values ====
    
    fun <T> getValues(any: Any): List<T?> {
        val list = mutableListOf<T?>()
        getFields(any).forEach { list.add(getValue(any, it)) }
        return list.toList()
    }
    
    fun <T> getNotnullValues(any: Any): List<T> {
        val list = mutableListOf<T>()
        getFields(any).forEach { list.add(getNotnullValue(any, it)) }
        return list.toList()
    }
    
    fun <T> getDeclaredValues(any: Any): List<T?> {
        val list = mutableListOf<T?>()
        getDeclaredFields(any).forEach { list.add(getValue(any, it)) }
        return list.toList()
    }
    
    fun <T> getDeclaredNotnullValues(any: Any): List<T> {
        val list = mutableListOf<T>()
        getDeclaredFields(any).forEach { list.add(getNotnullValue(any, it)) }
        return list.toList()
    }
    
    // ==== Set field value ====
    
    fun setValue(any: Any, fieldName: String, value: Any?,
                 ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND) {
        val field = getField(any, fieldName, ignoreNotFound) ?: return
        setValue(any, field, value)
    }
    
    fun setValue(any: Any, field: Field, value: Any?) {
        field.isAccessible = true
        field.set(any, value)
    }
    
    fun setNotnullValue(any: Any, fieldName: String, value: Any,
                        ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND) =
            setValue(any, fieldName, value, ignoreNotFound)
    
    fun setNotnullValue(any: Any, field: Field, value: Any) = setValue(any, field, value)
    
    // ==== Get declared field ====
    
    fun getDeclaredField(any: Any, fieldName: String): Field = getDeclaredField(any, fieldName, false)!!
    
    fun getDeclaredField(any: Any, fieldName: String,
                         ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND): Field? {
        return getDeclaredField(any.javaClass, fieldName, ignoreNotFound)
    }
    
    fun getDeclaredField(clazz: Class<*>, fieldName: String) = getDeclaredField(clazz, fieldName, false)!!
    
    fun getDeclaredField(clazz: Class<*>, fieldName: String,
                         ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND): Field? {
        return try {
            return clazz.getDeclaredField(fieldName)
        } catch (e: NoSuchFieldException) {
            if (!ignoreNotFound) throw e
            null
        }
    }
    
    // ==== Get declared fields====
    
    fun getDeclaredFields(any: Any): List<Field> {
        return getDeclaredFields(any.javaClass)
    }
    
    fun getDeclaredFields(clazz: Class<*>): List<Field> {
        val fields = rawGetDeclaredFields(clazz)
        return listOf(*fields)
    }
    
    private fun rawGetDeclaredFields(clazz: Class<*>): Array<Field> {
        return clazz.declaredFields
    }
    
    // ==== Get declared field value ====
    
    fun <T> getDeclaredValue(any: Any, fieldName: String,
                             ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND): T? {
        val field = getDeclaredField(any, fieldName, ignoreNotFound) ?: return null
        return getValue<Any>(any, field) as T?
    }
    
    fun <T> getDeclaredNotnullValue(any: Any, fieldName: String) = getDeclaredValue<T>(any, fieldName, false)!!
    
    // ==== Set declared field value ====
    
    fun setDeclaredValue(any: Any, fieldName: String, value: Any?,
                         ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND) {
        val field = getDeclaredField(any, fieldName, ignoreNotFound) ?: return
        setDeclaredValue(any, field, value)
    }
    
    fun setDeclaredValue(any: Any, field: Field, value: Any?) {
        field.isAccessible = true
        field.set(any, value)
    }
    
    fun setDeclaredNotnullValue(any: Any, fieldName: String, value: Any) = setDeclaredValue(any, fieldName, value)
    
    fun setDeclaredNotnullValue(any: Any, field: Field, value: Any) = setDeclaredValue(any, field, value)
}