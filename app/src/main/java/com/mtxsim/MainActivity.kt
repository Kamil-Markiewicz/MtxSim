package com.mtxsim

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RadioButton
import kotlinx.android.synthetic.main.dialog_buy_vp.view.*
import kotlinx.android.synthetic.main.dialog_view_items.view.*

class MainActivity : AppCompatActivity(), MainView {

    private val DEBUG_MODE = true

    private lateinit var presenter: IMainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this, MainModel(this.getPreferences(Context.MODE_PRIVATE)))

        if(DEBUG_MODE) {
            val buttonDebug = findViewById<Button>(R.id.buttonDebug)
            buttonDebug.visibility = View.VISIBLE
        }
        updateVP()
        updateItems()
    }

    override fun updateVP() {
        val vp = presenter.getVP().toString() + "VP"
        findViewById<TextView>(R.id.textViewVPCount).text = vp
    }

    override fun updateItems() {
        findViewById<TextView>(R.id.textViewItemsCount).text = presenter.getItemCount().toString()
    }

    override fun displayMessage(msg: String) {
        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("ResourceType")
    private fun buyVPDialog(){
        val pValues = presenter.getPurchaseValues()
        if(pValues.isEmpty()){
            displayMessage("Error. Try again later.")
            return
        }

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_buy_vp, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)

        val rGroup = dialogView.radioGroup
        for (i in 0 until pValues.size) {
            val rb = RadioButton(this)
            rb.text = getPvString(pValues[i])
            rb.id = i
            rGroup.addView(rb)
        }

        if(pValues.size > 5)
            rGroup.check(4)
        else
            rGroup.check(0)

        val alert = dialogBuilder.create()
        dialogView.buttonCancel.setOnClickListener {alert.dismiss()}
        dialogView.buttonBuy.setOnClickListener {
            alert.dismiss()
            presenter.buyVP(pValues[rGroup.checkedRadioButtonId])
        }
        alert.show()
    }

    private fun viewItemsDialog(){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_view_items, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val  alert = dialogBuilder.create()

        dialogView.buttonReturn.setOnClickListener {alert.dismiss()}
        dialogView.textViewItemList.text = presenter.getItems().toString()
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
        return "€" + "$value" + " for " + "$vp" + "VP"
    }

    fun onClickBuyVP(view: View){
        buyVPDialog()
    }

    fun onClickBuyItem(view: View){
        presenter.buyItem()
    }

    fun onClickViewItemList(view: View){
        viewItemsDialog()
    }

    fun onClickDebug(view: View){
        presenter.debugWipe()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
