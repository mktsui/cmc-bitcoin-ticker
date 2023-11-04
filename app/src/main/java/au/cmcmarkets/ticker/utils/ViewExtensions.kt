package au.cmcmarkets.ticker.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.getSystemService

fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, text, duration).show()

fun View?.hideKeyboard() {
    //Find the currently focused view, so we can grab the correct window token from it.
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    this?.also { view ->
        context.getSystemService< InputMethodManager>()?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}