package com.kushyk.test.data

import android.content.Context
import androidx.annotation.RawRes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kushyk.test.utils.SuffixCaseInsensitiveComparator
import com.kushyk.test.utils.binarySearchAll
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RawCitiesRepository @Inject constructor(
    coroutineScope: CoroutineScope,
    @ApplicationContext private val context: Context,
    @RawRes private val rawId: Int,
    private val gson: Gson
) : CitiesRepository {
    private lateinit var cities: List<CityDto>
    private val extractJob: Job
    private val insensitiveComparator = SuffixCaseInsensitiveComparator()

    init {
        extractJob = coroutineScope.launch {
            cities = extractCities()
        }
    }

    override fun cities(query: String): Flow<List<CityDto>> = flow {
        extractJob.join()
        val foundedCities = cities.binarySearchAll(query, insensitiveComparator) {
            it.name
        }

        emit(foundedCities)
    }

    private fun extractCities(): List<CityDto> {
        val cities: List<CityDto> = context.resources.openRawResource(rawId).bufferedReader().use {
            val type = object : TypeToken<ArrayList<CityDto>>() {}.type
            gson.fromJson(it, type)
        }
        return cities.sortedWith(compareBy(CityDto::name, CityDto::country))
    }
}