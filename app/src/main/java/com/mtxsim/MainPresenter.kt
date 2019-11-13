package com.mtxsim

class MainPresenter(private var mainView: MainView?, val model: IMainModel): IMainPresenter {

    override fun getVP(): Int{
        return model.getVpAmount()
    }

    override fun getItems(): ArrayList<String>{
        return model.getItems()
    }

    override fun getItemCount(): Int{
        return model.getItemAmount()
    }

    override fun getItemCost(): Int = model.getItemCost()

    override fun buyVP(pv: PurchaseValues){
        val success = model.buyVP(pv)
        if(success){
            mainView?.updateVP()
            mainView?.displayMessage("You have purchased:\n" + pv.vpGiven + "VP")
        }
        else mainView?.displayMessage("Error during purchasing VP")
    }

    override fun buyItems(count: Int){
        if(model.getVpAmount() < (model.getItemCost()*count))
            mainView?.displayMessage("Not enough VP to buy item!")
        else {
            val bItemsMap = model.buyItems(count)
            if (bItemsMap.isEmpty())
                mainView?.displayMessage("Error during purchasing item")
            else{
                mainView?.updateVP()
                mainView?.updateItems()

                val msgString = StringBuilder().append(
                    "Purchased " + Utilities.calculateItemCount(bItemsMap).toString() + " items:\n")
                var loopCounter = bItemsMap.size
                for(itemSet in bItemsMap){
                    msgString.append(itemSet.value.toString() + "x " + itemSet.key)
                    loopCounter--
                    if(loopCounter != 0) msgString.append("\n")
                }
                mainView?.displayMessage(msgString.toString())
            }
        }
    }

    override fun getPurchaseValues(): List<PurchaseValues> {
        return model.getPurchaseValues()
    }

    override fun debugWipe() {
        model.debugWipe()
        mainView?.updateVP()
        mainView?.updateItems()
    }

    override fun onDestroy() {
        mainView = null
    }
}
