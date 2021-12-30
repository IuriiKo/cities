package com.kushyk.test.utils

fun <E, T> List<E>.binarySearchAll(
    prefix: T,
    comparator: Comparator<T>,
    comparison: (E) -> T
): List<E> {
    val fromIndex: Int = binarySearchFromIndex(prefix, size, comparator, comparison)
    val toIndex = binarySearchToIndex(prefix, fromIndex, comparator, comparison)

    return if (fromIndex >= 0) subList(fromIndex, toIndex) else emptyList()
}

private fun <E, T> List<E>.binarySearchToIndex(
    prefix: T,
    fromIndex: Int,
    comparator: Comparator<T>,
    comparison: (E) -> T
): Int {
    if (fromIndex < 0) return fromIndex
    if (fromIndex >= size) return size

    val index = binarySearch(fromIndex, size) { comparator.compare(comparison(it), prefix) }
    return if (index in 1 until size) binarySearchToIndex(
        prefix,
        index + 1,
        comparator,
        comparison
    ) else fromIndex
}

private fun <E, T> List<E>.binarySearchFromIndex(
    prefix: T,
    toIndex: Int,
    comparator: Comparator<T>,
    comparison: (E) -> T
): Int {
    if (toIndex <= 0) return toIndex
    if (toIndex > size) return -1
    val index = binarySearch(0, toIndex) { comparator.compare(comparison(it), prefix) }
    return when {
        index > 0 -> binarySearchFromIndex(prefix, index, comparator, comparison)
        index == 0 -> index
        else -> toIndex
    }
}