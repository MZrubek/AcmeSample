package com.mikezrubek.acme_sample.model

/**
 * Collection of Delivery instances or error data.
 *
 * Created by Mike Zrubek, 6/17/22.
 */
data class Deliveries(val deliveryList: List<Delivery>? = null,
                      val errorMessage: String? = null)
