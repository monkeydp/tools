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
        classLoader: ClassLoader = Thread.currentThread().contextClassLoader
) = getReflections(listOf(packageName), classLoader)

fun getReflections(
        packageNames: Iterable<String>,
        classLoader: ClassLoader = Thread.currentThread().contextClassLoader
): Reflections {
    val urls = packageNames.map { ClasspathHelper.forPackage(it, classLoader) }.toList().flatten()
    return Reflections(ConfigurationBuilder()
            .setUrls(urls)
            .addClassLoader(classLoader))
}


inline fun <reified T> Reflections.getSubTypesOf() = getSubTypesOf(T::class.java)

fun Reflections.getAnnotatedSingletons(annotClass: KClass<out Annotation>) =
        getTypesAnnotatedWith(annotClass.java)
                .map { it.singleton() }.toSet()

@Suppress("UNCHECKED_CAST")
fun <T> Reflections.getAnnotatedSingletonsX(annotClass: KClass<out Annotation>) =
        getAnnotatedSingletons(annotClass) as Set<T>

fun Reflections.getAnnotatedClasses(annotKClass: KClass<out Annotation>, honorInherited: Boolean = false) =
        getTypesAnnotatedWith(annotKClass.java, honorInherited).map { it }.toSet()

fun Reflections.getAnnotatedKClasses(annotKClass: KClass<out Annotation>, honorInherited: Boolean = false) =
        getAnnotatedClasses(annotKClass, honorInherited).map { it.kotlin }.toSet()

fun Reflections.getAnnotatedAnnotClasses(annotKClass: KClass<out Annotation>, honorInherited: Boolean = false) =
        getAnnotatedClasses(annotKClass, honorInherited).map {
            @Suppress("UNCHECKED_CAST")
            it as Class<out Annotation>
        }.toSet()

fun Reflections.getAnnotatedAnnotKClasses(annotKClass: KClass<out Annotation>, honorInherited: Boolean = false) =
        getAnnotatedAnnotClasses(annotKClass, honorInherited).map { it.kotlin }.toSet()

@Suppress("UNCHECKED_CAST")
inline fun <reified T> Reflections.getTypesAnnotatedWithX(annotClass: Class<out Annotation>) =
        getTypesAnnotatedWith(annotClass).filter { it.kotlin.isSubclassOf(T::class) }.toSet() as Set<Class<T>>