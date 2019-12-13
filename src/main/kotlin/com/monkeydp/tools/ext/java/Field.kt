package com.monkeydp.tools.ext.java

import com.monkeydp.tools.util.FieldUtil
import java.lang.reflect.Field

/**
 * @author iPotato
 * @date 2019/12/14
 */
val Field.clazzX get() = FieldUtil.getValue<Class<*>>(this, "clazz", forceAccess = true)

val Field.kClazzX get() = clazzX.kotlin