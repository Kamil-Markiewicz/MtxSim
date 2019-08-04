package com.mtxsim

interface IMainPresenter {

    fun getVP(): Int
    fun getItems(): Int
    fun buyVP(pv: PurchaseValues)
    fun buyItem()
    fun getPurchaseValues(): List<PurchaseValues>

    fun onDestroy()
}