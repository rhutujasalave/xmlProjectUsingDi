package com.example.xmlprojectUsingDi.ui.screens


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.xmlprojectUsingDi.R
import com.example.xmlprojectUsingDi.databinding.FragmentRechargeWalletBinding
import com.example.xmlprojectUsingDi.ui.viewmodel.WalletViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RechargeWalletFragment : Fragment() {

    private var _binding: FragmentRechargeWalletBinding? = null
    private val binding get() = _binding!!

    private val walletViewModel: WalletViewModel by viewModels()

    val dialog = AddCardDialogFragment()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRechargeWalletBinding.inflate(inflater, container, false)
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

        binding.btnAddCard.setOnClickListener {
//            Toast.makeText(requireContext(), "add btn clicked", Toast.LENGTH_SHORT).show()
            dialog.show(parentFragmentManager, "AddCardDialog")
        }

        binding.tvBalance.setOnClickListener {
            Toast.makeText(requireContext(), "balance clicked", Toast.LENGTH_SHORT).show()

        }

        binding.btnBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, WalletFragment())
                .addToBackStack(null)
                .commit()
        }

    }

    private fun observeViewModel() {
        walletViewModel.walletBalance.observe(viewLifecycleOwner) { response ->
            val balanceText = "$ ${response.balance}"
            binding.tvBalance.text = balanceText
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