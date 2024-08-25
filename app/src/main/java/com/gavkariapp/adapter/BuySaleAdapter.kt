package com.gavkariapp.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gavkariapp.Model.AddFavBody
import com.gavkariapp.Model.BuySale
import com.gavkariapp.Model.CommonResponse
import com.gavkariapp.R
import com.gavkariapp.activity.HomeActivity
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
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import com.gavkariapp.utility.Util
import com.gavkariapp.viewHolder.BuySaleViewHolder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BuySaleAdapter(var myVillageResponse: ArrayList<BuySale>,
                     var onItemClickListener: OnItemClickListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<BuySaleViewHolder>() {

    companion object {
        private const val TYPE_BUY_SALE = 0
    }

    private lateinit var parentView : Context

    private lateinit var userLang :String

    private lateinit var userId : String

    override fun onBindViewHolder(viewHolder: BuySaleViewHolder, position: Int) {

        if (getItemViewType(position) == TYPE_BUY_SALE) {
            showBuySale(viewHolder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuySaleViewHolder {


        var viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        viewHolder = when (viewType) {

            TYPE_BUY_SALE -> {
                val viewEvent = inflater.inflate(R.layout.layout_buysale_row, parent, false)
                BuySaleViewHolder(viewEvent)
            }

            else -> {
                val view = inflater.inflate(R.layout.layout_buysale_row, parent, false)
                BuySaleViewHolder(view)
            }
        }

        parentView = parent.context

        val prefs = PreferenceHelper.customPrefs(parentView, "user_info")
        userLang = prefs[AppConstant.LANGUAGE, "-1"]!!
        userId = prefs[ApiConstant.USER_ID, "0"]!!


        return viewHolder
    }

    override fun getItemCount(): Int {
        return myVillageResponse.size
    }

    override fun getItemViewType(position: Int): Int {
        val obj = myVillageResponse[position]
        when (obj) {
            is BuySale -> return TYPE_BUY_SALE
            else -> obj is BuySale
        }
        return super.getItemViewType(position)
    }

    private fun showBuySale(viewHolder: BuySaleViewHolder, position: Int) {

        val data = myVillageResponse[position] as BuySale

        var villageName = ""

        if (userLang == AppConstant.ENGLISH) {
            villageName = data.village_en
        }
        if (userLang == AppConstant.MARATHI) {
            villageName =  data.village_mr
        }
        viewHolder.tvAddress.text = villageName
        val distance = String.format("%.0f", data.distance)
        viewHolder.tvDistance.text = "( " + distance +" " + parentView.getString(R.string.lbl_km) + " )"
        viewHolder.tvPrice.text = data.price

        Glide.with(viewHolder.itemView.context)
                .load(HttpConstant.BASE_BUYSALE_DOWNLOAD_URL +data.photo)
                .thumbnail(0.5f)
                .into(viewHolder.imgCard)

        var inputDate = Util.getFormatedDate(data.created_at,
                "yyyy-MM-dd", "d MMM, yyyy",viewHolder.itemView.resources)

        viewHolder.tvDate.text = inputDate

        if (!data.fav_user_id.isNullOrEmpty()){

            var userIds = data.fav_user_id.split(",").map { it.trim() }

            val likedUser = userIds.find { id -> userId.equals(id) }

            if (likedUser != null){
                Log.e("likedUser==",likedUser)
                viewHolder.imgLike.visibility = View.GONE
                viewHolder.imgLikeFill.visibility = View.VISIBLE
            }else{
                viewHolder.imgLikeFill.visibility = View.GONE
                viewHolder.imgLike.visibility = View.VISIBLE
            }
        }


        viewHolder.imgLike.setOnClickListener {addFav(1,viewHolder,position)}
        viewHolder.imgLikeFill.setOnClickListener {addFav(2,viewHolder,position)}

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
            else -> {

            }
        }

        if (data.status == SOLD){
            viewHolder.tvSold.visibility = View.VISIBLE
        }else{
            viewHolder.tvSold.visibility = View.GONE
        }

        viewHolder.itemView.setOnClickListener {
            if (data.status != SOLD){
                onItemClickListener.onItemClick(data)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: BuySale)
    }

    fun addFav(type : Int, viewHolder: BuySaleViewHolder, position: Int){

        val data = myVillageResponse[position]

        var addFavBody = AddFavBody(data.id,userId,type)

        ApiClient.get().create(ApiInterface::class.java)
                .deleteFav(addFavBody)
                .enqueue(object : Callback<CommonResponse> {
                    override fun onResponse(call: Call<CommonResponse>?, response: Response<CommonResponse>?) {
                        if (response!!.code() == 200) {

                            var commonResponse = response.body() as CommonResponse
                            if (commonResponse.status == HttpConstant.SUCCESS){
                                (parentView as Activity).runOnUiThread {
                                    if (addFavBody.type == 1){
                                        viewHolder.imgLike.visibility = View.GONE
                                        viewHolder.imgLikeFill.visibility = View.VISIBLE

                                    }else{
                                        viewHolder.imgLikeFill.visibility = View.GONE
                                        viewHolder.imgLike.visibility = View.VISIBLE
                                    }
                                }
                            }
                        } else {
                            if (response.errorBody() != null) {
                                (parentView as Activity).runOnUiThread {
                                    (parentView as HomeActivity).showError(response.errorBody().toString())
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<CommonResponse>?, t: Throwable?) {
                        (parentView as Activity).runOnUiThread {
                            (parentView as HomeActivity).showError(t.toString())
                        }
                    }
                })
    }
}

///home/sandeep/gavkariapp/Api/api/application/
