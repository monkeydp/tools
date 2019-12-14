package com.monkeydp.tools.test.util

import com.monkeydp.tools.util.FieldUtil
import org.junit.Assert
import org.junit.Test

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
        val field = FieldUtil.getField(mockChild(), Parent::phone.name)
        Assert.assertNotNull(field)
    }
    
    @Test
    fun getFieldOrNullTest() {
        val field = FieldUtil.getFieldOrNull(mockChild(), NOT_EXIST_FIELD_NAME)
        Assert.assertNull(field)
    }
    
    @Test
    fun getFieldsTest() {
        val fields = FieldUtil.getFields(Child::class.java)
        Assert.assertTrue(fields.size == 3)
    }
    
    @Test
    fun getDeclaredFieldsTest() {
        val declaredFields = FieldUtil.getDeclaredFields(Child::class.java)
        Assert.assertTrue(declaredFields.size == 1)
    }
}