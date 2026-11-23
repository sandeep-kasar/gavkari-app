package com.gavkariapp.viewholder

import android.view.View
import android.widget.TextView
import com.gavkariapp.R

class VillageSelectionViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    var tvVillageName: TextView = itemView.findViewById(R.id.tvVillageName)
    var tvAdd: TextView = itemView.findViewById(R.id.tvAdd)

}
