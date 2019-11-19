package com.mtxsim

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_holder_item.view.*

class RecyclerAdapter(private val dataset: List<Pair<String, Int>>) :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(val holderView: ViewGroup) : RecyclerView.ViewHolder(holderView)

    override fun getItemCount() = dataset.size

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        //Create recycler list item that will hold the values
        val recHolderItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_holder_item, parent, false) as ViewGroup
        return MyViewHolder(recHolderItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //Get element from the dataset at this position
        val pair = dataset[position]
        //Populate contents of view at this position
        holder.holderView.textViewCount.text = pair.second.toString()
        holder.holderView.textViewItem.text = " " + pair.first
    }
}