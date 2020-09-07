package com.monkeydp.tools.exception.inner

import com.monkeydp.tools.ext.logger.LogLevel
import com.monkeydp.tools.ext.logger.LogLevel.ERROR

/**
 * @author iPotato
 * @date 2019/10/14
 */
open class InnerEx(
        message: CharSequence = NO_EX_MESSAGE,
        cause: Throwable? = null,
        configInit: (InnerExConfigBuilder.() -> Unit)? = null
) : Exception(message.toString(), cause) {
    companion object {
        private const val NO_EX_MESSAGE = "<No Ex Message>"
    }

    val config: InnerExConfig

    val msg = message.toString()

    init {
        config = InnerExConfigBuilder().apply {
            configInit?.invoke(this)
        }.run {
            InnerExConfig(
                    logLevel = logLevel,
                    hidestack = hidestack
            )
        }
    }

    constructor(
            cause: Throwable,
            configInit: (InnerExConfigBuilder.() -> Unit)? = null
    ) : this(message = NO_EX_MESSAGE, cause = cause, configInit = configInit)
}

class InnerExConfig(
        val logLevel: LogLevel,
        val hidestack: Boolean
)

class InnerExConfigBuilder {
    var logLevel = ERROR

    var hidestack = false
}
