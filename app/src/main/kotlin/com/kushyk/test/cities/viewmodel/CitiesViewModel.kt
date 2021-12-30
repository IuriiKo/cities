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
) : ViewModel() {
    private val queryFlow = MutableSharedFlow<String?>()
    private val _viewState = MutableStateFlow<ViewState>(ViewState.StartSearch)
    val viewState = _viewState.asStateFlow()
    private val _viewAction = MutableSharedFlow<ViewAction>(extraBufferCapacity = 1)
    val viewAction = _viewAction.asSharedFlow()

    init {
        observeQuery()
    }

    fun onClickItem(model: CityModel) {
        _viewAction.tryEmit(ViewAction.ShowLocation(model.longitude, model.latitude))
    }

    fun onTextChange(query: String?) {
        viewModelScope.launch {
            queryFlow.emit(query)
        }
    }

    private fun observeQuery() {
        viewModelScope.launch {
            queryFlow.debounce(400.milliseconds)
                .flatMapConcat<String?, List<CityDto>?> {
                    if (it.isNullOrEmpty()) flow { emit(null) } else citiesUseCase.find(it)
                }
                .map { it?.map { city -> city.toModel() } }
                .map {
                    when {
                        it == null -> ViewState.StartSearch
                        it.isEmpty() -> ViewState.NotFound
                        else -> ViewState.Data(it)
                    }
                }
                .collect(_viewState)
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

