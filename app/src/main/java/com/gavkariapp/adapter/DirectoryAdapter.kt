package com.gavkariapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gavkariapp.Model.DirectoryList
import com.gavkariapp.R
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.constant.HttpConstant.BASE_AVATAR_DOWNLOAD_URL
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.viewHolder.DirectoryViewHolder

class DirectoryAdapter(var directoryList: List<DirectoryList>,var onClickCall: onClickCall)
    : androidx.recyclerview.widget.RecyclerView.Adapter<DirectoryViewHolder>() {

    private  var userId :String? = ""

    companion object {
        private const val TYPE_DIRECTORY = 0
    }

    override fun onBindViewHolder(viewHolder: DirectoryViewHolder, position: Int) {

        if (getItemViewType(position) == TYPE_DIRECTORY) {
            showDirectoryList(viewHolder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryViewHolder {

        var viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val prefs = PreferenceHelper.customPrefs(parent.context, "user_info")
        userId = prefs[ApiConstant.USER_ID, "-1"]

        viewHolder = when (viewType) {

            TYPE_DIRECTORY -> {
                val viewEvent = inflater.inflate(R.layout.layout_directory_row, parent, false)
                DirectoryViewHolder(viewEvent)
            }

            else -> {
                val view = inflater.inflate(R.layout.layout_directory_row, parent, false)
                DirectoryViewHolder(view)
            }
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return directoryList.size
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_DIRECTORY
    }

    private fun showDirectoryList(viewHolder: DirectoryViewHolder, position: Int) {

        var data = directoryList[position]


        if (userId == data.user_id){
            viewHolder.imgEdit.visibility = View.VISIBLE
        }else{
            viewHolder.imgEdit.visibility = View.GONE
        }

        viewHolder.tvType.text = data.business
        viewHolder.tvName.text = data.b_name
        viewHolder.tvStore.text = data.b_description
        viewHolder.tvMobile.text = data.mobile
        Glide.with(viewHolder.itemView.context)
                .load(BASE_AVATAR_DOWNLOAD_URL+data.avatar)
                .apply(RequestOptions.circleCropTransform())
                .into(viewHolder.imgCard)

        viewHolder.tvMobile.setOnClickListener {
            onClickCall.call(directoryList[position]) }

        viewHolder.imgCard.setOnClickListener {
            onClickCall.zoom(viewHolder.imgCard)
        }

        viewHolder.imgEdit.setOnClickListener {
            onClickCall.updateDir(data)
        }

    }

}

interface onClickCall{
    fun call(item : DirectoryList)
    fun zoom(v :View)
    fun updateDir(item : DirectoryList)
}