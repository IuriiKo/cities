package com.kushyk.test.cities.adapter

import com.kushyk.test.cities.viewmodel.CityModel

sealed interface ViewHolderAction {
    data class Click(val model: CityModel): ViewHolderAction
}
