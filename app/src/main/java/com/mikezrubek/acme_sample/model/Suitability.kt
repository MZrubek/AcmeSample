package com.mikezrubek.acme_sample.model

/**
 * Calculates and holds a working suitability score for a driver and shipment combination.
 *
 * Created by Mike Zrubek, 6/17/22.
 */
data class Suitability(val driver: Driver, val shipment: Shipment) {

    var score: Float = 1.0f

    init {
        calculateScore()
    }

    /**
     * - If the length of the shipment's destination street name is even, the base
     * suitability score (SS) is the number of vowels in the driver’s name multiplied by 1.5.
     * - If the length of the shipment's destination street name is odd, the base SS is the number
     *  of consonants in the driver’s name multiplied by 1. See @Shipment comments for shipment values.
     * - If the length of the shipment's destination street name shares any common factors
     * (besides 1) with the length of the driver’s name, the SS is increased by 50% above the
     * base SS.
     * [Assumption: this last one only occurs if the driver name and shipment address scoring
     * lengths are the same. There are no other evaluations at this time.]
     */
    private fun calculateScore() {
        if (shipment.nameLengthIsOdd) {
            score = driver.numberOfConsonants.toFloat()
        } else {
            score = driver.numberOfVowels * 1.5f
        }

        // compare shipment and driver name lengths:
        if (shipment.addressLengthForScoring == driver.name.length)
            score *= 1.5f
    }
}
