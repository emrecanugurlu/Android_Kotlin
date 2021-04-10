package com.example.materialtimepicker

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.materialtimepicker.databinding.FragmentTimePickerBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class TimePickerFragment : Fragment() {

    private var _binding: FragmentTimePickerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimePickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.timePickerOpenButton.setOnClickListener {
            openTimePicker()
        }
    }

    private fun openTimePicker() {
        val isSystem24Hour = is24HourFormat(requireContext())
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Set Alarm")
            .build()
        picker.show(childFragmentManager, "TAG")

        picker.addOnPositiveButtonClickListener {
            Log.d(TAG, "openTimePicker: POSITIVE")
            println("${picker.hour}:${picker.minute}")

        }
        picker.addOnNegativeButtonClickListener {
            Log.d(TAG, "openTimePicker: NEGATIVE")
        }
        picker.addOnCancelListener {
            Log.d(TAG, "openTimePicker: CANCEL")
        }
        picker.addOnDismissListener {
            Log.d(TAG, "openTimePicker: DISMISS")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}