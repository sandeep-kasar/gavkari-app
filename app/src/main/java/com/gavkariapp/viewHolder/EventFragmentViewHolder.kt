package com.gavkariapp

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class EventFragmentViewHolder(itemview: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemview) {

    var imgCard: ImageView = itemview.findViewById(R.id.imgCard)
    var tvTitle: TextView = itemview.findViewById(R.id.tvTitle)
    var tvSubtitle: TextView = itemview.findViewById(R.id.tvSubtitle)
    var tvVillageName: TextView = itemview.findViewById(R.id.tvVillageName)
    var tvDate: TextView = itemview.findViewById(R.id.tvDate)
    var layPatrika: LinearLayout = itemview.findViewById(R.id.layPatrika)

}