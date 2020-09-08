package com.monkeydp.tools.ext.jackson

import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.monkeydp.tools.global.objectMapper
import kotlin.annotation.AnnotationTarget.PROPERTY

/**
 * @author iPotato-Work
 * @date 2020/5/19
 */
class JsonFlattenModule : SimpleModule() {
    override fun getModuleName(): String = JsonFlattenModule::class.simpleName!!
    override fun setupModule(context: SetupContext?) {
        super.setupModule(context)
        TODO()
    }
}

/**
 * Flatten collection
 *
 * @see JsonUnwrapped If you want to flatten object, please use JsonUnwrapped
 *
 * @author iPotato-Work
 * @date 2020/5/18
 */
@Target(PROPERTY)
annotation class JsonFlatten

object JsonFlattener {

    /**
     * @param path path to collection, separate by `.`, like "data.content"
     */
    fun flattenData(objectNode: ObjectNode, path: String, jsonFlatten: JsonFlatten?): ObjectNode =
            objectNode.deepCopy().apply {
                if (jsonFlatten == null) return this

                val collection = getByPath(path) as ArrayNode
                val pairs =
                        collection.mapIndexed { index, jsonNode ->
                            jsonNode.fieldNames()
                                    .asSequence()
                                    .toList()
                                    .map {
                                        "$it[$index]" to jsonNode.get(it)
                                    }
                        }

                val replacement = objectMapper.createObjectNode()
                replacement.setAll<ObjectNode>(pairs.flatten().toMap())
                replaceByPath(path, replacement)
            }
}
