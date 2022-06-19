package com.mikezrubek.acme_sample.feature.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikezrubek.acme_sample.R
import com.mikezrubek.acme_sample.model.Delivery
import kotlinx.android.synthetic.main.delivery_list_item.view.*

/**
 * Adapter for handling the list of deliveries.
 *
 * Created by Mike Zrubek, 6/17/22.
 */
class DeliveriesListAdapter(val context: Context,
                            val selectionListener: DeliverySelectionListener? = null) :
    RecyclerView.Adapter<DeliveriesListAdapter.DeliveryItemViewHolder>() {

    var deliveriesList: List<Delivery> = emptyList()
        set(newList) {
            field = newList
            notifyDataSetChanged()
        }

    // layout of each item
    private val viewItemLayout: Int = R.layout.delivery_list_item

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DeliveryItemViewHolder(LayoutInflater.from(context).inflate(viewItemLayout, parent, false))

    override fun onBindViewHolder(holder: DeliveryItemViewHolder, position: Int) =
        holder.bind(deliveriesList[position])

    override fun getItemCount() = deliveriesList.size

    inner class DeliveryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(delivery: Delivery) = with(itemView) {
            driver.text = delivery.driver.name
            shipment.text = delivery.shipment.address

            scoreSlider.progress = delivery.suitabilityScore.toInt()
            scoreValue.text = delivery.suitabilityScore.toString()

            setOnClickListener {
                selectionListener?.onDeliverySelected(delivery)
            }

        }
    }

}