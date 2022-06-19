package com.mikezrubek.acme_sample.feature.main

import com.mikezrubek.acme_sample.model.Delivery

/**
 * Created by Mike Zrubek, 6/17/22.
 */
interface DeliverySelectionListener {

    fun onDeliverySelected(delivery: Delivery)
}