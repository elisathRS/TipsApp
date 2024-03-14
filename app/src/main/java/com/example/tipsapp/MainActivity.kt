package com.example.tipsapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.SeekBar.OnSeekBarChangeListener
import android.animation.ArgbEvaluator
import androidx.core.content.ContextCompat


private const val TAG = "MainActivity"
private const val INIT_PERC_TIP = 15

class MainActivity : AppCompatActivity() {

    private lateinit var editTxtBase: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var txtViewTip: TextView
    private lateinit var txtViewTotal: TextView
    private lateinit var txtViewPercentLabel: TextView
    private lateinit var txtViewTipDesc: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTxtBase = findViewById(R.id.editTxtBase)
        seekBarTip = findViewById(R.id.seekBarTip)
        txtViewTip = findViewById(R.id.txtViewTip)
        txtViewTotal = findViewById(R.id.txtViewTotal)
        txtViewPercentLabel = findViewById(R.id.txtViewPercentLabel)
        txtViewTipDesc = findViewById(R.id.txtViewTipDesc)


        seekBarTip.progress = INIT_PERC_TIP
        "$INIT_PERC_TIP%".also { txtViewPercentLabel.text = it }
        updateTipDesc(INIT_PERC_TIP)

        seekBarTip.setOnSeekBarChangeListener(object: OnSeekBarChangeListener{
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                txtViewPercentLabel.text = "$progress%"
                calculateTipAndTotal()
                updateTipDesc(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No implementation needed here
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No implementation needed here
            }

        })


        editTxtBase.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No implementation needed here
            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                calculateTipAndTotal()
            }

        })
    }

    private fun updateTipDesc(tipPerc: Int){
        val tipDesc = when (tipPerc){
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        txtViewTipDesc.text = tipDesc

        //Update tip Description color based on the value selected
        val tipColor = ArgbEvaluator().evaluate(
                tipPerc.toFloat()/ seekBarTip.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        ) as Int
        txtViewTipDesc.setTextColor((tipColor)
        )


    }
    private fun calculateTipAndTotal(){
        //Obtain the value of the base and the tip percent
        val baseAmount = editTxtBase.text.toString().toDoubleOrNull() ?: 0.0
        val tipPercent = seekBarTip.progress

        //Calculate tip and total
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount

        // Update the UI
        txtViewTip.text = String.format("%.2f", tipAmount)
        txtViewTotal.text = String.format("%.2f", totalAmount)
    }

}
