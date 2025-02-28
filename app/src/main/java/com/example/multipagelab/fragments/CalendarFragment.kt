package com.example.multipagelab.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.multipagelab.adapters.CalendarAdapter
import com.example.multipagelab.databinding.FragmentCalendarBinding
import com.example.multipagelab.services.CalendarProviderHelper

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewCalendar.layoutManager = LinearLayoutManager(requireContext())

        binding.btnLoadEvents.setOnClickListener {
            checkPermissionsAndLoadEvents()
        }
    }

    private fun checkPermissionsAndLoadEvents() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_CALENDAR
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            loadCalendarEvents()
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_CALENDAR), 100)
        }
    }

    private fun loadCalendarEvents() {
        val events = CalendarProviderHelper.getCalendarEvents(requireContext())
        binding.recyclerViewCalendar.adapter = CalendarAdapter(events)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadCalendarEvents()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
