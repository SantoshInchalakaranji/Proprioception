package com.prplmnstr.proprioception.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.prplmnstr.proprioception.getOrAwaitValue
import com.prplmnstr.proprioception.model.Patient
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class PatientDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var patientDao: PatientDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).allowMainThreadQueries().build()

        patientDao = database.patientDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun insertPatient_inserted() = runBlocking {
        val patient = Patient(0,"","santosh","23",0,"12 feb","nej")
        patientDao.insertPatient(patient)
        val patients = patientDao.getAllPatients().getOrAwaitValue()
        assertThat(patients).contains(patient)
    }

    @Test
    fun deletePatient_deleted() = runBlocking {
        val patient = Patient(0,"","santosh","23",0,"12 feb","nej")
        patientDao.insertPatient(patient)
        patientDao.deletePatient(patient)
        val patients = patientDao.getAllPatients().getOrAwaitValue()
        assertThat(patients).doesNotContain(patient)
    }
}