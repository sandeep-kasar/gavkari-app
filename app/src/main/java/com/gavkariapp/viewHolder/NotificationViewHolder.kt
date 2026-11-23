package com.gavkariapp.viewHolder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.gavkariapp.R

class NotificationViewHolder(itemView: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){
    var tvTitle: TextView = itemView.findViewById(R.id.tvTitleNoti)
    var tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    var tvDate: TextView = itemView.findViewById(R.id.tvDateNoti)
    var imgCardNoti: ImageView = itemView.findViewById(R.id.imgCardNoti)
}