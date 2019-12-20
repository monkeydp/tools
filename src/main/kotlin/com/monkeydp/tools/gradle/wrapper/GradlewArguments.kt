package com.monkeydp.tools.gradle.wrapper

import com.monkeydp.tools.ext.kotlin.initInstance

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
}

fun gradlewArguments(init: (GradlewArguments.() -> Unit)? = null): GradlewArguments =
        initInstance<StdGradlewArguments>(init)