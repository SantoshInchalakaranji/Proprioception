package com.prplmnstr.proprioception.view

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.prplmnstr.proprioception.R
import com.prplmnstr.proprioception.databinding.FragmentNewRecordBinding
import com.prplmnstr.proprioception.databinding.FragmentRecordBinding
import com.prplmnstr.proprioception.utils.Constants
import com.prplmnstr.proprioception.utils.Helper
import com.prplmnstr.proprioception.utils.Helper.Companion.getFormattedDate
import com.prplmnstr.proprioception.viewmodel.MainViewModel
import kotlin.math.abs


class NewRecordFragment : Fragment() {
    private lateinit var binding: FragmentNewRecordBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private var readingNumber = ReadingNumber.FirstReading
    private var readValue = 0


    private val bluetoothManager by lazy {
        requireActivity().applicationContext.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    // Check if Bluetooth is enabled
    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(Helper.isLightTheme(requireContext())){
            activity?.window?.statusBarColor = requireContext().getColor(R.color.green)

        }else{
            activity?.window?.statusBarColor = requireContext().getColor(R.color.green_dark)
        }
        binding = FragmentNewRecordBinding.inflate(inflater, container, false)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermission()
        binding.connectButton.setOnClickListener {
            if(binding.connectButton.text.equals("Connect")){
                Toast.makeText(context, "Connecting...", Toast.LENGTH_SHORT).show()
                mainViewModel.startBluetoothService()
            }else{
                Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show()
                mainViewModel.stopBluetoothService()
            }

        }
        binding.card1.strokeWidth = 3

        binding.recordButton.setOnClickListener {
            when(readingNumber){
                ReadingNumber.FirstReading ->{
                    binding.card1.strokeWidth = 0
                    binding.card2.strokeWidth = 3
                    binding.resetButton.isEnabled = true
                    readingNumber = ReadingNumber.SecondReading
                    mainViewModel.currentRecord.initialReading = readValue
                    binding.reading1Text.text =  "$readValue °"
                }
                ReadingNumber.SecondReading ->{
                    binding.card2.strokeWidth = 0
                    binding.card3.strokeWidth = 3
                    binding.resetButton.isEnabled = true
                    binding.recordButton.isEnabled = false
                    readingNumber = ReadingNumber.FirstReading
                    mainViewModel.currentRecord.finalReading = readValue
                    binding.reading2Text.text =  "$readValue °"
                    val difference = mainViewModel.currentRecord.initialReading - mainViewModel.currentRecord.finalReading
                    binding.reading3Text.text = "${abs(difference)} °"
                }
            }
        }

        binding.resetButton.setOnClickListener {
            readingNumber = ReadingNumber.FirstReading
            binding.card2.strokeWidth = 0
            binding.card3.strokeWidth = 0
            binding.card1.strokeWidth = 3
            binding.recordButton.isEnabled = true
            binding.resetButton.isEnabled = false
            binding.reading1Text.text =  "0 °"
            binding.reading2Text.text =  "0 °"
            binding.reading3Text.text =  "0 °"
            readValue  = 0
            mainViewModel.currentRecord.initialReading = 0
            mainViewModel.currentRecord.finalReading = 0
        }

        binding.backButton.setOnClickListener {
            mainViewModel.resetCurrentRecord()
            findNavController().navigateUp()
        }

        binding.saveButton.setOnClickListener {
            if(mainViewModel.currentRecord.jointType.isEmpty()){
                Toast.makeText(context, "Please select joint type", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mainViewModel.currentRecord.patientId = mainViewModel.currentPatient.id!!
            mainViewModel.currentRecord.date = getFormattedDate()
            //remark - databinding
            mainViewModel.addRecord(mainViewModel.currentRecord)
            mainViewModel.stopBluetoothService()
            findNavController().navigateUp()
        }


        for (option in Constants.JOINT_TYPE) {
            val chip =  layoutInflater.inflate(R.layout.chip_layout, null) as Chip
            chip.text = option
            chip.isClickable = true
            chip.isCheckable = true

            chip.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    mainViewModel.currentRecord.jointType = option
                }
            }

            binding.chipGroup.addView(chip)
        }



        mainViewModel.connectionState.observe(viewLifecycleOwner, Observer { state->
            if(state){
                Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show()
                binding.connectButton.text = "Disconnect"
                binding.connectButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bluetooth_connected, 0, 0, 0)

            }else{

                binding.connectButton.text = "Connect"
                binding.connectButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bluetooth_disabled, 0, 0, 0)
            }
        })

        mainViewModel.yValue.observe(viewLifecycleOwner, Observer {
            readValue = it.toInt()
            val progress = (it*100)/360
            binding.progressBar.progress = progress.toInt()
            binding.progressText.text = "$readValue °"
        })


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            mainViewModel.resetCurrentRecord()
            findNavController().navigateUp()

            true
        }

    }
    private fun requestPermission() {
        // Activity Result Launcher for enabling Bluetooth
        val enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { /* Not needed */ }


        // Activity Result Launcher for requesting permissions
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { perms ->
            // Check if Bluetooth permissions are granted
            val canEnableBluetooth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                perms[Manifest.permission.BLUETOOTH_CONNECT] == true
            } else true


            // If Bluetooth permissions are granted and Bluetooth is not enabled, request to enable Bluetooth
            if (canEnableBluetooth && !isBluetoothEnabled) {
                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }

        }

        // Launch permission request based on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            )
        } else {

            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,

                    )
            )
        }
    }


}
enum class ReadingNumber{
    FirstReading,
    SecondReading,

}