package com.gavkariapp.viewHolder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.gavkariapp.R

class DirectoryViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    var tvType: TextView = itemView.findViewById(R.id.tvType)
    var tvName: TextView = itemView.findViewById(R.id.tvName)
    var tvStore: TextView = itemView.findViewById(R.id.tvStore)
    var tvMobile: TextView = itemView.findViewById(R.id.tvMobile)
    var imgCard: ImageView = itemView.findViewById(R.id.imgCard)
    var imgEdit: ImageView = itemView.findViewById(R.id.imgEdit)
}
