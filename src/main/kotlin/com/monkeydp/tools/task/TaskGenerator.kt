package com.monkeydp.tools.task

interface TaskGenerator<ID : Any> {
    val id: ID
    val name: String
    val taskName: String
    val taskDesc: String
    val taskContent: String
    val taskHandlerName: String
    val cronExp: String

    val info
        get() =
            "TaskGenerator(id = $id, name = $name)"
}
