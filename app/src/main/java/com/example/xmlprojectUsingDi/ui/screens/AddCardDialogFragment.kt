package com.example.xmlprojectUsingDi.ui.screens

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.xmlprojectUsingDi.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCardDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        (activity as? MainActivity)?.showBottomNav(false)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_new_card)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setupUI(dialog)

        return dialog
    }

    private fun setupUI(dialog: Dialog) {
        val etCardName = dialog.findViewById<EditText>(R.id.etCardName)
        val etCardNumber = dialog.findViewById<EditText>(R.id.etCardNumber)
        val etExpiryDate = dialog.findViewById<EditText>(R.id.etExpiryDate)
        val etCvv = dialog.findViewById<EditText>(R.id.etCvv)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnAddCard = dialog.findViewById<Button>(R.id.btnAddCard)

        btnCancel.setOnClickListener {
            dismiss()
        }

        btnAddCard.setOnClickListener {
            val name = etCardName.text.toString().trim()
            val number = etCardNumber.text.toString().trim()
            val expiry = etExpiryDate.text.toString().trim()
            val cvv = etCvv.text.toString().trim()

            when {
                name.isEmpty() -> showToast("Please enter name on card")
                number.isEmpty() -> showToast("Please enter card number")
                expiry.isEmpty() -> showToast("Please enter expiry date")
                cvv.isEmpty() -> showToast("Please enter CVV")
                else -> {
                    showToast("Card added successfully!")
                    dismiss()
                }
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
//        (activity as? MainActivity)?.showBottomNav(true)
    }
}
