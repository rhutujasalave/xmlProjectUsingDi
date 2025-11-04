//package com.example.xmlprojectUsingDi.ui
//
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import com.example.xmlprojectUsingDi.R
//import com.example.xmlprojectUsingDi.databinding.FragmentWalletBinding
//
//class WalletFragment : Fragment() {
//
//    private var _binding: FragmentWalletBinding? = null
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentWalletBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
//        binding.menuIcon.setOnClickListener {
//            Toast.makeText(requireContext(), "menu clicked", Toast.LENGTH_SHORT).show()
//        }
//
//        binding.btnWithdraw.setOnClickListener {
//            Toast.makeText(requireContext(), "Withdraw clicked", Toast.LENGTH_SHORT).show()
//        }
//
//        binding.btnAdd.setOnClickListener {
//                parentFragmentManager.beginTransaction()
//                    .replace(R.id.fragmentContainer, RechargeWalletFragment())
//                    .addToBackStack(null)
//                    .commit()
//            }
//
//    }
//
//}


package com.example.xmlprojectUsingDi.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.xmlprojectUsingDi.R
import com.example.xmlprojectUsingDi.databinding.FragmentWalletBinding
import com.example.xmlprojectUsingDi.ui.viewmodel.WalletViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletFragment : Fragment() {

    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!

    private val walletViewModel: WalletViewModel by viewModels()

//    val token = (activity as? MainActivity)?.getAuthToken() ?: ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeViewModel()
        val token = (activity as? MainActivity)?.getAuthToken() ?: ""
        walletViewModel.fetchWalletBalance(token)

    }

    private fun setupListeners() {
        binding.menuIcon.setOnClickListener {
            Toast.makeText(requireContext(), "Menu clicked", Toast.LENGTH_SHORT).show()
        }

        binding.btnWithdraw.setOnClickListener {
            Toast.makeText(requireContext(), "Withdraw clicked", Toast.LENGTH_SHORT).show()
        }

        binding.btnAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, RechargeWalletFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun observeViewModel() {
        walletViewModel.walletBalance.observe(viewLifecycleOwner) { response ->
            val balanceText = "$ ${response.balance}"
            binding.tvAvailableBalance.text = balanceText
            binding.tvCurrentBalance.text = balanceText
        }

        walletViewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
