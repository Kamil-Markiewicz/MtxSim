package com.mtxsim

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RadioButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_buy_items.view.*
import kotlinx.android.synthetic.main.dialog_buy_vp.view.*
import kotlinx.android.synthetic.main.dialog_buy_vp.view.buttonBuy
import kotlinx.android.synthetic.main.dialog_buy_vp.view.buttonCancel
import kotlinx.android.synthetic.main.dialog_debug.view.*
import kotlinx.android.synthetic.main.dialog_view_items.view.*
import kotlinx.android.synthetic.main.dialog_view_items.view.buttonReturn

class MainActivity : AppCompatActivity(), MainView {

    private val DEBUG_MODE = true

    private lateinit var presenter: IMainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this, MainModel(this.getPreferences(Context.MODE_PRIVATE)))
        updateVP()
        updateItems()

        if(DEBUG_MODE) {findViewById<CardView>(R.id.cardDebug).visibility = View.VISIBLE}
    }

    override fun updateVP() {
        val vp = presenter.getVP().toString() + "VP"
        findViewById<TextView>(R.id.textViewVPCount).text = vp
    }

    override fun updateItems() {
        findViewById<TextView>(R.id.textViewItemsCount).text = presenter.getItemCount().toString()
    }

    override fun displayMessage(msg: String) {
        findViewById<TextView>(R.id.textViewLastActionText).text = msg
        findViewById<CardView>(R.id.cardAction).visibility = View.VISIBLE
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
        for (i in pValues.indices) {
            val rb = RadioButton(this)
            rb.text = getPvString(pValues[i])
            rb.id = i
            rGroup.addView(rb)
        }

        if(pValues.size > 5) rGroup.check(4)
        else rGroup.check(0)

        val alert = dialogBuilder.create()
        dialogView.buttonCancel.setOnClickListener {alert.dismiss()}
        dialogView.buttonBuy.setOnClickListener {
            alert.dismiss()
            presenter.buyVP(pValues[rGroup.checkedRadioButtonId])
        }
        alert.show()
    }

    private fun buyItemsDialog(){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_buy_items, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val  alert = dialogBuilder.create()

        var itemsToBuy = 1
        val itemCost = presenter.getItemCost()
        val buyCountText = dialogView.textViewBuyCount
        val itemCostText = dialogView.textViewItemCost
        dialogView.textViewItemVP.text = presenter.getVP().toString() + "VP"
        updateBuyFields(buyCountText, itemCostText, itemsToBuy, itemCost)

        dialogView.buttonMinus.setOnClickListener {
            if(itemsToBuy > 0){
                itemsToBuy--
                updateBuyFields(buyCountText, itemCostText, itemsToBuy, itemCost)
            }

        }
        dialogView.buttonPlus.setOnClickListener {
            itemsToBuy++
            updateBuyFields(buyCountText, itemCostText, itemsToBuy, itemCost)
        }

        dialogView.buttonCancel.setOnClickListener {alert.dismiss()}
        dialogView.buttonBuy.setOnClickListener {
            presenter.buyItems(itemsToBuy)
            alert.dismiss()
        }

        alert.show()
    }

    private fun updateBuyFields(buyCountField: TextView, itemCostField: TextView,
                                itemsToBuy: Int, itemCost: Int){
        buyCountField.text = itemsToBuy.toString()
        itemCostField.text = (itemsToBuy*itemCost).toString() + "VP"
    }

    private fun viewItemsDialog(){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_view_items, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val  alert = dialogBuilder.create()

        dialogView.buttonReturn.setOnClickListener {alert.dismiss()}

        val itemList = presenter.getItems().toTypedArray()
        val viewManager = LinearLayoutManager(this)
        val viewAdapter = RecyclerAdapter(itemList)

        dialogView.recyclerItemList.apply {
            setHasFixedSize(false)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        alert.show()
    }

    private fun viewDebugDialog(){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_debug, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val  alert = dialogBuilder.create()

        dialogView.buttonReturn.setOnClickListener {alert.dismiss()}
        dialogView.buttonClearVp.setOnClickListener {presenter.debugWipe()}
        alert.show()
    }

    private fun selectedVpValue(pvs: List<PurchaseValues>, selection: Int){
        displayMessage(getPvString(pvs[selection]))
    }

    private fun getPvCharSeqArray(pvs: List<PurchaseValues>): Array<CharSequence> {
        val strings = ArrayList<CharSequence>()
        for(pv in pvs) strings.add(getPvString(pv))
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
        buyItemsDialog()
    }

    fun onClickViewItemList(view: View){
        viewItemsDialog()
    }

    fun onClickDebug(view: View){
        viewDebugDialog()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}