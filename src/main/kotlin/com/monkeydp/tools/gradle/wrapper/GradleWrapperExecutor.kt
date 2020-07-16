package com.monkeydp.tools.gradle.wrapper

import com.monkeydp.tools.config.kodein
import com.monkeydp.tools.exception.ierror
import com.monkeydp.tools.ext.logger.getLogger
import com.monkeydp.tools.util.SystemUtil
import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.Executor
import org.kodein.di.generic.instance

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

    companion object {
        operator fun invoke(
                rootDirpath: String,
                init: (GradleWrapperExecutor.() -> Unit)? = null
        ): GradleWrapperExecutor =
                StdGradleWrapperExecutor(rootDirpath).apply { init?.invoke(this) }
    }
}

abstract class AbstractGradleWrapperExecutor(
        /**
         * Directory where the gradlew/gradlew.bat is
         */
        private val gradlewDirpath: String
) : GradleWrapperExecutor {

    companion object {
        private val log = getLogger()
    }

    private val executor by kodein.instance<Executor>()

    private val gradlewPath: String
        get() {
            val gradlewFilename =
                    when {
                        SystemUtil.IS_OS_WINDOWS -> "gradlew.bat"
                        SystemUtil.IS_OS_UNIX -> "gradlew"
                        else -> ierror("Unknown system ${SystemUtil.OS_NAME}")
                    }

            return StringBuilder()
                    .append(gradlewDirpath)
                    .append("/")
                    .append(gradlewFilename)
                    .toString()
        }

    override fun gradlew(init: GradlewArguments.() -> Unit) {
        val args = GradlewArguments(init)
        val line = "$gradlewPath ${args.toCmdLine()}"
        log.debug("Execute command `$line`...")
        val commandLine = CommandLine.parse(line)
        executor.execute(commandLine)
    }
}

private class StdGradleWrapperExecutor(rootDirpath: String) :
        AbstractGradleWrapperExecutor(rootDirpath)