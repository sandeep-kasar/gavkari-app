package com.gavkariapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gavkariapp.Model.MyEvent
import com.gavkariapp.MyAdViewHolder
import com.gavkariapp.R
import com.gavkariapp.constant.AppConstant.BIRTHDAY
import com.gavkariapp.constant.AppConstant.CREATED
import com.gavkariapp.constant.AppConstant.ENGAGEMENT
import com.gavkariapp.constant.AppConstant.MAHAPRASAD
import com.gavkariapp.constant.AppConstant.OTHER_EVENT
import com.gavkariapp.constant.AppConstant.PUBLISHED
import com.gavkariapp.constant.AppConstant.REJECTED
import com.gavkariapp.constant.AppConstant.RETIREMENT
import com.gavkariapp.constant.AppConstant.SATYANARAYAN_POOJA
import com.gavkariapp.constant.AppConstant.UNDER_REVIEW
import com.gavkariapp.constant.AppConstant.WEDDING
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.utility.Util


class MyEventAdapter(var myAdResponse: ArrayList<MyEvent>,
                     var onItemClickListener: OnItemClickListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<MyAdViewHolder>() {


    private lateinit var parentView : Context

    companion object {
        private const val TYPE_EVENT = 0
        private var tsLong: Long = System.currentTimeMillis()

    }

    override fun onBindViewHolder(viewHolder: MyAdViewHolder, position: Int) {

        if (getItemViewType(position) == TYPE_EVENT) {
            showEvent(viewHolder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdViewHolder {

        var viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        viewHolder = when (viewType) {

            TYPE_EVENT -> {
                val viewEvent = inflater.inflate(R.layout.layout_my_event_row, parent, false)
                MyAdViewHolder(viewEvent)
            }

            else -> {
                val view = inflater.inflate(R.layout.layout_my_event_row, parent, false)
                MyAdViewHolder(view)
            }
        }

        parentView = viewHolder.itemView.context.applicationContext

        return viewHolder
    }

    override fun getItemCount(): Int {
        return myAdResponse.size
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_EVENT
    }

    private fun showEvent(viewHolder: MyAdViewHolder, position: Int) {

        val data = myAdResponse[position]
        viewHolder.tvTitle.text = data.title
        viewHolder.tvSubtitle.text = data.subtitle

        if(data.type== WEDDING || data.type== ENGAGEMENT){
            var subtitle = data.subtitle +" "+ data.subtitle_one +" "+parentView.getString(R.string.lbl_and)+" "+
                    data.subtitle_three +" "+data.subtitle_four +" "+parentView.getString(R.string.lbl_there_wedding)
            viewHolder.tvSubtitle.text = subtitle
        }else{
            viewHolder.tvSubtitle.text = data.subtitle
        }

        var inputDate = Util.getFormatedDate(data.event_date,
                "yyyy-MM-dd", "EEE, MMM d, yyyy",viewHolder.itemView.resources)

        viewHolder.tvDate.text = inputDate
        
        var millisecond = data.event_date_ms.toLong()


        if (data.type == BIRTHDAY ||data.type == RETIREMENT ||data.type == SATYANARAYAN_POOJA ||
                data.type == MAHAPRASAD ||data.type == OTHER_EVENT){
            viewHolder.btnCard.visibility = View.GONE
        }else{
            viewHolder.btnCard.visibility = View.VISIBLE
        }


        if (data.status == UNDER_REVIEW){
            viewHolder.btnPublish.visibility = View.GONE
            viewHolder.btnCard.visibility = View.GONE
            viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_in_review)
            viewHolder.tvStatus.setTextColor(viewHolder.itemView.resources.getColor(R.color.sinopia))

        }

        if (data.status == REJECTED){
            viewHolder.btnPublish.visibility = View.GONE
            viewHolder.btnCard.visibility = View.GONE
            viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_rejected)
            viewHolder.tvStatus.setTextColor(viewHolder.itemView.resources.getColor(R.color.sinopia))
            viewHolder.btnEdit.visibility = View.VISIBLE
        }

        if (data.status == PUBLISHED){
            viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_published)
            viewHolder.btnPublish.visibility = View.GONE
            viewHolder.btnCard.visibility = View.VISIBLE
            viewHolder.btnEdit.visibility = View.VISIBLE
        }


        if (data.status == CREATED){
            viewHolder.btnPublish.visibility = View.VISIBLE
            viewHolder.btnCard.visibility = View.GONE
            viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_not_published)
            viewHolder.tvStatus.setTextColor(viewHolder.itemView.resources.getColor(R.color.sinopia))
        }




        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.ic_gaav_logo_final_circle_pl_square)
        Glide.with(viewHolder.itemView.context)
                .setDefaultRequestOptions(requestOptions)
                .load(HttpConstant.BASE_EVENT_DOWNLOAD_URL +data.photo)
                .thumbnail(0.5f)
                .into(viewHolder.imgCard)

        viewHolder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(data)
        }

        if (millisecond < tsLong){
            viewHolder.btnEdit.visibility = View.GONE
            viewHolder.btnPublish.visibility = View.GONE
            viewHolder.btnCard.visibility = View.GONE
            viewHolder.btnExpired.visibility = View.VISIBLE
        }

        viewHolder.btnPublish.setOnClickListener { onItemClickListener.onPublishClick(data) }
        viewHolder.btnEdit.setOnClickListener { onItemClickListener.onEditClick(data) }
        viewHolder.btnCard.setOnClickListener { onItemClickListener.onShareClick(data) }
        viewHolder.imgMenu.setOnClickListener {
            val popup = PopupMenu(viewHolder.imgMenu.context, viewHolder.imgMenu)
            popup.inflate(R.menu.my_ad_menu_)
            popup.setOnMenuItemClickListener { item ->
                when (item.getItemId()) {
                    R.id.action_delete -> {
                        onItemClickListener.onDeleteAdClick(data)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: MyEvent)
        fun onPublishClick(item: MyEvent)
        fun onEditClick(item: MyEvent)
        fun onShareClick(item: MyEvent)
        fun onDeleteAdClick(item: MyEvent)

    }

}