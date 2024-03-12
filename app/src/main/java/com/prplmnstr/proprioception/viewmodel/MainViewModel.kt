package com.prplmnstr.proprioception.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prplmnstr.proprioception.model.Patient
import com.prplmnstr.proprioception.model.Record
import com.prplmnstr.proprioception.repository.PatientRepository
import com.prplmnstr.proprioception.utils.Event
import com.prplmnstr.proprioception.utils.bluetooth.BluetoothService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PatientRepository,
    private val bluetoothService: BluetoothService
) : ViewModel() {

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage


    val samplePatient = Patient(
        null, "", "",
        "", 0, "", ""
    )

    val sampleRecord = Record(
        null, -1, "", "",
        0, 0, ""
    )

    var currentPatient: Patient
    var currentRecord: Record


    val patients: LiveData<List<Patient>>
        get() = repository.patients

    val yValue: LiveData<Double> = bluetoothService.yValue

    val connectionState = bluetoothService.connectionState

    init {

        currentPatient = samplePatient.copy()
        currentRecord = sampleRecord.copy()
        Log.e("TAG", "mainview model: created")
    }

    fun resetCurrentPatient() {
        currentPatient = samplePatient.copy()
    }

    fun resetCurrentRecord() {
        currentRecord = sampleRecord.copy()
    }


    fun addPatient(patient: Patient) = viewModelScope.launch(Dispatchers.IO) {

        val result = repository.addPatient(patient)
        resetCurrentPatient()

    }

    fun updatePatient(patient: Patient) = viewModelScope.launch(Dispatchers.IO) {
        repository.updatePatient(patient)
        resetCurrentPatient()

    }

    fun deletePatient(patient: Patient) = viewModelScope.launch(Dispatchers.IO) {
        repository.deletePatient(patient)
        resetCurrentPatient()
    }


    fun addRecord(record: Record) = viewModelScope.launch(Dispatchers.IO) {
        repository.addRecord(record)
        resetCurrentRecord()
    }

    fun deleteRecord(record: Record) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteRecord(record)
        resetCurrentRecord()
    }

    fun getPatientRecord(patientId: Int) =
        repository.getPatientRecord(patientId)


    fun startBluetoothService() {

        bluetoothService.connectBluetoothDevice()

    }

    fun stopBluetoothService() {
        bluetoothService.disconnectBluetoothDevice()
    }

    fun createFilterList(recordList: MutableList<Record>, option: String): MutableList<Record> {
        val filteredList = mutableListOf<Record>()

        for (record in recordList) {
            // Assuming you want to filter based on the jointType field
            if (record.jointType == option) {
                filteredList.add(record)
            }
        }

        return filteredList
    }

}