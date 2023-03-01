package com.appc.cameraroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ImageLocationAdapter : ListAdapter<ImageLocation, ImageLocationAdapter.MyViewHolder>(ImageLocationComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageLocationAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return(MyViewHolder(inflater))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val imageLocationView: TextView = itemView.findViewById(R.id.locationText)
        fun bind(data : ImageLocation) {
            imageLocationView.text = data.location
        }

        companion object {
            fun create(parent: ViewGroup): MyViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item, parent, false)
                return MyViewHolder(view)
            }
        }
    }

    class ImageLocationComparator : DiffUtil.ItemCallback<ImageLocation>() {
        override fun areItemsTheSame(oldItem: ImageLocation, newItem: ImageLocation): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ImageLocation, newItem: ImageLocation): Boolean {
            return oldItem.id == newItem.id
        }
    }

}


