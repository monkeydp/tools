package com.monkeydp.tools.gradle.wrapper

/**
 * @author iPotato
 * @date 2019/12/8
 */
interface GradlewArguments {
    val tasks: Set<String>
    val options: Set<Pair<String, String>>
    operator fun String.unaryPlus()
    operator fun Pair<String, String>.unaryPlus()
}