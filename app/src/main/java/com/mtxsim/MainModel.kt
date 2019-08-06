package com.mtxsim

import android.R.id.edit
import android.content.SharedPreferences.Editor
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences



class MainModel(private var prefs: SharedPreferences): IMainModel{

    private val ITEM_COST = 2
    private val PREFIX_LIST = listOf("Big", "Small", "Expensive", "Cheap", "Clean", "Dirty", "Old")
    private val ITEM_LIST = listOf("Bowl", "Table", "Knife", "Chair", "Spoon", "Bed", "Mug")
    private val VP_KEY = "ownedVP"

    private var vp: Int = 0
    private var items: ArrayList<String> = ArrayList()

    override fun buyVP(pv: PurchaseValues): Boolean {
        loadProgress()
        vp += pv.vpGiven
        saveProgress()
        return true
    }

    override fun buyItem(): String {
        loadProgress()
        if(vp >= 2){
            val generatedItem = generateRandomItem()
            items.add(generatedItem)
            vp -= 2
            saveProgress()
            return generatedItem
        }
        return ""
    }

    override fun getVpAmount(): Int {
        loadProgress()
        return vp
    }

    override fun getItemAmount(): Int {
        return items.size
    }

    override fun getItems(): ArrayList<String> {
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

    override fun getItemCost(): Int = ITEM_COST

    override fun generateRandomItem(): String {
        val prefix = PREFIX_LIST[((0 until PREFIX_LIST.size).random())]
        val item = ITEM_LIST[((0 until ITEM_LIST.size).random())]
        return "$prefix $item"
    }

    override fun debugWipe() {
        vp = 0
        saveProgress()
    }

    private fun saveProgress() {
        with (prefs.edit()) {
            putInt(VP_KEY, vp)
            apply()
        }
        val debugCheck = prefs.getInt(VP_KEY, 0)
        print(debugCheck)
    }

    private fun loadProgress(){
        vp = prefs.getInt(VP_KEY, 0)
    }

}