//package com.example.xmlprojectUsingDi.ui
//
//import android.app.DatePickerDialog
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.PopupMenu
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.xmlprojectUsingDi.R
//import com.example.xmlprojectUsingDi.adapter.TripListAdapter
//import com.example.xmlprojectUsingDi.data.model.response.TripItem
//import com.example.xmlprojectUsingDi.databinding.FragmentTripListBinding
//import com.example.xmlprojectUsingDi.utils.DateUtils.formatDateTime
//import com.example.xmlprojectUsingDi.viewmodel.TripListViewModel
//import dagger.hilt.android.AndroidEntryPoint
//import java.util.Calendar
//
//@AndroidEntryPoint
//class TripListFragment : Fragment() {
//
//    private var _binding: FragmentTripListBinding? = null
//    private val binding get() = _binding!!
//
//    private val viewModel: TripListViewModel by viewModels()
//    private lateinit var tripListAdapter: TripListAdapter
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentTripListBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        setupRecyclerView()
//        setupUI()
//
//        val token = (activity as? MainActivity)?.getAuthToken() ?: ""
//
//        // Fetch both upcoming and past trips
//        viewModel.getTripList(token, isUpcoming = true)
//        viewModel.getTripList(token, isUpcoming = false)
//
//        setupObservers()
//    }
//
//    private fun setupRecyclerView() {
//        tripListAdapter = TripListAdapter(emptyList()) { trip ->
//            openTripDetails(trip)
//        }
//        binding.recyclerViewTrips.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = tripListAdapter
//        }
//    }
//
//    private fun openTripDetails(trip: TripItem) {
//        val detailFragment = UpcomingTripDetailsFragment.newInstance(
//            trip.id ?: 0,
//            trip.isUpcoming ?: false
//        )
//
//        parentFragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainer, detailFragment)
//            .addToBackStack(null)
//            .commit()
//    }
//
//    private fun setupObservers() {
//        viewModel.upcomingTrips.observe(viewLifecycleOwner) { upcomingList ->
//            if (!upcomingList.isNullOrEmpty()) {
//                bindUpcomingTrip(upcomingList.first())
//            } else {
//                binding.cardUpcoming.visibility = View.GONE
//            }
//        }
//
//        viewModel.pastTrips.observe(viewLifecycleOwner) { pastTrips ->
//            tripListAdapter.updateList(pastTrips)
//        }
//
//        viewModel.error.observe(viewLifecycleOwner) {
//            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun setupUI() {
//        binding.layoutFromDate.setOnClickListener {
//            showDatePicker { date -> binding.tvFromDate.text = date }
//        }
//
//        binding.layoutToDate.setOnClickListener {
//            showDatePicker { date -> binding.tvToDate.text = date }
//        }
//
//        binding.layoutAll.setOnClickListener { showDropdown(it) }
//
//        binding.btnBack.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainer, HomePageFragment())
//                .addToBackStack(null)
//                .commit()
//        }
//
//        binding.cardUpcoming.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainer, UpcomingTripFragment())
//                .addToBackStack(null)
//                .commit()
//        }
//
//        binding.tvViewMore.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainer, UpcomingTripFragment())
//                .addToBackStack(null)
//                .commit()
//        }
//    }
//
//    private fun showDatePicker(onDateSelected: (String) -> Unit) {
//        val calendar = Calendar.getInstance()
//        val datePicker = DatePickerDialog(
//            requireContext(),
//            { _, y, m, d -> onDateSelected("$d/${m + 1}/$y") },
//            calendar.get(Calendar.YEAR),
//            calendar.get(Calendar.MONTH),
//            calendar.get(Calendar.DAY_OF_MONTH)
//        )
//        datePicker.show()
//    }
//
//    private fun showDropdown(view: View) {
//        val popup = PopupMenu(requireContext(), view)
//        popup.menuInflater.inflate(R.menu.filter_menu, popup.menu)
//        popup.setOnMenuItemClickListener { item ->
//            binding.tvFilter.text = item.title
//            true
//        }
//        popup.show()
//    }
//
//    private fun bindUpcomingTrip(trip: TripItem) {
//        binding.cardUpcoming.visibility = View.VISIBLE
//        binding.tvUpcomingPickup.text = "Factory"
//
//        val dropoff = trip.dropoffAddress
//        binding.tvUpcomingDropoff.text = listOfNotNull(dropoff?.line1, dropoff?.line2)
//            .joinToString(", ")
//            .ifEmpty { "N/A" }
//
//        binding.tvUpcomingDate.text = formatDateTime(trip.createdAt)
//        binding.tvUpcomingStatus.text = trip.status ?: "Unknown"
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}



package com.example.xmlprojectUsingDi.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xmlprojectUsingDi.R
import com.example.xmlprojectUsingDi.adapter.TripListAdapter
import com.example.xmlprojectUsingDi.data.model.response.TripItem
import com.example.xmlprojectUsingDi.databinding.FragmentTripListBinding
import com.example.xmlprojectUsingDi.utils.DateUtils.formatDateTime
import com.example.xmlprojectUsingDi.viewmodel.TripListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TripListFragment : Fragment() {

    private var _binding: FragmentTripListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TripListViewModel by viewModels()
    private lateinit var tripListAdapter: TripListAdapter

    private var fullPastTripList: List<TripItem> = emptyList()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTripListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupUI()

        val token = (activity as? MainActivity)?.getAuthToken() ?: ""

        // Fetch both upcoming and past trips
        viewModel.getTripList(token, isUpcoming = true)
        viewModel.getTripList(token, isUpcoming = false)

        setupObservers()
    }

    private fun setupRecyclerView() {
        tripListAdapter = TripListAdapter(emptyList()) { trip ->
            openTripDetails(trip)
        }
        binding.recyclerViewTrips.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tripListAdapter
        }
    }

    private fun openTripDetails(trip: TripItem) {
        val detailFragment = UpcomingTripDetailsFragment.newInstance(
            trip.id ?: 0,
            trip.isUpcoming ?: false
        )

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupObservers() {
        viewModel.upcomingTrips.observe(viewLifecycleOwner) { upcomingList ->
            val top = upcomingList
                ?.filter { it.isUpcoming == true }
                ?.maxByOrNull { it.createdAt ?: "" }
            if (top != null) {
                bindUpcomingTrip(top)
            } else {
                binding.cardUpcoming.visibility = View.GONE
            }
        }

        viewModel.pastTrips.observe(viewLifecycleOwner) { pastTrips ->
            fullPastTripList = pastTrips
            tripListAdapter.updateList(pastTrips)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI() {
        binding.layoutFromDate.setOnClickListener {
            showDatePicker { date ->
                binding.tvFromDate.text = date
                filterTripsByDateRange()
            }
        }

        binding.layoutToDate.setOnClickListener {
            showDatePicker { date ->
                binding.tvToDate.text = date
                filterTripsByDateRange()
            }
        }

        binding.layoutAll.setOnClickListener { showDropdown(it) }

        binding.btnBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomePageFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.cardUpcoming.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, UpcomingTripFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.tvViewMore.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, UpcomingTripFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, y, m, d -> onDateSelected(String.format("%02d/%02d/%04d", d, m + 1, y)) },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun showDropdown(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.filter_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            binding.tvFilter.text = item.title
            true
        }
        popup.show()
    }

    private fun filterTripsByDateRange() {
        val fromDateStr = binding.tvFromDate.text.toString()
        val toDateStr = binding.tvToDate.text.toString()

        if (fromDateStr.isEmpty() || toDateStr.isEmpty()) {
            tripListAdapter.updateList(fullPastTripList)
            return
        }

        try {
            val fromDate = dateFormat.parse(fromDateStr)
            val toDate = dateFormat.parse(toDateStr)

            if (fromDate != null && toDate != null) {
                val calendar = Calendar.getInstance()
                calendar.time = toDate
                calendar.add(Calendar.DAY_OF_MONTH, 1)
                val inclusiveToDate = calendar.time

                val filteredList = fullPastTripList.filter { trip ->
                    trip.createdAt?.let { dateStr ->
                        try {
                            val tripDate = dateFormat.parse(
                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                                        .parse(dateStr) ?: return@filter false
                                )
                            )
                            tripDate in fromDate..inclusiveToDate
                        } catch (e: Exception) {
                            false
                        }
                    } ?: false
                }

                tripListAdapter.updateList(filteredList)
            } else {
                tripListAdapter.updateList(fullPastTripList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            tripListAdapter.updateList(fullPastTripList)
        }
    }

    private fun bindUpcomingTrip(trip: TripItem) {
        binding.cardUpcoming.visibility = View.VISIBLE
        binding.tvUpcomingPickup.text = "Factory"

        val dropoff = trip.dropoffAddress
        binding.tvUpcomingDropoff.text = listOfNotNull(dropoff?.line1, dropoff?.line2)
            .joinToString(", ")
            .ifEmpty { "N/A" }

        binding.tvUpcomingDate.text = formatDateTime(trip.createdAt)
        binding.tvUpcomingStatus.text = trip.status ?: "Unknown"
        binding.tvUpcomingCost.text = trip.totalCost?.toString() ?: "N/A"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
