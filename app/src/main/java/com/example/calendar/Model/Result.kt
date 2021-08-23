package com.example.calendar.Model


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("content")
    val content: String,
    @SerializedName("dateEnd")
    val dateEnd: String,
    @SerializedName("dateStart")
    val dateStart: String,
    @SerializedName("id")
    val id: Int
)