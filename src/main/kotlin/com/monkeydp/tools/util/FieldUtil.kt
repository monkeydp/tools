package com.monkeydp.tools.util

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
     * Whether to ignore the exception when the field not found
     */
    private const val DEFAULT_IGNORE_NOT_FOUND = false
    /**
     * Whether to force access not public field
     */
    private const val DEFAULT_FORCE_ACCESS = false
    
    // ==== Get field ====
    
    fun getField(any: Any, fieldName: String) = getField(any.javaClass, fieldName, false)!!
    
    fun getField(any: Any, fieldName: String,
                 ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND
    ) = getField(any.javaClass, fieldName, ignoreNotFound)
    
    fun getField(clazz: Class<*>, fieldName: String) = getField(clazz, fieldName, false)!!
    
    fun getField(clazz: Class<*>, fieldName: String,
                 ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND
    ): Field? {
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
                    ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND
    ) = try {
        clazz.getField(fieldName)
    } catch (e: NoSuchFieldException) {
        if (!ignoreNotFound) throw e
        null
    }
    
    // ==== Get fields ====
    // ==== The field in current class overrides same name field in superclass ====
    
    fun getFields(any: Any) = getFields(any.javaClass)
    
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
    
    fun <T> getValueOrNull(any: Any, fieldName: String,
                           ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND,
                           forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): T? {
        val field = getField(any, fieldName, ignoreNotFound) ?: return null
        return getValueOrNull<T>(any, field, forceAccess)
    }
    
    fun <T> getValueOrNull(any: Any, field: Field,
                           forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): T? {
        if (forceAccess) field.isAccessible = true
        return field.get(any) as T
    }
    
    fun <T> getValueOrNull(clazz: Class<*>, fieldName: String,
                           ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND,
                           forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): T? {
        val field = getField(clazz, fieldName, ignoreNotFound) ?: return null
        return getValueOrNull(clazz, field, forceAccess)
    }
    
    fun <T> getValueOrNull(clazz: Class<*>, field: Field,
                           forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): T? {
        if (forceAccess) field.isAccessible = true
        return field.get(clazz) as T
    }
    
    fun <T> getValue(any: Any, fieldName: String,
                     forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ) = getValueOrNull<T>(any, fieldName, false, forceAccess)!!
    
    fun <T> getValue(any: Any, field: Field,
                     forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ) = getValueOrNull<T>(any, field, forceAccess)!!
    
    fun <T> getValue(clazz: Class<*>, fieldName: String,
                     forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): T = getValueOrNull<T>(clazz, fieldName, false, forceAccess)!!
    
    fun <T> getValue(clazz: Class<*>, field: Field,
                     forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): T = getValueOrNull<T>(clazz, field, forceAccess)!!
    
    // ==== Get fields values ====
    
    fun <T> getNullableValues(any: Any,
                              forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): List<T?> {
        val list = mutableListOf<T?>()
        getFields(any).forEach { list.add(getValueOrNull(any, it, forceAccess)) }
        return list.toList()
    }
    
    fun <T> getValues(any: Any,
                      forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): List<T> {
        val list = mutableListOf<T>()
        getFields(any).forEach { list.add(getValue(any, it, forceAccess)) }
        return list.toList()
    }
    
    fun <T> getDeclaredNullableValues(any: Any,
                                      forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): List<T?> {
        val list = mutableListOf<T?>()
        getDeclaredFields(any).forEach { list.add(getValueOrNull(any, it, forceAccess)) }
        return list.toList()
    }
    
    fun <T> getDeclaredValues(any: Any): List<T> {
        val list = mutableListOf<T>()
        getDeclaredFields(any).forEach { list.add(getValue(any, it)) }
        return list.toList()
    }
    
    // ==== Set field value ====
    
    fun setNullableValue(any: Any, fieldName: String, value: Any?,
                         ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND
    ) {
        val field = getField(any, fieldName, ignoreNotFound) ?: return
        setNullableValue(any, field, value)
    }
    
    fun setNullableValue(any: Any, field: Field, value: Any?,
                         forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ) {
        if (forceAccess) field.isAccessible = true
        field.set(any, value)
    }
    
    fun setValue(any: Any, fieldName: String, value: Any,
                 ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND
    ) = setNullableValue(any, fieldName, value, ignoreNotFound)
    
    fun setValue(any: Any, field: Field, value: Any,
                 forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ) = setNullableValue(any, field, value, forceAccess)
    
    // ==== Get declared field ====
    
    fun getDeclaredField(any: Any, fieldName: String): Field = getDeclaredField(any, fieldName, false)!!
    
    fun getDeclaredField(any: Any, fieldName: String,
                         ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND
    ) = getDeclaredField(any.javaClass, fieldName, ignoreNotFound)
    
    fun getDeclaredField(clazz: Class<*>, fieldName: String) = getDeclaredField(clazz, fieldName, false)!!
    
    fun getDeclaredField(clazz: Class<*>, fieldName: String,
                         ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND
    ) = try {
        clazz.getDeclaredField(fieldName)
    } catch (e: NoSuchFieldException) {
        if (!ignoreNotFound) throw e
        null
    }
    
    // ==== Get declared fields====
    
    fun getDeclaredFields(any: Any) = getDeclaredFields(any.javaClass)
    
    fun getDeclaredFields(clazz: Class<*>) = listOf(*rawGetDeclaredFields(clazz))
    
    private fun rawGetDeclaredFields(clazz: Class<*>) = clazz.declaredFields
    
    // ==== Get declared field value ====
    
    fun <T> getDeclaredValueOrNull(any: Any, fieldName: String,
                                   ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND,
                                   forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ): T? {
        val field = getDeclaredField(any, fieldName, ignoreNotFound) ?: return null
        return getValueOrNull<Any>(any, field, forceAccess) as T?
    }
    
    fun <T> getDeclaredValue(any: Any, fieldName: String,
                             forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ) = getDeclaredValueOrNull<T>(any, fieldName, false, forceAccess)!!
    
    // ==== Set declared field value ====
    
    fun setDeclaredNullableValue(any: Any, fieldName: String, value: Any?,
                                 ignoreNotFound: Boolean = DEFAULT_IGNORE_NOT_FOUND
    ) {
        val field = getDeclaredField(any, fieldName, ignoreNotFound) ?: return
        setDeclaredNullableValue(any, field, value)
    }
    
    fun setDeclaredNullableValue(any: Any, field: Field, value: Any?,
                                 forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ) {
        if (forceAccess) field.isAccessible = true
        field.set(any, value)
    }
    
    fun setDeclaredValue(any: Any, fieldName: String, value: Any) = setDeclaredNullableValue(any, fieldName, value)
    
    fun setDeclaredValue(any: Any, field: Field, value: Any,
                         forceAccess: Boolean = DEFAULT_FORCE_ACCESS
    ) = setDeclaredNullableValue(any, field, value, forceAccess)
}