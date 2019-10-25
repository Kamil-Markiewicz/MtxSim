package com.mtxsim

interface IMainModel{
    fun buyVP(pv: PurchaseValues): Boolean
    fun buyItems(count: Int): Map<String, Int>
    fun getVpAmount(): Int
    fun getItems(): ArrayList<String>
    fun getItemAmount(): Int
    fun getPurchaseValues(): List<PurchaseValues>
    fun getItemCost(): Int
    fun generateRandomItem(): String
    fun debugWipe()
}