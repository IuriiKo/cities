package com.kushyk.test.data

import kotlinx.coroutines.flow.Flow

interface CitiesRepository {
    fun cities(query: String): Flow<List<CityDto>>
}