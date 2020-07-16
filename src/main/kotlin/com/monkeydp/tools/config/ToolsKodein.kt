package com.monkeydp.tools.config

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.javafaker.Faker
import com.monkeydp.tools.config.ToolsKodeinModuleContainer.moduleArray
import com.monkeydp.tools.ext.kodein.AbstractKodeinModuleContainer
import com.monkeydp.tools.ext.logger.getLogger
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.Executor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*

/**
 * @author iPotato
 * @date 2019/12/8
 */
val logger = getLogger()

val toolsKodeinModule = Kodein.Module("toolsKodeinModule") {
    bind<Charset>() with singleton { UTF_8 }
    bind<Locale>() with singleton { Locale.CHINESE }
    bind<Executor>() with singleton { DefaultExecutor() }
    bind<ObjectMapper>() with singleton {
        ObjectMapper()
                .registerKotlinModule()
    }
    bind<Faker>() with singleton { Faker(instance<Locale>()) }

    importAll(*moduleArray, allowOverride = true)
}

internal val kodein = Kodein {
    import(toolsKodeinModule, allowOverride = true)
    ToolsKodeinModuleContainer.logRegistered()
}

object ToolsKodeinModuleContainer : AbstractKodeinModuleContainer("ToolsKodein")