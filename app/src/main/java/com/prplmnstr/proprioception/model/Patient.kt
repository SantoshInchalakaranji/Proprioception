package com.prplmnstr.proprioception.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prplmnstr.proprioception.utils.Constants.Companion.PATIENT_TABLE

@Entity(tableName = PATIENT_TABLE)
data class Patient(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var imageId: String,
    var name: String,
    var age: String,
    var gender: Int,
    var lastVisit: String,
    var address: String
)
