package com.example.xmlprojectUsingDi.ui.screens

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.xmlprojectUsingDi.R
import com.example.xmlprojectUsingDi.databinding.ItemDetailPastTripBinding
import com.example.xmlprojectUsingDi.ui.viewmodel.SharedViewModel
import com.example.xmlprojectUsingDi.utils.DateUtils.formatDateTime
import com.example.xmlprojectUsingDi.utils.fixDuplicateUrl
import com.example.xmlprojectUsingDi.viewmodel.TripDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingTripDetailsFragment : Fragment() {

    private var _binding: ItemDetailPastTripBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TripDetailsViewModel by viewModels()
    private var tripId: Int = 0
    private var isUpcoming: Boolean = false
    private var receiverPhoneNumber: String? = null
    private var receiverCountryCode: String? = null
    private var customerPhoneNumber: String? = null
    private var customerCountryCode: String? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()


    companion object {
        private const val ARG_TRIP_ID = "tripId"
        private const val ARG_IS_UPCOMING = "isUpcoming"

        fun newInstance(tripId: Int, isUpcoming: Boolean): UpcomingTripDetailsFragment {
            val fragment = UpcomingTripDetailsFragment()
            val args = Bundle().apply {
                putInt(ARG_TRIP_ID, tripId)
                putBoolean(ARG_IS_UPCOMING, isUpcoming)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemDetailPastTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.showBottomNav(false)

        initArguments()
        setupUI()
        setupObservers()
        setupClickListeners()

//        val token = (activity as? MainActivity)?.getAuthToken() ?: ""
//        viewModel.getTripDetails(token, tripId)

        sharedViewModel.authToken.observe(viewLifecycleOwner) { token ->
            if (!token.isNullOrEmpty()) {
                viewModel.getTripDetails(token, tripId)
            } else {
                Toast.makeText(requireContext(), "Token not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initArguments() {
        tripId = arguments?.getInt(ARG_TRIP_ID) ?: 0
        isUpcoming = arguments?.getBoolean(ARG_IS_UPCOMING) ?: false
    }

    private fun setupUI() {
        if (isUpcoming) {
            binding.tripAccepted.visibility = View.GONE
            binding.llBtn.visibility = View.VISIBLE
            binding.tvTitle.text = "Upcoming Trip Details"
        } else {
            binding.llBtn.visibility = View.GONE
            binding.tripAccepted.visibility = View.VISIBLE
            binding.tvTitle.text = "Past Trip Details"
        }
    }

    private fun setupObservers() {
        viewModel.tripDetail.observe(viewLifecycleOwner) { detail ->

            binding.profileName.text = detail.receiver?.name ?: "N/A"

//            detail.schedule?.toDate?.let { isoDate ->
//                val formatted = formatDateTime(isoDate).replace(",", " | ")
//                binding.tvdate.text = formatted
//            } ?: run { binding.tvdate.text = "N/A" }


            detail.schedule?.toDate?.let { isoDate ->
                val formatted = formatDateTime(isoDate).replace(",", " | ")
                binding.tvdate.text = formatted
                binding.tvAcceptedDate.text = formatted
            } ?: run {
                binding.tvdate.text = "N/A"
                binding.tvAcceptedDate.text = "N/A"
            }


            binding.tvDropAddress.text = buildString {
                append(detail.dropoffAddress?.line1 ?: "")
                append(", ${detail.dropoffAddress?.line2 ?: ""}")
                append(", ${detail.dropoffAddress?.city ?: ""}")
                append(", ${detail.dropoffAddress?.state ?: ""}")
                append(", ${detail.dropoffAddress?.postal_code ?: ""}")
            }

            binding.tvMaterialCost.text = detail.materialCost?.toString() ?: "N/A"
            binding.tvTransportationCost.text = detail.transportCost?.toString() ?: "N/A"
            binding.tvTotalCost.text = detail.totalCost?.toString() ?: "N/A"
            binding.tvLaborCost.text = detail.totalLabourCost?.toString() ?: "N/A"
            binding.tvMaterialType.text = "Material : ${detail.category?.name ?: "N/A"}"
            
            receiverPhoneNumber = detail.receiver?.phone
            receiverCountryCode = detail.receiver?.diaCode
            customerPhoneNumber = detail.customer?.phone
            customerCountryCode = detail.customer?.country?.diaCode

            binding.tvQuantity.text = detail.materials?.joinToString("\n") {
                "${it.name} : ${it.value}"
            } ?: "N/A"

            Glide.with(requireContext())
                .load(detail.category?.icon)
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(binding.imgPickup)

            Glide.with(requireContext())
                .load(detail.customer?.profileImage)
                .circleCrop()
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(binding.profileImage)

            val fixedDropUrl = detail.destinationImage?.fixDuplicateUrl()
            Glide.with(requireContext())
                .load(fixedDropUrl)
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(binding.imgDrop)

        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

    }

    private fun setupClickListeners() {
        binding.backIcon.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnCancel.setOnClickListener {
            showConfirmationDialog(
                "Cancel Trip",
                "Are you sure you want to cancel this trip?"
            ) {
                Toast.makeText(requireContext(), "Trip cancelled successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.btnReconfirm.setOnClickListener {
            showConfirmationDialog(
                "Re-Confirm Trip",
                "Are you sure you want to confirm this trip?"
            ) {
                Toast.makeText(requireContext(), "Trip confirmed successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.callIcon.setOnClickListener {
            val phoneNumber = receiverPhoneNumber ?: customerPhoneNumber
            phoneNumber?.let { number ->
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$number")
                startActivity(intent)
            } ?: run {
                Toast.makeText(requireContext(), "Phone number not available", Toast.LENGTH_SHORT).show()
            }
        }

        binding.whatsappIcon.setOnClickListener {

            val phoneNumber = receiverPhoneNumber ?: customerPhoneNumber
            val countryCode = receiverCountryCode ?: customerCountryCode

            if (!phoneNumber.isNullOrEmpty() && !countryCode.isNullOrEmpty()) {
                openWhatsApp(phoneNumber, countryCode)
            } else {
                Toast.makeText(requireContext(), "Phone number not available", Toast.LENGTH_SHORT).show()
            }
        }

        binding.msgIcon.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ChatScreenFragment())
                .addToBackStack(null)
                .commit()
        }

    }

    private fun openWhatsApp(phoneNumber: String, countryCode: String) {
        try {
            val cleanCountry = countryCode.replace("+", "").trim()
            val cleanNumber = phoneNumber.replace("+", "").replace("-", "").replace(" ", "").trim()
            val fullNumber = "$cleanCountry$cleanNumber"

            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$fullNumber")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            when {

                isAppInstalled("com.whatsapp") -> {
                    intent.setPackage("com.whatsapp")
                    startActivity(intent)
                }
                isAppInstalled("com.whatsapp.w4b") -> {
                    intent.setPackage("com.whatsapp.w4b")
                    startActivity(intent)
                }
                else -> {
                    val webUri = Uri.parse("https://wa.me/$fullNumber")
                    val webIntent = Intent(Intent.ACTION_VIEW, webUri)
                    webIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(webIntent)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error opening WhatsApp: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                requireContext().packageManager.getPackageInfo(packageName, android.content.pm.PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                requireContext().packageManager.getPackageInfo(packageName, 0)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun showConfirmationDialog(title: String, message: String, positiveAction: () -> Unit) {
        val builder = AlertDialog.Builder(requireContext(), R.style.WhiteAlertDialog)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Confirm") { dialog, _ ->
            dialog.dismiss()
            positiveAction()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()
        dialog.show()

        dialog.window?.setBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(requireContext(), android.R.color.white))
        )

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            ?.setTextColor(
                ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark)
            )

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            ?.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (activity as? MainActivity)?.showBottomNav(true)

    }
}
