package com.appc.cameraroom

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.NonDisposableHandle.parent
import java.io.File

class ImageLocationAdapter : ListAdapter<ImageLocation, ImageLocationAdapter.MyViewHolder>(ImageLocationComparator()) {

    var onItemClick : ((ImageLocation) -> Unit)? = null
    var context : Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageLocationAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        context = parent.context
        return(MyViewHolder(inflater))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, context!!)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(current)
        }
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val imageLocationView: TextView = itemView.findViewById(R.id.locationText)
        private val imageView: ImageView = itemView.findViewById(R.id.im_view)
        fun bind(data : ImageLocation, context: Context) {
            val file = File(data.location)
            val uri = FileProvider.getUriForFile(context, "com.example.myapp.fileprovider", file)
            var res = file.name
            imageLocationView.text = res
            imageView.setImageURI(uri)
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



