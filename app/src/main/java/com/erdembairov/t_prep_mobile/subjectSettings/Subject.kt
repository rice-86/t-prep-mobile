package com.erdembairov.t_prep_mobile.subjectSettings

import com.erdembairov.t_prep_mobile.partSettings.Part
import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val name: String,
    val id: String,
    var parts: ArrayList<Part>
)