package com.kushyk.test.utils

import com.kushyk.test.CityRepositoryRule
import com.kushyk.test.data.CityDto
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.empty
import org.junit.Rule
import org.junit.Test

class ListExtensionsTest {
    @get:Rule
    val cityRepositoryRule = CityRepositoryRule()

    @Test
    fun `search cities with prefix Biri`() {
        val prefix = "Biri"

        val cities = cityRepositoryRule.cities.binarySearchAll(
            prefix,
            suffixCaseInsensitiveComparator,
            CityDto::name
        )

        assertThat(cities.size, `is`(4))
        assertThat(cities.map(CityDto::name), everyItem(startsWith(prefix)))
    }

    @Test
    fun `search cities with invalid prefix`() {
        val prefix = "prefix"

        val cities = cityRepositoryRule.cities.binarySearchAll(
            prefix,
            suffixCaseInsensitiveComparator,
            CityDto::name
        )

        assertThat(cities , `is`(empty()))
    }

    @Test
    fun `search cities with empty prefix`() {
        val prefix = ""

        val cities = cityRepositoryRule.cities.binarySearchAll(
            prefix,
            suffixCaseInsensitiveComparator,
            CityDto::name
        )

        assertThat(cities , `is`(empty()))
    }
}