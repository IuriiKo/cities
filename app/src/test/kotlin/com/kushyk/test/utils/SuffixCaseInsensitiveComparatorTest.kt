package com.kushyk.test.utils

import com.google.gson.Gson
import com.kushyk.test.data.CityDto
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.BeforeClass
import org.junit.Test
import java.io.File

class SuffixCaseInsensitiveComparatorTest {

    @Test
    fun `compare a valid suffix`() {
        val comparator = SuffixCaseInsensitiveComparator()
        val suffix = "suffix"
        val data = "suffixData"

        val result = comparator.compare(data, suffix)

        assertThat(result, `is`(0))
    }

    @Test
    fun `compare the empty suffix`() {
        val comparator = SuffixCaseInsensitiveComparator()
        val suffix = ""
        val data = "suffixData"

        val result = comparator.compare(data, suffix)

        assertThat(result, not(0))
    }

    @Test
    fun `compare suffix with data where suffix_length more than data_length`() {
        val comparator = SuffixCaseInsensitiveComparator()
        val suffix = "suffix"
        val data = "suf"

        val result = comparator.compare(data, suffix)

        assertThat(result, not(0))
    }

    private companion object {
        val gson = Gson()
        lateinit var cities: List<CityDto>

        @BeforeClass
        @JvmStatic
        fun setupClass() {
            val url = CityDto::class.java.classLoader?.getResource("cities.json")
            val file = File(url!!.path)
            cities = file.bufferedReader().use {
                gson.fromJson(it, Array<CityDto>::class.java)
                    .sortedWith(compareBy(CityDto::name, CityDto::country))
            }
        }
    }
}