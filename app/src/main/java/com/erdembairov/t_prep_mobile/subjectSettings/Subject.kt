package com.erdembairov.t_prep_mobile.subjectSettings

import com.erdembairov.t_prep_mobile.qaSettings.QA
import kotlinx.serialization.Serializable

@Serializable
data class Subject(
    val name: String,
    val id: String,
    var qas: ArrayList<QA>
)