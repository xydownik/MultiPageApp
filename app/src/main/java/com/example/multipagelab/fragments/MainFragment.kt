package com.example.multipagelab.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.multipagelab.R
import com.example.multipagelab.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnIntent.setOnClickListener {
            findNavController().navigate(R.id.intentFragment)
        }

        binding.btnBroadcast.setOnClickListener {
            findNavController().navigate(R.id.broadcastFragment)
        }

        binding.btnMusic.setOnClickListener {
            findNavController().navigate(R.id.musicFragment)
        }

        binding.btnCalendar.setOnClickListener {
            findNavController().navigate(R.id.calendarFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
