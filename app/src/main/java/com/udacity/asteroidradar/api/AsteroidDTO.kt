package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.database.entities.DatabaseAsteroid

data class AsteroidDTO(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)


fun List<AsteroidDTO>.asDatabaseModel(): Array<DatabaseAsteroid> {
    return map { asteroidDTO ->
        DatabaseAsteroid(
            id = asteroidDTO.id,
            codename = asteroidDTO.codename,
            closeApproachDate = asteroidDTO.closeApproachDate,
            absoluteMagnitude = asteroidDTO.absoluteMagnitude,
            estimatedDiameter = asteroidDTO.estimatedDiameter,
            relativeVelocity = asteroidDTO.relativeVelocity,
            distanceFromEarth = asteroidDTO.distanceFromEarth,
            isPotentiallyHazardous = asteroidDTO.isPotentiallyHazardous
        )
    }.toTypedArray()
}