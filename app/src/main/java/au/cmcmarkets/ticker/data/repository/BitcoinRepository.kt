package au.cmcmarkets.ticker.data.repository

import android.util.Log
import au.cmcmarkets.ticker.data.api.BitcoinApi
import au.cmcmarkets.ticker.data.common.Response
import au.cmcmarkets.ticker.data.model.MarketPrice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BitcoinRepository @Inject constructor(private val bitcoinAPI: BitcoinApi){
    fun getBitcoinMarketValue(): Flow<Map<String, MarketPrice>> = flow {
        emit(bitcoinAPI.getMarketValue())
    }
}