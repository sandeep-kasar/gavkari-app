package com.gavkariapp.viewHolder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.gavkariapp.R

class RefundHistoryViewHolder(itemView: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){
    var tvRfEventSub:TextView=itemView.findViewById(R.id.tvRfEventSub)
    var tvRfAmount:TextView=itemView.findViewById(R.id.tvRfAmount)
    var tvRfTranId:TextView=itemView.findViewById(R.id.tvRfTranId)
    var tvRfDate:TextView=itemView.findViewById(R.id.tvRfDate)
}