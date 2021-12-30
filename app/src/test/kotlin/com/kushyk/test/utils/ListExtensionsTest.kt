package com.kushyk.test.utils

import com.kushyk.test.CityRepositoryRule
import com.kushyk.test.data.CityDto
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.hasSize
import org.junit.Rule
import org.junit.Test

class ListExtensionsTest {
    @get:Rule
    val cityRepositoryRule = CityRepositoryRule()

    @Test
    fun `search for cities in an empty list`() {
        val prefix = "Biri"

        val cities = emptyList<CityDto>().binarySearchAll(
            prefix,
            suffixCaseInsensitiveComparator,
            CityDto::name
        )

        assertThat(cities, empty())
    }

    @Test
    fun `search for cities with prefix Biri`() {
        val prefix = "Biri"

        val cities = cityRepositoryRule.cities.binarySearchAll(
            prefix,
            suffixCaseInsensitiveComparator,
            CityDto::name
        )

        assertThat(cities.size, `is`(10))
        assertThat(cities.map(CityDto::name), everyItem(startsWith(prefix)))
    }

    @Test
    fun `search for cities with invalid prefix`() {
        val prefix = "prefix"

        val cities = cityRepositoryRule.cities.binarySearchAll(
            prefix,
            suffixCaseInsensitiveComparator,
            CityDto::name
        )

        assertThat(cities, `is`(empty()))
    }

    @Test
    fun `search for cities with empty prefix`() {
        val prefix = ""

        val cities = cityRepositoryRule.cities.binarySearchAll(
            prefix,
            suffixCaseInsensitiveComparator,
            CityDto::name
        )

        assertThat(cities, `is`(empty()))
    }

    @Test
    fun `search for cities with prefix denia`() {
        val prefix = "denia"

        val cities = cityRepositoryRule.cities.binarySearchAll(
            prefix,
            suffixCaseInsensitiveComparator,
            CityDto::name
        )

        assertThat(cities, hasSize(2))
    }

    @Test
    fun `search for cities in the beginning`() {
        val prefix = "de"
        val list = listOf("den", "deni", "denis", "dynis")

        val cities = list.binarySearchAll(
            prefix,
            suffixCaseInsensitiveComparator,
            { it }
        )

        assertThat(cities, hasSize(3))
    }

    @Test
    fun `search for cities in the end`() {
        val prefix = "de"
        val list = listOf("dan", "den", "deni", "denis")

        val cities = list.binarySearchAll(
            prefix,
            suffixCaseInsensitiveComparator,
            { it }
        )

        assertThat(cities, hasSize(3))
    }

    @Test
    fun `search for cities in the middle`() {
        val prefix = "den"
        val list = listOf("dan", "den", "deni", "denis", "develop")

        val cities = list.binarySearchAll(
            prefix,
            suffixCaseInsensitiveComparator,
            { it }
        )

        assertThat(cities, hasSize(3))
    }
}