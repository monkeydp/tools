package com.monkeydp.tools.config

import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.Executor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

/**
 * @author iPotato
 * @date 2019/12/8
 */
internal val kodein = Kodein {
    bind<Executor>() with singleton { DefaultExecutor() }
}