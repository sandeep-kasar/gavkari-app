package com.gavkariapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gavkariapp.Model.MyVillageEvent
import com.gavkariapp.EventFragmentViewHolder
import com.gavkariapp.R
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.utility.Util
import java.util.*


class EventFragmentAdapter(var myVillageResponse: LinkedList<Any>,
                           var onItemClickListener: OnItemClickListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<EventFragmentViewHolder>() {

    companion object {
        private const val TYPE_EVENT = 0
    }

    private lateinit var parentView : Context

    override fun onBindViewHolder(viewHolder: EventFragmentViewHolder, position: Int) {

        if (getItemViewType(position) == TYPE_EVENT) {
            showEvent(viewHolder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventFragmentViewHolder {


        var viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        viewHolder = when (viewType) {

            TYPE_EVENT -> {
                val viewEvent = inflater.inflate(R.layout.layout_home_tab_event, parent, false)
                EventFragmentViewHolder(viewEvent)
            }

            else -> {
                val view = inflater.inflate(R.layout.layout_home_tab_event, parent, false)
                EventFragmentViewHolder(view)
            }
        }

        parentView = viewHolder.itemView.context.applicationContext

        return viewHolder
    }

    override fun getItemCount(): Int {
        return myVillageResponse.size
    }

    override fun getItemViewType(position: Int): Int {
        val obj = myVillageResponse[position]
        when (obj) {
            is MyVillageEvent -> return TYPE_EVENT
            else -> obj is MyVillageEvent
        }
        return super.getItemViewType(position)
    }

    private fun showEvent(viewHolder: EventFragmentViewHolder, position: Int) {

        val data = myVillageResponse[position] as MyVillageEvent
        viewHolder.tvTitle.text =data.title

        if(data.type== AppConstant.WEDDING || data.type== AppConstant.ENGAGEMENT){
            var subtitle = data.subtitle +" "+ data.subtitle_one +" "+parentView.getString(R.string.lbl_and)+" "+
                    data.subtitle_three +" "+data.subtitle_four +" "+parentView.getString(R.string.lbl_there_wedding)
            viewHolder.tvSubtitle.text = subtitle
        }else{
            viewHolder.tvSubtitle.text = data.subtitle
        }

        var inputDay = Util.getFormatedDate(data.event_date,
                "yyyy-MM-dd", "EEEE",viewHolder.itemView.resources)

        var inputDate = Util.getFormatedDate(data.event_date,
                "yyyy-MM-dd", "d MMMM, yyyy",viewHolder.itemView.resources)

        viewHolder.tvDate.text = inputDay +"\n" +inputDate

        Glide.with(viewHolder.itemView.context)
                .load(HttpConstant.BASE_EVENT_DOWNLOAD_URL +data.photo)
                .thumbnail(0.5f)
                .into(viewHolder.imgCard)

        viewHolder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(data)
        }

        viewHolder.layPatrika.setOnClickListener {
            onItemClickListener.onCardClick(data)
        }

        if (data.type == AppConstant.BIRTHDAY ||data.type == AppConstant.RETIREMENT ||data.type == AppConstant.SATYANARAYAN_POOJA ||
                data.type == AppConstant.MAHAPRASAD ||data.type == AppConstant.OTHER_EVENT){
            viewHolder.layPatrika.visibility = View.GONE
        }else{
            viewHolder.layPatrika.visibility = View.VISIBLE
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: MyVillageEvent)
        fun onCardClick(item: MyVillageEvent)
    }
}