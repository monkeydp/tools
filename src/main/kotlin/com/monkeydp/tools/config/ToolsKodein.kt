package com.monkeydp.tools.config

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.Executor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

/**
 * @author iPotato
 * @date 2019/12/8
 */
internal val kodein = Kodein {
    bind<Executor>() with singleton { DefaultExecutor() }
    bind<ObjectMapper>() with singleton {
        ObjectMapper()
                .registerModule(KotlinModule())
                .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
    importAll(*ToolsKodeinModules.moduleArray, allowOverride = true)
}