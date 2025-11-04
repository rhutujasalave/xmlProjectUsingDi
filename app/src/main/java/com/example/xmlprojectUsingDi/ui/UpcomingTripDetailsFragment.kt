//package com.example.xmlprojectUsingDi.ui
//
//import android.app.AlertDialog
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import com.bumptech.glide.Glide
//import com.example.xmlprojectUsingDi.R
//import com.example.xmlprojectUsingDi.databinding.ItemDetailPastTripBinding
//import com.example.xmlprojectUsingDi.viewmodel.TripDetailsViewModel
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class UpcomingTripDetailsFragment : Fragment() {
//
//    private var _binding: ItemDetailPastTripBinding? = null
//    private val binding get() = _binding!!
//    private val viewModel: TripDetailsViewModel by viewModels()
//    private var tripId: Int = 0
//    private var isUpcoming: Boolean = false
//
////            companion object {
////                private const val ARG_TRIP_ID = "tripId"
//
//    companion object {
//        private const val ARG_TRIP_ID = "tripId"
//        private const val ARG_IS_UPCOMING = "isUpcoming"
//
//
//        fun newInstance(tripId: Int, isUpcoming: Boolean): UpcomingTripDetailsFragment {
//                val fragment = UpcomingTripDetailsFragment()
//                val args = Bundle()
//                args.putInt("tripId", tripId)
//                args.putBoolean("isUpcoming", isUpcoming)
//                fragment.arguments = args
//                return fragment
//            }
//        }
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = ItemDetailPastTripBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        tripId = arguments?.getInt(ARG_TRIP_ID) ?: 0
//        isUpcoming = arguments?.getBoolean(ARG_IS_UPCOMING) ?: false
////        val isUpcoming = arguments?.getBoolean("isUpcoming") ?: true
//
//        if (isUpcoming) {
//            binding.tripAccepted.visibility = View.GONE
//            binding.llBtn.visibility = View.VISIBLE
//            binding.tvTitle.text = "Upcoming Trip Details"
//        } else {
//            binding.llBtn.visibility = View.GONE
//            binding.tripAccepted.visibility = View.VISIBLE
//            binding.tvTitle.text = "Past Trip Details"
//        }
//
//        val token = (activity as? MainActivity)?.getAuthToken() ?: ""
//
//        viewModel.getTripDetails(token, tripId)
//
//        viewModel.tripDetail.observe(viewLifecycleOwner) { detail ->
//            binding.profileName.text = detail.receiver?.name ?: "N/A"
//            binding.tvDropAddress.text =
//                "${detail.dropoffAddress?.line1 ?: ""}, ${detail.dropoffAddress?.line2 ?: ""}, " +
//                        "${detail.dropoffAddress?.city ?: ""}, ${detail.dropoffAddress?.state ?: ""}, " +
//                        "${detail.dropoffAddress?.postal_code ?: ""}"
//            binding.tvMaterialCost.text = detail.materialCost?.toString() ?: "N/A"
//            binding.tvTransportationCost.text = detail.transportCost?.toString() ?: "N/A"
//            binding.tvTotalCost.text = detail.totalCost?.toString() ?: "N/A"
//
//            Glide.with(requireContext())
//                .load(detail.destinationImage)
//                .into(binding.imgDrop)
//        }
//
//        viewModel.error.observe(viewLifecycleOwner) { msg ->
//            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
//        }
//
//        binding.backIcon.setOnClickListener {
//            parentFragmentManager.popBackStack()
//        }
//
////        binding.btnCancel.setOnClickListener {
////            showCancelConfirmationDialog()
////        }
//
//        binding.btnCancel.setOnClickListener {
//            showConfirmationDialog(
//                title = "Cancel Trip",
//                message = "Are you sure you want to cancel this trip?",
//                positiveAction = {
//                    Toast.makeText(requireContext(), "Trip cancelled successfully", Toast.LENGTH_SHORT).show()
//                }
//            )
//        }
//        binding.btnReconfirm.setOnClickListener {
//            showConfirmationDialog(
//                title = "Re-Confirm Trip",
//                message = "Are you sure you want to confirm this trip?",
//                positiveAction = {
//                    Toast.makeText(requireContext(), "Trip confirmed successfully", Toast.LENGTH_SHORT).show()
//                }
//            )
//        }
//    }
//
//
//    private fun showConfirmationDialog(title: String, message: String, positiveAction: () -> Unit) {
//        val builder = AlertDialog.Builder(requireContext(), R.style.WhiteAlertDialog)
//        builder.setTitle(title)
//        builder.setMessage(message)
//        builder.setPositiveButton("Confirm") { dialog, _ ->
//            dialog.dismiss()
//            positiveAction()
//        }
//        builder.setNegativeButton("Cancel") { dialog, _ ->
//            dialog.dismiss()
//        }
//
//        val dialog = builder.create()
//        dialog.show()
//
//        val greenColor = ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark)
//        val redColor = ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)
//        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(greenColor)
//        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(redColor)
//    }
//
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}


package com.example.xmlprojectUsingDi.ui

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.xmlprojectUsingDi.R
import com.example.xmlprojectUsingDi.data.model.response.Schedule
import com.example.xmlprojectUsingDi.databinding.ItemDetailPastTripBinding
import com.example.xmlprojectUsingDi.utils.DateUtils
import com.example.xmlprojectUsingDi.utils.DateUtils.formatDateTime
import com.example.xmlprojectUsingDi.viewmodel.TripDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingTripDetailsFragment : Fragment() {

    private var _binding: ItemDetailPastTripBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TripDetailsViewModel by viewModels()

    private var tripId: Int = 0
    private var isUpcoming: Boolean = false

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

        initArguments()
        setupUI()
        setupObservers()
        setupClickListeners()

        val token = (activity as? MainActivity)?.getAuthToken() ?: ""
        viewModel.getTripDetails(token, tripId)
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
            binding.tvdate.text = detail.schedule?.toDate ?: "N/A"

            detail.schedule?.toDate?.let { isoDate ->
                val formattedDate = formatDateTime(isoDate)
                    .replace(",", " | ")
                binding.tvdate.text = formattedDate
            } ?: run {
                binding.tvdate.text = "N/A"
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
            binding.tvMaterialType.text = "Material : ${detail.category?.name ?: "N/A"}"
            binding.tvQuantity.text = detail.materials?.
            joinToString(separator = "\n") { mat ->
                "${mat.name} : ${mat.value}"
            }
                ?: "N/A"

            Glide.with(requireContext())
                .load(detail.destinationImage)
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
                title = "Cancel Trip",
                message = "Are you sure you want to cancel this trip?",
                positiveAction = {
                    Toast.makeText(
                        requireContext(),
                        "Trip cancelled successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }

        binding.btnReconfirm.setOnClickListener {
            showConfirmationDialog(
                title = "Re-Confirm Trip",
                message = "Are you sure you want to confirm this trip?",
                positiveAction = {
                    Toast.makeText(
                        requireContext(),
                        "Trip confirmed successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
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
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()

        dialog.window?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.white
                )
            )
        )

        val greenColor = ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark)
        val redColor = ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(greenColor)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(redColor)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
