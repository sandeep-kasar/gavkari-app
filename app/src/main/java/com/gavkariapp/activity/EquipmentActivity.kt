package com.gavkariapp.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.lifecycle.Observer
import com.gavkariapp.Model.*
import com.gavkariapp.R
import com.gavkariapp.adapter.BuySaleAdapter
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.AppConstant.ALL
import com.gavkariapp.constant.AppConstant.CULTIVATOR
import com.gavkariapp.constant.AppConstant.IRRIGATION_MATERIAL
import com.gavkariapp.constant.AppConstant.OTHER
import com.gavkariapp.constant.AppConstant.PLOUGH
import com.gavkariapp.constant.AppConstant.ROTOVATOR
import com.gavkariapp.constant.AppConstant.SEED_DRILL
import com.gavkariapp.constant.AppConstant.SPRAY_PUMP
import com.gavkariapp.constant.AppConstant.STEEL
import com.gavkariapp.constant.AppConstant.TROLLEY
import com.gavkariapp.constant.AppConstant.WATER_MOTOR_PUMP
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_animal.*


class EquipmentActivity : BaseActivity(),BuySaleAdapter.OnItemClickListener,
        View.OnClickListener {

    lateinit var buySaleAdapter: BuySaleAdapter

    lateinit var homeViewModel: HomeViewModel

    lateinit var observer: Observer<ApiResponse<BuySaleResponse, String>>

    var eqip_type: Int = 0

    var eqip_sort: Int = 0

    var pricerange: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_animal)
        setupView()
    }

    fun setupView(){
        homeViewModel = HomeViewModel()
        tvSelectType.text = getString(R.string.lbl_select_equip)
        rvBuySale.applyVerticalWithDividerLinearLayoutManager()
        laySelectType.setOnClickListener(this)
        tvSort.setOnClickListener(this)
        tvFilter.setOnClickListener(this)
        imgBack.setOnClickListener(this)
        imgFav.setOnClickListener(this)
        imgCreateAd.setOnClickListener(this)
        initObserver()
        accessData()
    }

    override fun onItemClick(buysale: BuySale){
        buysale.fromActivity = "EquipmentActivity"
        startActivity(Intent(applicationContext,BuySaleDetailActivity::class.java).putExtra("buySaleData",buysale))
    }

    override fun onClick(v: View?) {
        when(v){
            laySelectType -> {
                openDialog()
            }

            tvSort -> {
                sortDialog()
            }

            tvFilter ->{
                filterDialog()
            }

            imgBack -> {
                onBackPressed()
            }

            imgFav ->{
                startActivity(Intent(applicationContext, MyFavAdActivity::class.java) .putExtra("tab_type",3))
            }

            imgCreateAd ->{
                startActivity(Intent(applicationContext, SelectBuySaleAdTypeActivity::class.java)
                        .putExtra("adType", AppConstant.EQUIPMENTS))
            }
        }
    }

    fun openDialog(){
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_buy_sale_filter_equipment)

        val tvAllEquip = dialog.findViewById(R.id.tvAllEquip) as TextView
        val tvCultivator = dialog.findViewById(R.id.tvCultivator) as TextView
        val tvRotovator = dialog.findViewById(R.id.tvRotovator) as TextView
        val tvPlough = dialog.findViewById(R.id.tvPlough) as TextView
        val tvTrolley = dialog.findViewById(R.id.tvTrolley) as TextView
        val tvSeedDrill = dialog.findViewById(R.id.tvSeedDrill) as TextView
        val tvPump = dialog.findViewById(R.id.tvPump) as TextView
        val tvSprayPump = dialog.findViewById(R.id.tvSprayPump) as TextView
        val tvIrrigation = dialog.findViewById(R.id.tvIrrigation) as TextView
        val tvSteel = dialog.findViewById(R.id.tvSteel) as TextView
        val tvOtherEqui = dialog.findViewById(R.id.tvOtherEqui) as TextView

        tvAllEquip.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvAllEquip.text.toString()
            eqip_type = ALL
            accessData()
        }

        tvCultivator.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvCultivator.text.toString()
            eqip_type = CULTIVATOR
            accessData()
        }

        tvRotovator.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvRotovator.text.toString()
            eqip_type = ROTOVATOR
            accessData()
        }

        tvPlough.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvPlough.text.toString()
            eqip_type = PLOUGH
            accessData()
        }

        tvTrolley.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvTrolley.text.toString()
            eqip_type = TROLLEY
            accessData()
        }

        tvSeedDrill.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvSeedDrill.text.toString()
            eqip_type = SEED_DRILL
            accessData()
        }

        tvPump.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvPump.text.toString()
            eqip_type = WATER_MOTOR_PUMP
            accessData()
        }

        tvSprayPump.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvSprayPump.text.toString()
            eqip_type = SPRAY_PUMP
            accessData()
        }

        tvIrrigation.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvIrrigation.text.toString()
            eqip_type = IRRIGATION_MATERIAL
            accessData()
        }

        tvSteel.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvSteel.text.toString()
            eqip_type = STEEL
            accessData()
        }

        tvOtherEqui.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvOtherEqui.text.toString()
            eqip_type = OTHER
            accessData()
        }
        dialog.show()
    }

    fun sortDialog(){
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_sort)

        val rdBtnOne = dialog.findViewById(R.id.rdBtnOne) as RadioButton
        val rdBtnTwo = dialog.findViewById(R.id.rdBtnTwo) as RadioButton
        val rdBtnThree = dialog.findViewById(R.id.rdBtnThree) as RadioButton
        val rdBtnFour = dialog.findViewById(R.id.rdBtnFour) as RadioButton
        val tvCancel = dialog.findViewById(R.id.tvCancel) as TextView
        val tvApply = dialog.findViewById(R.id.tvApply) as TextView

        rdBtnOne.setOnClickListener {
            eqip_sort = 1
        }
        rdBtnTwo.setOnClickListener {
            eqip_sort = 2
        }
        rdBtnThree.setOnClickListener {
            eqip_sort = 3
        }
        rdBtnFour.setOnClickListener {
            eqip_sort = 4
        }

        tvCancel.setOnClickListener {
            dialog.cancel()
            eqip_sort = 0
        }
        tvApply.setOnClickListener {
            dialog.cancel()
            if (eqip_sort > 0) {
                accessData()
            }
        }

        if (eqip_sort == 1){
            rdBtnOne.isChecked = true
        }else if (eqip_sort == 2){
            rdBtnTwo.isChecked = true
        }else if (eqip_sort == 3){
            rdBtnThree.isChecked = true
        }else if (eqip_sort == 4){
            rdBtnFour.isChecked = true
        }else{
        }

        dialog.show()
    }

    fun filterDialog(){
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_equipment_filter)
        dialog.show()

        val lay1 = dialog.findViewById(R.id.lay1) as LinearLayout
        val lay2 = dialog.findViewById(R.id.lay2) as LinearLayout
        val lay3 = dialog.findViewById(R.id.lay3) as LinearLayout
        val lay4 = dialog.findViewById(R.id.lay4) as LinearLayout
        val lay5 = dialog.findViewById(R.id.lay5) as LinearLayout
        val lay6 = dialog.findViewById(R.id.lay6) as LinearLayout
        val lay7 = dialog.findViewById(R.id.lay7) as LinearLayout
        val lay8 = dialog.findViewById(R.id.lay8) as LinearLayout
        val lay9 = dialog.findViewById(R.id.lay9) as LinearLayout

        val img1 = dialog.findViewById(R.id.img1) as ImageView
        val img2 = dialog.findViewById(R.id.img2) as ImageView
        val img3 = dialog.findViewById(R.id.img3) as ImageView
        val img4 = dialog.findViewById(R.id.img4) as ImageView
        val img5 = dialog.findViewById(R.id.img5) as ImageView
        val img6 = dialog.findViewById(R.id.img6) as ImageView
        val img7 = dialog.findViewById(R.id.img7) as ImageView
        val img8 = dialog.findViewById(R.id.img8) as ImageView
        val img9 = dialog.findViewById(R.id.img9) as ImageView

        val tvCancel = dialog.findViewById(R.id.tvCancel) as TextView
        val tvApply = dialog.findViewById(R.id.tvApply) as TextView

        tvCancel.setOnClickListener {
            dialog.cancel()
            pricerange =0
            img1.setImageResource(R.drawable.ic_done_tick)
            img2.setImageResource(R.drawable.ic_done_tick)
            img3.setImageResource(R.drawable.ic_done_tick)
            img4.setImageResource(R.drawable.ic_done_tick)
            img5.setImageResource(R.drawable.ic_done_tick)
            img6.setImageResource(R.drawable.ic_done_tick)
            img7.setImageResource(R.drawable.ic_done_tick)
            img8.setImageResource(R.drawable.ic_done_tick)
            img9.setImageResource(R.drawable.ic_done_tick)
            accessData()
        }
        tvApply.setOnClickListener {
            dialog.cancel()
            accessData()
        }

        if (pricerange == 1){
            img1.setImageResource(R.drawable.ic_done_tick_green)
        }else if(pricerange == 2){
            img2.setImageResource(R.drawable.ic_done_tick_green)
        }else if(pricerange == 3){
            img3.setImageResource(R.drawable.ic_done_tick_green)
        }else if(pricerange == 4){
            img4.setImageResource(R.drawable.ic_done_tick_green)
        }else if(pricerange == 5){
            img5.setImageResource(R.drawable.ic_done_tick_green)
        }else if(pricerange == 6){
            img6.setImageResource(R.drawable.ic_done_tick_green)
        }else if(pricerange == 7){
            img7.setImageResource(R.drawable.ic_done_tick_green)
        }else if(pricerange == 8){
            img8.setImageResource(R.drawable.ic_done_tick_green)
        }else if(pricerange == 9){
            img9.setImageResource(R.drawable.ic_done_tick_green)
        }else{

        }



        lay1.setOnClickListener {
            if (pricerange == 1){
                img1.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                img1.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 1
                img2.setImageResource(R.drawable.ic_done_tick)
                img3.setImageResource(R.drawable.ic_done_tick)
                img4.setImageResource(R.drawable.ic_done_tick)
                img5.setImageResource(R.drawable.ic_done_tick)
                img6.setImageResource(R.drawable.ic_done_tick)
            }
        }

        lay2.setOnClickListener {
            if (pricerange == 2){
                img2.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                img2.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 2
                img1.setImageResource(R.drawable.ic_done_tick)
                img3.setImageResource(R.drawable.ic_done_tick)
                img4.setImageResource(R.drawable.ic_done_tick)
                img5.setImageResource(R.drawable.ic_done_tick)
                img6.setImageResource(R.drawable.ic_done_tick)
            }
        }

        lay3.setOnClickListener {
            if (pricerange == 3){
                img3.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                img3.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 3
                img1.setImageResource(R.drawable.ic_done_tick)
                img2.setImageResource(R.drawable.ic_done_tick)
                img4.setImageResource(R.drawable.ic_done_tick)
                img5.setImageResource(R.drawable.ic_done_tick)
                img6.setImageResource(R.drawable.ic_done_tick)
            }

        }

        lay4.setOnClickListener {
            if (pricerange == 4){
                img4.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                img4.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 4
                img1.setImageResource(R.drawable.ic_done_tick)
                img2.setImageResource(R.drawable.ic_done_tick)
                img3.setImageResource(R.drawable.ic_done_tick)
                img5.setImageResource(R.drawable.ic_done_tick)
                img6.setImageResource(R.drawable.ic_done_tick)
            }

        }

        lay5.setOnClickListener {
            if (pricerange == 5){
                img5.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                img5.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 5
                img1.setImageResource(R.drawable.ic_done_tick)
                img2.setImageResource(R.drawable.ic_done_tick)
                img3.setImageResource(R.drawable.ic_done_tick)
                img4.setImageResource(R.drawable.ic_done_tick)
                img6.setImageResource(R.drawable.ic_done_tick)
            }

        }

        lay6.setOnClickListener {
            if (pricerange == 6){
                img6.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                img6.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 6
                img1.setImageResource(R.drawable.ic_done_tick)
                img2.setImageResource(R.drawable.ic_done_tick)
                img3.setImageResource(R.drawable.ic_done_tick)
                img4.setImageResource(R.drawable.ic_done_tick)
                img5.setImageResource(R.drawable.ic_done_tick)
            }

        }

        lay7.setOnClickListener {
            if (pricerange == 6){
                img7.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                img7.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 6
                img1.setImageResource(R.drawable.ic_done_tick)
                img2.setImageResource(R.drawable.ic_done_tick)
                img3.setImageResource(R.drawable.ic_done_tick)
                img4.setImageResource(R.drawable.ic_done_tick)
                img5.setImageResource(R.drawable.ic_done_tick)
                img6.setImageResource(R.drawable.ic_done_tick)
                img8.setImageResource(R.drawable.ic_done_tick)
                img9.setImageResource(R.drawable.ic_done_tick)
            }
        }

        lay8.setOnClickListener {
            if (pricerange == 6){
                img8.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                img8.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 6
                img1.setImageResource(R.drawable.ic_done_tick)
                img2.setImageResource(R.drawable.ic_done_tick)
                img3.setImageResource(R.drawable.ic_done_tick)
                img4.setImageResource(R.drawable.ic_done_tick)
                img5.setImageResource(R.drawable.ic_done_tick)
                img6.setImageResource(R.drawable.ic_done_tick)
                img7.setImageResource(R.drawable.ic_done_tick)
                img9.setImageResource(R.drawable.ic_done_tick)
            }
        }

        lay9.setOnClickListener {
            if (pricerange == 6){
                img9.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                img9.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 6
                img1.setImageResource(R.drawable.ic_done_tick)
                img2.setImageResource(R.drawable.ic_done_tick)
                img3.setImageResource(R.drawable.ic_done_tick)
                img4.setImageResource(R.drawable.ic_done_tick)
                img5.setImageResource(R.drawable.ic_done_tick)
                img6.setImageResource(R.drawable.ic_done_tick)
                img7.setImageResource(R.drawable.ic_done_tick)
                img8.setImageResource(R.drawable.ic_done_tick)
            }
        }

    }

    fun initObserver(){
        observer = Observer { t ->

            if (t?.response != null) {
                var BuySaleResponse = t.response
                if (BuySaleResponse?.status == HttpConstant.SUCCESS) {
                    displayData(BuySaleResponse)
                }else if (BuySaleResponse?.status == HttpConstant.NO_DATA_AVAILABLE){
                    nvBuySale.visibility = View.GONE
                    layNoInternetBuySale.visibility = View.GONE
                    proBuySale.visibility= View.GONE
                    layNoDataBuySale.visibility = View.VISIBLE
                }

            } else {
                Log.e("warning", t?.error!!)
                showError(getString(R.string.msg_unexpected_error))
            }
        }
    }

    fun accessData(){

        if (InternetUtil.isInternetOn()) {
            proBuySale.visibility= View.VISIBLE
            nvBuySale.visibility = View.GONE
            layNoDataBuySale.visibility = View.GONE
            layNoInternetBuySale.visibility = View.GONE
            val prefs = PreferenceHelper.customPrefs(applicationContext, "user_info")
            var user_id : String? = prefs[ApiConstant.USER_ID, ""]
            var latitude : String? = prefs[ApiConstant.LATITUDE, ""]
            var longitude : String? = prefs[ApiConstant.LONGITUDE, ""]
            var buySaleBody = BuySaleTypeBody(user_id!!,eqip_type,eqip_sort,pricerange,latitude!!,longitude!!)
            homeViewModel.accessBuySaleEquipment(buySaleBody).observe(this, observer)
        } else {
            waitForInternet()
        }
    }

    fun displayData(buySale: BuySaleResponse){
        proBuySale.visibility= View.GONE
        nvBuySale.visibility = View.VISIBLE
        layNoInternetBuySale.visibility = View.GONE
        layNoDataBuySale.visibility = View.GONE
        buySaleAdapter = BuySaleAdapter(buySale.BuySaleAds, this)
        rvBuySale.adapter = buySaleAdapter

    }
}

