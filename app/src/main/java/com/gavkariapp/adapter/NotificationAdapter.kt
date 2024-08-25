package com.gavkariapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gavkariapp.Model.BuySale
import com.gavkariapp.Model.Event
import com.gavkariapp.Model.News
import com.gavkariapp.R
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.AppConstant.ANIMAL
import com.gavkariapp.constant.AppConstant.BUFFALO
import com.gavkariapp.constant.AppConstant.CAR
import com.gavkariapp.constant.AppConstant.COW
import com.gavkariapp.constant.AppConstant.CULTIVATOR
import com.gavkariapp.constant.AppConstant.EQUIPMENTS
import com.gavkariapp.constant.AppConstant.GOAT
import com.gavkariapp.constant.AppConstant.HEIFER_BUFFALO
import com.gavkariapp.constant.AppConstant.HEIFER_COW
import com.gavkariapp.constant.AppConstant.IRRIGATION_MATERIAL
import com.gavkariapp.constant.AppConstant.KUTTI_MACHINE
import com.gavkariapp.constant.AppConstant.MACHINERY
import com.gavkariapp.constant.AppConstant.MALE_BUFFALO
import com.gavkariapp.constant.AppConstant.MALE_GOAT
import com.gavkariapp.constant.AppConstant.OTHER_DOMESTIC_ANIMALS
import com.gavkariapp.constant.AppConstant.OX
import com.gavkariapp.constant.AppConstant.PICKUP
import com.gavkariapp.constant.AppConstant.PLOUGH
import com.gavkariapp.constant.AppConstant.ROTOVATOR
import com.gavkariapp.constant.AppConstant.SEED_DRILL
import com.gavkariapp.constant.AppConstant.SPRAY_BLOWER
import com.gavkariapp.constant.AppConstant.STEEL
import com.gavkariapp.constant.AppConstant.TEMPO_TRUCK
import com.gavkariapp.constant.AppConstant.THRESHER
import com.gavkariapp.constant.AppConstant.TRACTOR
import com.gavkariapp.constant.AppConstant.TROLLEY
import com.gavkariapp.constant.AppConstant.TWO_WHEELER
import com.gavkariapp.constant.AppConstant.WATER_MOTOR_PUMP
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.utility.Util
import java.util.*

class NotificationAdapter(context: Context,var notificationResponse: LinkedList<Any>,
                               var onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_EVENT = 0
        private const val TYPE_NEWS = 1
        private const val TYPE_SALE_AD = 2
    }

    private val context: Context = context

    var list: LinkedList<Any> = notificationResponse

    private lateinit var userLang :String

    private lateinit var userId : String

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == TYPE_EVENT) {
            (viewHolder as EventViewHolder).bind(position)
        }

        if (getItemViewType(position) == TYPE_NEWS) {
            (viewHolder as NewsViewHolder).bind(position)
        }

        if (getItemViewType(position) == TYPE_SALE_AD) {
            (viewHolder as SaleAdViewHolder).bind(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val prefs = PreferenceHelper.customPrefs(context, "user_info")
        userLang = prefs[AppConstant.LANGUAGE, "-1"]!!
        userId = prefs[ApiConstant.USER_ID, "0"]!!

        if (viewType == TYPE_EVENT) {
            return EventViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.layout_home_tab_event, parent, false)
            )
        }

        if (viewType == TYPE_NEWS) {
            return NewsViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.layout_home_tab_news, parent, false)
            )
        }

        if (viewType == TYPE_SALE_AD) {
            return SaleAdViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.layout_buysale_row, parent, false)
            )
        }

        return EventViewHolder(
                LayoutInflater.from(context).inflate(R.layout.layout_home_tab_event, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return notificationResponse.size
    }

    override fun getItemViewType(position: Int): Int {
        when (val `object` = notificationResponse[position]) {
            is BuySale -> return TYPE_SALE_AD
            is News -> return TYPE_NEWS
            is Event -> return TYPE_EVENT
            else -> `object` is Event
        }
        return super.getItemViewType(position)
    }

    interface OnItemClickListener {
        fun onItemClick(item: Any)
    }

    private inner class EventViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var imgCard: ImageView = itemview.findViewById(R.id.imgCard)
        var tvTitle: TextView = itemview.findViewById(R.id.tvTitle)
        var tvSubtitle: TextView = itemview.findViewById(R.id.tvSubtitle)
        var tvDate: TextView = itemview.findViewById(R.id.tvDate)
        var layPatrika: LinearLayout = itemview.findViewById(R.id.layPatrika)
        fun bind(position: Int) {
            val data = list[position] as Event
            tvTitle.text = data.title
            tvSubtitle.text = data.subtitle

            if(data.type== AppConstant.WEDDING || data.type== AppConstant.ENGAGEMENT){
                var subtitle = data.subtitle +" "+ data.subtitle_one +" "+context.getString(R.string.lbl_and)+" "+
                        data.subtitle_three +" "+data.subtitle_four +" "+context.getString(R.string.lbl_there_wedding)
                tvSubtitle.text = subtitle
            }else{
                tvSubtitle.text = data.subtitle
            }

            var inputDay = Util.getFormatedDate(data.event_date,
                    "yyyy-MM-dd", "EEEE",itemView.resources)

            var inputDate = Util.getFormatedDate(data.event_date,
                    "yyyy-MM-dd", "d MMMM, yyyy",itemView.resources)

            tvDate.text = inputDay +"\n" +inputDate

            Glide.with(itemView.context)
                    .load(HttpConstant.BASE_EVENT_DOWNLOAD_URL +data.photo)
                    .thumbnail(0.5f)
                    .into(imgCard)

            itemView.setOnClickListener {
                onItemClickListener.onItemClick(data)
            }

            layPatrika.visibility = View.GONE
        }
    }

    private inner class NewsViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var imgCard: ImageView = itemview.findViewById(R.id.imgCard)
        var tvTitle: TextView = itemview.findViewById(R.id.tvTitle)
        var tvSubtitle: TextView = itemview.findViewById(R.id.tvSubtitle)
        var tvDate: TextView = itemview.findViewById(R.id.tvDate)
        fun bind(position: Int) {
            val data = list[position] as News
            tvTitle.text = data.title
            tvSubtitle.text = data.description

            var inputDay = Util.getFormatedDate(data.news_date,
                    "yyyy-MM-dd", "EEEE",itemView.resources)

            var inputDate = Util.getFormatedDate(data.news_date,
                    "yyyy-MM-dd", "d MMMM, yyyy",itemView.resources)

            tvDate.text = inputDay +"\n" +inputDate

            Glide.with(itemView.context)
                    .load(HttpConstant.BASE_NEWS_DOWNLOAD_URL+data.photo)
                    .thumbnail(0.5f)
                    .into(imgCard)
            itemView.setOnClickListener { onItemClickListener.onItemClick(data) }

        }
    }

    private inner class SaleAdViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var imgCard :ImageView = itemView.findViewById(R.id.imgCard)
        var tvType :TextView = itemView.findViewById(R.id.tvType)
        var tvBrand :TextView = itemView.findViewById(R.id.tvBrand)
        var imgLike :ImageView = itemView.findViewById(R.id.imgLike)
        var imgLikeFill :ImageView = itemView.findViewById(R.id.imgLikeFill)
        var tvDetails :TextView = itemView.findViewById(R.id.tvDetails)
        var tvAddress:TextView = itemView.findViewById(R.id.tvAddress)
        var tvDistance:TextView = itemView.findViewById(R.id.tvDistance)
        var tvPrice:TextView = itemView.findViewById(R.id.tvPrice)
        var tvDate:TextView = itemView.findViewById(R.id.tvDate)

        fun bind(position: Int) {
            val data = list[position] as BuySale

            var villageName = ""

            if (userLang == AppConstant.ENGLISH) {
                villageName = data.village_en
            }
            if (userLang == AppConstant.MARATHI) {
                villageName =  data.village_mr
            }
            tvAddress.text = villageName
            tvPrice.text = data.price

            val distance = String.format("%.0f", data.distance)
            tvDistance.text = "( " + distance +" " + context.getString(R.string.lbl_km) + " )"

            if (data.distance == 0.0){
                tvDistance.visibility = View.GONE
            }

            Glide.with(itemView.context)
                    .load(HttpConstant.BASE_BUYSALE_DOWNLOAD_URL +data.photo)
                    .thumbnail(0.5f)
                    .into(imgCard)

            var inputDate = Util.getFormatedDate(data.created_at,
                    "yyyy-MM-dd HH:MM:SS", "d MMM, yyyy",itemView.resources)

            tvDate.text = inputDate

            if (!data.fav_user_id.isNullOrEmpty()){

                var userIds = data.fav_user_id.split(",").map { it.trim() }

                val likedUser = userIds.find { id -> userId.equals(id) }

                if (likedUser != null){
                    imgLike.visibility = View.GONE
                    imgLikeFill.visibility = View.VISIBLE
                }else{
                    imgLikeFill.visibility = View.GONE
                    imgLike.visibility = View.VISIBLE
                }
            }

            itemView.setOnClickListener {onItemClickListener.onItemClick(data)}

            imgLike.visibility = View.GONE
            imgLikeFill.visibility = View.GONE

            when {
                data.tab_type == ANIMAL -> {

                    if (data.type == COW || data.type == BUFFALO || data.type == HEIFER_COW ||
                            data.type == HEIFER_BUFFALO || data.type == GOAT ){

                        tvType.text = data.name + ","

                        if (data.type == GOAT){
                            tvBrand.visibility = View.GONE
                        }else{
                            tvBrand.text = data.breed
                        }

                        var preg_status = context.getString(R.string.lbl_not_pregnant)
                        var milk = context.getString(R.string.lbl_no_milk)

                        if (data.pregnancy_status > 0){
                            preg_status = context.getString(R.string.lbl_pregnant)
                        }
                        if (data.milk >0){
                            milk = data.milk.toString() +" "+ context.getString(R.string.lbl_milk_liter)
                        }

                        tvDetails.text = preg_status +",  "+ milk

                    }else if (data.type == OX || data.type == MALE_BUFFALO || data.type == OTHER_DOMESTIC_ANIMALS){
                        tvType.text = data.name
                        tvDetails.text = data.title
                        tvBrand.visibility = View.INVISIBLE

                    }else if (data.type == MALE_GOAT){
                        tvType.text = data.name
                        tvDetails.text = data.weight + " " + context.getString(R.string.lbl_kg)
                        tvBrand.visibility = View.INVISIBLE
                    }

                }
                data.tab_type == MACHINERY -> {

                    tvType.text = data.name + ","
                    tvBrand.text = data.company

                    var year = data.year + ", "
                    var extra_info = ""

                    if (data.type == TRACTOR || data.type == PICKUP || data.type == TEMPO_TRUCK ||
                            data.type == CAR || data.type == TWO_WHEELER ){

                        extra_info = data.km_driven +" "+ context.getString(R.string.lbl_km)

                    }else if (data.type == THRESHER || data.type == KUTTI_MACHINE){

                        extra_info = data.power

                    }else if (data.type == SPRAY_BLOWER){

                        extra_info = data.capacity +" "+ context.getString(R.string.lbl_capacity)

                    }else{
                        extra_info = data.title
                    }

                    tvDetails.text = year + extra_info
                }
                data.tab_type == EQUIPMENTS -> {

                    tvType.text = data.name + ","
                    tvBrand.text = data.company

                    var year = data.year + ", "
                    var extra_info = ""

                    if (data.type == CULTIVATOR || data.type == SEED_DRILL){

                        extra_info = data.tynes_count +" "+ context.getString(R.string.lbl_tynes)

                    }else if (data.type == ROTOVATOR){

                        extra_info = data.material

                    }else if (data.type == PLOUGH){

                        extra_info = data.weight + " " + context.getString(R.string.lbl_kg)

                    }else if (data.type == TROLLEY){

                        extra_info = data.capacity

                    }else if (data.type == WATER_MOTOR_PUMP){

                        extra_info = data.phase + ", " + data.power

                    }else if (data.type == IRRIGATION_MATERIAL || data.type == STEEL){

                        extra_info = data.title

                    }else {

                        extra_info = data.title
                    }

                    tvDetails.text = year + extra_info

                }
                else -> {

                }
            }

        }
    }
}