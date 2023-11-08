package au.cmcmarkets.ticker.utils

import android.text.InputFilter
import android.text.Spanned


class DecimalInputFilter : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        if (Regex("^([0-9]{0,9})(\\.[0-9]{0,1})?\$").matches(dest.toString()))  // accept only 2 decimal place digits
            return null
        return ""
    }
}
