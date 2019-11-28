package com.monkeydp.tools.util

import com.atlassian.plugin.util.VersionStringComparator

/**
 * @author iPotato
 * @date 2019/11/28
 */
object VersionUtil {
    private val comparator = VersionStringComparator()
    fun compare(version1: String, version2: String) = comparator.compare(version1, version2)
    fun newerThan(version1: String, version2: String) = compare(version1, version2) > 0
}