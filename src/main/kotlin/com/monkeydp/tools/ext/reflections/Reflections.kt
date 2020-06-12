package com.monkeydp.tools.ext.reflections

import com.monkeydp.tools.ext.java.hasAnnot
import com.monkeydp.tools.ext.java.inClass
import com.monkeydp.tools.ext.java.singleton
import com.monkeydp.tools.util.FieldUtil
import org.reflections.Reflections
import org.reflections.scanners.Scanner
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import java.lang.reflect.Field
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * @author iPotato
 * @date 2019/11/8
 */
val defaultScanners = arrayOf<Scanner>(TypeAnnotationsScanner(), SubTypesScanner())

val defaultScannerList = defaultScanners.toList()

fun reflections(any: Any) = reflections(any::class)

fun reflections(kClass: KClass<*>) = reflections(kClass.java)

fun reflections(clazz: Class<*>) =
        reflections(clazz.`package`.name, defaultScannerList, clazz.classLoader)

fun reflections(
        packageName: String,
        scanners: Iterable<Scanner> = defaultScannerList,
        classLoader: ClassLoader = Thread.currentThread().contextClassLoader
) = reflections(listOf(packageName), scanners, classLoader)

fun reflections(
        packageNames: Iterable<String>,
        scanners: Iterable<Scanner> = defaultScannerList,
        classLoader: ClassLoader = Thread.currentThread().contextClassLoader
): Reflections {
    val urls = packageNames.map { ClasspathHelper.forPackage(it, classLoader) }.toList().flatten()
    return Reflections(ConfigurationBuilder()
            .setUrls(urls)
            .setScanners(*scanners.toList().toTypedArray())
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

inline fun <reified A : Annotation> Reflections.getAnnotatedAnnotClasses(honorInherited: Boolean = false) =
        getAnnotatedAnnotClasses(A::class, honorInherited)

fun Reflections.getAnnotatedAnnotKClasses(annotKClass: KClass<out Annotation>, honorInherited: Boolean = false) =
        getAnnotatedAnnotClasses(annotKClass, honorInherited).map { it.kotlin }.toSet()

inline fun <reified A : Annotation> Reflections.getAnnotatedAnnotKClasses(honorInherited: Boolean = false) =
        getAnnotatedAnnotKClasses(A::class, honorInherited)

fun Reflections.getAnnotatedInterfaces(annotKClass: KClass<out Annotation>, honorInherited: Boolean = false) =
        getAnnotatedClasses(annotKClass, honorInherited).filter { it.isInterface }.toSet()

fun Reflections.getAnnotatedKInterfaces(annotKClass: KClass<out Annotation>, honorInherited: Boolean = false) =
        getAnnotatedInterfaces(annotKClass, honorInherited).map { it.kotlin }.toSet()

@Suppress("UNCHECKED_CAST")
inline fun <reified T> Reflections.getTypesAnnotatedWithX(annotClass: Class<out Annotation>) =
        getTypesAnnotatedWith(annotClass).filter { it.kotlin.isSubclassOf(T::class) }.toSet() as Set<Class<T>>

fun Reflections.getAnnotatedFields(
        annotKClass: KClass<out Annotation>
): Collection<Field> = getFieldsAnnotatedWith(annotKClass.java)

/**
 * The class where annotated field in, must be a singleton
 */
fun Reflections.getAnnotatedFieldValueMap(
        annotKClass: KClass<out Annotation>,
        configInit: (FieldUtil.GetValueConfig.() -> Unit)? = null
): Map<Field, Any> = getAnnotatedFields(annotKClass).map {
    it to it.inClass.singleton().run { FieldUtil.getValue<Any>(this, it, configInit) }
}.toMap()

fun Reflections.getAnnotatedParameters(annotKClass: KClass<out Annotation>) =
        getMethodsWithAnyParamAnnotated(annotKClass.java).map { method ->
            method.parameters.filter {
                it.hasAnnot(annotKClass)
            }
        }.flatten()