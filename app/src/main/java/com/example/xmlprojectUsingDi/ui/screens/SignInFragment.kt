package com.example.xmlprojectUsingDi.ui.screens

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.xmlprojectUsingDi.R
import com.example.xmlprojectUsingDi.data.model.response.DialCode
import com.example.xmlprojectUsingDi.databinding.FragmentSigninBinding
import com.example.xmlprojectUsingDi.ui.viewmodel.CountryViewModel
import com.example.xmlprojectUsingDi.ui.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.view.GestureDetector
import android.widget.EditText

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!
    private val signInViewModel: SignInViewModel by viewModels()
    private val countryViewModel: CountryViewModel by viewModels()
    private var dialCodeList: List<DialCode> = emptyList()
    private var selectedCountry: DialCode? = null
    private lateinit var gestureDetector: GestureDetector


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setupPasswordVisibility()
        setupCountryDropdown()
        setupNavigation()
        observeViewModels()
//        setupSwipeGesture(view)

        countryViewModel.fetchCountryList()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }


//    private fun setupSwipeGesture(view: View) {
//        gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
//            private val SWIPE_THRESHOLD = 100
//            private val SWIPE_VELOCITY_THRESHOLD = 100
//
//            override fun onFling(
//                e1: android.view.MotionEvent?,
//                e2: android.view.MotionEvent?,
//                velocityX: Float,
//                velocityY: Float
//            ): Boolean {
//                if (e1 == null || e2 == null) return false
//                val diffX = e2.x - e1.x
//                val diffY = e2.y - e1.y
//
//                if (kotlin.math.abs(diffX) > kotlin.math.abs(diffY)) {
//                    if (kotlin.math.abs(diffX) > SWIPE_THRESHOLD && kotlin.math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                        if (diffX < 0) {
//                            navigateToSignUp() // Swipe left → Go to SignUp
//                        }
//                        return true
//                    }
//                }
//                return false
//            }
//        })
//
//        view.setOnTouchListener { _, event ->
//            gestureDetector.onTouchEvent(event)
//            true
//        }
//    }


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

    private fun togglePasswordVisibility(editText: EditText, icon: View) {
        if (editText.inputType == (InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_PASSWORD)
        ) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or
                   InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            (icon as? android.widget.ImageView)?.setImageResource(R.drawable.ic_eye)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or
                  InputType.TYPE_TEXT_VARIATION_PASSWORD
            (icon as? android.widget.ImageView)?.setImageResource(R.drawable.ic_eye_cross)
        }
        editText.setSelection(editText.text.length)
    }

    private fun setupNavigation() {
        binding.btnSignIn.setOnClickListener {
            val mobile = binding.etMobile.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            val diaCode = selectedCountry?.diaCode ?: ""

            if (!isValidPhone(mobile)) {
                Toast.makeText(requireContext(), "Enter valid phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidPassword(password)) {
                Toast.makeText(requireContext(), "Invalid password format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (diaCode.isEmpty()) {
                Toast.makeText(requireContext(), "Please select country", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signInViewModel.loginUser(diaCode, mobile, password)
        }

        binding.tvSignUp.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SignUpFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.tvForgotPassword.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CreatePasswordFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnSignUp.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SignUpFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.ivHidePassword.setOnClickListener {
            togglePasswordVisibility(binding.edtPassword, binding.ivHidePassword)
        }


    }

    private fun observeViewModels() {
        signInViewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SignInViewModel.LoginState.Idle -> {}
                is SignInViewModel.LoginState.Loading -> {
                    binding.btnSignIn.isEnabled = false
                    binding.btnSignIn.text = "Signing In..."
                }
                is SignInViewModel.LoginState.Success -> {
                    binding.btnSignIn.isEnabled = true
                    binding.btnSignIn.text = "Sign In"
                    Toast.makeText(requireContext(), "Login Successful!", Toast.LENGTH_SHORT).show()

                    val token = state.response.data?.accessToken ?: ""
                    if (token.isNotEmpty()) {
                        (activity as? MainActivity)?.saveAuthToken(token)
                        Toast.makeText(requireContext(), "Access token saved successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Access token not found in response", Toast.LENGTH_SHORT).show()
                    }

                    (activity as MainActivity).loadFragment(HomePageFragment(), showBottomNav = true)
                }
                is SignInViewModel.LoginState.Error -> {
                    binding.btnSignIn.isEnabled = true
                    binding.btnSignIn.text = "Sign In"
                    val message = state.message
                    if (message.contains("Invalid credentials", ignoreCase = true)) {
                        Toast.makeText(requireContext(), "Invalid credentials", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun isValidPhone(phone: String) = phone.matches(Regex("^[0-9]{10}$"))

    private fun isValidPassword(password: String): Boolean {
        val pattern =
            Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
        return password.matches(pattern)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
