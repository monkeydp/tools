package com.monkeydp.tools.test.util

import com.monkeydp.tools.ext.kotlin.equalsX
import com.monkeydp.tools.util.FieldUtil
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.reflect.jvm.javaField

/**
 * @author iPotato
 * @date 2019/10/15
 */
class FieldUtilTest {
    private companion object {
        const val NOT_EXIST_FIELD_NAME = "notExist"
    }
    
    open class Parent {
        open val name = "dad"
        val phone = "15845678910"
        val age = 22
    }
    
    class Child : Parent() {
        companion object {
            val mock get() = Child()
        }
        
        override val name = "son"
    }
    
    @Test
    fun getFieldTest() {
        Child::phone.apply {
            FieldUtil.getField(Child.mock, name).also {
                assertEquals(it, javaField)
            }
        }
    }
    
    @Test
    fun getFieldOrNullTest() {
        FieldUtil.getFieldOrNull(Child.mock, NOT_EXIST_FIELD_NAME).also {
            assertNull(it)
        }
    }
    
    @Test
    fun getFieldsTest1() {
        FieldUtil.getFields(Child::class).also {
            val expected = listOf(
                    Child::name.javaField!!,
                    Parent::phone.javaField!!,
                    Parent::age.javaField!!)
            assertTrue(it.equalsX(expected, true))
        }
    }
    
    @Test
    fun getFieldsTest2() {
        FieldUtil.getFields(Child::class, override = false).also {
            val expected = listOf(
                    Child::name.javaField!!,
                    Parent::name.javaField!!,
                    Parent::phone.javaField!!,
                    Parent::age.javaField!!)
            assertTrue(it.equalsX(expected, true))
        }
    }
    
    @Test
    fun getValueTest() {
        Child.mock.apply {
            val prop = Child::phone
            val value = FieldUtil.getValue(this, prop, forceAccess = true)
            assertTrue(value == prop.get(this))
        }
    }
    
    @Test
    fun getValuesTest() {
        Child.mock.apply {
            val values = FieldUtil.getValues<Any>(this, forceAccess = true)
            val expected = listOf(name, phone, age)
            assertTrue(values.equalsX(expected, true))
        }
    }
    
    @Test
    fun getDeclaredFieldsTest() {
        FieldUtil.getDeclaredFields(Child::class.java).also {
            val expected = listOf(Child::name.javaField!!)
            assertTrue(it == expected)
        }
    }
    
    @Test
    fun getDeclaredValuesTest() {
        Child.mock.apply {
            val values = FieldUtil.getDeclaredValues<Any>(this, forceAccess = true)
            val expected = listOf(name)
            assertTrue(values.equalsX(expected, true))
        }
    }
}