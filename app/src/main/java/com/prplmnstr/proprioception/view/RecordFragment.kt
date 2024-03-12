package com.prplmnstr.proprioception.view

import android.animation.LayoutTransition
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.prplmnstr.proprioception.R
import com.prplmnstr.proprioception.adapter.PatientRecordAdapter
import com.prplmnstr.proprioception.databinding.FragmentRecordBinding
import com.prplmnstr.proprioception.model.Record
import com.prplmnstr.proprioception.utils.Constants
import com.prplmnstr.proprioception.utils.Helper
import com.prplmnstr.proprioception.viewmodel.MainViewModel
import java.util.Locale

class RecordFragment : Fragment() {

    private lateinit var binding: FragmentRecordBinding
    private lateinit var recordRvAdapter: PatientRecordAdapter
    private val mainViewModel: MainViewModel by activityViewModels()
    private var recordList: MutableList<Record> = mutableListOf()
    private var filterList: MutableList<Record> = mutableListOf()
    private var filterChecked = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (Helper.isLightTheme(requireContext())) {
            activity?.window?.statusBarColor = requireContext().getColor(R.color.green)

        } else {
            activity?.window?.statusBarColor = requireContext().getColor(R.color.green_dark)
        }
        binding = FragmentRecordBinding.inflate(inflater, container, false)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //set toolbar title
        val patientName = mainViewModel.currentPatient.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        binding.toolbarText.text = "$patientName's Record"

        initializeRecycler()
        loadRecords()

        binding.expandingLinearLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.expandTextview.setOnClickListener {
            val jointType: Drawable? =
                ContextCompat.getDrawable(requireContext(), R.drawable.joint_type)

            val chipsGroupVisibility = if (binding.chipGroup.visibility == View.GONE) {
                val expandLess: Drawable? =
                    ContextCompat.getDrawable(requireContext(), R.drawable.expand_less)
                binding.expandTextview.setCompoundDrawablesWithIntrinsicBounds(
                    jointType,
                    null,
                    expandLess,
                    null
                )
                View.VISIBLE

            } else {
                val expandMore: Drawable? =
                    ContextCompat.getDrawable(requireContext(), R.drawable.expand_more)
                binding.expandTextview.setCompoundDrawablesWithIntrinsicBounds(
                    jointType,
                    null,
                    expandMore,
                    null
                )
                View.GONE
            }
            binding.chipGroup.visibility = chipsGroupVisibility


        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_recordFragment_to_newRecordFragment)
        }

        binding.backButton.setOnClickListener {
            mainViewModel.resetCurrentPatient()
            findNavController().navigateUp()
        }




        for (option in Constants.JOINT_TYPE) {
            val chip = layoutInflater.inflate(R.layout.chip_layout, null) as Chip
            chip.text = option
            chip.isClickable = true
            chip.isCheckable = true

            chip.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    filterList.clear()
                    filterList.addAll(mainViewModel.createFilterList(recordList, option))
                    recordRvAdapter.setList(filterList)
                } else {
                    recordRvAdapter.setList(recordList)
                }
                filterChecked = isChecked
            }

            binding.chipGroup.addView(chip)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            mainViewModel.resetCurrentPatient()
            findNavController().navigateUp()
            true
        }

    }

    private fun loadRecords() {
        mainViewModel.getPatientRecord(mainViewModel.currentPatient.id!!)
            .observe(viewLifecycleOwner, Observer { records ->
                if (records.isNotEmpty()) {
                    recordList.clear()
                    recordList.addAll(records)
                    binding.noRecordImage.visibility = View.GONE
                    binding.recycler.visibility = View.VISIBLE
                } else {
                    binding.recycler.visibility = View.GONE
                    binding.noRecordImage.visibility = View.VISIBLE
                }
                if (filterChecked) {
                    recordRvAdapter.setList(filterList)
                } else {
                    recordRvAdapter.setList(records)
                }
            })
    }

    private fun initializeRecycler() {
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        recordRvAdapter =
            PatientRecordAdapter { deletedRecord: Record -> deletePatient(deletedRecord) }
        binding.recycler.adapter = recordRvAdapter

    }

    private fun deletePatient(deletedRecord: Record) {
        if (filterChecked) {
            filterList.remove(deletedRecord)
        }
        mainViewModel.deleteRecord(deletedRecord)

    }


}