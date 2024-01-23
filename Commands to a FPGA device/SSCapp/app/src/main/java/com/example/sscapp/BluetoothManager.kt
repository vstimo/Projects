package com.example.sscapp
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.util.UUID
class BluetoothManager(private val context: Context) {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var bluetoothSocket: BluetoothSocket? = null
    private var communicationThread: HandlerThread? = null
    private var communicationHandler: Handler? = null
    private var connectedThread: ConnectedThread? = null

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                // Adăugați dispozitivul la o listă sau afișați numele și adresa acestuia
            }
        }
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 1
        private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

    fun initializeBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Bluetooth not supported", Toast.LENGTH_SHORT).show()
            return
        }
        if (!bluetoothAdapter.isEnabled) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Request Bluetooth permission
                return
            }
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            (context as ComponentActivity).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {
            startBluetoothDiscovery()
            communicationThread = HandlerThread("CommunicationThread")
            communicationThread?.start()
            communicationHandler = Handler(communicationThread?.looper ?: Looper.getMainLooper())
        }
    }

    fun connectToDevice(deviceAddress: String) {
        val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress)
        // UUID should match the UUID that the server device is listening on.
        val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // Standard SerialPortService ID
        try {
            showToast("Connecting to device...")
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
            bluetoothSocket?.connect()
            showToast("Connected to ${device.name}")
            connectedThread = ConnectedThread(bluetoothSocket!!)
            connectedThread?.start()
            // You can now use the socket to communicate with the device
        } catch (e: IOException) {
            showToast("Connection failed: ${e.message}")
            try {
                bluetoothSocket?.close()
            } catch (ex: IOException) {
                showToast("Socket close failed: ${ex.message}")
            }
        }
    }
    fun receiveData(): Int? {
       return connectedThread?.getData()
    }
    fun sendData(data: Int) {
        communicationHandler?.post {
            connectedThread?.sendStringData(data)
        }
    }

    private fun showToast(message: String) {
        (context as ComponentActivity).runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    fun getPairedDevices(): Set<BluetoothDevice>? {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }
        return bluetoothAdapter.bondedDevices
    }

    fun startBluetoothDiscovery() {
        val discoverDevicesIntent = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(receiver, discoverDevicesIntent)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        bluetoothAdapter.startDiscovery()
    }

    fun onDestroy() {
        context.unregisterReceiver(receiver)
        try {
            communicationThread?.quitSafely()
            communicationThread?.join()
            bluetoothSocket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}
