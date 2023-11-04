package au.cmcmarkets.ticker.feature.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import au.cmcmarkets.ticker.R
import au.cmcmarkets.ticker.feature.orderticket.OrderTicketFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            setOrderTickerFragment()
        }
    }

    private fun setOrderTickerFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, OrderTicketFragment())
            .commitNow()
    }
}
