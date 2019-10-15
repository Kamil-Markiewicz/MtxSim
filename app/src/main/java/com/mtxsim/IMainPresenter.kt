package com.mtxsim

interface IMainPresenter {

    fun getVP(): Int
    fun getItems(): ArrayList<String>
    fun getItemCount(): Int
    fun getItemCost(): Int
    fun buyVP(pv: PurchaseValues)
    fun buyItems(count: Int)
    fun getPurchaseValues(): List<PurchaseValues>
    fun debugWipe()

    fun onDestroy()
}