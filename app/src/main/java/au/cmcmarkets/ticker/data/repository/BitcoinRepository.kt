package au.cmcmarkets.ticker.data.repository

import au.cmcmarkets.ticker.data.api.BitcoinApi
import au.cmcmarkets.ticker.data.common.Response
import au.cmcmarkets.ticker.data.model.MarketValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BitcoinRepository @Inject constructor(private val repository: BitcoinApi){

    fun getBitcoinMarketValue(): Flow<Response<Map<String, MarketValue>>> = flow {
        try {
            emit(Response.Success(repository.getMarketValue()))
        } catch (e: HttpException) {
            emit(
                Response.Error(errorMessage = "Retrying...", data = null)
            )
        } catch (e: IOException) {
            Response.Error(errorMessage = "Internet Error",data = null)
        }
    }
}