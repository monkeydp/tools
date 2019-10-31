package com.monkeydp.tools.ext

import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty

/**
 * @author iPotato
 * @date 2019/10/29
 */
fun <T : Any> Delegates.notNullSingleton(): ReadWriteProperty<Any?, T> =
        NotNullSingleInitVar()