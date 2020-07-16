package com.monkeydp.tools.ext.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ContainerNode
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.monkeydp.tools.global.objectMapper

/**
 * @author iPotato-Work
 * @date 2020/5/15
 */
fun JsonNode.removeAllKeys(): ArrayNode =
        objectMapper.createArrayNode().also { arrayNode ->
            this.forEach {
                arrayNode.add(recurRemoveKey(it))
            }
        }

private fun JsonNode.recurRemoveKey(node: JsonNode): JsonNode =
        when (node) {
            is ContainerNode<*> -> {
                val arrayNode = objectMapper.createArrayNode()
                node.forEach { arrayNode.add(recurRemoveKey(it)) }
                arrayNode
            }
            else -> node
        }

inline fun <reified T> JsonNode.convertValue(): T =
        objectMapper.treeToValue<T>(this)!!