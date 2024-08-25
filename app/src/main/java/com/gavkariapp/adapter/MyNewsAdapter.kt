package com.gavkariapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gavkariapp.Model.MyNews
import com.gavkariapp.MyNewsViewHolder
import com.gavkariapp.R
import com.gavkariapp.constant.AppConstant.PUBLISHED
import com.gavkariapp.constant.AppConstant.REJECTED
import com.gavkariapp.constant.AppConstant.UNDER_REVIEW
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.utility.Util
import java.util.*


class MyNewsAdapter(var myNewsResponse: ArrayList<MyNews>,
                    var onItemClickListener: OnItemClickListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<MyNewsViewHolder>() {


    private lateinit var parentView : Context

    companion object {
        private const val NEWS_TYPE = 0
    }

    override fun onBindViewHolder(viewHolder: MyNewsViewHolder, position: Int) {

        if (getItemViewType(position) == NEWS_TYPE) {
            showNews(viewHolder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNewsViewHolder {

        var viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        viewHolder = when (viewType) {

            NEWS_TYPE -> {
                val viewEvent = inflater.inflate(R.layout.layout_my_news_row, parent, false)
                MyNewsViewHolder(viewEvent)
            }

            else -> {
                val view = inflater.inflate(R.layout.layout_my_news_row, parent, false)
                MyNewsViewHolder(view)
            }
        }

        parentView = viewHolder.itemView.context.applicationContext

        return viewHolder
    }

    override fun getItemCount(): Int {
        return myNewsResponse.size
    }

    override fun getItemViewType(position: Int): Int {
        return NEWS_TYPE
    }

    private fun showNews(viewHolder: MyNewsViewHolder, position: Int) {

        val data = myNewsResponse[position]
        viewHolder.tvTitle.text = data.title
        viewHolder.tvSubtitle.text = data.description

        var inputDate = Util.getFormatedDate(data.news_date,
                "yyyy-MM-dd", "EEE, d MMM, yyyy",viewHolder.itemView.resources)

        viewHolder.tvDate.text = inputDate

        when {
            data.status == UNDER_REVIEW -> {
                viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_in_review)
                viewHolder.tvStatus.setTextColor(viewHolder.itemView.resources.getColor(R.color.sinopia))
                viewHolder.btnEdit.visibility = View.VISIBLE
            }
            data.status == REJECTED -> {
                viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_rejected)
                viewHolder.tvStatus.setTextColor(viewHolder.itemView.resources.getColor(R.color.sinopia))
                viewHolder.btnEdit.visibility = View.VISIBLE
            }
            data.status == PUBLISHED -> {
                viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_published)
                viewHolder.btnEdit.visibility = View.VISIBLE
            }
        }

        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.ic_gaav_logo_final_circle_pl_square)

        Glide.with(viewHolder.itemView.context)
                .setDefaultRequestOptions(requestOptions)
                .load(HttpConstant.BASE_NEWS_DOWNLOAD_URL +data.photo)
                .thumbnail(0.5f)
                .into(viewHolder.imgCard)

        viewHolder.itemView.setOnClickListener { onItemClickListener.onItemClick(data) }
        viewHolder.btnEdit.setOnClickListener { onItemClickListener.onEditClick(data) }
        viewHolder.btnDelete.setOnClickListener { onItemClickListener.onDeleteAdClick(data) }

    }

    interface OnItemClickListener {
        fun onItemClick(item: MyNews)
        fun onEditClick(item: MyNews)
        fun onDeleteAdClick(item: MyNews)

    }

}