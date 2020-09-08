package com.monkeydp.tools.ext.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode

/**
 * @author iPotato-Work
 * @date 2020/9/8
 */
fun ObjectNode.removeByPath(path: String, separator: String = DEFAULT_PATH_SEPARATOR) =
        removeByPath(path.split(separator))

fun ObjectNode.removeByPath(path: List<String>): JsonNode? {
    require(path.isNotEmpty()) { "Path cannot be empty!" }
    return (secondToLastNode(path) as ObjectNode)
            .remove(path.last())
}

fun ObjectNode.replaceByPath(
        path: String,
        node: JsonNode,
        separator: String = DEFAULT_PATH_SEPARATOR
) =
        replaceByPath(path.split(separator), node)

fun ObjectNode.replaceByPath(
        path: List<String>,
        node: JsonNode
) {
    require(path.isNotEmpty()) { "Path cannot be empty!" }
    (secondToLastNode(path) as ObjectNode)
            .set<ObjectNode>(path.last(), node)
}

fun ObjectNode.unwrapByPath(path: String, separator: String = DEFAULT_PATH_SEPARATOR) =
        unwrapByPath(path.split(separator))

fun ObjectNode.unwrapByPath(path: List<String>) {
    (secondToLastNode(path) as ObjectNode).let {
        val node = getByPath(path) as ObjectNode
        it.remove(path.last())
        it.setAll<ObjectNode>(node)
    }
}

