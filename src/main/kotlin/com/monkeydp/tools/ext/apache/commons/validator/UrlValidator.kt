package com.monkeydp.tools.ext.apache.commons.validator

import com.monkeydp.tools.exception.ierror
import org.apache.commons.validator.routines.UrlValidator

/**
 * @author iPotato-Work
 * @date 2020/10/13
 */
fun UrlValidator.checkValid(url: String) {
    if (!isValid(url))
        ierror("Not valid url `$url`")
}
