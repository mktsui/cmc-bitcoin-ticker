package au.cmcmarkets.ticker.data.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class MarketPrice(
    @SerializedName("15m")
    val delay15m: BigDecimal = BigDecimal.ZERO,
    @SerializedName("last")
    val priceLast: BigDecimal = BigDecimal.ZERO,
    @SerializedName("buy")
    val priceBuy: BigDecimal = BigDecimal.ZERO,
    @SerializedName("sell")
    val priceSell: BigDecimal = BigDecimal.ZERO,
    @SerializedName("symbol")
    val currencySymbol: String = ""
)
