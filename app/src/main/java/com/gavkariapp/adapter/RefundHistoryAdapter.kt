package com.gavkariapp.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gavkariapp.Model.RefundHistory
import com.gavkariapp.R
import com.gavkariapp.utility.Util
import com.gavkariapp.viewHolder.RefundHistoryViewHolder
import java.net.URLDecoder

class RefundHistoryAdapter(var refundHistory: List<RefundHistory>)
    : androidx.recyclerview.widget.RecyclerView.Adapter<RefundHistoryViewHolder>() {

    private var context: Context? = null

    companion object {
        private const val TYPE_REFUND = 0
    }

    override fun onBindViewHolder(viewHolder: RefundHistoryViewHolder, position: Int) {

        if (getItemViewType(position) == TYPE_REFUND) {
            showHistoryList(viewHolder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefundHistoryViewHolder {

        context = parent.context

        var viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        viewHolder = when (viewType) {

            TYPE_REFUND -> {
                val viewEvent = inflater.inflate(R.layout.layout_refund_row, parent, false)
                RefundHistoryViewHolder(viewEvent)
            }

            else -> {
                val view = inflater.inflate(R.layout.layout_refund_row, parent, false)
                RefundHistoryViewHolder(view)
            }
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return refundHistory.size
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_REFUND
    }

    private fun showHistoryList(viewHolder: RefundHistoryViewHolder, position: Int) {

        var data = refundHistory[position]

        viewHolder.tvRfEventSub.text =  URLDecoder.decode(data.subtitle, "utf-8")
        viewHolder.tvRfAmount.text = "\u20B9 "+data.amount
        viewHolder.tvRfTranId.text = data.transaction_no

        var inputDate = Util.getFormatedDate(data.refund_date,
                "yyyy-MM-dd", "EEE, MMM d, yyyy",viewHolder.itemView.resources)

        viewHolder.tvRfDate.text = inputDate

    }

}
