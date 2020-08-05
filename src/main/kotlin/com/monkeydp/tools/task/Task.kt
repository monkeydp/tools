package com.monkeydp.tools.task

interface Task<ID : Any, C : TaskContent> {
    val id: ID
    val name: String
    val content: C
    val executorName: String
    val info
        get() =
            "Task(id = $id, name = $name)"
}

abstract class BaseTask<ID : Any, C : TaskContent> : Task<ID, C>

interface TaskExecutor<T : Task<*, *>> {
    fun execute(task: T)
}

abstract class BaseTaskExecutor<T : Task<*, *>> : TaskExecutor<T>

interface TaskContent
