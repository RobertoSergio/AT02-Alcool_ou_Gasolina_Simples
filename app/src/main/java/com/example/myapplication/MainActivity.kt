package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var alcoolPrice: TextInputEditText
    private lateinit var gasolinaPrice: TextInputEditText
    private lateinit var percentSwitch: Switch
    private lateinit var calculateButton: Button
    private lateinit var resultText: TextView
    private lateinit var percentText: TextView

    private var use75Percent = false
    private val sharedPrefKey = "fuel_calc_prefs"
    private val percentageKey = "use_75_percent"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        loadSavedPreference()

        initViews()
        setupClickListeners()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(percentageKey, use75Percent)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        use75Percent = savedInstanceState.getBoolean(percentageKey, false)
        updateSwitchText()
    }

    private fun initViews() {
        alcoolPrice = findViewById(R.id.alcoolPrice)
        gasolinaPrice = findViewById(R.id.gasolinaPrice)
        percentSwitch = findViewById(R.id.percentSwitch)
        calculateButton = findViewById(R.id.calculateButton)
        resultText = findViewById(R.id.resultText)
        percentText = findViewById(R.id.percentText)

        percentSwitch.isChecked = use75Percent
        updateSwitchText()
    }

    private fun setupClickListeners() {
        percentSwitch.setOnCheckedChangeListener { _, isChecked ->
            use75Percent = isChecked
            updateSwitchText()
            savePreference()
        }

        calculateButton.setOnClickListener {
            calculateBestFuel()
        }
    }

    private fun updateSwitchText() {
        val percentage = if (use75Percent) "75%" else "70%"
        percentText.text = percentage
    }

    private fun calculateBestFuel() {
        val alcoolText = alcoolPrice.text.toString().replace(',', '.')
        val gasolinaText = gasolinaPrice.text.toString().replace(',', '.')

        if (alcoolText.isEmpty() || gasolinaText.isEmpty()) {
            showResult("Digite ambos os preços", false)
            return
        }

        try {
            val alcoolValue = alcoolText.toDouble()
            val gasolinaValue = gasolinaText.toDouble()

            if (alcoolValue <= 0 || gasolinaValue <= 0) {
                showResult("Digite valores positivos", false)
                return
            }

            val percentage = if (use75Percent) 0.75 else 0.70
            val threshold = gasolinaValue * percentage

            val result = if (alcoolValue <= threshold) {
                "Melhor usar ÁLCOOL\n\n" +
                        "Preço do álcool: R$ ${formatCurrency(alcoolValue)}\n" +
                        "Limite (${(percentage * 100).toInt()}% da gasolina): R$ ${formatCurrency(threshold)}\n\n" +
                        "Álcool ≤ ${(percentage * 100).toInt()}% da Gasolina"
            } else {
                "Melhor usar GASOLINA\n\n" +
                        "Preço do álcool: R$ ${formatCurrency(alcoolValue)}\n" +
                        "Limite (${(percentage * 100).toInt()}% da gasolina): R$ ${formatCurrency(threshold)}\n\n" +
                        "Álcool > ${(percentage * 100).toInt()}% da Gasolina"
            }

            showResult(result, true)

        } catch (e: NumberFormatException) {
            showResult("Digite valores válidos\nUse vírgula ou ponto para decimais", false)
        }
    }

    private fun showResult(message: String, isSuccess: Boolean) {
        resultText.text = message
        if (isSuccess) {
            resultText.setTextColor(getColor(android.R.color.holo_green_light))
        } else {
            resultText.setTextColor(getColor(android.R.color.holo_red_light))
        }
    }

    private fun formatCurrency(value: Double): String {
        val df = DecimalFormat("#0.00")
        return df.format(value).replace('.', ',')
    }

    private fun savePreference() {
        val sharedPref = getSharedPreferences(sharedPrefKey, MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean(percentageKey, use75Percent)
            apply()
        }
    }

    private fun loadSavedPreference() {
        val sharedPref = getSharedPreferences(sharedPrefKey, MODE_PRIVATE)
        use75Percent = sharedPref.getBoolean(percentageKey, false)
    }
}