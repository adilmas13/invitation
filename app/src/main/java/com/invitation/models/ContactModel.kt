package com.invitation.models

import com.google.gson.annotations.SerializedName


data class ContactModel(
    @SerializedName("name") val name: String,
    @SerializedName("number") val number: String,
    @SerializedName("image") val image: String
)