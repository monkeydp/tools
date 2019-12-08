package com.monkeydp.tools.gradle.wrapper

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