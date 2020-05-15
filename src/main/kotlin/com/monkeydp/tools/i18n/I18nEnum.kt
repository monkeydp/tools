package com.monkeydp.tools.i18n

import com.monkeydp.tools.config.kodein
import com.monkeydp.tools.constant.Symbol.DOT
import com.monkeydp.tools.constant.Symbol.HYPHEN
import com.monkeydp.tools.enumx.Enumx
import com.monkeydp.tools.ext.java.getStringX
import com.monkeydp.tools.ext.kotlin.camelCase2Chain
import com.monkeydp.tools.ext.kotlin.snakeCase2chain
import org.kodein.di.generic.instance
import java.util.*

/**
 * Assume i18nKeyPrefix = "my-prefix", enumClassName = "BigZoo", enumName = "RED_DOG",
 * then the propertyKey in messages.properties should be "my-prefix.big-zoo.red-dog"
 */
interface I18nEnum<E> : Enumx<E>
        where E : I18nEnum<E>, E : Enum<E> {

    companion object {
        private const val DELIMITER = DOT
        private const val JOINER = HYPHEN
    }

    val rbBaseName get() = "messages"
    val resourceBundle: ResourceBundle
        get() {
            val locale by kodein.instance<Locale>()
            return ResourceBundle.getBundle(rbBaseName, locale)
        }

    val i18nKeyPrefix get() = ""
    val i18nKey: String
        get() =
            StringBuilder().let {
                if (!i18nKeyPrefix.isBlank())
                    it.append(i18nKeyPrefix)
                            .append(DELIMITER)

                it.append(javaClass.simpleName.camelCase2Chain(JOINER).toLowerCase())
                        .append(DELIMITER)
                        .append(asEnum().name.snakeCase2chain(JOINER).toLowerCase())
                it.toString()
            }

    val i18nValue get() = resourceBundle.getStringX(i18nKey)
}