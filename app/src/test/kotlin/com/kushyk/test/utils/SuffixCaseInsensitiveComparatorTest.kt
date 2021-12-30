package com.kushyk.test.utils

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class SuffixCaseInsensitiveComparatorTest {

    @Test
    fun `compare a valid suffix`() {
        val suffix = "suffix"
        val data = "suffixData"

        val result = suffixCaseInsensitiveComparator.compare(data, suffix)

        assertThat(result, `is`(0))
    }

    @Test
    fun `compare the empty suffix`() {
        val suffix = ""
        val data = "suffixData"

        val result = suffixCaseInsensitiveComparator.compare(data, suffix)

        assertThat(result, not(0))
    }

    @Test
    fun `compare suffix with data where suffix_length more than data_length`() {
        val suffix = "suffix"
        val data = "suf"

        val result = suffixCaseInsensitiveComparator.compare(data, suffix)

        assertThat(result, not(0))
    }
}