package com.mtxsim

interface IMainModel{
    fun buyVP(pv: PurchaseValues): Boolean
    fun buyItem(): Boolean
    fun getVpAmount(): Int
    fun getItemAmount(): Int
    fun getPurchaseValues(): List<PurchaseValues>
    fun getItemCost(): Int
}