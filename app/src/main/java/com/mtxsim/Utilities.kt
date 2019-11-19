package com.mtxsim

object Utilities{
    fun calculateItemCount(itemMap: Map<String, Int>): Int {
        //Get a list of values of each item in the map
        val values = itemMap.values
        //Add up all the values
        var total = 0
        for(v in values)
            total += v
        return total
    }

    fun addToMap(map: MutableMap<String, Int>, key: String, value: Int){
        //If the map does not contain the key, add it with the provided value
        if (!map.containsKey(key)) map[key] = value
        //Otherwise add the provided value to the existing key
        else map[key] = (map[key]!! + value)
    }
}