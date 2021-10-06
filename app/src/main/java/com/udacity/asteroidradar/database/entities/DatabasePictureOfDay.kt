package com.udacity.asteroidradar.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.PictureOfDay

@Entity
data class DatabasePictureOfDay (
    @PrimaryKey
    val url: String,
    val mimeType: String,
    val title: String
)

fun DatabasePictureOfDay.asDomainModel(): PictureOfDay {
    return PictureOfDay(
        mimeType = mimeType,
        title = title,
        url = url
    )
}