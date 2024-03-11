package com.prplmnstr.proprioception.utils.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.util.UUID


class BluetoothService(
    private val context: Context
) : Service() {
    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }
    private lateinit var bluetoothSocket: BluetoothSocket
    private lateinit var inputStream: InputStream
     var isBluetoothConnected: Boolean = false
    // LiveData to hold y value

    private val _connectionState = MutableLiveData<Boolean>().apply {
        value = false
    }
    val connectionState: LiveData<Boolean> = _connectionState


    // LiveData to hold y value
    private val _yValue = MutableLiveData<Double>()
    val yValue: LiveData<Double> = _yValue



    override fun onDestroy() {
        super.onDestroy()
        disconnectBluetoothDevice()
    }

    @SuppressLint("MissingPermission")
     fun connectBluetoothDevice() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            Toast.makeText(context, "Please Enable Bluetooth Permissions. ", Toast.LENGTH_SHORT)
                .show()
             throw SecurityException("No BLUETOOTH_CONNECT permission")
        }



        val bluetoothDevice: BluetoothDevice? = bluetoothAdapter!!.bondedDevices
            .find { it.name == "KLE6" } // Replace with your device name

        if (bluetoothDevice != null) {
            Log.e("TAG", "${bluetoothDevice.address}", )
            try {
                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(
                    SERVICE_UUID))
                bluetoothSocket.connect()
                inputStream = bluetoothSocket.inputStream
                isBluetoothConnected = true
                _connectionState.postValue(true)

                startReadingData()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }else{
            Toast.makeText(context, "Device not found", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun startReadingData() {
        GlobalScope.launch(Dispatchers.IO) {
            while (isBluetoothConnected) {
                try {
                    val data = inputStream.bufferedReader().readLine().toDouble()
                    _yValue.postValue(data)
                    Log.e("TAG", "startReadingData   : $data", )

                } catch (e: Exception) {
                    disconnectBluetoothDevice()
                    Log.e("TAG", "startReadingData   : error", )
                    e.printStackTrace()
                }

            }
        }
    }

    private fun stopReadingData() {
        isBluetoothConnected = false
        _connectionState.postValue(false)
        try {
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

     fun disconnectBluetoothDevice() {
        isBluetoothConnected = false
        _connectionState.postValue(false)
        try {
            bluetoothSocket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun hasPermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }

    }

    companion object {
        const val SERVICE_UUID = "00001101-0000-1000-8000-00805F9B34FB"
    }

}
