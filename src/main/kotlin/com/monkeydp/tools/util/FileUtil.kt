package com.monkeydp.tools.util

import java.io.File

/**
 * @author iPotato
 * @date 2019/10/13
 */
object FileUtil {
    /**
     * List all files matched the given regex under the given directory
     */
    fun listFiles(dirPath: String, pattern: String): Array<File> {
        val dir = File(dirPath)
        if (!dir.isDirectory)
            throw RuntimeException(String.format("Cannot find dir in path: %s", dirPath))
        val files = dir.listFiles { _, filename ->
            filename.matches(pattern.toRegex())
        }
        return files!!
    }
}