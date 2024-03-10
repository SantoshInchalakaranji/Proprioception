package com.prplmnstr.proprioception.view

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.prplmnstr.proprioception.R
import com.prplmnstr.proprioception.databinding.FragmentAddOrUpdatePatientBinding
import com.prplmnstr.proprioception.databinding.FragmentRecordBinding
import com.prplmnstr.proprioception.utils.Helper
import com.prplmnstr.proprioception.viewmodel.MainViewModel

class RecordFragment : Fragment() {

    private lateinit var binding: FragmentRecordBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(Helper.isLightTheme(requireContext())){
            activity?.window?.statusBarColor = requireContext().getColor(R.color.green)

        }else{
            activity?.window?.statusBarColor = requireContext().getColor(R.color.green_dark)
        }
        binding = FragmentRecordBinding.inflate(inflater, container, false)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.expandingLinearLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.expandTextview.setOnClickListener {
            val jointType: Drawable? = ContextCompat.getDrawable(requireContext(),R.drawable.joint_type)

            val chipsGroupVisibility = if(binding.chipGroup.visibility == View.GONE){
                val expandLess: Drawable? =ContextCompat.getDrawable(requireContext(),R.drawable.expand_less)
                binding.expandTextview.setCompoundDrawablesWithIntrinsicBounds(jointType,null,expandLess,null)
                View.VISIBLE

            } else{
                val expandMore: Drawable? = ContextCompat.getDrawable(requireContext(),R.drawable.expand_more)
                binding.expandTextview.setCompoundDrawablesWithIntrinsicBounds(jointType,null,expandMore,null)
                View.GONE
            }
            binding.chipGroup.visibility = chipsGroupVisibility


        }

        binding.backButton.setOnClickListener {
            mainViewModel.resetCurrentPatient()
            findNavController().navigateUp()
        }

        val filterOptions = arrayOf("Shoulder front", "Shoulder Side", "Knee","Elbow", "Hamstring", "Ankle")
        for (option in filterOptions) {
            val chip =  layoutInflater.inflate(R.layout.chip_layout, null) as Chip
            chip.text = option
            chip.isClickable = true
            chip.isCheckable = true

            chip.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    //Toast.makeText(context, option, Toast.LENGTH_SHORT).show()
                }else{
                    //Toast.makeText(context, option, Toast.LENGTH_SHORT).show()
                }
            }

            binding.chipGroup.addView(chip)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            mainViewModel.resetCurrentPatient()
            findNavController().navigateUp()

            true
        }
    }
}