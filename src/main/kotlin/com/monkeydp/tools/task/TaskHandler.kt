package com.monkeydp.tools.task

interface TaskHandler<T : Task<*>> {
    fun run(task: T): T
    fun cancel(task: T): T
}

abstract class BaseTaskHandler<T : Task<*>> : TaskHandler<T>
