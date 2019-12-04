package com.monkeydp.tools.ext

import org.reflections.Reflections
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * @author iPotato
 * @date 2019/11/8
 */
fun getReflections(any: Any) = getReflections(any::class)

fun getReflections(kClass: KClass<*>) = getReflections(kClass.java)

fun getReflections(clazz: Class<*>) = getReflections(clazz.`package`.name, clazz.classLoader)

fun getReflections(
        packageName: String,
        classLoader: ClassLoader
): Reflections {
    val urls =
            ClasspathHelper.forPackage(packageName, classLoader)
    return Reflections(ConfigurationBuilder()
            .setUrls(urls)
            .addClassLoader(classLoader))
}

fun Reflections.getAnnotSingletons(annotClass: KClass<out Annotation>) =
        getTypesAnnotatedWith(annotClass.java)
                .map { it.singleton() }.toSet()

@Suppress("UNCHECKED_CAST")
fun <T> Reflections.getAnnotSingletonsX(annotClass: KClass<out Annotation>) =
        getAnnotSingletons(annotClass) as Set<T>

fun Reflections.getAnnotClasses(annotClass: KClass<out Annotation>) =
        getTypesAnnotatedWith(annotClass.java).map { it }.toSet()

fun Reflections.getAnnotKClasses(annotClass: KClass<out Annotation>) =
        getAnnotClasses(annotClass).map { it.kotlin }.toSet()

@Suppress("UNCHECKED_CAST")
inline fun <reified T> Reflections.getTypesAnnotatedWithX(annotClass: Class<out Annotation>) =
        getTypesAnnotatedWith(annotClass).filter { it.kotlin.isSubclassOf(T::class) }.toSet() as Set<Class<T>>