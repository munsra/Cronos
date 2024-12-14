package it.pierosilvestri.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionDto(
    val distance: Double,
    @SerialName("session_date") val startDate: String,
    val laps: List<LapDto>?
)
