package au.cmcmarkets.ticker.data.repository

import au.cmcmarkets.ticker.data.api.BitcoinApi
import au.cmcmarkets.ticker.data.model.MarketPrice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BitcoinRepository @Inject constructor(private val bitcoinAPI: BitcoinApi){
    fun getBitcoinMarketValue(): Flow<Map<String, MarketPrice>> = flow {
        emit(bitcoinAPI.getMarketValue())
    }
}