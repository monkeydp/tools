package com.monkeydp.tools.test.util

import com.monkeydp.tools.util.StringUtil
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
        Assert.assertTrue(StringUtil.isEmpty(null))
        Assert.assertTrue(StringUtil.isEmpty(""))
        // false
        Assert.assertFalse(StringUtil.isEmpty("iPotato"))
        Assert.assertFalse(StringUtil.isEmpty(" "))
    }
}