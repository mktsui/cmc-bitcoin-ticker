package au.cmcmarkets.ticker.data.api

import au.cmcmarkets.ticker.data.model.MarketValue
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface BitcoinApi {

    @GET("ticker")
    suspend fun getMarketValue(): Map<String, MarketValue>

}