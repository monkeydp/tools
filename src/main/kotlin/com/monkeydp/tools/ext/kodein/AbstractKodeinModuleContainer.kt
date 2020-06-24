package com.monkeydp.tools.ext.kodein

import com.monkeydp.tools.ext.kotlin.linesln
import com.monkeydp.tools.ext.logger.getLogger
import org.kodein.di.Kodein

abstract class AbstractKodeinModuleContainer(
        private val kodeinName: String
) {

    companion object {
        private val logger = getLogger()
    }

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

    fun logRegistered(vararg modules: Kodein.Module = this.moduleArray) {
        if (modules.isEmpty())
            logger.info("No kodein module register to $kodeinName.")
        else logger.info("Following kodein modules register to $kodeinName: ${modules.map { it.name }.linesln()}")
    }
}