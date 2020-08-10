package com.monkeydp.tools.task

interface Task<ID : Any, C : TaskContent> {
    val id: ID
    val name: String
    val content: C
    val executorName: String
    var desc: String
    val info
        get() =
            "Task(id = $id, name = $name)"
}

abstract class BaseTask<ID : Any, C : TaskContent> : Task<ID, C>

interface TaskRunnable<T : Task<*, *>> {
    fun run(task: T)
}

abstract class BaseTaskRunnable<T : Task<*, *>> : TaskRunnable<T>

interface TaskContent
