package com.monkeydp.tools.ext.kodein

import org.kodein.di.KodeinAware
import org.kodein.di.generic.provider

/**
 * @author iPotato
 * @date 2019/12/31
 */
inline fun <reified T : Any> KodeinAware.providerX(tag: Any? = null): T {
    val instance: () -> T by provider(tag)
    return instance()
}