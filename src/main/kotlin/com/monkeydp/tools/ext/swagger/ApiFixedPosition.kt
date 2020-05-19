package com.monkeydp.tools.ext.swagger

import kotlin.annotation.AnnotationTarget.CLASS

/**
 * Auto assign position by current property order
 *
 * @author iPotato-Work
 * @date 2020/5/16
 */
@Target(CLASS)
annotation class ApiFixedPosition