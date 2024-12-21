package com.erdembairov.t_prep_mobile.dataClasses

// Класс для вопроса
data class QA(
    val question: String,
    var answer: String,
    var boolArrow: Boolean,
    var testStatus: Boolean,
)