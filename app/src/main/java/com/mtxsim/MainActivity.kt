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

    private fun buyVPDialog(){
        val dialogBuilder = AlertDialog.Builder(this)
        val pValues = presenter.getPurchaseValues()

        var selection = 0
        //dialogBuilder.setMessage(getString(R.string.ChooseVpAmount))
        dialogBuilder.setSingleChoiceItems(getPvCharSeqArray(pValues), selection)
            { _, i -> selection = i}
        dialogBuilder.setPositiveButton("Buy", DialogInterface.OnClickListener {
                    dialog, _ -> dialog.dismiss()
                    presenter.buyVP(pValues[selection])
            })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, _ -> dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("VP Purchase")
        alert.show()
    }

    private fun selectedVpValue(pvs: List<PurchaseValues>, selection: Int){
        displayMessage(getPvString(pvs[selection]))
    }

    private fun getPvCharSeqArray(pvs: List<PurchaseValues>): Array<CharSequence> {
        val strings = ArrayList<CharSequence>()
        for(i in 0 until pvs.size)
            strings.add(getPvString(pvs[i]))
        return strings.toTypedArray()
    }

    private fun getPvString(pv: PurchaseValues): String {
        val value = pv.value
        val vp = pv.vpGiven
        return "$value" + " Euro for " + "$vp" + "VP"
    }

    fun onClickBuyVP(view: View){
        buyVPDialog()
    }

    fun onClickBuyItem(view: View){
        presenter.buyItem()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
