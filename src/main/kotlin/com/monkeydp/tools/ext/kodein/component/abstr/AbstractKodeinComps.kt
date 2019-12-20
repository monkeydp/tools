package com.monkeydp.tools.ext.kodein.component.abstr

import com.monkeydp.tools.ext.kodein.component.KodeinComponent
import com.monkeydp.tools.ext.kodein.component.contract.KodeinComp
import com.monkeydp.tools.ext.kodein.component.contract.KodeinComps
import com.monkeydp.tools.ext.kodein.component.contract.kodeinFieldComp
import com.monkeydp.tools.ext.kodein.component.contract.kodeinKClassComp
import com.monkeydp.tools.ext.kotlin.findAnnot
import com.monkeydp.tools.ext.logger.getLogger
import com.monkeydp.tools.ext.reflections.getAnnotatedAnnotKClasses
import com.monkeydp.tools.ext.reflections.getAnnotatedFieldValueMap
import com.monkeydp.tools.ext.reflections.getAnnotatedKClasses
import com.monkeydp.tools.ext.reflections.reflectionsDefaultScanners
import org.reflections.Reflections
import org.reflections.scanners.FieldAnnotationsScanner
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2019/12/14
 */
abstract class AbstractKodeinComps : KodeinComps {
    
    companion object {
        val log = getLogger()
    }
    
    private val compAnnotKClass = KodeinComponent::class
    
    protected open val packageNames = listOf(javaClass.`package`.name)
    
    private val reflections = Reflections(
            packageNames,
            javaClass.classLoader,
            *reflectionsDefaultScanners,
            FieldAnnotationsScanner()
    )
    
    override val comps: Collection<KodeinComp> by lazy { findAllComps() }
    
    private fun findAllComps(): Collection<KodeinComp> =
            reflections.getAnnotatedAnnotKClasses(compAnnotKClass, true)
                    .map { findComps(it) }.flatten()
    
    private fun findComps(annotKClass: KClass<out Annotation>): Collection<KodeinComp> {
        val comps = mutableListOf<KodeinComp>()
        val target = annotKClass.findAnnot<Target>()
        target.allowedTargets.forEach {
            val part = when (it) {
                CLASS -> reflections.getAnnotatedKClasses(annotKClass).map {
                    kodeinKClassComp {
                        annot = it.findAnnot(annotKClass)
                        annotatedKClass = it
                    }
                }
                FIELD -> reflections.getAnnotatedFieldValueMap(annotKClass, true).map { (field, value) ->
                    kodeinFieldComp {
                        annot = field.getAnnotation(annotKClass.java)
                        this.field = field
                        this.value = value
                    }
                }
                else -> return@forEach
            }
            comps.addAll(part)
        }
        return comps.toList()
    }
}