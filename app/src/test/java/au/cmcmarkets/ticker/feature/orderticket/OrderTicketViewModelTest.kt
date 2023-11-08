package au.cmcmarkets.ticker.feature.orderticket.au.cmcmarkets.ticker.feature.orderticket

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.testing.TestLifecycleOwner
import au.cmcmarkets.ticker.data.model.MarketPrice
import au.cmcmarkets.ticker.data.repository.BitcoinRepository
import au.cmcmarkets.ticker.feature.orderticket.OrderTicketViewModel
import au.cmcmarkets.ticker.feature.orderticket.model.PanelRate
import com.example.android.kotlincoroutines.main.utils.MainCoroutineScopeRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

import java.math.BigDecimal


@OptIn(ExperimentalCoroutinesApi::class)
class OrderTicketViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    private val bitcoinRepository: BitcoinRepository = mockk()

    private lateinit var viewModel: OrderTicketViewModel

    private val mockLifeCycleOwner = TestLifecycleOwner()

    private val samplePanelRate = PanelRate(
        BigDecimal.TEN,
        BigDecimal.TEN,
        "GBP"
    )

    private val sampleMarketPrices = MarketPrice (
        delay15m = BigDecimal.TEN,
        priceLast = BigDecimal.TEN,
        priceBuy = BigDecimal.TEN,
        priceSell = BigDecimal.TEN,
        currencySymbol = "GBP"
    )

    private var sampleResponse = hashMapOf<String, MarketPrice> (
        "GBP" to sampleMarketPrices
    )

    private val sampleBitCoinResponse = sampleResponse.toMap()

    @Before
    fun setUp() {
        viewModel = OrderTicketViewModel(bitcoinRepository)

    }

    @Test
    fun `API response can correctly transform to panel price`() {
        var panelPrice = viewModel.setOrderTicket(sampleBitCoinResponse)
        assertEquals(panelPrice, samplePanelRate)
    }

    @Test
    fun `polling starts after onResume`() {
        viewModel.stopPolling()

        viewModel.onResume(mockLifeCycleOwner)

        val started = if (viewModel.pollingJob==null) false else viewModel.pollingJob!!.isActive
        assertTrue(started)
    }

    @Test
    fun `polling stops after onPause`() {
        viewModel.startPolling(mockLifeCycleOwner)

        viewModel.onPause(mockLifeCycleOwner)

        val stopped = if (viewModel.pollingJob==null) true else viewModel.pollingJob!!.isCancelled
        assertTrue(stopped)
    }

}