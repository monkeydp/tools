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
        open var name = "dad"
        val phone = "15845678910"
        val age = 22
    }
    
    class Child : Parent() {
        override var name = "son"
    }
    
    private fun mockChild(): Child {
        return Child()
    }
    
    @Test
    fun getFieldTest() {
        getFieldSuccessTest()
        getFieldFailTest()
    }
    
    private fun getFieldSuccessTest() {
        val field = FieldUtil.getField(mockChild(), Parent::phone.name)
        Assert.assertNotNull(field)
    }
    
    private fun getFieldFailTest() {
        val field = FieldUtil.getField(mockChild(), NOT_EXIST_FIELD_NAME, true)
        Assert.assertNull(field)
    }
    
    @Test
    fun getDeclaredFieldsTest() {
        val declaredFields = FieldUtil.getDeclaredFields(Child::class.java)
        Assert.assertTrue(declaredFields.size == 1)
    }
    
    @Test
    fun getFieldsTest() {
        val fields = FieldUtil.getFields(Child::class.java)
        Assert.assertTrue(fields.size == 3)
    }
}