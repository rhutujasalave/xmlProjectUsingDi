package com.example.xmlprojectUsingDi.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.xmlprojectUsingDi.databinding.FragmentChatScreenBinding

class ChatScreenFragment : Fragment() {

    private var _binding: FragmentChatScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.showBottomNav(false)

        binding.ivBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.ivSend.setOnClickListener {
            val msg = binding.etMessage.text.toString().trim()
            if (msg.isNotEmpty()) {
                binding.etMessage.text.clear()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (activity as? MainActivity)?.showBottomNav(true)
    }

}
