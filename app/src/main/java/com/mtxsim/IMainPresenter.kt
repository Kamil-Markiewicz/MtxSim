package com.mtxsim

interface IMainPresenter {

    fun getVP(): Int
    fun getItems(): Map<String, Int>
    fun getItemCount(): Int
    fun getItemCost(): Int
    fun getPurchaseValues(): List<PurchaseValues>
    fun buyVP(pv: PurchaseValues)
    fun buyItems(count: Int)
    fun debugWipe()

    fun onDestroy()
}