package com.example.mad9042_week2

import android.bluetooth.BluetoothAdapter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.widget.ArrayAdapter
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log
import android.widget.ListView
import java.lang.Exception


class BluetoothActivity : AppCompatActivity() {

    lateinit var listView:ListView
    var deviceList = ArrayList<String>()
    lateinit var listAdapter : ArrayAdapter<String>
    var mReceiver = MyReceiver()
    lateinit  var bluetoothAdapter : BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        try {
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)

            registerReceiver(mReceiver, filter)
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

            bluetoothAdapter.startDiscovery()

            listView = findViewById<ListView>(R.id.listview)
            listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceList)
            listView.adapter = listAdapter
        }catch (e:Exception)
        {
            Log.e("Error", e.message)
        }
    }

    inner class MyReceiver : BroadcastReceiver()
    {
        override fun onReceive(context: Context, intent: Intent)
        {
            if (intent.action == BluetoothDevice.ACTION_FOUND )
            {
                //The RSSI is the signal strength
                val rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, java.lang.Short.MIN_VALUE)

                //The device tells you about the device
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                //Get the bluetooth device name, and address address:
                deviceList.add( device.name + "\n" + device.address)

               //update the listview since you added to the ArrayList
                listAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        bluetoothAdapter.cancelDiscovery()
        super.onDestroy()
    }
}
