package com.prplmnstr.proprioception.model

data class Record(
    val id:Int,
    val patientId:Int,
    val date: String,
    val jointType:String,
    val degreeValue:Int
)
