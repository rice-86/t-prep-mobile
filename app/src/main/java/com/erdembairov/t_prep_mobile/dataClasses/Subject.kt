package com.erdembairov.t_prep_mobile.dataClasses

import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val name: String,
    val id: String,
    var parts: ArrayList<Part>
)