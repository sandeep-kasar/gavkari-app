package com.gavkariapp

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyNewsViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

    var imgCard: ImageView = itemview.findViewById(R.id.imgCard)
    var tvTitle: TextView = itemview.findViewById(R.id.tvTitle)
    var tvSubtitle: TextView = itemview.findViewById(R.id.tvSubtitle)
    var tvStatus: TextView = itemview.findViewById(R.id.tvStatus)
    var tvDate: TextView = itemview.findViewById(R.id.tvDate)
    var btnEdit: Button = itemview.findViewById(R.id.btnEdit)
    var btnDelete: Button = itemview.findViewById(R.id.btnDelete)

}