package com.kushyk.test.utils

import java.util.*

class SuffixCaseInsensitiveComparator : Comparator<String> {
    override fun compare(s1: String, s2: String): Int {
        val n1 = s1.length
        val n2 = s2.length

        for (i in s2.indices) {
            if (i >= n1) return n1 - n2
            var c1 = s1[i]
            var c2 = s2[i]
            if (c1 != c2) {
                c1 = Character.toUpperCase(c1)
                c2 = Character.toUpperCase(c2)
                if (c1 != c2) {
                    c1 = Character.toLowerCase(c1)
                    c2 = Character.toLowerCase(c2)
                    if (c1 != c2) {
                        // No overflow because of numeric promotion
                        return c1 - c2
                    }
                }
            }

            if (i == s2.lastIndex) return 0
        }
        return n1 - n2
    }
}