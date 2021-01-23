package com.monkeydp.tools.task

interface TaskGenerator {
    val name: String
    val taskName: String
    val taskDesc: String
    val taskContent: String
    val taskHandlerName: String
    val cronExp: String
}
