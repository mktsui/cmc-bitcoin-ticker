package au.cmcmarkets.ticker.feature.orderticket

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import au.cmcmarkets.ticker.data.common.Response
import au.cmcmarkets.ticker.data.model.MarketPrice
import au.cmcmarkets.ticker.data.repository.BitcoinRepository
import au.cmcmarkets.ticker.utils.AppConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


class OrderTicketViewModel @Inject constructor(
    private val bitcoinRepository: BitcoinRepository
) : ViewModel(), DefaultLifecycleObserver {

    private val _priceList = MutableLiveData<Response<Map<String, MarketPrice>>>()
    val priceLiveData: LiveData<Response<Map<String, MarketPrice>>> get() = _priceList

    private var pollingJob: Job? = null

    private fun getPrices() {
        viewModelScope.launch{
            val bitcoinPrice = bitcoinRepository.getBitcoinMarketValue()
            bitcoinPrice.collect {
                when (it) {
                    is Response.Loading -> {
                        _priceList.value = Response.Loading()
                    }
                    is Response.Success -> {
                        _priceList.value = Response.Success(it.data!!)
                        Log.d(TAG, "getPrices: " + it.data[AppConstant.CURRENCY].toString())
                    }
                    is Response.Error -> {
                        _priceList.value = Response.Error(it.errorMessage)
                    }
                }
            }
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        Log.d(TAG, "onResume: ")
        pollingJob = owner.lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                getPrices()
                delay(AppConstant.POLL_INTERVAL_SEC.toLong() * 1000)
            }
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        Log.d(TAG, "onPause: ")
        pollingJob?.cancel()
        pollingJob = null
    }

    companion object {
        private const val TAG = "OrderTicketViewModel"
    }
}