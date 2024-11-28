package com.erdembairov.t_prep_mobile.dataClasses

data class QA(
    val question: String,
    val answer: String,
    var boolArrow: Boolean,
    var testStatus: Boolean
)