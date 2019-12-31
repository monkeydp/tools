package com.monkeydp.tools.ext.kodein

import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

/**
 * @author iPotato
 * @date 2019/12/31
 */
inline fun <reified T : Any> KodeinAware.findImpl(tag: Any? = null): T {
    val instance: T by instance(tag)
    return instance
}