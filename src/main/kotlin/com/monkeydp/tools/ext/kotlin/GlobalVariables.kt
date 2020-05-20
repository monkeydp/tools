package com.monkeydp.tools.ext.kotlin

import com.fasterxml.jackson.databind.ObjectMapper
import com.monkeydp.tools.config.kodein
import org.kodein.di.generic.instance

/**
 * Global variables of current package
 *
 * @author iPotato-Work
 * @date 2020/5/20
 */
val objectMapper by kodein.instance<ObjectMapper>()