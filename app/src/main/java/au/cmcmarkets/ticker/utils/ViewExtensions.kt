package au.cmcmarkets.ticker.utils

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.getSystemService
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat


fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, text, duration).show()

fun View?.hideKeyboard() {
    //Find the currently focused view, so we can grab the correct window token from it.
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    this?.also { view ->
        context.getSystemService< InputMethodManager>()?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

// Convert BigDecimal to String to always show 2 decimal places
fun BigDecimal?.to2DecCurrency(): String? {
    this?.let {
        val df = DecimalFormat("#,###,###,##0.00")
        return df.format(this.setScale(2, RoundingMode.HALF_UP))
    }
    return null
}

// Convert Double to String to always show 2 decimal places
fun Double?.to2Dec(): String {
    this?.let {
        val df = DecimalFormat("0.##")
        return df.format(it)
    }
    return ""
}

// Remove "," from currency string
fun String?.parseCurrency(): String? {
    this?.let {
        return it.replace(",", "")
    }
    return null
}

// Create strings for views to display smaller font size after decimal
fun String?.spanDecSmall(reduceBy: Float, afterChar:String): SpannableString? {
    this?.let {
        val smallSizeText = RelativeSizeSpan(reduceBy)
        val ssBuilder = SpannableString(this)
        ssBuilder.setSpan(
            smallSizeText,
            this.indexOf(afterChar),
            this.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return ssBuilder
    }
    return null
}

// Apply blink animation once for text view
fun TextView?.blink() {
    this?.let {
        val colorAnim = ObjectAnimator.ofArgb(
            this, "textColor",
            Color.WHITE, Color.GREEN, Color.WHITE
        )
        colorAnim.setEvaluator(ArgbEvaluator())
        colorAnim.duration = 800
        colorAnim.start()
    }
}
