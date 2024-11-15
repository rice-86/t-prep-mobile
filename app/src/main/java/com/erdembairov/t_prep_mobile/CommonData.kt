package com.erdembairov.t_prep_mobile

import com.erdembairov.t_prep_mobile.partSettings.Part
import com.erdembairov.t_prep_mobile.subjectSettings.Subject

object CommonData {
    lateinit var authToken: String
    val user_id = "1"
    lateinit var openedSubject: Subject
    lateinit var openedPart: Part
    lateinit var subjects: ArrayList<Subject>
}