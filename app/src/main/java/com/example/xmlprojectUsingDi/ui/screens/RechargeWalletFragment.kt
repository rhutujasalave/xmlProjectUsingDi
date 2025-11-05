//package com.example.xmlprojectUsingDi.ui.screens
//
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import com.example.xmlprojectUsingDi.R
//import com.example.xmlprojectUsingDi.databinding.FragmentRechargeWalletBinding
//import com.example.xmlprojectUsingDi.ui.viewmodel.WalletViewModel
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class RechargeWalletFragment : Fragment() {
//
//    private var _binding: FragmentRechargeWalletBinding? = null
//    private val binding get() = _binding!!
//
//    private val walletViewModel: WalletViewModel by viewModels()
//
//    val dialog = AddCardDialogFragment()
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentRechargeWalletBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        setupListeners()
//        observeViewModel()
//
//        val token = (activity as? MainActivity)?.getAuthToken() ?: ""
//        walletViewModel.fetchWalletBalance(token)
//
//    }
//
//    private fun setupListeners() {
//
//        binding.btnAddCard.setOnClickListener {
////            Toast.makeText(requireContext(), "add btn clicked", Toast.LENGTH_SHORT).show()
//            dialog.show(parentFragmentManager, "AddCardDialog")
//        }
//
//        binding.tvBalance.setOnClickListener {
//            Toast.makeText(requireContext(), "balance clicked", Toast.LENGTH_SHORT).show()
//
//        }
//
//        binding.btnBack.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainer, WalletFragment())
//                .addToBackStack(null)
//                .commit()
//        }
//
//    }
//
//    private fun observeViewModel() {
//        walletViewModel.walletBalance.observe(viewLifecycleOwner) { response ->
//            val balanceText = "$ ${response.balance}"
//            binding.tvBalance.text = balanceText
//        }
//
//        walletViewModel.error.observe(viewLifecycleOwner) { error ->
//            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xmlprojectUsingDi.R
import com.example.xmlprojectUsingDi.databinding.FragmentRechargeWalletBinding
import com.example.xmlprojectUsingDi.ui.adapter.CardListAdapter
import com.example.xmlprojectUsingDi.ui.viewmodel.CardListViewModel
import com.example.xmlprojectUsingDi.ui.viewmodel.WalletViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RechargeWalletFragment : Fragment() {

    private var _binding: FragmentRechargeWalletBinding? = null
    private val binding get() = _binding!!

    private val walletViewModel: WalletViewModel by viewModels()
    private val cardListViewModel: CardListViewModel by viewModels()

    private lateinit var cardListAdapter: CardListAdapter

    private val dialog = AddCardDialogFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRechargeWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        observeViewModels()

        val token = (activity as? MainActivity)?.getAuthToken() ?: ""
        if (token.isNotEmpty()) {
            walletViewModel.fetchWalletBalance(token)
            cardListViewModel.fetchCardList(token)
        } else {
            Toast.makeText(requireContext(), "Token not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        cardListAdapter = CardListAdapter(emptyList())
        binding.rvPaymentMethods.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cardListAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupListeners() {
        binding.btnAddCard.setOnClickListener {
            dialog.show(parentFragmentManager, "AddCardDialog")
        }

        binding.btnBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, WalletFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun observeViewModels() {
        walletViewModel.walletBalance.observe(viewLifecycleOwner) { response ->
            binding.tvBalance.text = "$ ${response.balance}"
        }

        walletViewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

        cardListViewModel.cardList.observe(viewLifecycleOwner) { list ->
            cardListAdapter.updateData(list)
        }

        cardListViewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}