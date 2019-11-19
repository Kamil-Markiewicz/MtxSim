package com.mtxsim

import android.content.SharedPreferences

class MainModel(private var prefs: SharedPreferences): IMainModel{

    //VP Cost of one item
    private val ITEM_COST = 2
    //List of all possible item prefixes
    private val PREFIX_LIST = listOf("Big", "Small", "Expensive", "Cheap", "Clean", "Dirty", "Old")
    //List of all possible items
    private val ITEM_LIST = listOf("Bowl", "Table", "Knife", "Chair", "Spoon", "Bed", "Mug")
    //Key for VP storage in preferences
    private val VP_KEY = "ownedVP"
    //Key for item storage in preferences
    private val ITEMS_KEY = "ownedItems"

    override fun buyVP(pv: PurchaseValues): Boolean {
        saveVP(loadVP() + pv.vpGiven)
        return true
    }

    override fun buyItems(itemsToBuy: Int): Map<String, Int> {
        val virtualPoints = loadVP()
        //Map to store newly acquired items
        val boughtMap = mutableMapOf<String, Int>()
        //If not enough vp to make the purchase return empty map
        if(virtualPoints < (ITEM_COST*itemsToBuy)) return boughtMap

        val itemMap = loadItems()

        for(i in 0 until itemsToBuy){
            //Generate a random item
            val generatedItem = generateRandomItem()

            //Add item to each map
            Utilities.addToMap(boughtMap, generatedItem, 1)
            Utilities.addToMap(itemMap, generatedItem, 1)
        }

        //Save new values
        saveVP(virtualPoints - (ITEM_COST * itemsToBuy))
        saveItems(itemMap)
        return boughtMap//Return map of newly acquired items
    }

    override fun getVpAmount(): Int = loadVP()

    override fun getItemAmount(): Int = Utilities.calculateItemCount(loadItems())

    override fun getItems(): Map<String, Int> = loadItems()

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

    /**
     * This function generates a random item string by combining a prefix and item randomly
     * from the lists of possible choices.
     */
    override fun generateRandomItem(): String {
        val prefix = PREFIX_LIST[((PREFIX_LIST.indices).random())]
        val item = ITEM_LIST[((ITEM_LIST.indices).random())]
        return "$prefix $item"
    }

    /**
     * This function wipes the state of the app to a completely clean state
     */
    override fun debugWipe() {
        //Wipe VP
        saveVP(0)
        //Wipe items
        saveItems(mapOf<String, Int>())
    }

    private fun loadVP(): Int = prefs.getInt(VP_KEY, 0)

    private fun loadItems(): MutableMap<String, Int> {
        //Load items string from SharedPreferences
        val str = prefs.getString(ITEMS_KEY, "")!!
        //Create list of item pairs by splitting read string on semicolon-newline combinations
        val itemPairs = str.split(";\n")
        //Create map to add loaded items to
        val itemMap = mutableMapOf<String, Int>()

        //Parse the item string and populate the itemMap
        for(itemPair in itemPairs){
            //Discard malformed entries(Empty newlines at the end of file or otherwise)
            if (!itemPair.contains(";")) continue

            //Create
            val itemAndCount = itemPair.split(";")
            Utilities.addToMap(itemMap, itemAndCount[0], itemAndCount[1].toInt())
        }
        return itemMap
    }

    private fun saveItems(itemMap: Map<String, Int>){
        //Create string of item-count pairs separated by a semicolon
        // and each pair by a semicolon-newline combination
        val itemsSB = StringBuilder()
        for ((item, count) in itemMap) itemsSB.append("$item;$count;\n")

        //Write the items string to SharedPreferences using it's key
        with (prefs.edit()) {
            putString(ITEMS_KEY, itemsSB.toString())
            apply()
        }
    }

    private fun saveVP(virtualPoints: Int){
        //Write the VP Int to SharedPreferences using it's key
        with (prefs.edit()) {
            putInt(VP_KEY, virtualPoints)
            apply()
        }
    }
}