package com.mtxsim

interface IMainPresenter {

    fun getVP(): Int
    fun getItems(): ArrayList<String>
    fun getItemCount(): Int
    fun buyVP(pv: PurchaseValues)
    fun buyItem()
    fun getPurchaseValues(): List<PurchaseValues>
    fun debugWipe()

    fun onDestroy()
}