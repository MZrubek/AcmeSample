package com.mikezrubek.acme_sample.model

/**
 * Delivery details, associating a driver, shipment, and related data.
 *
 * Created by Mike Zrubek, 6/17/22.
 */
data class Delivery(val driver: Driver,
                    val shipment: Shipment,
                    var suitabilityScore: Float = 0.0f)

