package com.kushyk.test.utils

fun <E, T> List<E>.binarySearchAll(
    prefix: T,
    comparator: Comparator<T>,
    comparison: (E) -> T
): List<E> {
    val startIndex = binarySearch { comparator.compare(comparison(it), prefix) }

    return if (startIndex >= 0) {
        var toIndex = startIndex
        while (++toIndex < size) {
            val isFailed =
                comparator.compare(comparison(get(toIndex)), prefix) != 0
            if (isFailed) {
                break
            }
        }
        subList(startIndex, toIndex = toIndex)
    } else {
        emptyList()
    }
}