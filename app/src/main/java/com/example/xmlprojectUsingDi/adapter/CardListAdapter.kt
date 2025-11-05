package com.example.xmlprojectUsingDi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xmlprojectUsingDi.R
import com.example.xmlprojectUsingDi.data.model.response.CardListResponse

class CardListAdapter(
    private var cardList: List<CardListResponse>
) : RecyclerView.Adapter<CardListAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCardIcon: ImageView = itemView.findViewById(R.id.ivCardIcon)
        val tvCardNumber: TextView = itemView.findViewById(R.id.tvCardNumber)
        val tvConnected: TextView = itemView.findViewById(R.id.tvConnected)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_payment_method, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cardList[position]
        holder.tvCardNumber.text = "•••• •••• •••• ${card.cardLast4}"

        // Example: show connected status
        holder.tvConnected.text = "Connected"

        // Change icon based on brand
        when (card.cardBrand.lowercase()) {
            "visa" -> holder.ivCardIcon.setImageResource(R.drawable.ic_eye)
            "mastercard" -> holder.ivCardIcon.setImageResource(R.drawable.ic_eye)
            else -> holder.ivCardIcon.setImageResource(R.drawable.ic_credit_card)
        }
    }

    override fun getItemCount(): Int = cardList.size

    fun updateData(newList: List<CardListResponse>) {
        cardList = newList
        notifyDataSetChanged()
    }
}
