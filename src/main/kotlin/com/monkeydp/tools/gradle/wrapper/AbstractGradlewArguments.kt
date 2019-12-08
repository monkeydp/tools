package com.monkeydp.tools.gradle.wrapper

import com.monkeydp.tools.enumeration.Symbol.SPACE

/**
 * @author iPotato
 * @date 2019/12/8
 */
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
    
    fun toCmdLine(): String {
        val line = StringBuilder()
        tasks.forEach { line.append(it).append(SPACE) }
        options.forEach { (name, value) ->
            line.append(name).append(SPACE).append(value).append(SPACE)
        }
        return line.trim().toString()
    }
}