package com.monkeydp.tools.util

import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/**
 * @author iPotato
 * @date 2019/10/14
 */
object YamlUtil {

    private val yaml = Yaml()

    fun <T> loadAs(file: File, type: Class<T>): T {
        return loadAs(FileInputStream(file), type)
    }

    fun <T> loadAs(inputStream: InputStream, type: Class<T>): T {
        return yaml.loadAs<T>(inputStream, type)
    }
}