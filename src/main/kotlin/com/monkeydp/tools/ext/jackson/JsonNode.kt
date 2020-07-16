package com.monkeydp.tools.ext.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ContainerNode
import com.monkeydp.tools.config.kodein
import com.monkeydp.tools.global.objectMapper
import org.kodein.di.generic.instance

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
