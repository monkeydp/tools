package com.monkeydp.tools.util

import com.monkeydp.tools.exception.ierror
import java.io.File
import java.io.FileFilter
import java.io.FilenameFilter

/**
 * @author iPotato
 * @date 2019/10/13
 */
object FileUtil {
    
    /**
     * List all files under the given directory path by FilenameFilter
     */
    fun listFiles(dirpath: String, filter: FilenameFilter): Array<File> {
        val dir = File(dirpath)
        return listFiles(dir, filter)
    }
    
    /**
     * List all files under the given directory by FilenameFilter
     */
    fun listFiles(dir: File, filter: FilenameFilter? = null): Array<File> {
        checkIsDir(dir)
        val files: Array<File>? = dir.listFiles(filter)
        return files!!
    }
    
    /**
     * List all files under the given directory path by FileFilter
     */
    fun listFiles(dirpath: String, filter: FileFilter? = null): Array<File> {
        val dir = File(dirpath)
        return listFiles(dir, filter)
    }
    
    /**
     * List all files under the given directory by FileFilter
     */
    fun listFiles(dir: File, filter: FileFilter? = null): Array<File> {
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
        if (!file.isDirectory) ierror("File $file is not a directory!")
    }
}