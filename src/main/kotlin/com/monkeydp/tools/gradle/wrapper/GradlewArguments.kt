package com.monkeydp.tools.gradle.wrapper

import com.monkeydp.tools.enumeration.Symbol

/**
 * @author iPotato
 * @date 2019/12/8
 */
interface GradlewArguments {
    val tasks: Set<String>
    val options: Set<Pair<String, String>>
    operator fun String.unaryPlus()
    operator fun Pair<String, String>.unaryPlus()
    
    fun toCmdLine(): String
    
    companion object {
        operator fun invoke(init: (GradlewArguments.() -> Unit)? = null): GradlewArguments =
                StdGradlewArguments().apply { init?.invoke(this) }
    }
}

abstract class AbstractGradlewArguments : GradlewArguments {
    
    private val _tasks: MutableSet<String> = mutableSetOf()
    private val _options: MutableSet<Pair<String, String>> = mutableSetOf()
    
    override val tasks: Set<String>
        get() = _tasks.toSet()
    override val options: Set<Pair<String, String>>
        get() = _options.toSet()
    
    override operator fun String.unaryPlus() {
        _tasks.add(this)
    }
    
    override operator fun Pair<String, String>.unaryPlus() {
        _options.add(this)
    }
    
    override fun toCmdLine(): String {
        val line = StringBuilder()
        tasks.forEach { line.append(it).append(Symbol.SPACE) }
        options.forEach { (name, value) ->
            line.append(name).append(Symbol.SPACE).append(value).append(Symbol.SPACE)
        }
        return line.trim().toString()
    }
}

internal class StdGradlewArguments : AbstractGradlewArguments()