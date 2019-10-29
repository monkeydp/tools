package com.monkeydp.tools.function

import com.monkeydp.tools.ext.NotNullSingleInitVar
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty

/**
 * @author iPotato
 * @date 2019/10/29
 */
fun <T : Any> Delegates.notNullSingleInit(): ReadWriteProperty<Any?, T> = NotNullSingleInitVar()