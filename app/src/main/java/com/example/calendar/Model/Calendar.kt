package com.example.calendar.Model


import com.google.gson.annotations.SerializedName

data class Calendar(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: List<Result>
)