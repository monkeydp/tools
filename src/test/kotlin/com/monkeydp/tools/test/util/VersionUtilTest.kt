package com.monkeydp.tools.test.util

import com.monkeydp.tools.util.VersionUtil
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * @author iPotato
 * @date 2019/11/28
 */
class VersionUtilTest {
    private val v1 = "my-app-0.0.3-SNAPSHOT"
    private val v2 = "my-app-0.0.12-SNAPSHOT"
    private val v3 = "my-app-0.1.1-SNAPSHOT"
    private val v4 = "my-app-1.0.0-SNAPSHOT"
    
    @Test
    fun newerThanTest() {
       assertTrue(VersionUtil.newerThan(v2, v1))
       assertTrue(VersionUtil.newerThan(v3, v1))
       assertTrue(VersionUtil.newerThan(v4, v1))
    }
}