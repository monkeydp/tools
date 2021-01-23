package com.monkeydp.tools.task

import java.util.*

interface Task {
    val name: String
    var desc: String
    val content: String
    val handlerName: String
    val startedAt: Date
    val expectedToRunAt: Date
    val runAt: Date?
}

interface TaskContent
