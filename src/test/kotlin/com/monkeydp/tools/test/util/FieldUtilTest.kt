package com.monkeydp.tools.test.util

import com.monkeydp.tools.util.FieldUtil
import org.junit.Assert
import org.junit.Test
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
        override val name = "son"
    }
    
    private fun mockChild(): Child {
        return Child()
    }
    
    @Test
    fun getFieldTest() {
        val prop = Child::phone
        val field = FieldUtil.getField(mockChild(), prop.name)
        Assert.assertTrue(field == prop.javaField)
    }
    
    @Test
    fun getFieldOrNullTest() {
        val field = FieldUtil.getFieldOrNull(mockChild(), NOT_EXIST_FIELD_NAME)
        Assert.assertNull(field)
    }
    
    @Test
    fun getFieldsTest1() {
        val fields = FieldUtil.getFields(Child::class)
        val expected = listOf(
                Child::name.javaField!!,
                Parent::phone.javaField!!,
                Parent::age.javaField!!)
        Assert.assertTrue(fields == expected)
    }
    
    @Test
    fun getFieldsTest2() {
        val fields = FieldUtil.getFields(Child::class, override = false)
        val expected = listOf(
                Child::name.javaField!!,
                Parent::name.javaField!!,
                Parent::phone.javaField!!,
                Parent::age.javaField!!)
        Assert.assertTrue(fields == expected)
    }
    
    @Test
    fun getValueTest() {
        val prop = Child::phone
        val value = FieldUtil.getValue(mockChild(), prop, forceAccess = true)
        Assert.assertTrue(value == prop.get(Child()))
    }
    
    @Test
    fun getDeclaredFieldsTest() {
        val fields = FieldUtil.getDeclaredFields(Child::class.java)
        val expected = listOf(
                Child::name.javaField!!
        )
        Assert.assertTrue(fields == expected)
    }
}