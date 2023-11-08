package au.cmcmarkets.ticker.feature.orderticket.model

import java.math.BigDecimal

class OrderTicket (
    val buyEntity: String,
    val sellEntity: String,
    val marketBuyPrice: BigDecimal,
    val marketSellPrice: BigDecimal,
    val units: BigDecimal,
    val amount: BigDecimal
)