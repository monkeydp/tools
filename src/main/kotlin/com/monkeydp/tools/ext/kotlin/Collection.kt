package com.monkeydp.tools.ext.kotlin

/**
 * @author iPotato
 * @date 2019/12/14
 */
fun Collection<*>.hasIndex(index: Int) = size >= index + 1

fun Collection<*>.hasNoIndex(index: Int) = !hasIndex(index)

fun Collection<*>.equalsX(another: Collection<*>, ignoreSorting: Boolean = false) =
        if (ignoreSorting)
            size == another.size && containsAll(another)
        else this == another

/**
 * val listA = listOf("a", "b", "c")
 * val listB = listOf("b", "c", "d")
 * listA.diff(listB) // [d]
 * listB.diff(listA) // [a]
 */
fun <T> Collection<T>.diff(another: Collection<T>): List<T> =
        filter {
            !another.contains(it)
        }.toList()

fun <T> listOfAll(vararg lists: List<T>): List<T> =
        mutableListOf<T>().apply {
            lists.forEach {
                addAll(it)
            }
        }.toList()
