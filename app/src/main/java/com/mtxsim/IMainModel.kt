package com.mtxsim

interface IMainModel{
    fun buyVP(pv: PurchaseValues): Boolean
    fun buyItems(itemsToBuy: Int): Map<String, Int>
    fun getVpAmount(): Int
    fun getItems(): Map<String, Int>
    fun getItemAmount(): Int
    fun getPurchaseValues(): List<PurchaseValues>
    fun getItemCost(): Int
    fun generateRandomItem(): String
    fun debugWipe()
}