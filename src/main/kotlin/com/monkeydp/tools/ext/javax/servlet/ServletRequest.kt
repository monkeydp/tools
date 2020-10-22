package com.monkeydp.tools.ext.javax.servlet

import java.util.stream.Collectors
import javax.servlet.ServletRequest

/**
 * @author iPotato-Work
 * @date 2020/10/22
 */
val ServletRequest.body
    get() = reader
            .lines()
            .collect(Collectors.joining(System.lineSeparator()))
