package au.cmcmarkets.ticker.feature.orderticket.model

import java.math.BigDecimal

data class PanelRate(
    val priceBuy: BigDecimal,
    val priceSell: BigDecimal,
    val currencySymbol: String
) {
    val priceSpread: BigDecimal = (priceBuy - priceSell).abs()
}
