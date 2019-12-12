package com.monkeydp.tools.gradle.wrapper

import com.monkeydp.tools.config.kodein
import com.monkeydp.tools.ext.main.ierror
import com.monkeydp.tools.ext.logger.getLogger
import com.monkeydp.tools.util.SystemUtil.IS_OS_UNIX
import com.monkeydp.tools.util.SystemUtil.IS_OS_WINDOWS
import com.monkeydp.tools.util.SystemUtil.OS_NAME
import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.Executor
import org.kodein.di.generic.instance

/**
 * @author iPotato
 * @date 2019/12/8
 */
abstract class AbstractGradleWrapperExecutor(
        /**
         * Directory where the gradlew/gradlew.bat is
         */
        private val gradlewDirpath: String
) : GradleWrapperExecutor {
    
    companion object {
        private val log = getLogger()
    }
    
    private val executor: Executor by kodein.instance()
    
    private val gradlewPath: String
        get() {
            val gradlewFilename =
                    when {
                        IS_OS_WINDOWS -> "gradlew.bat"
                        IS_OS_UNIX -> "gradlew"
                        else -> ierror("Unknown system $OS_NAME")
                    }
            
            return StringBuilder()
                    .append(gradlewDirpath)
                    .append("/")
                    .append(gradlewFilename)
                    .toString()
        }
    
    override fun gradlew(init: GradlewArguments.() -> Unit) {
        val args = StdGradlewArguments()
        args.init()
        val line = "$gradlewPath ${args.toCmdLine()}"
        log.debug("Execute command `$line`...")
        val commandLine = CommandLine.parse(line)
        executor.execute(commandLine)
    }
}