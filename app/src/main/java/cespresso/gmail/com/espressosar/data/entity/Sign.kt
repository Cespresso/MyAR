package cespresso.gmail.com.espressosar.data.entity

import com.squareup.moshi.Json

data class Sign(
    @field:Json(name = "image_url")
    val imageUrl:String,
    val name:String
)