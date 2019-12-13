package com.monkeydp.tools.ext.kodein.component

import com.monkeydp.tools.ext.reflections.*
import org.reflections.Reflections
import org.reflections.scanners.FieldAnnotationsScanner
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

/**
 * @author iPotato
 * @date 2019/12/14
 */
abstract class AbstractKodeinComponentConfig : KodeinComponentConfig {
    
    protected open val packageNames = listOf(javaClass.`package`.name)
    
    private val reflections = Reflections(
            packageNames,
            javaClass.classLoader,
            *reflectionsDefaultScanners,
            FieldAnnotationsScanner()
    )
    
    override val componentsMap by lazy {
        val annotKClasses = reflections.getAnnotatedAnnotKClasses(KodeinComponent::class, true)
        annotKClasses.map { annotKClass ->
            val kodeinComponent = annotKClass.findAnnotation<KodeinComponent<*>>()!!
            annotKClass to when (kodeinComponent.type) {
                KodeinComponent.Type.SINGLETON -> findSingletons(annotKClass)
                KodeinComponent.Type.K_CLASS -> findKClass(annotKClass)
            }
        }.toMap()
    }
    
    private fun findSingletons(annotKClass: KClass<out Annotation>): Collection<Any> {
        val singletons = mutableListOf<Any>()
        val target = annotKClass.findAnnotation<Target>()!!
        target.allowedTargets.forEach {
            val part = when (it) {
                AnnotationTarget.CLASS -> reflections.getAnnotatedSingletons(annotKClass)
                AnnotationTarget.FIELD -> reflections.getAnnotatedFieldValues(annotKClass, true)
                else -> return@forEach
            }
            singletons.addAll(part)
        }
        return singletons.toList()
    }
    
    private fun findKClass(annotKClass: KClass<out Annotation>) = reflections.getAnnotatedKClasses(annotKClass)
}