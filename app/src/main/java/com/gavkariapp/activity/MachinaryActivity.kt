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
import com.gavkariapp.Model.ApiResponse
import com.gavkariapp.Model.BuySale
import com.gavkariapp.Model.BuySaleTypeBody
import com.gavkariapp.Model.BuySaleResponse
import com.gavkariapp.R
import com.gavkariapp.adapter.BuySaleAdapter
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.AppConstant.ALL
import com.gavkariapp.constant.AppConstant.CAR
import com.gavkariapp.constant.AppConstant.KUTTI_MACHINE
import com.gavkariapp.constant.AppConstant.OTHER_MACHINE
import com.gavkariapp.constant.AppConstant.PICKUP
import com.gavkariapp.constant.AppConstant.SPRAY_BLOWER
import com.gavkariapp.constant.AppConstant.TEMPO_TRUCK
import com.gavkariapp.constant.AppConstant.THRESHER
import com.gavkariapp.constant.AppConstant.TRACTOR
import com.gavkariapp.constant.AppConstant.TWO_WHEELER
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_animal.*


class MachinaryActivity : BaseActivity(),BuySaleAdapter.OnItemClickListener,
        View.OnClickListener {

    lateinit var buySaleAdapter: BuySaleAdapter

    lateinit var homeViewModel: HomeViewModel

    lateinit var observer: Observer<ApiResponse<BuySaleResponse, String>>

    var machinary_type: Int = 0

    var machinary_sort: Int = 0

    var pricerange: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_animal)
        setupView()
    }

    fun setupView(){
        homeViewModel = HomeViewModel()
        tvSelectType.text = getString(R.string.lbl_select_machine)
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
        buysale.fromActivity = "MachinaryActivity"
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
                startActivity(Intent(applicationContext, MyFavAdActivity::class.java) .putExtra("tab_type",2))
            }

            imgCreateAd ->{
                startActivity(Intent(applicationContext, SelectBuySaleAdTypeActivity::class.java)
                        .putExtra("adType", AppConstant.MACHINERY))
            }

        }
    }

    fun openDialog(){
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_buy_sale_filter_machine)

        val tvAllMc = dialog.findViewById(R.id.tvAllMc) as TextView
        val tvTractor = dialog.findViewById(R.id.tvTractor) as TextView
        val tvPickup = dialog.findViewById(R.id.tvPickup) as TextView
        val tvTempoTruck = dialog.findViewById(R.id.tvTempoTruck) as TextView
        val tvCar = dialog.findViewById(R.id.tvCar) as TextView
        val tvTwoWheeler = dialog.findViewById(R.id.tvTwoWheeler) as TextView
        val tvThresher = dialog.findViewById(R.id.tvThresher) as TextView
        val tvKuttiMachine = dialog.findViewById(R.id.tvKuttiMachine) as TextView
        val tvSprayBlower = dialog.findViewById(R.id.tvSprayBlower) as TextView
        val tvOtherMc = dialog.findViewById(R.id.tvOtherMc) as TextView

        tvAllMc.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvAllMc.text.toString()
            machinary_type = ALL
            accessData()
        }

        tvTractor.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvTractor.text.toString()
            machinary_type = TRACTOR
            accessData()
        }

        tvPickup.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvPickup.text.toString()
            machinary_type = PICKUP
            accessData()
        }

        tvTempoTruck.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvTempoTruck.text.toString()
            machinary_type = TEMPO_TRUCK
            accessData()
        }

        tvCar.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvCar.text.toString()
            machinary_type = CAR
            accessData()
        }

        tvTwoWheeler.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvTwoWheeler.text.toString()
            machinary_type = TWO_WHEELER
            accessData()
        }

        tvThresher.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvThresher.text.toString()
            machinary_type = THRESHER
            accessData()
        }

        tvKuttiMachine.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvKuttiMachine.text.toString()
            machinary_type = KUTTI_MACHINE
            accessData()
        }

        tvSprayBlower.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvSprayBlower.text.toString()
            machinary_type = SPRAY_BLOWER
            accessData()
        }

        tvOtherMc.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvOtherMc.text.toString()
            machinary_type = OTHER_MACHINE
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
            machinary_sort = 1
        }
        rdBtnTwo.setOnClickListener {
            machinary_sort = 2
        }
        rdBtnThree.setOnClickListener {
            machinary_sort = 3
        }
        rdBtnFour.setOnClickListener {
            machinary_sort = 4
        }

        tvCancel.setOnClickListener {
            dialog.cancel()
            machinary_sort = 0
        }
        tvApply.setOnClickListener {
            dialog.cancel()
            if (machinary_sort > 0) {
                accessData()
            }
        }

        if (machinary_sort == 1){
            rdBtnOne.isChecked = true
        }else if (machinary_sort == 2){
            rdBtnTwo.isChecked = true
        }else if (machinary_sort == 3){
            rdBtnThree.isChecked = true
        }else if (machinary_sort == 4){
            rdBtnFour.isChecked = true
        }else{
        }

        dialog.show()
    }

    fun filterDialog(){
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_machine_filter)
        dialog.show()

        val layBelow15k = dialog.findViewById(R.id.layBelow15k) as LinearLayout
        val layP15to30k = dialog.findViewById(R.id.layP15to30k) as LinearLayout
        val layP30to60k = dialog.findViewById(R.id.layP30to60k) as LinearLayout
        val layP60to1L = dialog.findViewById(R.id.layP60to1L) as LinearLayout
        val layP1Lto15L = dialog.findViewById(R.id.layP1Lto15L) as LinearLayout
        val layP15Lto2L = dialog.findViewById(R.id.layP15Lto2L) as LinearLayout
        val layp2lto3l = dialog.findViewById(R.id.layp2lto3l) as LinearLayout
        val layP3Lto4L = dialog.findViewById(R.id.layP3Lto4L) as LinearLayout
        val layP4Lplus = dialog.findViewById(R.id.layP4Lplus) as LinearLayout

        val imgBelow15k = dialog.findViewById(R.id.imgBelow15K) as ImageView
        val imgP15to30k = dialog.findViewById(R.id.imgP15to30k) as ImageView
        val imgP30to60k = dialog.findViewById(R.id.imgP30to60k) as ImageView
        val imgP60to1L = dialog.findViewById(R.id.imgP60to1L) as ImageView
        val imgP1Lto15L = dialog.findViewById(R.id.imgP1Lto15L) as ImageView
        val imgP15Lto2L = dialog.findViewById(R.id.imgP15Lto2L) as ImageView
        val imgp2lto3l = dialog.findViewById(R.id.imgp2lto3l) as ImageView
        val imgP3Lto4L = dialog.findViewById(R.id.imgP3Lto4L) as ImageView
        val imgP4lplus = dialog.findViewById(R.id.imgP4lplus) as ImageView

        val tvCancel = dialog.findViewById(R.id.tvCancel) as TextView
        val tvApply = dialog.findViewById(R.id.tvApply) as TextView

        tvCancel.setOnClickListener {
            dialog.cancel()
            pricerange = 0
            imgBelow15k.setImageResource(R.drawable.ic_done_tick)
            imgP15to30k.setImageResource(R.drawable.ic_done_tick)
            imgP30to60k.setImageResource(R.drawable.ic_done_tick)
            imgP60to1L.setImageResource(R.drawable.ic_done_tick)
            imgP1Lto15L.setImageResource(R.drawable.ic_done_tick)
            imgP15Lto2L.setImageResource(R.drawable.ic_done_tick)
            imgp2lto3l.setImageResource(R.drawable.ic_done_tick)
            imgP3Lto4L.setImageResource(R.drawable.ic_done_tick)
            imgP4lplus.setImageResource(R.drawable.ic_done_tick)
            accessData()
        }
        tvApply.setOnClickListener {
            dialog.cancel()
            accessData()
        }

        if (pricerange == 1){
            imgBelow15k.setImageResource(R.drawable.ic_done_tick_green)
        }else if(pricerange == 2){
            imgP15to30k.setImageResource(R.drawable.ic_done_tick_green)
        }else if(pricerange == 3){
            imgP30to60k.setImageResource(R.drawable.ic_done_tick_green)
        }else if(pricerange == 4){
            imgP60to1L.setImageResource(R.drawable.ic_done_tick_green)
        }else if(pricerange == 5){
            imgP1Lto15L.setImageResource(R.drawable.ic_done_tick_green)
        }else if(pricerange == 6){
            imgP15Lto2L.setImageResource(R.drawable.ic_done_tick_green)
        }else if(pricerange == 7){
            imgp2lto3l.setImageResource(R.drawable.ic_done_tick_green)
        }else if(pricerange == 8){
            imgP3Lto4L.setImageResource(R.drawable.ic_done_tick_green)
        }else if(pricerange == 9){
            imgP4lplus.setImageResource(R.drawable.ic_done_tick_green)
        }else{

        }



        layBelow15k.setOnClickListener {
            if (pricerange == 1){
                imgBelow15k.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                imgBelow15k.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 1
                imgP15to30k.setImageResource(R.drawable.ic_done_tick)
                imgP30to60k.setImageResource(R.drawable.ic_done_tick)
                imgP60to1L.setImageResource(R.drawable.ic_done_tick)
                imgP1Lto15L.setImageResource(R.drawable.ic_done_tick)
                imgP15Lto2L.setImageResource(R.drawable.ic_done_tick)
                imgp2lto3l.setImageResource(R.drawable.ic_done_tick)
                imgP3Lto4L.setImageResource(R.drawable.ic_done_tick)
                imgP4lplus.setImageResource(R.drawable.ic_done_tick)
            }
        }



        layP15to30k.setOnClickListener {
            if (pricerange == 2){
                imgP15to30k.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                imgP15to30k.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 2
                imgBelow15k.setImageResource(R.drawable.ic_done_tick)
                imgP30to60k.setImageResource(R.drawable.ic_done_tick)
                imgP60to1L.setImageResource(R.drawable.ic_done_tick)
                imgP1Lto15L.setImageResource(R.drawable.ic_done_tick)
                imgP15Lto2L.setImageResource(R.drawable.ic_done_tick)
                imgp2lto3l.setImageResource(R.drawable.ic_done_tick)
                imgP3Lto4L.setImageResource(R.drawable.ic_done_tick)
                imgP4lplus.setImageResource(R.drawable.ic_done_tick)
            }
        }

        layP30to60k.setOnClickListener {
            if (pricerange == 3){
                imgP30to60k.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                imgP30to60k.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 3
                imgBelow15k.setImageResource(R.drawable.ic_done_tick)
                imgP15to30k.setImageResource(R.drawable.ic_done_tick)
                imgP60to1L.setImageResource(R.drawable.ic_done_tick)
                imgP1Lto15L.setImageResource(R.drawable.ic_done_tick)
                imgP15Lto2L.setImageResource(R.drawable.ic_done_tick)
                imgp2lto3l.setImageResource(R.drawable.ic_done_tick)
                imgP3Lto4L.setImageResource(R.drawable.ic_done_tick)
                imgP4lplus.setImageResource(R.drawable.ic_done_tick)
            }

        }

        layP60to1L.setOnClickListener {
            if (pricerange == 4){
                imgP60to1L.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                imgP60to1L.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 4
                imgBelow15k.setImageResource(R.drawable.ic_done_tick)
                imgP15to30k.setImageResource(R.drawable.ic_done_tick)
                imgP30to60k.setImageResource(R.drawable.ic_done_tick)
                imgP1Lto15L.setImageResource(R.drawable.ic_done_tick)
                imgP15Lto2L.setImageResource(R.drawable.ic_done_tick)
                imgp2lto3l.setImageResource(R.drawable.ic_done_tick)
                imgP3Lto4L.setImageResource(R.drawable.ic_done_tick)
                imgP4lplus.setImageResource(R.drawable.ic_done_tick)
            }

        }

        layP1Lto15L.setOnClickListener {
            if (pricerange == 5){
                imgP1Lto15L.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                imgP1Lto15L.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 5
                imgBelow15k.setImageResource(R.drawable.ic_done_tick)
                imgP15to30k.setImageResource(R.drawable.ic_done_tick)
                imgP30to60k.setImageResource(R.drawable.ic_done_tick)
                imgP60to1L.setImageResource(R.drawable.ic_done_tick)
                imgP15Lto2L.setImageResource(R.drawable.ic_done_tick)
                imgp2lto3l.setImageResource(R.drawable.ic_done_tick)
                imgP3Lto4L.setImageResource(R.drawable.ic_done_tick)
                imgP4lplus.setImageResource(R.drawable.ic_done_tick)
            }

        }

        layP15Lto2L.setOnClickListener {
            if (pricerange == 6){
                imgP15Lto2L.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                imgP15Lto2L.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 6
                imgBelow15k.setImageResource(R.drawable.ic_done_tick)
                imgP15to30k.setImageResource(R.drawable.ic_done_tick)
                imgP30to60k.setImageResource(R.drawable.ic_done_tick)
                imgP60to1L.setImageResource(R.drawable.ic_done_tick)
                imgP1Lto15L.setImageResource(R.drawable.ic_done_tick)
                imgp2lto3l.setImageResource(R.drawable.ic_done_tick)
                imgP3Lto4L.setImageResource(R.drawable.ic_done_tick)
                imgP4lplus.setImageResource(R.drawable.ic_done_tick)
            }

        }

        layp2lto3l.setOnClickListener {
            if (pricerange == 7){
                imgp2lto3l.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                imgp2lto3l.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 7
                imgBelow15k.setImageResource(R.drawable.ic_done_tick)
                imgP15to30k.setImageResource(R.drawable.ic_done_tick)
                imgP30to60k.setImageResource(R.drawable.ic_done_tick)
                imgP60to1L.setImageResource(R.drawable.ic_done_tick)
                imgP1Lto15L.setImageResource(R.drawable.ic_done_tick)
                imgP15Lto2L.setImageResource(R.drawable.ic_done_tick)
                imgP3Lto4L.setImageResource(R.drawable.ic_done_tick)
                imgP4lplus.setImageResource(R.drawable.ic_done_tick)
            }

        }

        layP3Lto4L.setOnClickListener {
            if (pricerange == 8){
                imgP3Lto4L.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                imgP3Lto4L.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 8
                imgBelow15k.setImageResource(R.drawable.ic_done_tick)
                imgP15to30k.setImageResource(R.drawable.ic_done_tick)
                imgP30to60k.setImageResource(R.drawable.ic_done_tick)
                imgP60to1L.setImageResource(R.drawable.ic_done_tick)
                imgP1Lto15L.setImageResource(R.drawable.ic_done_tick)
                imgP15Lto2L.setImageResource(R.drawable.ic_done_tick)
                imgp2lto3l.setImageResource(R.drawable.ic_done_tick)
                imgP4lplus.setImageResource(R.drawable.ic_done_tick)
            }

        }

        layP4Lplus.setOnClickListener {
            if (pricerange == 9){
                imgP4lplus.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                imgP4lplus.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 9
                imgBelow15k.setImageResource(R.drawable.ic_done_tick)
                imgP15to30k.setImageResource(R.drawable.ic_done_tick)
                imgP30to60k.setImageResource(R.drawable.ic_done_tick)
                imgP60to1L.setImageResource(R.drawable.ic_done_tick)
                imgP1Lto15L.setImageResource(R.drawable.ic_done_tick)
                imgP15Lto2L.setImageResource(R.drawable.ic_done_tick)
                imgp2lto3l.setImageResource(R.drawable.ic_done_tick)
                imgP3Lto4L.setImageResource(R.drawable.ic_done_tick)
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
            var buySaleBody = BuySaleTypeBody(user_id!!,machinary_type,machinary_sort,pricerange,latitude!!,longitude!!)
            homeViewModel.accessBuySaleMachine(buySaleBody).observe(this, observer)
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

