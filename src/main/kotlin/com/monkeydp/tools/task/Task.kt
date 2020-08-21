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
    fun cancel(task: T)
}

abstract class BaseTaskHandler<T : Task<*, *>> : TaskHandler<T> {

    override fun run(task: T) {
        beforeRun(task)
        innerRun(task)
        afterRun(task)
    }

    protected open fun beforeRun(task: T) {}

    protected abstract fun innerRun(task: T)

    protected open fun afterRun(task: T) {}

    override fun cancel(task: T) {
        beforeCancel(task)
        innerCancel(task)
        afterCancel(task)
    }

    protected open fun beforeCancel(task: T) {}

    protected abstract fun innerCancel(task: T)

    protected open fun afterCancel(task: T) {}
}

interface TaskContent
