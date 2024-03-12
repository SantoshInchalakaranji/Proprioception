package com.prplmnstr.proprioception.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.prplmnstr.proprioception.R
import com.prplmnstr.proprioception.databinding.FragmentAddOrUpdatePatientBinding
import com.prplmnstr.proprioception.utils.Constants
import com.prplmnstr.proprioception.utils.Helper
import com.prplmnstr.proprioception.utils.Helper.Companion.isLightTheme
import com.prplmnstr.proprioception.viewmodel.MainViewModel
import java.util.Calendar


class AddOrUpdatePatientFragment : Fragment() {

    private lateinit var binding: FragmentAddOrUpdatePatientBinding
    private var spinnerAdapter: ArrayAdapter<String>? = null
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (isLightTheme(requireContext())) {
            activity?.window?.statusBarColor = requireContext().getColor(R.color.green)

        } else {
            activity?.window?.statusBarColor = requireContext().getColor(R.color.green_dark)
        }
        binding = FragmentAddOrUpdatePatientBinding.inflate(inflater, container, false)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val isNewPatient = mainViewModel.currentPatient.id == null
        if (isNewPatient) {
            binding.customToolbar.title = "New Patient"
            binding.deleteButton.visibility = View.GONE
        } else {
            binding.customToolbar.title = "Update Patient"
            binding.deleteButton.visibility = View.VISIBLE
        }

        // set spinner
        spinnerAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            Constants.GENDER_LIST
        )
        spinnerAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genderSpinner.adapter = spinnerAdapter


        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                mainViewModel.currentPatient.gender = i
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        binding.doneImage.setOnClickListener {
            if (isNewPatient) {
                if (mainViewModel.currentPatient.name.isEmpty()) {
                    binding.patientNameEt.error = "Please enter patient name"
                } else {
                    mainViewModel.addPatient(mainViewModel.currentPatient)
                    findNavController().navigateUp()
                }
            } else {
                if (mainViewModel.currentPatient.name.isEmpty()) {
                    binding.patientNameEt.error = "Please enter patient name"
                } else {
                    mainViewModel.updatePatient(mainViewModel.currentPatient)
                    findNavController().navigateUp()
                }
            }

        }

        binding.dateEditText.setOnClickListener {
            showDatePickerDialog(binding.dateEditText)


        }

        binding.deleteButton.setOnClickListener {
            mainViewModel.deletePatient(mainViewModel.currentPatient)
            findNavController().navigateUp()
        }


        //on back press
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            mainViewModel.resetCurrentPatient()
            findNavController().navigateUp()
            true
        }
    }

    fun showDatePickerDialog(editText: EditText) {
        // Get the current date

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a DatePickerDialog and set the listener
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                // Update the selected date in your UI


                editText.setText(Helper.getDateInStringFormat(day, month + 1, year))


            },
            year, month, dayOfMonth
        )

        // Show the date picker dialog
        datePickerDialog.show()

    }
}