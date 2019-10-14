package com.monkeydp.tools.util

import java.io.File
import java.io.FilenameFilter

/**
 * @author iPotato
 * @date 2019/10/13
 */
object FileUtil {
    /**
     * List all files under the given directory
     */
    fun listFiles(dirPath: String, filter: FilenameFilter): Array<File> {
        val dir = File(dirPath)
        return listFiles(dir, filter)
    }

    /**
     * List all files under the given directory
     */
    fun listFiles(dir: File, filter: FilenameFilter): Array<File> {
        if (!dir.isDirectory)
            throw RuntimeException(String.format("File %s is not a directory!", dir))
        val files: Array<File>? = dir.listFiles(filter)
        return files!!
    }
}