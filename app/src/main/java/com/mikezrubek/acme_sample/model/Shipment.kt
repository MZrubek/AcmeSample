package com.mikezrubek.acme_sample.model

/**
 * Shipment destination.
 * For suitability scoring, an adjusted address is used, instead of the complete full address.
 * The only part of the address used is the street name; preceding address numbers and secondary
 * address modifications (Apt. and Suite) are ignored.
 * Assumptions:
 * - shipment address is in English characters and in a format of [number name [optional extensions]]
 * - the optional extensions must start with "Apt." or "Suite" (case insensitive), along with leading blanks
 *
 * Values addressLengthForScoring can be used to get the above described portion of address;
 * the nameLengthIsOdd value is based on this same scoring length.
 * These values may differ from the full address string length.
 *
 * Created by Mike Zrubek, 6/17/22.
 */
data class Shipment(val address: String) {

    /** adjusted length of the address; excludes the leading numeric part of the address */
    val addressLengthForScoring: Int

    /** @return true if the adjusted address is odd, false if even. */
    val nameLengthIsOdd: Boolean

    init {
        addressLengthForScoring = getAddressLength()

        // bitwise-and the last bit to check for 1:
        nameLengthIsOdd = (addressLengthForScoring and 1) > 0
    }

    /**
     * Determine the length of the street name part of an address.
     * To accomplish this, ignore the leading numeric part of the address, and
     * if there is a " Apt." or " Suite ", ignores those words and anything after that.
     * ASSUMPTIONS: street name is in a format as expected; otherwise the entire length
     * of the address may be used.
     *
     * @return length of portion of the address, or entire length if format is not as expected.
    */
    private fun getAddressLength(): Int {
        // filter out leading and trailing numbers, and 'apt.' and 'suite':
        val noStreetNumber = address.lowercase().substringAfter(' ')
        var streetOnly = noStreetNumber.substringBefore(SUBSTRING_APPT)
        streetOnly = streetOnly.substringBefore(SUBSTRING_SUITE)

        return streetOnly.length
    }

    companion object {
        private const val SUBSTRING_APPT = " apt."
        private const val SUBSTRING_SUITE = " suite "
    }
}
