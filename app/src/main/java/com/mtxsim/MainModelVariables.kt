package com.mtxsim

class MainModelVariables: IMainModel{

    private val ITEM_COST = 2
    private val PREFIX_LIST = listOf("Big", "Small", "Expensive", "Cheap", "Clean", "Dirty", "Old")
    private val ITEM_LIST = listOf("Bowl", "Table", "Knife", "Chair", "Spoon", "Bed", "Mug")

    private var vp: Int = 0
    private var items: ArrayList<String> = ArrayList()

    override fun buyVP(pv: PurchaseValues): Boolean {
        vp += pv.vpGiven
        return true
    }

    override fun buyItem(): String {
        if(vp >= 2){
            val generatedItem = generateRandomItem()
            items.add(generatedItem)
            vp -= 2
            return generatedItem
        }
        return ""
    }

    override fun getVpAmount(): Int {
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
        val prefix = PREFIX_LIST[((0..PREFIX_LIST.size-1).random())]
        val item = ITEM_LIST[((0..ITEM_LIST.size-1).random())]
        return "$prefix $item"
    }

}