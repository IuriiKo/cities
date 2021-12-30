package com.kushyk.test.cities.adapter

sealed interface ViewHolderAction {
    data class Click(val id: Int): ViewHolderAction
}
