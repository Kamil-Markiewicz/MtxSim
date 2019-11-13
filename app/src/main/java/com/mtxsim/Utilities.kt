package com.mtxsim

object Utilities{
    fun calculateItemCount(itemMap: Map<String, Int>): Int {
        val values = itemMap.values
        var total = 0
        for(v in values)
            total += v
        return total
    }
}