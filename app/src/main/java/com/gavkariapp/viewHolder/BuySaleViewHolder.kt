package com.gavkariapp.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gavkariapp.R

class BuySaleViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var imgCard :ImageView = itemView.findViewById(R.id.imgCard)
    var tvType :TextView = itemView.findViewById(R.id.tvType)
    var tvBrand :TextView = itemView.findViewById(R.id.tvBrand)
    var imgLike :ImageView = itemView.findViewById(R.id.imgLike)
    var imgLikeFill :ImageView = itemView.findViewById(R.id.imgLikeFill)
    var tvDetails :TextView = itemView.findViewById(R.id.tvDetails)
    var layDetails :LinearLayout = itemView.findViewById(R.id.layDetails)
    var tvAddress:TextView = itemView.findViewById(R.id.tvAddress)
    var tvDistance:TextView = itemView.findViewById(R.id.tvDistance)
    var tvPrice:TextView = itemView.findViewById(R.id.tvPrice)
    var tvDate:TextView = itemView.findViewById(R.id.tvDate)
    var tvSold:TextView = itemView.findViewById(R.id.tvSold)
}