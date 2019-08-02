package com.mtxsim

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.content.DialogInterface

class MainActivity : AppCompatActivity(), MainView {

    private val presenter = MainPresenter(this, MainModelVariables())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateVP()
        updateItems()
    }

    override fun updateVP() {
        val counterText = findViewById<TextView>(R.id.textViewVPCount)
        counterText.text = presenter.getVP().toString()
    }

    override fun updateItems() {
        val counterItemText = findViewById<TextView>(R.id.textViewItemsCount)
        counterItemText.text = presenter.getItems().toString()
    }

    override fun displayMessage(msg: String) {
        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
    }

    fun onClickBuyVP(view: View){
        buyVPDialog()
    }

    fun onClickBuyItem(view: View){
        presenter.buyItem()
    }

    private fun buyVPDialog(){
        val dialogBuilder = AlertDialog.Builder(this)
        val pValues1 = PurchaseValues(2.0, 2)
        //val pValues2 = PurchaseValues(5.0, 6)
        //val pValues3 = PurchaseValues(10.0, 13)
        //val pValues4 = PurchaseValues(20.0, 27)
        //val pValues5 = PurchaseValues(50.0, 60)
        //getVPValueString(pValues1)
        dialogBuilder.setMessage(getString(R.string.ChooseVpAmount))
            .setPositiveButton("Buy", DialogInterface.OnClickListener {
                    dialog, id -> dialog.dismiss()
                    presenter.buyVP(pValues1)
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("VP Purchase")
        alert.show()
    }

    fun getVPValueString(pv: PurchaseValues): String {
        val value = pv.value
        val vp = pv.vpGiven
        return "$value" + " Euro for " + "$vp" + "VP"
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
