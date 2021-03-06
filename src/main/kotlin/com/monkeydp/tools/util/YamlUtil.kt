package com.monkeydp.tools.util

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.representer.Representer
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/**
 * @author iPotato
 * @date 2019/10/14
 */
object YamlUtil {
    
    private val representer: Representer
    private var options: DumperOptions
    val yaml: Yaml
    
    init {
        representer = Representer()
        options = DumperOptions()
        representer.getPropertyUtils().setSkipMissingProperties(true)
        yaml = Yaml(representer, options)
    }
    
    inline fun <reified T> load(yamlString: String) = this.yaml.load<T>(yamlString)
    
    inline fun <reified T> loadAs(yaml: String) = this.yaml.loadAs(yaml, T::class.java)
    
    fun <T> loadAs(string: String, type: Class<T>) = yaml.loadAs(string, type)
    
    inline fun <reified T> loadAs(inputStream: InputStream) = yaml.loadAs<T>(inputStream, T::class.java)
    
    fun <T> loadAs(inputStream: InputStream, type: Class<T>) = yaml.loadAs<T>(inputStream, type)
    
    inline fun <reified T> loadAs(file: File) = loadAs(FileInputStream(file), T::class.java)
    
    fun <T> loadAs(file: File, type: Class<T>) = loadAs(FileInputStream(file), type)
}