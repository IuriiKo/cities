package com.kushyk.test.domain

import com.kushyk.test.data.CitiesRepository
import com.kushyk.test.data.CityDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindCitiesUseCaseImpl @Inject constructor(
    private val repository: CitiesRepository
) : FindCitiesUseCase {
    override fun find(query: String): Flow<List<CityDto>> =
        repository.cities(query)
}