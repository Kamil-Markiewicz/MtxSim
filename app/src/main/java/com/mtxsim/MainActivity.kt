package com.mtxsim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickBuyVP(view: View){
        val counterText = findViewById<TextView>(R.id.textViewVPCount)
        counterText.text = (counterText.text.toString().toInt() + 1).toString()
        Toast.makeText(this@MainActivity, "You purchased 1 point!", Toast.LENGTH_SHORT).show()
    }

    fun onClickBuyItem(view: View){
        val counterVPText = findViewById<TextView>(R.id.textViewVPCount)
        counterVPText.text = (counterVPText.text.toString().toInt() - 2).toString()
        val counterItemText = findViewById<TextView>(R.id.textViewItemsCount)
        counterItemText.text = (counterItemText.text.toString().toInt() + 1).toString()
        Toast.makeText(this@MainActivity, "You purchased an item for 2 points!", Toast.LENGTH_SHORT).show()
    }
}
