package com.invitation.models

import com.google.gson.annotations.SerializedName

data class LocationModel(
    @SerializedName("title") val title: String,
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)