package com.gavkariapp

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class MyAdViewHolder(itemview: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemview) {

    var imgCard: ImageView = itemview.findViewById(R.id.imgCard)
    var tvTitle: TextView = itemview.findViewById(R.id.tvTitle)
    var tvSubtitle: TextView = itemview.findViewById(R.id.tvSubtitle)
    var tvDate: TextView = itemview.findViewById(R.id.tvDate)
    var tvStatus: TextView = itemview.findViewById(R.id.tvStatus)
    var imgMenu: ImageView = itemview.findViewById(R.id.imgMenu)
    var btnEdit: Button = itemview.findViewById(R.id.btnEdit)
    var btnPublish: Button = itemview.findViewById(R.id.btnPublish)
    var btnCard: Button = itemview.findViewById(R.id.btnCard)
    var btnExpired: Button = itemview.findViewById(R.id.btnExpired)

}