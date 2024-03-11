package com.prplmnstr.proprioception.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.prplmnstr.proprioception.utils.Constants

@Entity(tableName = Constants.RECORD_TABLE,
    foreignKeys = [
        ForeignKey(entity = Patient::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE)
    ])
data class Record(
    @PrimaryKey(autoGenerate = true)
    var id:Int?,
    var patientId:Int,
    var date: String,
    var jointType:String,
    var initialReading:Int,
    var finalReading:Int,
    var remark: String
)
