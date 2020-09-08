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

fun <T : JsonNode> ObjectNode.replaceByPath(
        path: String,
        node: T,
        separator: String = DEFAULT_PATH_SEPARATOR
) =
        replaceByPath(path.split(separator), node)

fun <T : JsonNode> ObjectNode.replaceByPath(
        path: List<String>,
        node: T
) {
    require(path.isNotEmpty()) { "Path cannot be empty!" }
    (secondToLastNode(path) as ObjectNode)
            .set<T>(path.last(), node)
}
