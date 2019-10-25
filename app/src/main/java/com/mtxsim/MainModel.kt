package com.mtxsim

import android.content.SharedPreferences

class MainModel(private var prefs: SharedPreferences): IMainModel{

    private val ITEM_COST = 2
    private val PREFIX_LIST = listOf("Big", "Small", "Expensive", "Cheap", "Clean", "Dirty", "Old")
    private val ITEM_LIST = listOf("Bowl", "Table", "Knife", "Chair", "Spoon", "Bed", "Mug")
    private val VP_KEY = "ownedVP"
    private val ITEMS_KEY = "ownedItems"

    override fun buyVP(pv: PurchaseValues): Boolean {
        var virtualPoints = loadVP()
        virtualPoints += pv.vpGiven
        saveVP(virtualPoints)
        return true
    }

    override fun buyItems(count: Int): Map<String, Int> {
        var virtualPoints = loadVP()
        val boughtMap = mutableMapOf<String, Int>()
        if(virtualPoints < (ITEM_COST*count)) return boughtMap

        val itemMap = loadItems()

        for(i in 0 until count){
            val generatedItem = generateRandomItem()
            if (!boughtMap.containsKey(generatedItem)) boughtMap[generatedItem] = 1
            else boughtMap[generatedItem] = (boughtMap[generatedItem]!! + 1)

            if (!itemMap.containsKey(generatedItem)) itemMap[generatedItem] = 1
            else itemMap[generatedItem] = (itemMap[generatedItem]!! + 1)
        }

        virtualPoints -= (ITEM_COST * count)
        saveVP(virtualPoints)
        saveItems(itemMap)
        return boughtMap
    }

    override fun getVpAmount(): Int = loadVP()

    override fun getItemAmount(): Int {
        return calculateItemCount(loadItems())
    }

    override fun getItems(): ArrayList<String> {
        val itemStrings = ArrayList<String>()
        for ((item, count) in loadItems()) itemStrings.add("$count;$item")
        return itemStrings
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
        val prefix = PREFIX_LIST[((PREFIX_LIST.indices).random())]
        val item = ITEM_LIST[((ITEM_LIST.indices).random())]
        return "$prefix $item"
    }

    override fun debugWipe() {
        saveVP(0)
        saveItems(mapOf<String, Int>())
    }

    private fun calculateItemCount(itemMap: Map<String, Int>): Int {
        val values = itemMap.values
        var total = 0
        for(v in values)
            total += v
        return total
    }

    private fun loadVP(): Int = prefs.getInt(VP_KEY, 0)

    private fun loadItems(): MutableMap<String, Int> {
        val str = prefs.getString(ITEMS_KEY, "")!!
        val itemMap = mutableMapOf<String, Int>()
        val itemSets = str.split(";\n")
        for(itemSet in itemSets){
            if (!itemSet.contains(";")) continue
            val itemCount = itemSet.split(";")
            for (i in 1..itemCount[1].toInt()) {
                if (!itemMap.containsKey(itemCount[0])) itemMap[itemCount[0]] = 1
                else itemMap[itemCount[0]] = (itemMap[itemCount[0]]!! + 1)
            }
        }
        return itemMap
    }

    private fun saveItems(itemMap: Map<String, Int>){
        val itemsSB = StringBuilder()
        for ((item, count) in itemMap) itemsSB.append("$item;$count;\n")
        with (prefs.edit()) {
            putString(ITEMS_KEY, itemsSB.toString())
            apply()
        }
    }

    private fun saveVP(virtualPoints: Int){
        with (prefs.edit()) {
            putInt(VP_KEY, virtualPoints)
            apply()
        }
    }
}