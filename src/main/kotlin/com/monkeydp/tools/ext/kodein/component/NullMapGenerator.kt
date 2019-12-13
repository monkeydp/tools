package com.monkeydp.tools.ext.kodein.component

import com.monkeydp.tools.ext.main.ierror

/**
 * @author iPotato
 * @date 2019/12/13
 */
class NullMapGenerator : KodeinMapGenerator<Any, Any> {
    private constructor()
    
    override fun generate(components: Iterable<Any>): Map<Any, Any> {
        ierror("Cannot call this function!")
    }
}