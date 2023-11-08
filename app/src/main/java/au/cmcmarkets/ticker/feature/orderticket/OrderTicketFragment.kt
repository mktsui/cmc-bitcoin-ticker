package au.cmcmarkets.ticker.feature.orderticket

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import au.cmcmarkets.ticker.core.di.viewmodel.ViewModelFactory
import au.cmcmarkets.ticker.databinding.FragmentOrderTicketBinding
import au.cmcmarkets.ticker.feature.orderticket.enums.EditType
import au.cmcmarkets.ticker.feature.orderticket.model.PanelRate
import au.cmcmarkets.ticker.utils.DecimalInputFilter
import au.cmcmarkets.ticker.utils.EditTextWatcher
import au.cmcmarkets.ticker.utils.blink
import au.cmcmarkets.ticker.utils.hideKeyboard
import au.cmcmarkets.ticker.utils.parseCurrency
import au.cmcmarkets.ticker.utils.spanDecSmall
import au.cmcmarkets.ticker.utils.to2Dec
import au.cmcmarkets.ticker.utils.to2DecCurrency
import au.cmcmarkets.ticker.utils.toast
import dagger.android.support.DaggerFragment
import java.math.BigDecimal
import javax.inject.Inject


class OrderTicketFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    /*
    *  kotlin-android-extensions is deprecated, using databinding library now to reference id from XML layouts
    *  For more details: https://developer.android.com/topic/libraries/view-binding/migration
    */
    private var _binding: FragmentOrderTicketBinding? = null
    private val binding get() = _binding!!

    private val _inputUpdate = MutableLiveData<String>()
    val inputLD: LiveData<String> get() = _inputUpdate

    private var buyRate: BigDecimal = BigDecimal.ZERO

    private val unitsTextWatcher = object : EditTextWatcher() {
        override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
            // update amount when units change
            setAmount()
        }
    }

    private val amountTextWatcher = object : EditTextWatcher() {
        override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
            // update units when amount change
            setUnits()
        }
    }

    private val viewModel: OrderTicketViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[OrderTicketViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restoreOrderTicket(savedInstanceState)
        setObserver()
        setListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ORDER_UNITS, binding.etUnits.text.toString())
        outState.putString(ORDER_AMOUNT, binding.etAmount.text.toString())
    }

    private fun setListeners() {
        binding.btnCancel.setOnClickListener { resetInput() }
        binding.btnConfirm.setOnClickListener { confirmOrder() }

        // apply filter to only allow 2 decimal places
        binding.etAmount.filters = arrayOf<InputFilter>(DecimalInputFilter())
        binding.etUnits.filters = arrayOf<InputFilter>(DecimalInputFilter())

        // listen to text change only when corresponding edit text is on focus
        binding.etAmount.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // update units first
                setUnits()
                binding.etAmount.addTextChangedListener(amountTextWatcher)
            } else {
                binding.etAmount.removeTextChangedListener(amountTextWatcher)
            }
        }
        binding.etUnits.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                setAmount()
                binding.etUnits.addTextChangedListener(unitsTextWatcher)
            } else {
                binding.etUnits.removeTextChangedListener(unitsTextWatcher)
            }
        }
    }

    private fun setObserver() {
        viewModel.panelRateLD.observe(viewLifecycleOwner) {
            refreshPanel(it)
            buyRate = it.priceBuy
            setAmount()
            setUnits()
        }
        viewModel.toastMsgLD.observe(viewLifecycleOwner) {
            binding.root.context.toast(it.toString())
        }
    }

    private fun restoreOrderTicket(savedInstanceState: Bundle?) {
        binding.etUnits.setText(savedInstanceState?.getString(ORDER_UNITS))
        binding.etAmount.setText(savedInstanceState?.getString(ORDER_AMOUNT))
    }

    private fun enableBtnConfirm(state: Boolean) {
        binding.btnConfirm.isActivated = state
        binding.btnConfirm.isEnabled = state
        binding.btnConfirm.isClickable = state
    }

    private fun refreshPanel(panelRate: PanelRate) {
        binding.priceSell.text = panelRate.priceSell.to2DecCurrency().spanDecSmall(0.7f, ".")
        binding.priceBuy.text = panelRate.priceBuy.to2DecCurrency().spanDecSmall(0.7f, ".")
        binding.tvSpread.text = panelRate.priceSpread.to2DecCurrency()

        binding.priceSell.blink()
        binding.priceBuy.blink()

    }



    private fun updateEditfields(buyRate: BigDecimal, checkField: EditText, setField: EditText): BigDecimal? {

        if (buyRate == BigDecimal.ZERO) {
            setField.setText("0")
            return null
        }

        if (checkField.text.isNullOrBlank()) {
            if (!setField.text.isNullOrBlank()) setField.setText("")
            return null
        }

        val newInput = checkField.text.toString().parseCurrency()?.toBigDecimalOrNull()

        if (newInput == null) {
            setField.setText("")
            return null
        }

        if (newInput == BigDecimal.ZERO) {
            setField.setText("0")
            return null
        }

        return newInput
    }

    private fun isInputValid(buyRate: BigDecimal, thisField: EditText, nextField: EditText, editType: EditType): Boolean {
        val nextValue = updateEditfields(buyRate, nextField, thisField) ?: return false
        val thisValue = when (editType) {
            EditType.UNITS -> (nextValue.toDouble() / buyRate.toDouble()).to2Dec()
            EditType.AMOUNT -> (nextValue.toDouble() * buyRate.toDouble()).to2Dec()
        }
        thisField.setText(thisValue)
        return thisValue != "0"
    }

    private fun setUnits() {

        if (!binding.etUnits.hasFocus()) {
            enableBtnConfirm(isInputValid(
                buyRate,
                binding.etUnits,
                binding.etAmount,
                EditType.UNITS)
            )
        }
    }

    private fun setAmount() {
        if (!binding.etAmount.hasFocus()) {
            enableBtnConfirm(isInputValid(buyRate,
                    binding.etAmount,
                    binding.etUnits,
                    EditType.AMOUNT)
            )
        }
    }

    private fun confirmOrder() {
        if (binding.btnConfirm.isActivated) {
            binding.etUnits.clearFocus()
            binding.etAmount.clearFocus()
            binding.root.hideKeyboard()
        }
    }

    private fun resetInput() {
        binding.etUnits.clearFocus()
        binding.etAmount.clearFocus()
        binding.etUnits.setText("")
        binding.etAmount.setText("")
        binding.root.hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ORDER_UNITS = "OrderTicket_Units"
        private const val ORDER_AMOUNT = "OrderTicket_Amount"
    }
}

