package com.monkeydp.tools.gradle.wrapper

import com.monkeydp.tools.ext.kotlin.initInstance

/**
 * @author iPotato
 * @date 2019/12/8
 */
interface GradleWrapperExecutor {
    /**
     * Execute command line by "gradlew"
     * gradlew [option ...] [task ...]
     */
    fun gradlew(init: GradlewArguments.() -> Unit)
}

fun gradleWrapperExecutor(
        init: (GradleWrapperExecutor.() -> Unit)? = null,
        rootDirpath: String
): GradleWrapperExecutor =
        initInstance<StdGradleWrapperExecutor>(init, rootDirpath)