package com.mtxsim

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_holder_item.view.*

class RecyclerAdapter(private val dataset: Array<String>) :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(val holderView: ViewGroup) : RecyclerView.ViewHolder(holderView)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        val recHolderItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_holder_item, parent, false) as ViewGroup
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(recHolderItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val itemSet = dataset[position].split(";")
        holder.holderView.textViewCount.text = itemSet[0]
        holder.holderView.textViewItem.text = " " + itemSet[1]
    }

    override fun getItemCount() = dataset.size
}