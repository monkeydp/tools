package com.monkeydp.tools.ext.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ContainerNode
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.monkeydp.tools.ext.kotlin.removeFirst
import com.monkeydp.tools.ext.kotlin.removeLast
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


val DEFAULT_PATH_SEPARATOR get() = "."

fun JsonNode.getByPath(path: String, separator: String = DEFAULT_PATH_SEPARATOR): JsonNode? =
        getByPath(path.split(separator))

fun JsonNode.getByPath(path: List<String>): JsonNode? {
    require(path.isNotEmpty()) { "Path cannot be empty!" }
    return get(path.first())?.run {
        if (path.size == 1) this
        else getByPath(path.toMutableList().apply { removeFirst() })
    }
}

fun JsonNode.secondToLastNode(path: String, separator: String = DEFAULT_PATH_SEPARATOR) =
        secondToLastNode(path.split(separator))

fun JsonNode.secondToLastNode(path: List<String>) =
        if (path.size == 1) this
        else getByPath(path.toMutableList().removeLast())
