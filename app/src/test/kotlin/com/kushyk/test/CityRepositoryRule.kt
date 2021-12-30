package com.kushyk.test

import com.google.gson.Gson
import com.kushyk.test.data.CityDto
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.File

class CityRepositoryRule : TestRule {
    private val gson = Gson()
    lateinit var cities: List<CityDto>

    override fun apply(
        base: Statement,
        description: Description?
    ): Statement = object : Statement() {
        override fun evaluate() {
            cities = retrieveSortedCities()
            base.evaluate()
        }
    }

    fun retrieveSortedCities(): List<CityDto> {
        val url = CityDto::class.java.classLoader?.getResource("cities.json")
        val file = File(url!!.path)
        return file.bufferedReader().use {
            gson.fromJson(it, Array<CityDto>::class.java)
                .sortedWith(compareBy(CityDto::name, CityDto::country))
        }
    }
}