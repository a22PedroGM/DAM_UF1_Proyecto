package com.example.pmdm_dam_proyecto

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pmdm_dam_proyecto.databinding.FragmentGameBinding
import com.example.pmdm_dam_proyecto.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var binding2: FragmentGameBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaPlayer2: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding2 = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.sonidonico)
        mediaPlayer2 = MediaPlayer.create(requireContext(), R.raw.sonidonico2)

        binding.btnPlay.setOnClickListener {
            mediaPlayer.start()
            findNavController().navigate(R.id.action_mainFragment_to_gameFragment)
        }
        binding.btnScores.setOnClickListener {
            mediaPlayer2.start()
            findNavController().navigate(R.id.action_mainFragment_to_scoresFragment)
        }
        binding.btnExit.setOnClickListener {
            salirDeLaAplicacion()
        }
    }

    private fun salirDeLaAplicacion() {
        requireActivity().finish()
    }
}