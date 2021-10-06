package com.udacity.asteroidradar.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.Asteroid

@Entity
data class DatabaseAsteroid (
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

fun List<DatabaseAsteroid>.asDomaniModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            code = it.codename,
            closeApproachDate = it.closeApproachDate,
            magnitude = it.absoluteMagnitude,
            diameter = it.estimatedDiameter,
            velocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}
