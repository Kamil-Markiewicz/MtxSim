package com.mtxsim

class MainModelVariables: IMainModel{

    private var vp: Int = 0
    private var items: Int = 0

    override fun buyVP(pv: PurchaseValues): Boolean {
        vp += pv.vpGiven
        return true
    }

    override fun buyItem(): Boolean {
        if(vp >= 2){
            items++
            vp -= 2
            return true
        }
        return false
    }

    override fun getVpAmount(): Int {
        return vp
    }

    override fun getItemAmount(): Int {
        return items
    }

    override fun getPurchaseValues(): List<PurchaseValues> {
        return listOf(
            PurchaseValues(0.99, 2),
            PurchaseValues(1.99, 5),
            PurchaseValues(4.99, 13),
            PurchaseValues(9.99, 27),
            PurchaseValues(19.99, 60),
            PurchaseValues(49.99, 150),
            PurchaseValues(99.99, 310))
    }

    override fun getItemCost(): Int {
        return 2
    }

}