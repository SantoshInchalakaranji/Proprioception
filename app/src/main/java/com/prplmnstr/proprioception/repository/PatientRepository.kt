package com.prplmnstr.proprioception.repository

import com.prplmnstr.proprioception.database.PatientDao
import com.prplmnstr.proprioception.database.RecordDao
import com.prplmnstr.proprioception.model.Patient
import com.prplmnstr.proprioception.model.Record
import javax.inject.Inject

class PatientRepository @Inject constructor(
    private val patientDao: PatientDao,
    private val recordDao: RecordDao
) {

    val patients = patientDao.getAllPatients()

    suspend fun addPatient(patient: Patient) = patientDao.insertPatient(patient)
    suspend fun updatePatient(patient: Patient) = patientDao.updatePatient(patient)
    suspend fun deletePatient(patient: Patient) = patientDao.deletePatient(patient)

    //records
    suspend fun addRecord(record: Record) = recordDao.insertRecord(record)
    suspend fun updateRecord(record: Record) = recordDao.updateRecord(record)
    suspend fun deleteRecord(record: Record) = recordDao.deleteRecord(record)
    fun getPatientRecord(patientId: Int) = recordDao.getAllRecords(patientId)
}