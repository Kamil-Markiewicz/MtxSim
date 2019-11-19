package com.mtxsim

class MainPresenter(private var mainView: MainView?, private val model: IMainModel): IMainPresenter {

    override fun getVP(): Int = model.getVpAmount()

    override fun getItems(): Map<String, Int> =  model.getItems()

    override fun getItemCount(): Int = model.getItemAmount()

    override fun getItemCost(): Int = model.getItemCost()

    override fun getPurchaseValues(): List<PurchaseValues> = model.getPurchaseValues()

    override fun buyVP(pv: PurchaseValues){
        //If purchase went through successfully
        if(model.buyVP(pv)){
            //Update balance and display how much VP was purchased as a confirmation
            mainView?.updateVP()
            mainView?.displayMessage("You have purchased:\n" + pv.vpGiven + "VP")
        }
        //If the purchase was unsuccessful notify of an error occurring
        else mainView?.displayMessage("Error during purchasing VP")
    }

    override fun buyItems(count: Int){
        //Check if there is enough balance to make the purchase
        if(model.getVpAmount() < (model.getItemCost()*count)) {
            //Notify the user the balance is too low to make the purchase
            mainView?.displayMessage("Not enough VP to buy item!")
            return
        }
        //Otherwise continue on to make purchase
        val bItemsMap = model.buyItems(count)   //Store items bought in this purchase
        //As a final check if the bought items list is empty notify the user an error haws occurred.
        if (bItemsMap.isEmpty()){
            mainView?.displayMessage("Error during purchasing item")
            return
        }

        //Create purchase message string
        val msgString = StringBuilder().append(
            "Purchased " + Utilities.calculateItemCount(bItemsMap).toString() + " items:\n")
        //Store loop counter to prevent adding newline after last item
        var loopCounter = bItemsMap.size
        for (itemPair in bItemsMap) {
            //Append item and it's count to message string
            msgString.append(itemPair.value.toString() + "x " + itemPair.key)
            loopCounter--
            //Add newline to every item except the last
            if (loopCounter != 0) msgString.append("\n")
        }

        //Update the GUI
        mainView?.updateVP()
        mainView?.updateItems()
        mainView?.displayMessage(msgString.toString())
    }

    override fun debugWipe() {
        model.debugWipe()
        //Update gui with wiped values
        mainView?.updateVP()
        mainView?.updateItems()
    }

    override fun onDestroy() {
        //Ensure main view is destroyed to prevent sending events to destroyed activity
        mainView = null
    }
}
