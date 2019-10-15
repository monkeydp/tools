package com.monkeydp.tools.util

import com.monkeydp.tools.exception.inner.StdInnerException
import com.monkeydp.tools.exception.inner.raw.RawIllegalAccessException
import com.monkeydp.tools.exception.inner.raw.RawNoSuchFieldException
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
     * Whether to ignore the exception when the field does not exist
     */
    private const val DEFAULT_IGNORE_NOT_EXIST = false

    // ==== Get field ====

    fun getField(any: Any, fieldName: String): Field? {
        return getField(any.javaClass, fieldName, DEFAULT_IGNORE_NOT_EXIST)
    }

    fun getField(any: Any, fieldName: String, ignoreNotExist: Boolean): Field? {
        return getField(any.javaClass, fieldName, ignoreNotExist)
    }

    fun getField(clazz: Class<*>, fieldName: String): Field? {
        return getField(clazz, fieldName, DEFAULT_IGNORE_NOT_EXIST)
    }

    fun getField(clazz: Class<*>, fieldName: String, ignoreNotExist: Boolean): Field? {

        var field: Field? = null

        try {
            // get field from current class
            field = getDeclaredField(clazz, fieldName, false)
        } catch (e: RawNoSuchFieldException) {
            // ignore
        }

        if (field != null) {
            return field
        }

        val superclass = clazz.superclass
        if (null != superclass && !Class::class.java.isAssignableFrom(superclass)) {
            // get field from superclass
            return getField(superclass, fieldName, ignoreNotExist)
        }

        if (!ignoreNotExist && null == superclass) {
            throw RawNoSuchFieldException()
        }

        return null
    }

    /**
     * Raw method to get field
     *
     * Can only use to get public static field
     *
     * @return
     */
    fun rawGetField(clazz: Class<*>, fieldName: String, ignoreNotExist: Boolean): Field? {
        try {
            return clazz.getField(fieldName)
        } catch (e: NoSuchFieldException) {
            if (!ignoreNotExist) {
                throw RawNoSuchFieldException()
            }
            // ignore
        }
        return null
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
                if (fieldNames.contains(superField.name)) {
                    return@forEach
                }
                fields.add(superField)
                fieldNames.add(superField.name)
            }
        }

        return fields
    }

    // ==== Get field value ====

    fun <T> getValue(any: Any, fieldName: String): T? {
        return getValue<T>(any, fieldName, DEFAULT_IGNORE_NOT_EXIST)
    }

    fun <T> getValue(any: Any, fieldName: String, ignoreNotExist: Boolean): T? {
        val field = getField(any, fieldName, ignoreNotExist) ?: return null
        return getValue<T>(any, field)
    }

    fun <T> getValue(any: Any, field: Field): T? {
        field.isAccessible = true
        try {
            return field.get(any) as T
        } catch (e: IllegalAccessException) {
            throw StdInnerException(e)
        }

    }

    // ==== Set field value ====

    fun setValue(any: Any, fieldName: String, value: Any?) {
        val field = getField(any, fieldName, DEFAULT_IGNORE_NOT_EXIST) ?: return
        setValue(any, field, value)
    }

    fun setValue(any: Any, fieldName: String, value: Any?, ignoreNotExist: Boolean) {
        val field = getField(any, fieldName, ignoreNotExist) ?: return
        setValue(any, field, value)
    }

    fun setValue(any: Any, field: Field, value: Any?) {
        field.isAccessible = true
        try {
            field.set(any, value)
        } catch (e: IllegalAccessException) {
            throw RawIllegalAccessException()
        }
    }

    // ==== Get declared field ====

    fun getDeclaredField(any: Any, fieldName: String): Field? {
        return getDeclaredField(any.javaClass, fieldName, DEFAULT_IGNORE_NOT_EXIST)
    }

    fun getDeclaredField(any: Any, fieldName: String, ignoreNotExist: Boolean): Field? {
        return getDeclaredField(any.javaClass, fieldName, ignoreNotExist)
    }

    fun getDeclaredField(clazz: Class<*>, fieldName: String): Field? {
        return getDeclaredField(clazz, fieldName, DEFAULT_IGNORE_NOT_EXIST)
    }

    fun getDeclaredField(clazz: Class<*>, fieldName: String, ignoreNotExist: Boolean): Field? {

        var field: Field? = null
        try {
            field = clazz.getDeclaredField(fieldName)
        } catch (e: NoSuchFieldException) {
            if (!ignoreNotExist) {
                throw RawNoSuchFieldException()
            }
            // ignore
        }

        return field
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

    fun <T> getDeclaredValue(any: Any, fieldName: String): T? {
        val field = getDeclaredField(any, fieldName, DEFAULT_IGNORE_NOT_EXIST) ?: return null
        return getValue<Any>(any, field) as T?
    }

    fun <T> getDeclaredValue(any: Any, fieldName: String, ignoreNotExist: Boolean): T? {
        val field = getDeclaredField(any, fieldName, ignoreNotExist) ?: return null
        return getValue<Any>(any, field) as T?
    }


    // ==== Set declared field value ====

    fun setDeclaredValue(any: Any, fieldName: String, value: Any?) {
        val field = getDeclaredField(any, fieldName, DEFAULT_IGNORE_NOT_EXIST) ?: return
        setDeclaredValue(any, field, value)
    }

    fun setDeclaredValue(any: Any, fieldName: String, value: Any?, ignoreNotExist: Boolean) {
        val field = getDeclaredField(any, fieldName, ignoreNotExist) ?: return
        setDeclaredValue(any, field, value)
    }

    fun setDeclaredValue(any: Any, field: Field, value: Any?) {
        field.isAccessible = true
        try {
            field.set(any, value)
        } catch (e: IllegalAccessException) {
            throw RawIllegalAccessException()
        }
    }
}