package com.mtxsim

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.Button
import kotlinx.android.synthetic.main.view_items_dialog.view.*

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
        findViewById<TextView>(R.id.textViewVPCount).text = presenter.getVP().toString()
    }

    override fun updateItems() {
        findViewById<TextView>(R.id.textViewItemsCount).text = presenter.getItemCount().toString()
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

    private fun viewItemsDialog(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.view_items_dialog, null)


        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        val  mAlertDialog = mBuilder.show()

        mDialogView.buttonReturn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
        mDialogView.textViewItemList.text = presenter.getItems().toString()
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
