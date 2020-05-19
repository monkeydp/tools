package com.monkeydp.tools.ext.kodein

import org.kodein.di.Kodein

abstract class AbstractKodeinModuleContainer {

    private val modules: MutableCollection<Kodein.Module> = mutableSetOf()

    val moduleArray get() = modules.toTypedArray()

    fun addModule(moduleName: String, init: Kodein.Builder.() -> Unit) {
        val module = Kodein.Module(moduleName, init = init)
        modules.add(module)
    }
}