package com.example.xmlprojectUsingDi.ui.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.xmlprojectUsingDi.R
import com.example.xmlprojectUsingDi.data.model.response.DialCode
import com.example.xmlprojectUsingDi.databinding.FragmentSignupBinding
import com.example.xmlprojectUsingDi.ui.viewmodel.CountryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val countryViewModel: CountryViewModel by viewModels()
    private var dialCodeList: List<DialCode> = emptyList()
    private var selectedCountry: DialCode? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCountryDropdown()
        addTextWatchers()

        countryViewModel.fetchCountryList()

        binding.btnSignUp.setOnClickListener {
            if (validateInputs()) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, CreatePasswordFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        binding.tvSignIn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SignInFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupCountryDropdown() {
        val countryDropdown = binding.actvCountry

        countryViewModel.countryList.observe(viewLifecycleOwner) { list ->
            dialCodeList = list

            val countryCodes = list.map { "${it.countryCode} (${it.diaCode})" }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, countryCodes)
            countryDropdown.setAdapter(adapter)

            val defaultIndex = dialCodeList.indexOfFirst { it.countryCode.equals("IN", true) }
            if (defaultIndex != -1) {
                selectedCountry = dialCodeList[defaultIndex]
                countryDropdown.setText("${selectedCountry?.countryCode} (${selectedCountry?.diaCode})", false)
            }

            countryDropdown.setOnItemClickListener { _, _, position, _ ->
                selectedCountry = dialCodeList[position]
                Toast.makeText(
                    requireContext(),
                    "Selected: ${selectedCountry?.countryCode} ${selectedCountry?.diaCode}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        countryViewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

        countryDropdown.setOnClickListener {
            if (dialCodeList.isEmpty()) {
                countryViewModel.fetchCountryList()
            } else {
                countryDropdown.showDropDown()
            }
        }
    }

    private fun addTextWatchers() {
        binding.etMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.etMobile.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.etEmail.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validateInputs(): Boolean {
        val phone = binding.etMobile.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()

        if (phone.isEmpty()) {
            binding.etMobile.error = "Enter phone number"
            binding.etMobile.requestFocus()
            return false
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Enter valid email"
            binding.etEmail.requestFocus()
            return false
        }

        if (!binding.acceptTerms.isChecked) {
            Toast.makeText(requireContext(), "Please accept terms", Toast.LENGTH_SHORT).show()
            return false
        }

        if (selectedCountry == null) {
            Toast.makeText(requireContext(), "Please select country", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
