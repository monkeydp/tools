package com.monkeydp.tools.ext.jackson

import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.monkeydp.tools.config.kodein
import com.monkeydp.tools.ext.jackson.JsonFlatten.Times.ONE
import com.monkeydp.tools.ext.jackson.JsonFlatten.Times.TWO
import org.kodein.di.generic.instance

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
@Target(AnnotationTarget.PROPERTY)
annotation class JsonFlatten(val times: Times = ONE) {
    enum class Times {
        ONE, TWO
    }
}

object JsonFlattenHandler {

    private val objectMapper by kodein.instance<ObjectMapper>()

    fun flattenData(objectNode: ObjectNode, dataName: String, jsonFlatten: JsonFlatten?): ObjectNode =
            objectNode.deepCopy().apply {
                if (jsonFlatten == null) return this

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
                    ONE -> dataPairs.forEachIndexed { index, list ->
                        val node = objectMapper.createObjectNode()
                        node.setAll<ObjectNode>(list.toMap())
                        set<ObjectNode>(index.toString(), node)
                    }
                    TWO -> setAll<ObjectNode>(dataPairs.flatten().toMap())
                }
            }
}
