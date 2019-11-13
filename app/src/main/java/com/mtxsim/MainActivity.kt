package com.mtxsim

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.view.LayoutInflater
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

        //Show debug button when in debug mode
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
        //Populate message and show message field
        findViewById<TextView>(R.id.textViewLastActionText).text = msg
        findViewById<CardView>(R.id.cardAction).visibility = View.VISIBLE
    }

    @SuppressLint("ResourceType")
    private fun buyVPDialog(){
        //Hide message box after new menu
        findViewById<CardView>(R.id.cardAction).visibility = View.GONE

        //Get list of vp points and corresponding prices
        val pValuesList = presenter.getPurchaseValues()
        if(pValuesList.isEmpty()){
            //If the model returns no purchase value an error has occurred
            displayMessage("Error. Try again later.")
            return
        }

        //Create dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_buy_vp, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val alert = dialogBuilder.create()

        //Populate radio group with price options
        val rGroup = dialogView.radioGroup
        for (i in pValuesList.indices) {
            val rb = RadioButton(this)
            rb.text = getPvString(pValuesList[i])
            rb.id = i
            rGroup.addView(rb)
        }

        //Select default price to 4th item if enough in the list
        if(pValuesList.size > 5) rGroup.check(4)
        else rGroup.check(0)

        //Hook up buttons to events and show dialog
        dialogView.buttonCancel.setOnClickListener {alert.dismiss()}
        dialogView.buttonBuy.setOnClickListener {
            alert.dismiss()
            presenter.buyVP(pValuesList[rGroup.checkedRadioButtonId])
        }
        alert.show()
    }

    private fun buyItemsDialog(){
        //Hide message box after new menu
        findViewById<CardView>(R.id.cardAction).visibility = View.GONE

        //Create dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_buy_items, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val  alert = dialogBuilder.create()

        //Display balance
        dialogView.textViewItemVP.text = presenter.getVP().toString() + "VP"

        //Store price of items in cart
        var itemsToBuy = 1
        val itemCost = presenter.getItemCost()
        val buyCountText = dialogView.textViewBuyCount
        val itemCostText = dialogView.textViewItemCost
        updateBuyFields(buyCountText, itemCostText, itemsToBuy, itemCost)

        //Hook up buttons to events and show dialog
        dialogView.buttonMinus.setOnClickListener {
            //If current count of items in the cart is more than 0, subtract one and update GUI
            if(itemsToBuy > 0){
                itemsToBuy--
                updateBuyFields(buyCountText, itemCostText, itemsToBuy, itemCost)
            }

        }
        dialogView.buttonPlus.setOnClickListener {
            //Add another item to cart and update GUI
            itemsToBuy++
            updateBuyFields(buyCountText, itemCostText, itemsToBuy, itemCost)
        }
        dialogView.buttonBuy.setOnClickListener {
            presenter.buyItems(itemsToBuy)
            alert.dismiss()
        }
        dialogView.buttonCancel.setOnClickListener {alert.dismiss()}
        alert.show()
    }

    /**
     * This function updates 2 text fields with the item count and their total cost.
     *
     * @param buyCountField The textView displaying the item count
     * @param itemCostField The textView displaying the total cost of the items
     * @param itemsToBuy Amount of items being purchased
     * @param itemCost The cost of each item
     */
    private fun updateBuyFields(buyCountField: TextView, itemCostField: TextView,
                                itemsToBuy: Int, itemCost: Int){
        buyCountField.text = itemsToBuy.toString()
        itemCostField.text = (itemsToBuy*itemCost).toString() + "VP"
    }

    private fun viewItemsDialog(){
        //Hide message box after new menu
        findViewById<CardView>(R.id.cardAction).visibility = View.GONE

        //Create dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_view_items, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val  alert = dialogBuilder.create()

        //Populate recyclerList with owned items
        val viewManager = LinearLayoutManager(this)
        dialogView.recyclerItemList.apply {
            setHasFixedSize(false)
            layoutManager = viewManager
            adapter = RecyclerAdapter(presenter.getItems().toTypedArray())
        }

        //Hook up button to event and show dialog
        dialogView.buttonReturn.setOnClickListener {alert.dismiss()}
        alert.show()
    }

    private fun viewDebugDialog(){
        //Hide message box after new menu
        findViewById<CardView>(R.id.cardAction).visibility = View.GONE

        //Create dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_debug, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val  alert = dialogBuilder.create()

        //Hook up buttons to events and show dialog
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

    /**
     * This function returns a readable string from a PurchaseValues item
     */
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
        //Destroy presenter to ensure everything is terminated.
        presenter.onDestroy()
        super.onDestroy()
    }
}