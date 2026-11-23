package com.gavkariapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gavkariapp.Model.News
import com.gavkariapp.NewsFragmentViewHolder
import com.gavkariapp.R
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.utility.Util
import kotlin.collections.ArrayList

class NewsAdapter(var newsResponse: ArrayList<News>, var onItemClickListener: OnItemClickListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<NewsFragmentViewHolder>() {

    companion object {
        private const val TYPE_NEWS = 1
    }

    override fun onBindViewHolder(viewHolder: NewsFragmentViewHolder, position: Int) {

        if (getItemViewType(position) == TYPE_NEWS) {
            showNews(viewHolder, position)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsFragmentViewHolder {

        var viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        viewHolder = when (viewType) {

            TYPE_NEWS -> {
                val viewNews = inflater.inflate(R.layout.layout_home_tab_news, parent, false)
                NewsFragmentViewHolder(viewNews)
            }

            else -> {
                val view = inflater.inflate(R.layout.layout_home_tab_news, parent, false)
                NewsFragmentViewHolder(view)
            }
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return newsResponse.size
    }


    override fun getItemViewType(position: Int): Int {
        val obj = newsResponse[position]
        when (obj) {
            is News -> return TYPE_NEWS
            else -> obj is News
        }
        return super.getItemViewType(position)
    }

    private fun showNews(viewHolder: NewsFragmentViewHolder, position: Int) {

        val data = newsResponse[position]
        viewHolder.tvTitle.text = data.title
        viewHolder.tvSubtitle.text = data.description

        var inputDay = Util.getFormatedDate(data.news_date,
                "yyyy-MM-dd", "EEEE",viewHolder.itemView.resources)

        var inputDate = Util.getFormatedDate(data.news_date,
                "yyyy-MM-dd", "d MMMM, yyyy",viewHolder.itemView.resources)

        viewHolder.tvDate.text = inputDay +"\n" +inputDate

        Glide.with(viewHolder.itemView.context)
                .load(HttpConstant.BASE_NEWS_DOWNLOAD_URL+data.photo)
                .thumbnail(0.5f)
                .into(viewHolder.imgCard)
        viewHolder.itemView.setOnClickListener { onItemClickListener.onItemClick(data) }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Any)
    }


}