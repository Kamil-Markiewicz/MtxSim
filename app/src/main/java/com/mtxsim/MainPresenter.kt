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

    override fun buyVP(pv: PurchaseValues){
        val success = model.buyVP(pv)
        if(success){
            mainView?.updateVP()
            mainView?.displayMessage("You have purchased:\n" + pv.vpGiven + "VP")
        }
        else
            mainView?.displayMessage("Error during purchasing VP")
    }

    override fun buyItem(){
        if(model.getVpAmount() < model.getItemCost())
            mainView?.displayMessage("Not enough VP to buy item!")
        else {
            val itemString = model.buyItem()
            if (itemString != "") {
                mainView?.updateVP()
                mainView?.updateItems()
                mainView?.displayMessage("You have received:\n$itemString!")
            } else
                mainView?.displayMessage("Error during purchasing item")
        }
    }

    override fun getPurchaseValues(): List<PurchaseValues> {
        return model.getPurchaseValues()
    }

    override fun debugWipe() {
        model.debugWipe()
        mainView?.updateVP()
    }

    override fun onDestroy() {
        mainView = null
    }
}
