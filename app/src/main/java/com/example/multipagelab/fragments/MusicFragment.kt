package com.example.multipagelab.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.multipagelab.databinding.FragmentMusicBinding
import com.example.multipagelab.services.MusicService

class MusicFragment : Fragment() {

    private var _binding: FragmentMusicBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStartMusic.setOnClickListener {
            startMusicService("START")
        }

        binding.btnPauseMusic.setOnClickListener {
            startMusicService("PAUSE")
        }

        binding.btnStopMusic.setOnClickListener {
            startMusicService("STOP")
        }
    }

    private fun startMusicService(action: String) {
        val intent = Intent(requireContext(), MusicService::class.java).apply {
            this.action = action
        }
        requireContext().startService(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
