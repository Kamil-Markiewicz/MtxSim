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

}