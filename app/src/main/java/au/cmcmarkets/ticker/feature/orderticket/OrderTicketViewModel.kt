package au.cmcmarkets.ticker.feature.orderticket

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import au.cmcmarkets.ticker.data.model.MarketPrice
import au.cmcmarkets.ticker.data.repository.BitcoinRepository
import au.cmcmarkets.ticker.feature.orderticket.model.PanelRate
import au.cmcmarkets.ticker.utils.AppConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


class OrderTicketViewModel @Inject constructor(
    private val bitcoinRepository: BitcoinRepository
) : ViewModel(), DefaultLifecycleObserver {

    private val _toastMsg = MutableLiveData<String>()
    val toastMsgLD: LiveData<String> get() = _toastMsg

    private val _panelRate = MutableLiveData<PanelRate>()
    val panelRateLD: LiveData<PanelRate> get() = _panelRate


    var pollingJob: Job? = null

    fun setOrderTicket(prices: Map<String, MarketPrice>): PanelRate? {
        return prices[AppConstant.CURRENCY]?.toOrderTicket()
    }

    fun MarketPrice.toOrderTicket() = PanelRate(
        priceBuy = priceBuy,
        priceSell = priceSell,
        currencySymbol = currencySymbol
    )

    fun startPolling(owner: LifecycleOwner) {
        pollingJob = owner.lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                bitcoinRepository.getBitcoinMarketValue()
                    .catch { e ->
                        // Error Handling
                        stopPolling()
                        _toastMsg.postValue("Error: $e")
                    }
                    .collect { newPrices ->
                        setOrderTicket(newPrices)?.let {
                            _panelRate.postValue(it)
                        }
                    }
                delay(AppConstant.POLL_INTERVAL_SEC.toLong() * 1000)
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }


    override fun onResume(owner: LifecycleOwner) {
        startPolling(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        stopPolling()
    }
}