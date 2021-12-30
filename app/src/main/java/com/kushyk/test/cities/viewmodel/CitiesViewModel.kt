package com.kushyk.test.cities.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kushyk.test.data.CityDto
import com.kushyk.test.domain.FindCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime

@FlowPreview
@ExperimentalTime
@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val citiesUseCase: FindCitiesUseCase
): ViewModel() {
    private val queryFlow = MutableSharedFlow<String?>()
    private val _viewState = MutableStateFlow<ViewState>(ViewState.StartSearch)
    val viewState = _viewState.asStateFlow()
    private val _viewAction = MutableSharedFlow<ViewAction>()
    val viewAction = _viewAction.asSharedFlow()

    init {
        viewModelScope.launch {
            queryFlow.debounce(400.milliseconds)
                .flatMapConcat {
                    if (it != null) citiesUseCase.find(it) else flow { emit(emptyList()) }
                }
                .map {it.map { city -> city.toModel()}}
                .collect {
                    _viewState.emit(ViewState.Data(it))
                }
        }
    }

    fun onClickItem(id: Int) {
        when (val viewState = viewState.value) {
            is ViewState.Data -> {
                val index = viewState.cities.binarySearch { it.id.compareTo(id) }
                if (index >= 0) {
                    val item = viewState.cities[index]
                    _viewAction.tryEmit(ViewAction.ShowLocation(item.longitude, item.latitude))
                }
            }
            else -> {
                // Invalid state
            }
        }
    }

    fun onTextChange(query: String?) {
        viewModelScope.launch {
            queryFlow.emit(query)
        }
    }

    sealed interface ViewState {
        object StartSearch : ViewState
        object NotFound : ViewState
        data class Data(val cities: List<CityModel>) : ViewState
    }

    sealed interface ViewAction {
        data class ShowLocation(
            val longitude: Float,
            val latitude: Float
        ) : ViewAction
    }

    private fun CityDto.toModel(): CityModel =
        CityModel(
            id = id,
            title = name,
            description = country,
            longitude = coordinate.longitude,
            latitude = coordinate.latitude
        )
}

