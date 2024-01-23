package com.example.sscapp
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ConnectedThread(private val bluetoothSocket: BluetoothSocket) : Thread() {
    private val inputStream: InputStream?
    private val outputStream: OutputStream?
    var fromBoard: Int = 0

    init {
        var tempIn: InputStream? = null
        var tempOut: OutputStream? = null

        try {
            tempIn = bluetoothSocket.inputStream
            tempOut = bluetoothSocket.outputStream
        } catch (e: IOException) {
            e.printStackTrace()
        }

        inputStream = tempIn
        outputStream = tempOut
    }
    override fun run() {
        val buffer = ByteArray(2)
        var bytes: Int

        while (true) {
            try {
                // Așteaptă să vină date de la dispozitivul conectat
                bytes = inputStream?.read(buffer) ?: 0
                if (bytes == 1) {
                    fromBoard = buffer[0].toInt() and 0xFF
                }
            } catch (e: IOException) {
                e.printStackTrace()
                break
            }
        }
    }

    private fun convertByteArrayToInt(byteArray: ByteArray): Int {
        var result = 0
        val reversedArray = byteArray.reversedArray()

        for (i in reversedArray.indices) {
            result += (reversedArray[i].toInt() and 0xFF) shl (2 * i)
        }
        return result
    }

    fun getData(): Int {
        return fromBoard
    }


    // Metoda pentru a trimite date de tip șir de caractere
    fun sendStringData(data: Int) {
        try {
            outputStream?.write(data)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    fun cancel() {
        try {
            bluetoothSocket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}
