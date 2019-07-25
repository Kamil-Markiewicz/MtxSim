package com.mtxsim

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.content.DialogInterface

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickBuyVP(view: View){
        //val counterText = findViewById<TextView>(R.id.textViewVPCount)
        //counterText.text = (counterText.text.toString().toInt() + 1).toString()
        //val pValues1 = PurchaseValues(2.0, 2)
        //val pValues2 = PurchaseValues(5.0, 6)
        //val pValues3 = PurchaseValues(10.0, 13)
        //val pValues4 = PurchaseValues(20.0, 27)
        //val pValues5 = PurchaseValues(50.0, 60)
        //Toast.makeText(this@MainActivity, getVPValueString(pValues2), Toast.LENGTH_SHORT).show()

        buyVPDialog()
    }

    fun onClickBuyItem(view: View){
        adjustVP(-2)
        val counterItemText = findViewById<TextView>(R.id.textViewItemsCount)
        counterItemText.text = (counterItemText.text.toString().toInt() + 1).toString()
        Toast.makeText(this@MainActivity, "You purchased an item for 2 points!", Toast.LENGTH_SHORT).show()
    }

    private fun buyVPDialog(){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(getString(R.string.ChooseVpAmount))
            .setPositiveButton("Buy", DialogInterface.OnClickListener {
                    dialog, id -> dialog.dismiss()
                    adjustVP(2)
                    Toast.makeText(this@MainActivity, "You purchased 2 VP!", Toast.LENGTH_SHORT).show()
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("VP Purchase")
        alert.show()
    }

    private fun adjustVP(diff: Int){
        val counterText = findViewById<TextView>(R.id.textViewVPCount)
        counterText.text = (counterText.text.toString().toInt() + diff).toString()
    }

    fun getVPValueString(pv: PurchaseValues): String {
        val value = pv.value
        val vp = pv.vpGiven
        return "$value" + " Euro for " + "$vp" + "VP"
    }
}
