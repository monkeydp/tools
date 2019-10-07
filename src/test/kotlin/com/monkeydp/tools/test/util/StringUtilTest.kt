package com.monkeydp.tools.test.util

import com.monkeydp.tools.util.StringUtil.isEmpty
import org.junit.Assert
import org.junit.Test

/**
 * @author iPotato
 * @date 2019/10/4
 */
class StringUtilTest {
    @Test
    fun isEmptyTest() {
        // true
        Assert.assertTrue(isEmpty(null))
        Assert.assertTrue(isEmpty(""))
        // false
        Assert.assertFalse(isEmpty("iPotato"))
        Assert.assertFalse(isEmpty(" "))
    }
}