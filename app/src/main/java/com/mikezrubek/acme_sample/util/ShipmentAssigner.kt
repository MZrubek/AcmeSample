package com.mikezrubek.acme_sample.util

import com.mikezrubek.acme_sample.model.*

/**
 * Utility class for assigning shipments, using suitability scoring.
 *
 * Created by Mike Zrubek, 6/17/22.
 */
class ShipmentAssigner {

    companion object {

        /**
         * Associates a Driver to a Shipment, creating a list of Delivery instances
         * in a Deliveries container.
         *
         * The resulting Delivery instances list contains only one instance of any driver
         * name and one instance of any shipment address. No driver nor shipment exists more than
         * once in the returned list.
         *
         * Prioritizes this list on highest suitability scores, such that the overall highest scores
         * for any driver-shipment combination are provided in descending-score order.
         * Lower scores of other driver-shipment combinations are ignored.
         *
         * To accomplish this, processing is as follows:
         * - create a list of all possible driver and shipment combinations (p x s), stored
         * in Suitability instances, which automatically calculate a score upon creation.
         * - sort the list by suitability score descending order, placing the highest scores
         * higher in the list.
         * - iterate through this sorted list, and for each driver and shipment combination,
         * check if either already exist in the Delivery instances list; if neither exist,
         * add a new Delivery for the driver-shipment (and suitability score) into the Delivery list.
         * If either the driver or shipment are already assigned to a Delivery,
         * ignore them and move on to the next Suitability.
         *
         * The quantity of Delivery instances provided will be the minimum number of drivers or
         * shipments, whichever is fewer in count. For example, if there are 10 drivers and 6
         * shipments, the produced list will be 6 Delivery instances, of those containing the
         * highest suitability scores. If there are 0 drivers or 0 shipments, an empty list
         * is provided in the returned Deliveries instance.
         *
         * @param acmeData input data to process, consisting of drivers and shipments.
         * @return list of Delivery instances or error data in a Deliveries instance. An empty
         * Delivery list can be produced if there are 0 drivers or 0 shipments and no error occurred.
         */
        fun assignShipments(acmeData: AcmeData): Deliveries {
            // first determine suitability for all shipments and drivers,
            // and sort the returned list by highest suitability score descending to lowest.
            // Note that this creates a list of shipment x driver entries.
            val suitabilityList = buildSuitabilities(acmeData).sortedByDescending { it.score }

            // Read down the sorted list, and for each driver and shipment that is not added
            // to the deliveries list, add an entry for that driver-shipment pair.

            val deliveryList = mutableListOf<Delivery>()

            for (suitability in suitabilityList) {
                // check existing data in deliveryList for the driver or shipment; if neither are found,
                // add the suitability as a new delivery.
                if (deliveryList.find { delivery -> delivery.driver.name.equals(suitability.driver.name) } == null
                    && deliveryList.find { delivery -> delivery.shipment.address.equals(suitability.shipment.address) } == null) {
                    // the driver and supplier are not yet a delivery; create it.
                    deliveryList.add(Delivery(suitability.driver, suitability.shipment, suitability.score))
                }
            }

            return Deliveries(deliveryList)
        }

        /**
         * Builds a list of all driver x shipment combinations, creating a Suitability instance
         * of each.
         *
         * @param acmeData input data to process, consisting of drivers and shipments.
         * @return list of all possible Suitability instances. Of 0 drivers or 0 shipments, an
         * empty Suitability list is returned.
         */
        private fun buildSuitabilities(acmeData: AcmeData): List<Suitability> {
            val allSuitabiilties = mutableListOf<Suitability>()

            for (driver in acmeData.drivers) {
                for (shipment in acmeData.shipments) {
                    allSuitabiilties.add(Suitability(Driver(driver), Shipment(shipment)))
                }
            }

            return allSuitabiilties
        }
    }
}