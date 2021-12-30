package com.kushyk.test.data

import com.google.gson.annotations.SerializedName

data class CityDto(
    @SerializedName("_id")
    val id: Int,
    val country: String,
    val name: String,
    @SerializedName("coord")
    val coordinate: CoordinateDto
)
