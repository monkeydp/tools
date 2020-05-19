package com.monkeydp.tools.ext.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.monkeydp.tools.config.kodein
import com.monkeydp.tools.exception.inner.UnsupportedCaseException
import org.kodein.di.generic.instance

/**
 * @author iPotato-Work
 * @date 2020/5/19
 */
object JsonFlattenHandler {

    private val objectMapper by kodein.instance<ObjectMapper>()

    fun flattenData(objectNode: ObjectNode, dataName: String, jsonFlatten: JsonFlatten?): ObjectNode =
            objectNode.deepCopy().apply {
                if (jsonFlatten == null || jsonFlatten.times <= 0) return this

                val data = get(dataName) as ArrayNode
                val dataPairs = data.mapIndexed { index, jsonNode ->
                    jsonNode.fieldNames()
                            .asSequence()
                            .toList()
                            .map {
                                "$it[$index]" to jsonNode.get(it)
                            }
                }
                remove(dataName)
                when (jsonFlatten.times) {
                    1 -> dataPairs.forEachIndexed { index, list ->
                        val node = objectMapper.createObjectNode()
                        node.setAll<ObjectNode>(list.toMap())
                        set<ObjectNode>(index.toString(), node)
                    }
                    2 -> setAll<ObjectNode>(dataPairs.flatten().toMap())
                    else -> throw UnsupportedCaseException()
                }
            }
}