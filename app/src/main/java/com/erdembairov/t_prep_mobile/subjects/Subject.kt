package com.erdembairov.t_prep_mobile.subjects

import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class Subject(
    val name: String,
    val user_id: String,
    // val time: Int,
    // val file: File
)
