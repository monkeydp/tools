package com.monkeydp.tools.ext.kodein

import org.kodein.di.Kodein

abstract class AbstractKodeinModuleContainer {

    private val modules: MutableCollection<Kodein.Module> = mutableSetOf()

    val moduleArray get() = modules.toTypedArray()
    val reverseModuleArray get() = moduleArray.reversedArray()

    fun addModule(module: Kodein.Module) {
        modules.add(module)
    }

    fun addModule(moduleName: String, init: Kodein.Builder.() -> Unit) {
        addModule(
                Kodein.Module(moduleName, init = init)
        )
    }
}