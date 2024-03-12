package com.prplmnstr.proprioception.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.prplmnstr.proprioception.R
import com.prplmnstr.proprioception.adapter.PatientRvAdapter
import com.prplmnstr.proprioception.databinding.FragmentHomeBinding
import com.prplmnstr.proprioception.model.Patient
import com.prplmnstr.proprioception.utils.Helper.Companion.isLightTheme
import com.prplmnstr.proprioception.viewmodel.MainViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var patientRvAdapter: PatientRvAdapter
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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initializeRecycler()
        loadPatients()

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addOrUpdatePatientFragment)
        }
    }

    private fun loadPatients() {
        mainViewModel.patients.observe(viewLifecycleOwner, Observer { patientList ->
            if (patientList.isNotEmpty()) {
                binding.noRecordImage.visibility = View.GONE
                binding.recycler.visibility = View.VISIBLE

            } else {
                binding.recycler.visibility = View.GONE
                binding.noRecordImage.visibility = View.VISIBLE
            }
            patientRvAdapter.setList(patientList)
        })
    }


    private fun initializeRecycler() {
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        patientRvAdapter =
            PatientRvAdapter({ selectedPatient: Patient -> patientSelected(selectedPatient) },
                { patientInfoClick: Patient -> infoClick(patientInfoClick) })
        binding.recycler.adapter = patientRvAdapter

    }

    private fun patientSelected(selectedPatient: Patient) {
        mainViewModel.currentPatient = selectedPatient.copy()
        findNavController().navigate(R.id.action_homeFragment_to_recordFragment)
    }

    private fun infoClick(patientInfoClick: Patient) {
        mainViewModel.currentPatient = patientInfoClick.copy()
        findNavController().navigate(R.id.action_homeFragment_to_addOrUpdatePatientFragment)

    }


}