package com.mikezrubek.acme_sample.feature.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikezrubek.acme_sample.R
import com.mikezrubek.acme_sample.model.AcmeData
import com.mikezrubek.acme_sample.model.Deliveries
import com.mikezrubek.acme_sample.model.Delivery
import com.mikezrubek.acme_sample.model.Driver
import com.mikezrubek.acme_sample.viewmodel.DeliveriesViewModel
import kotlinx.android.synthetic.main.main_fragment.*

/**
 * Created by Mike Zrubek, 6/17/22.
 */
class MainFragment: Fragment(), DeliverySelectionListener {

    private val viewModel: DeliveriesViewModel by lazy {
        ViewModelProvider(requireActivity())[DeliveriesViewModel::class.java]
    }

    private lateinit var listAdapter: DeliveriesListAdapter

    private val dataObserver =
        Observer<Deliveries> { data ->
            endProgress()
            if (data.deliveryList != null) {

                listAdapter.deliveriesList = data.deliveryList

            } else if (data.errorMessage != null) {
                // todo: show error message, implement as needed
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = DeliveriesListAdapter(requireContext(), this)
        deliveriesList.adapter = listAdapter
        deliveriesList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }

    override fun onStart() {
        super.onStart()
        // start getting the data; let it queue up before the UI is ready so it appears faster to user.
        viewModel.fetchAcmeData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.deliveriesLiveData.observe(this, dataObserver)
    }

    override fun onPause() {
        super.onPause()
        viewModel.deliveriesLiveData.removeObserver(dataObserver)
    }

    private fun endProgress() {
        progress.visibility = View.GONE
        deliveriesList.visibility = View.VISIBLE
    }


    override fun onDeliverySelected(delivery: Delivery) {
        // handle delivery selection from the list, such as opening for more details, etc.
    }
}