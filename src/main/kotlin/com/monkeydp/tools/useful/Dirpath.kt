package com.monkeydp.tools.useful

import com.monkeydp.tools.ext.kotlin.toStdPath

/**
 * @author iPotato
 * @date 2019/12/7
 */
interface Dirpath {
    val root: String get() = System.getProperty("user.dir").toStdPath()
    val src get() = "$root/src"
    val main get() = "$src/main"
    val build get() = "$root/build"
    val classes get() = "$build/classes"
    val generated get() = "$src/generated"
    val kotlinGenerated get() = "$generated/kotlin"
}

abstract class AbstractDirpath : Dirpath