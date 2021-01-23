package com.monkeydp.tools.task

import kotlin.reflect.KClass

interface TaskHandler<T : Task> {
    fun run(task: T)
    fun cancel(task: T)

    /**
     * For example, task rerun after server restart
     *
     * @return rerun tasks
     */
    fun rerun(): Set<T>

    fun clean(tasks: Iterable<T>): Set<T>
}

abstract class BaseTaskHandler<T : Task> : TaskHandler<T>

val <T : TaskHandler<*>> KClass<T>.handlerName
    get() = simpleName!!.decapitalize()
