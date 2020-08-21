package com.monkeydp.tools.task

import java.util.*

interface Task<ID : Any, C : TaskContent> {
    val id: ID
    val name: String
    val content: C
    val handlerName: String
    val startedAt: Date
    val runAt: Date
    var desc: String
    val info
        get() =
            "Task(id = $id, name = $name, desc = $desc)"
}

abstract class BaseTask<ID : Any, C : TaskContent> : Task<ID, C>

interface TaskHandler<T : Task<*, *>> {
    fun run(task: T)
}

abstract class BaseTaskHandler<T : Task<*, *>> : TaskHandler<T> {
    override fun run(task: T) {
        beforeRun(task)
        innerRun(task)
        afterRun(task)
    }

    protected abstract fun beforeRun(task: T)

    protected abstract fun innerRun(task: T)

    protected abstract fun afterRun(task: T)
}

interface TaskContent
