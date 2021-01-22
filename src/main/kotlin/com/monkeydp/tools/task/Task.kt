package com.monkeydp.tools.task

import java.util.*

interface Task<ID : Any> {
    val id: ID
    val name: String
    val content: String
    val handlerName: String
    val startedAt: Date
    val expectedToRunAt: Date
    val runAt: Date?
    var desc: String
    val info
        get() =
            "Task(id = $id, name = $name, desc = $desc)"
}

abstract class BaseTask<ID : Any> : Task<ID>

interface TaskContent
