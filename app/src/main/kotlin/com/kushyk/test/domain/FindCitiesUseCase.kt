package com.kushyk.test.domain

import com.kushyk.test.data.CityDto
import kotlinx.coroutines.flow.Flow

interface FindCitiesUseCase {
    fun find(query: String): Flow<List<CityDto>>
}