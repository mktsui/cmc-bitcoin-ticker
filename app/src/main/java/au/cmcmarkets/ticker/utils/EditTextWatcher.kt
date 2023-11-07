package au.cmcmarkets.ticker.utils

import android.text.Editable
import android.text.TextWatcher

open class EditTextWatcher : TextWatcher {
    override fun afterTextChanged(sequence: Editable?) {}

    override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {}
}