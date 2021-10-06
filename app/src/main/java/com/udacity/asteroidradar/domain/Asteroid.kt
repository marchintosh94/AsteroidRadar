package com.udacity.asteroidradar.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Asteroid (
    val id: Long,
    val code: String,
    val closeApproachDate: String,
    val magnitude: Double,
    val diameter: Double,
    val velocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
): Parcelable