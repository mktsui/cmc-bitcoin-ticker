package au.cmcmarkets.ticker.feature.orderticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import au.cmcmarkets.ticker.R
import au.cmcmarkets.ticker.core.di.viewmodel.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class OrderTicketFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: OrderTicketViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(OrderTicketViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_order_ticket, container, false)

    override fun onResume() {
        super.onResume()

        // TODO("Resume Polling")
    }

    override fun onPause() {
        super.onPause()

        // TODO("Stop polling")
    }
}

