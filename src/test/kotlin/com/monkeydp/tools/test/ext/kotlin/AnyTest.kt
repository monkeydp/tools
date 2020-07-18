package com.monkeydp.tools.test.ext.kotlin

import com.monkeydp.tools.ext.kotlin.sameNameMemberPropsEqual
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * @author iPotato-Work
 * @date 2020/6/18
 */
class AnyTest {
    object Dog {
        val name = "Tom"
        val age = 5
        val color = "Grey"
    }

    object Cat {
        val age = 5
        val color = "Grey"
    }

    object Mouse {
        val name = "Bob"
        val age = 5
        val color = "Grey"
    }

    @Test
    fun sameNamePropsEqualTest() {
        assertTrue(Dog.sameNameMemberPropsEqual(Cat))
        assertFalse(Cat.sameNameMemberPropsEqual(Dog))
        assertFalse(Dog.sameNameMemberPropsEqual(Mouse))
        assertFalse(Mouse.sameNameMemberPropsEqual(Dog))
    }
}