package com.gavkariapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gavkariapp.Model.NotificationData
import com.gavkariapp.R
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.utility.Util
import com.gavkariapp.viewHolder.NotificationViewHolder

class Notificationadapter(var notificationList: List<NotificationData>,
                          var onClickRow: onClickRow)
    : androidx.recyclerview.widget.RecyclerView.Adapter<NotificationViewHolder>() {

    companion object {
        private const val TYPE_NOTI = 0
    }

    override fun onBindViewHolder(viewHolder: NotificationViewHolder, position: Int) {

        if (getItemViewType(position) == TYPE_NOTI) {
            showDirectoryList(viewHolder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {

        var viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        viewHolder = when (viewType) {

            TYPE_NOTI -> {
                val viewEvent = inflater.inflate(R.layout.layout_notification, parent, false)
                NotificationViewHolder(viewEvent)
            }

            else -> {
                val view = inflater.inflate(R.layout.layout_notification, parent, false)
                NotificationViewHolder(view)
            }
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_NOTI
    }

    private fun showDirectoryList(viewHolder: NotificationViewHolder, position: Int) {

        var data = notificationList[position]

        var inputDate = Util.getFormatedDate(data.date,
                "yyyy-MM-dd", "EEE, MMM d, yyyy",viewHolder.itemView.resources)

        viewHolder.tvTitle.text = data.title
        viewHolder.tvDescription.text = data.description
        viewHolder.tvDate.text = inputDate
        Glide.with(viewHolder.itemView.context)
                .load(HttpConstant.BASE_BANNER_DOWNLOAD_URL + data.photo)
                .thumbnail(0.5f)
                .apply(RequestOptions.circleCropTransform())
                .into(viewHolder.imgCardNoti)

        viewHolder.itemView.setOnClickListener { onClickRow.onClickRow(data) }
    }

}

interface onClickRow {
    fun onClickRow(item: NotificationData)
}