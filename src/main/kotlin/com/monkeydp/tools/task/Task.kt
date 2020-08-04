package com.monkeydp.tools.task

interface Task<C : TaskContent> {
    val content: C
    val executorName: String
}

interface TaskExecutor<C : TaskContent> {
    fun execute(task: Task<C>)
}

interface TaskContent
