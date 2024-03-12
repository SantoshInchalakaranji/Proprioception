package com.prplmnstr.proprioception.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.prplmnstr.proprioception.model.Patient
import com.prplmnstr.proprioception.utils.Constants

@Dao
interface PatientDao {

    @Insert
    suspend fun insertPatient(patient: Patient): Long

    @Update
    suspend fun updatePatient(patient: Patient): Int

    @Delete
    suspend fun deletePatient(patient: Patient): Int

    @Query("SELECT * FROM ${Constants.PATIENT_TABLE}")
    fun getAllPatients(): LiveData<List<Patient>>
}