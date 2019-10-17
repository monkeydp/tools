package com.monkeydp.tools.util

import com.monkeydp.tools.exception.inner.StdInnerException
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
        checkIsDir(dir)
        val files: Array<File>? = dir.listFiles(filter)
        return files!!
    }

    /**
     * Delete all children files/dirs
     */
    fun deleteAllChildren(dir: File) {
        checkIsDir(dir)
        dir.deleteRecursively()
        dir.mkdir()
    }

    private fun checkIsDir(file: File) {
        if (!file.isDirectory)
            throw StdInnerException(String.format("File %s is not a directory!", file))
    }
}