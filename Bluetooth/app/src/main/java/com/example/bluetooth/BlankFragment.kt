package com.example.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.bluetooth.databinding.FragmentBlankBinding


class BlankFragment : Fragment() {

    private var _binding: FragmentBlankBinding? = null
    private val binding get() = _binding!!

    lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var bluetoothState: MutableLiveData<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothState = MutableLiveData<Boolean>()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bluetoothState.value = bluetoothAdapter.isEnabled

        binding.bluetoothEnabledButton.setOnClickListener {
            bluetoothEnabled()
        }
        binding.bluetoothDisabledButton.setOnClickListener {
            bluetoothDisabled()
        }

        bluetoothState.observe(viewLifecycleOwner, {
            it?.let {
                if (it) {
                    binding.bluetoothImage.setBackgroundColor(0xFF00A64B.toInt())
                } else {
                    binding.bluetoothImage.setBackgroundColor(0xFFFF0005.toInt())
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            bluetoothState.value = true
        }
    }

    fun bluetoothEnabled() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, 1)
    }

    fun bluetoothDisabled() {
        bluetoothAdapter.disable()
    }


    override fun onResume() {
        super.onResume()

        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        requireActivity().registerReceiver(receiver, intentFilter)

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val state: Int? = intent?.getIntExtra(
                BluetoothAdapter.EXTRA_STATE,
                BluetoothAdapter.ERROR
            )
            when (state) {
                BluetoothAdapter.STATE_OFF -> bluetoothState.value = false
                BluetoothAdapter.STATE_TURNING_OFF -> Toast.makeText(
                    context,
                    "Turning Bluetooth off...",
                    Toast.LENGTH_SHORT
                ).show()
                BluetoothAdapter.STATE_ON -> bluetoothState.value = true
                BluetoothAdapter.STATE_TURNING_ON -> Toast.makeText(
                    context,
                    "Turning Bluetooth on...",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

