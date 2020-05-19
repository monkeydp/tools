package com.monkeydp.tools.config

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.javafaker.Faker
import com.monkeydp.tools.ext.kodein.AbstractKodeinModuleContainer
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.Executor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.util.*

/**
 * @author iPotato
 * @date 2019/12/8
 */
val toolsKodeinModule = Kodein.Module("toolsKodeinModule") {
    bind<Executor>() with singleton { DefaultExecutor() }
    bind<ObjectMapper>() with singleton {
        ObjectMapper()
                .registerModule(KotlinModule())
                .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
    bind<Locale>() with singleton { Locale.CHINA }
    bind<Faker>() with singleton { Faker(instance<Locale>()) }
    importAll(*ToolsKodeinModuleContainer.moduleArray, allowOverride = true)
}

internal val kodein = Kodein {
    import(toolsKodeinModule, allowOverride = true)
}

object ToolsKodeinModuleContainer : AbstractKodeinModuleContainer()