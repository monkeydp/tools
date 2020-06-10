package com.monkeydp.tools.global

import com.monkeydp.tools.config.kodein
import org.kodein.di.generic.instance
import java.util.*

/**
 * @author iPotato-Work
 * @date 2020/6/10
 */
val locale by lazy {
    val locale by kodein.instance<Locale>()
    locale
}