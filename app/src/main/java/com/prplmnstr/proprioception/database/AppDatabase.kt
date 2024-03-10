package com.prplmnstr.proprioception.database


import androidx.room.Database

import androidx.room.RoomDatabase
import com.prplmnstr.proprioception.model.Patient



@Database(
    entities = [ Patient::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun patientDao(): PatientDao

}