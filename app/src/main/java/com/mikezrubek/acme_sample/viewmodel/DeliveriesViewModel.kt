package com.mikezrubek.acme_sample.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mikezrubek.acme_sample.model.*
import com.mikezrubek.acme_sample.repository.AcmeDataRepository
import com.mikezrubek.acme_sample.util.ShipmentAssigner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * View model supporting the loading of Acme's data, processing it into a collection
 * of "Delivery" instances, containing a Driver and a Shipment.
 *
 * Initiate the data acquisition by calling #fetchAcmeData. The resulting Deliveries
 * instance is posted to #deliveriesLiveData for Observers to consume.
 *
 * Created by Mike Zrubek, 6/17/22.
 */
class DeliveriesViewModel(application: Application): AndroidViewModel(application) {

    // contains the drivers and schedules data; assumes the data is not too large for livedata
    val deliveriesLiveData = MutableLiveData<Deliveries>()

    // use this flag to only fetch the data one time. The last instance will be in livedata.
    private var dataFetched = false

    /**
     * Load the data and deliveries. Uses existing data if already present, otherwise loads
     * the data and processes it before making it available in livedata.
     */
    fun fetchAcmeData() {

        if (!dataFetched) {

            // use a coroutine to read the file data from an IO thread.
            // Result is posted back to livedata when done.
            viewModelScope.launch(Dispatchers.IO) {
                val acmeData = loadAcmeDataFromFileRepository()
                dataFetched = true
                val deliveries = if (acmeData.noError()) {
                    assignShipments(acmeData)
                } else {
                    Deliveries(errorMessage = acmeData.errorMessage)
                }
                deliveriesLiveData.postValue(deliveries)
            }
        }
        // else the current data should already be in the livedata instance
    }

    /**
     * Loads json data from its repository.
     * @return AcmeData instance with the loaded data or with error details.
     */
    private suspend fun loadAcmeDataFromFileRepository(): AcmeData {
        return withContext(Dispatchers.IO) {
            // post the results directly to live data
            AcmeDataRepository(getApplication()).getData()
        }
    }

    /**
     * Assigns shipments to drivers.
     *
     * @param acmeData input data to process, consisting of drivers and shipments.
     * @return list of Delivery instances or error data in a Deliveries instance.
     */
    private suspend fun assignShipments(acmeData: AcmeData): Deliveries {

        // simulate a delay of slow processing upon loading..just for this exercise.
        // todo: remove before release! [this is purposely left here for this exercise. MZ.]
        delay(2000)

        // defer the logic and details to thie specialized class.
        return ShipmentAssigner.assignShipments(acmeData)

        /*
        // early-dev mock data code before using more appropriate logic; no longer used, left here
        // for purposes of this exercise; it would otherwise normally be removed.

        val deliveryList = mutableListOf<Delivery>()
        for (i in 0..9) {
            deliveryList.add(Delivery(Driver(acmeData.drivers[i]), Shipment(acmeData.shipments[i]), i.toFloat()))
        }
        return Deliveries(deliveryList)
        */
    }
}