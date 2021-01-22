package com.monkeydp.tools.task

import java.util.*

interface Task<ID : Any> {
    val id: ID
    val name: String
    var desc: String
    val content: String
    val handlerName: String
    val startedAt: Date
    val expectedToRunAt: Date
    val runAt: Date?
    val info
        get() =
            "Task(id = $id, name = $name, desc = $desc)"
}

interface TaskContent
