package com.monkeydp.tools.task

import java.util.*

interface Task<ID : Any, C : TaskContent> {
    val id: ID
    val name: String
    val content: C
    val executorName: String
    val startedAt: Date
    val executedAt: Date
    var desc: String
    val info
        get() =
            "Task(id = $id, name = $name)"
}

abstract class BaseTask<ID : Any, C : TaskContent> : Task<ID, C>

interface TaskRunnable<T : Task<*, *>> {
    fun run(task: T)
}

abstract class BaseTaskRunnable<T : Task<*, *>> : TaskRunnable<T> {
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
