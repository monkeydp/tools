package com.monkeydp.tools.util

import com.monkeydp.tools.useful.VersionComparator
import java.io.File

/**
 * @author iPotato
 * @date 2019/11/28
 */
object VersionUtil {
    private val comparator = VersionComparator()
    private fun compare(version1: String, version2: String) = comparator.compare(version1, version2)
    
    fun newerThan(file1: File, file2: File) = newerThan(file1.name, file2.name)
    fun newerThan(version1: String, version2: String) = compare(version1, version2) > 0
}