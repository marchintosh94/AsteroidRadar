package com.udacity.asteroidradar.api

import com.squareup.moshi.Json
import com.udacity.asteroidradar.database.entities.DatabasePictureOfDay
import java.util.*

data class PictureOfDayDTO(
    @Json(name = "media_type")
    val mediaType: String,
    val title: String,
    val url: String
)

fun PictureOfDayDTO.asDatabaseModel(): DatabasePictureOfDay {
    return DatabasePictureOfDay(
        url = url,
        mimeType = mediaType,
        title = title
    )
}