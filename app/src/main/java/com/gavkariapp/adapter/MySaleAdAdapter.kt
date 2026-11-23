package com.gavkariapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gavkariapp.Model.BuySale
import com.gavkariapp.Model.MySaleAd
import com.gavkariapp.R
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant.ANIMAL
import com.gavkariapp.constant.AppConstant.BUFFALO
import com.gavkariapp.constant.AppConstant.CAR
import com.gavkariapp.constant.AppConstant.COW
import com.gavkariapp.constant.AppConstant.CULTIVATOR
import com.gavkariapp.constant.AppConstant.ENGLISH
import com.gavkariapp.constant.AppConstant.EQUIPMENTS
import com.gavkariapp.constant.AppConstant.GOAT
import com.gavkariapp.constant.AppConstant.HEIFER_BUFFALO
import com.gavkariapp.constant.AppConstant.HEIFER_COW
import com.gavkariapp.constant.AppConstant.IRRIGATION_MATERIAL
import com.gavkariapp.constant.AppConstant.KUTTI_MACHINE
import com.gavkariapp.constant.AppConstant.LANGUAGE
import com.gavkariapp.constant.AppConstant.MACHINERY
import com.gavkariapp.constant.AppConstant.MALE_BUFFALO
import com.gavkariapp.constant.AppConstant.MALE_GOAT
import com.gavkariapp.constant.AppConstant.MARATHI
import com.gavkariapp.constant.AppConstant.OTHER_DOMESTIC_ANIMALS
import com.gavkariapp.constant.AppConstant.OX
import com.gavkariapp.constant.AppConstant.PICKUP
import com.gavkariapp.constant.AppConstant.PLOUGH
import com.gavkariapp.constant.AppConstant.PUBLISHED
import com.gavkariapp.constant.AppConstant.REJECTED
import com.gavkariapp.constant.AppConstant.ROTOVATOR
import com.gavkariapp.constant.AppConstant.SEED_DRILL
import com.gavkariapp.constant.AppConstant.SOLD
import com.gavkariapp.constant.AppConstant.SPRAY_BLOWER
import com.gavkariapp.constant.AppConstant.STEEL
import com.gavkariapp.constant.AppConstant.TEMPO_TRUCK
import com.gavkariapp.constant.AppConstant.THRESHER
import com.gavkariapp.constant.AppConstant.TRACTOR
import com.gavkariapp.constant.AppConstant.TROLLEY
import com.gavkariapp.constant.AppConstant.TWO_WHEELER
import com.gavkariapp.constant.AppConstant.UNDER_REVIEW
import com.gavkariapp.constant.AppConstant.WATER_MOTOR_PUMP
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.utility.Util
import com.gavkariapp.viewHolder.MySaleAdHolder


class MySaleAdAdapter(var myVillageResponse: ArrayList<MySaleAd>,
                      var onItemClickListener: OnItemClickListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<MySaleAdHolder>() {

    companion object {
        private const val TYPE_BUY_SALE = 0
    }

    private lateinit var parentView : Context

    private lateinit var userLang :String

    private lateinit var userId : String

    override fun onBindViewHolder(viewHolder: MySaleAdHolder, position: Int) {
        if (getItemViewType(position) == TYPE_BUY_SALE) {
            showBuySale(viewHolder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySaleAdHolder {

        var viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        viewHolder = when (viewType) {

            TYPE_BUY_SALE -> {
                val viewEvent = inflater.inflate(R.layout.layout_my_sale_ad_row, parent, false)
                MySaleAdHolder(viewEvent)
            }

            else -> {
                val view = inflater.inflate(R.layout.layout_my_sale_ad_row, parent, false)
                MySaleAdHolder(view)
            }
        }

        parentView = parent.context

        val prefs = PreferenceHelper.customPrefs(parentView, "user_info")
        userLang = prefs[LANGUAGE, "-1"]!!
        userId = prefs[ApiConstant.USER_ID, "0"]!!


        return viewHolder
    }

    override fun getItemCount(): Int {
        return myVillageResponse.size
    }

    override fun getItemViewType(position: Int): Int {
        val obj = myVillageResponse[position]
        return TYPE_BUY_SALE
        return super.getItemViewType(position)
    }

    private fun showBuySale(viewHolder: MySaleAdHolder, position: Int) {

        val data = myVillageResponse[position] as MySaleAd

        var villageName = ""

        if (userLang == ENGLISH) {
            villageName = data.village_en
        }
        if (userLang == MARATHI) {
            villageName =  data.village_mr
        }
        viewHolder.tvAddress.text = villageName
        viewHolder.tvPrice.text = data.price

        Glide.with(viewHolder.itemView.context)
                .load(HttpConstant.BASE_BUYSALE_DOWNLOAD_URL +data.photo)
                .thumbnail(0.5f)
                .into(viewHolder.imgCard)

        var inputDate = Util.getFormatedDate(data.created_at,
                "yyyy-MM-dd", "d MMM, yyyy",viewHolder.itemView.resources)

        viewHolder.tvDate.text = inputDate

        viewHolder.itemView.setOnClickListener {onItemClickListener.onItemClick(data)}
        viewHolder.btnDelete.setOnClickListener {onItemClickListener.onClickDelete(data)}
        viewHolder.btnEdit.setOnClickListener {onItemClickListener.onClickEdit(data)}
        viewHolder.btnSold.setOnClickListener {onItemClickListener.onClickSold(data)}

        when {
            data.tab_type == ANIMAL -> {

                if (data.type == COW || data.type == BUFFALO || data.type == HEIFER_COW ||
                        data.type == HEIFER_BUFFALO || data.type == GOAT ){

                    viewHolder.tvType.text = data.name + ","

                    if (data.type == GOAT){
                        viewHolder.tvBrand.visibility = View.GONE
                    }else{
                        viewHolder.tvBrand.text = data.breed
                    }

                    var preg_status = parentView.getString(R.string.lbl_not_pregnant)
                    var milk = parentView.getString(R.string.lbl_no_milk)

                    if (data.pregnancy_status > 0){
                        preg_status = parentView.getString(R.string.lbl_pregnant)
                    }
                    if (data.milk >0){
                        milk = data.milk.toString() +" "+ parentView.getString(R.string.lbl_milk_liter)
                    }

                    viewHolder.tvDetails.text = preg_status +",  "+ milk

                }else if (data.type == OX || data.type == MALE_BUFFALO || data.type == OTHER_DOMESTIC_ANIMALS){
                    viewHolder.tvType.text = data.name
                    viewHolder.tvDetails.text = data.title
                    viewHolder.tvBrand.visibility = View.INVISIBLE

                    if (data.title.isNotEmpty()){
                        viewHolder.tvDetails.text = data.title
                    }else{
                        viewHolder.layDetails.visibility = View.GONE
                    }

                }else if (data.type == MALE_GOAT){
                    viewHolder.tvType.text = data.name
                    viewHolder.tvDetails.text = data.weight + " " + parentView.getString(R.string.lbl_kg)
                    viewHolder.tvBrand.visibility = View.INVISIBLE
                }

            }
            data.tab_type == MACHINERY -> {

                viewHolder.tvType.text = data.name + ","
                viewHolder.tvBrand.text = data.company

                var year = data.year + ", "
                var extra_info = ""

                if (data.type == TRACTOR || data.type == PICKUP || data.type == TEMPO_TRUCK ||
                        data.type == CAR || data.type == TWO_WHEELER ){

                    extra_info = data.km_driven +" "+ parentView.getString(R.string.lbl_km)

                }else if (data.type == THRESHER || data.type == KUTTI_MACHINE){

                    extra_info = data.power

                }else if (data.type == SPRAY_BLOWER){

                    extra_info = data.capacity +" "+ parentView.getString(R.string.lbl_capacity)

                }else{
                    extra_info = data.title
                }

                viewHolder.tvDetails.text = year + extra_info
            }
            data.tab_type == EQUIPMENTS -> {

                viewHolder.tvType.text = data.name + ","
                viewHolder.tvBrand.text = data.company

                var year = data.year + ", "
                var extra_info = ""

                if (data.type == CULTIVATOR || data.type == SEED_DRILL){

                    extra_info = data.tynes_count +" "+ parentView.getString(R.string.lbl_tynes)

                }else if (data.type == ROTOVATOR){

                    extra_info = data.material

                }else if (data.type == PLOUGH){

                    extra_info = data.weight + " " + parentView.getString(R.string.lbl_kg)

                }else if (data.type == TROLLEY){

                    extra_info = data.capacity

                }else if (data.type == WATER_MOTOR_PUMP){

                    extra_info = data.phase + ", " + data.power

                }else if (data.type == IRRIGATION_MATERIAL || data.type == STEEL){

                    extra_info = data.title

                }else {

                    extra_info = data.title
                }

                viewHolder.tvDetails.text = year + extra_info

            }
            else -> {}
        }

        when {
            data.status == UNDER_REVIEW -> {
                viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_in_review)
                viewHolder.tvStatus.setTextColor(viewHolder.itemView.resources.getColor(R.color.sinopia))
                viewHolder.btnSold.visibility = View.GONE
            }
            data.status == REJECTED -> {
                viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_rejected)
                viewHolder.tvStatus.setTextColor(viewHolder.itemView.resources.getColor(R.color.sinopia))
                viewHolder.btnSold.visibility = View.GONE
            }
            data.status == PUBLISHED -> {
                viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_published)
                viewHolder.btnSold.visibility = View.VISIBLE
            }

            data.status == SOLD -> {
                viewHolder.tvStatus.text = viewHolder.itemView.context.getString(R.string.lbl_sold)
                viewHolder.tvStatus.setTextColor(viewHolder.itemView.resources.getColor(R.color.sinopia))
                viewHolder.btnSold.visibility = View.GONE
                viewHolder.btnEdit.visibility = View.GONE
                viewHolder.btnDelete.visibility = View.VISIBLE
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(item: MySaleAd)
        fun onClickDelete(item: MySaleAd)
        fun onClickEdit(item: MySaleAd)
        fun onClickSold(item: MySaleAd)
    }
}

///home/sandeep/gavkariapp/Api/api/application/
