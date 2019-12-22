package com.monkeydp.tools.ext.kodein.component.abstr

import com.monkeydp.tools.ext.kodein.component.KodeinComponent
import com.monkeydp.tools.ext.kodein.component.contract.KodeinComp
import com.monkeydp.tools.ext.kodein.component.contract.KodeinCompRepo
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
import org.reflections.scanners.Scanner
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.reflect.KClass

/**
 * @author iPotato
 * @date 2019/12/14
 */
abstract class AbstractKodeinCompRepo : KodeinCompRepo {
    
    companion object {
        val log = getLogger()
    }
    
    private val compAnnotKClass = KodeinComponent::class
    
    private val defaultScanners: Array<Scanner> = arrayOf(*reflectionsDefaultScanners, FieldAnnotationsScanner())
    
    protected fun reflections(
            packageNames: List<String> = listOf(javaClass.`package`.name),
            classLoader: ClassLoader = javaClass.classLoader,
            vararg scanners: Scanner = defaultScanners
    ) = Reflections(
            packageNames,
            classLoader,
            scanners
    )
    
    /**
     * @see compAnnotKClass Used to find annotation annotated by `compAnnotKClass`
     */
    protected open val annotReflections: Reflections = reflections()
    
    /**
     * Used to find comp
     */
    protected open val compReflections: Reflections = reflections()
    
    override val comps: Collection<KodeinComp> by lazy { findAllComps() }
    
    private fun findAllComps(): Collection<KodeinComp> =
            annotReflections.getAnnotatedAnnotKClasses(compAnnotKClass, true)
                    .map { findComps(it) }.flatten()
    
    private fun findComps(annotKClass: KClass<out Annotation>): Collection<KodeinComp> {
        val comps = mutableListOf<KodeinComp>()
        val target = annotKClass.findAnnot<Target>()
        target.allowedTargets.forEach {
            val part = when (it) {
                CLASS -> compReflections.getAnnotatedKClasses(annotKClass).map {
                    kodeinKClassComp {
                        annot = it.findAnnot(annotKClass)
                        annotatedKClass = it
                    }
                }
                FIELD -> compReflections.getAnnotatedFieldValueMap(annotKClass, true).map { (field, value) ->
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