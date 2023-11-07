package au.cmcmarkets.ticker.feature.orderticket

import android.util.Log
import android.widget.EditText
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import au.cmcmarkets.ticker.data.model.MarketPrice
import au.cmcmarkets.ticker.data.repository.BitcoinRepository
import au.cmcmarkets.ticker.feature.orderticket.enums.EditType
import au.cmcmarkets.ticker.feature.orderticket.model.PanelRate
import au.cmcmarkets.ticker.utils.AppConstant
import au.cmcmarkets.ticker.utils.parseCurrency
import au.cmcmarkets.ticker.utils.to2Dec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import java.math.BigDecimal
import javax.inject.Inject


class OrderTicketViewModel @Inject constructor(
    private val bitcoinRepository: BitcoinRepository
) : ViewModel(), DefaultLifecycleObserver {

    private val _marketPrices = MutableLiveData<Map<String, MarketPrice>>()
    val marketLD: LiveData<Map<String, MarketPrice>> get() = _marketPrices

    private val _panelRate = MutableLiveData<PanelRate>()
    val panelRateLD: LiveData<PanelRate> get() = _panelRate


    private var pollingJob: Job? = null

    private fun setOrderTicket(prices: Map<String, MarketPrice>): PanelRate? {
        return prices[AppConstant.CURRENCY]?.toOrderTicket()
    }

    private fun MarketPrice.toOrderTicket() = PanelRate(
        priceBuy = priceBuy,
        priceSell = priceSell,
        currencySymbol = currencySymbol
    )

    private fun startPolling(owner: LifecycleOwner) {
        pollingJob = owner.lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                bitcoinRepository.getBitcoinMarketValue()
                    .catch { _ ->
                        // Error Handling
                        stopPolling()

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

    private fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }


    private fun inputCheck(buyRate: BigDecimal, checkField: EditText, setField: EditText): BigDecimal? {

        if (buyRate == BigDecimal.ZERO) {
            setField.setText("0")
            return null
        }

        if (checkField.text.isNullOrBlank()) {
            if (!setField.text.isNullOrBlank()) setField.setText("")
            return null
        }

        val newInput = checkField.text.toString().parseCurrency()?.toBigDecimalOrNull()

        if (newInput == null) {
            setField.setText("")
            return null
        }

        if (newInput == BigDecimal.ZERO) {
            setField.setText("0")
            return null
        }

        return newInput
    }

    fun isInputValid(buyRate: BigDecimal, thisField: EditText, nextField: EditText, editType: EditType): Boolean {
        val nextValue = inputCheck(buyRate, nextField, thisField) ?: return false
        val thisValue = when (editType) {
            EditType.UNITS -> (nextValue.toDouble() / buyRate.toDouble()).to2Dec()
            EditType.AMOUNT -> (nextValue.toDouble() * buyRate.toDouble()).to2Dec()
        }
        thisField.setText(thisValue)
        return thisValue != "0"
    }

    override fun onResume(owner: LifecycleOwner) {
        startPolling(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        stopPolling()
    }
}