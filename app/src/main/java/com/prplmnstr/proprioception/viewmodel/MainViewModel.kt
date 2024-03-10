package com.prplmnstr.proprioception.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prplmnstr.proprioception.model.Patient
import com.prplmnstr.proprioception.repository.PatientRepository
import com.prplmnstr.proprioception.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val  repository: PatientRepository
):ViewModel() {

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage


    val samplePatient = Patient(
        null, "", "",
        "", 0, "",""
    )

    var currentPatient :Patient

    val patients : LiveData<List<Patient>>
        get() = repository.patients

    init {
        currentPatient = samplePatient.copy()
        Log.e("TAG", "mainview model: created", )
    }

    fun resetCurrentPatient() {currentPatient = samplePatient.copy()}

    fun addPatient(patient: Patient) = viewModelScope.launch(Dispatchers.IO) {

       val result =  repository.addPatient(patient)
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





}