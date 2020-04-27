package com.monkeydp.tools.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.Executor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

/**
 * @author iPotato
 * @date 2020/4/27
 */
object ToolsKodeinModules {

    private val modules: MutableCollection<Kodein.Module> = mutableSetOf()

    val moduleArray get() = modules.toTypedArray()

    fun addModule(moduleName: String, init: Kodein.Builder.() -> Unit) {
        val module = Kodein.Module(moduleName, init = init)
        modules.add(module)
    }
}