package com.prplmnstr.proprioception.repository

import com.prplmnstr.proprioception.database.PatientDao
import com.prplmnstr.proprioception.model.Patient
import javax.inject.Inject

class PatientRepository @Inject constructor(
    private val patientDao: PatientDao
) {

    val patients = patientDao.getAllPatients()

    suspend fun addPatient(patient: Patient) = patientDao.insertPatient(patient)
    suspend fun updatePatient(patient: Patient) = patientDao.updatePatient(patient)
    suspend fun deletePatient(patient: Patient) = patientDao.deletePatient(patient)
}