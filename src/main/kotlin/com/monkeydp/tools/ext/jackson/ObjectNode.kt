package com.monkeydp.tools.ext.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.monkeydp.tools.exception.ierror

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
    val secondToLastNode = secondToLastNode(path) as ObjectNode
    val node = getByPath(path)
    if (node == null) return
    val lastName = path.last()
    secondToLastNode.remove(lastName)
    when (node) {
        is ObjectNode -> secondToLastNode.setAll<ObjectNode>(node)
        is ArrayNode -> node.forEachIndexed { index, it ->
            secondToLastNode.set<ObjectNode>("$lastName[$index]", it)
        }
        else -> ierror("Unhandled node type, $node")
    }
}
