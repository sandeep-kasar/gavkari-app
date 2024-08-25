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
import com.gavkariapp.constant.AppConstant.BUFFALO
import com.gavkariapp.constant.AppConstant.COW
import com.gavkariapp.constant.AppConstant.GOAT
import com.gavkariapp.constant.AppConstant.HEIFER_BUFFALO
import com.gavkariapp.constant.AppConstant.HEIFER_COW
import com.gavkariapp.constant.AppConstant.MALE_BUFFALO
import com.gavkariapp.constant.AppConstant.MALE_GOAT
import com.gavkariapp.constant.AppConstant.OTHER_DOMESTIC_ANIMALS
import com.gavkariapp.constant.AppConstant.OX
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_animal.*


class AnimalActivity : BaseActivity(),BuySaleAdapter.OnItemClickListener,
        View.OnClickListener {

    lateinit var buySaleAdapter: BuySaleAdapter

    lateinit var homeViewModel: HomeViewModel

    lateinit var observer: Observer<ApiResponse<BuySaleResponse, String>>

    var animal_type: Int = 0

    var animal_sort: Int = 0

    var pricerange: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_animal)
        setupView()
    }

    fun setupView(){
        homeViewModel = HomeViewModel()
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
        buysale.fromActivity = "AnimalActivity"
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
                startActivity(Intent(applicationContext, MyFavAdActivity::class.java) .putExtra("tab_type",1))
            }

            imgCreateAd ->{
                startActivity(Intent(applicationContext, SelectBuySaleAdTypeActivity::class.java)
                        .putExtra("adType", AppConstant.ANIMAL))
            }
        }
    }

    fun openDialog(){
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_buy_sale_filter_animal)

        val tvAllAnimal = dialog.findViewById(R.id.tvAllAnimal) as TextView
        val tvCow = dialog.findViewById(R.id.tvCow) as TextView
        val tvBuffalo = dialog.findViewById(R.id.tvBuffalo) as TextView
        val tvHeiferCow = dialog.findViewById(R.id.tvHeiferCow) as TextView
        val tvHeiferBuffalo = dialog.findViewById(R.id.tvHeiferBuffalo) as TextView
        val tvOx = dialog.findViewById(R.id.tvOx) as TextView
        val tvMaleBuffalo = dialog.findViewById(R.id.tvMaleBuffalo) as TextView
        val tvGoat = dialog.findViewById(R.id.tvGoat) as TextView
        val tvMaleGoat = dialog.findViewById(R.id.tvMaleGoat) as TextView
        val tvDomesticAnimal = dialog.findViewById(R.id.tvDomesticAnimal) as TextView

        tvAllAnimal.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvAllAnimal.text.toString()
            animal_type = ALL
            accessData()
        }

        tvCow.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvCow.text.toString()
            animal_type = COW
            accessData()
        }

        tvBuffalo.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvBuffalo.text.toString()
            animal_type = BUFFALO
            accessData()
        }

        tvHeiferCow.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvHeiferCow.text.toString()
            animal_type = HEIFER_COW
            accessData()
        }

        tvHeiferBuffalo.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvHeiferBuffalo.text.toString()
            animal_type = HEIFER_BUFFALO
            accessData()
        }

        tvOx.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvOx.text.toString()
            animal_type = OX
            accessData()
        }

        tvMaleBuffalo.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvMaleBuffalo.text.toString()
            animal_type = MALE_BUFFALO
            accessData()
        }

        tvGoat.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvGoat.text.toString()
            animal_type = GOAT
            accessData()
        }

        tvMaleGoat.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvMaleGoat.text.toString()
            animal_type = MALE_GOAT
            accessData()
        }

        tvDomesticAnimal.setOnClickListener { dialog.dismiss()
            tvSelectType.text = tvDomesticAnimal.text.toString()
            animal_type = OTHER_DOMESTIC_ANIMALS
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
            animal_sort = 1
        }
        rdBtnTwo.setOnClickListener {
            animal_sort = 2
        }
        rdBtnThree.setOnClickListener {
            animal_sort = 3
        }
        rdBtnFour.setOnClickListener {
            animal_sort = 4
        }

        tvCancel.setOnClickListener {
            dialog.cancel()
            animal_sort = 0
        }
        tvApply.setOnClickListener {
            dialog.cancel()
            if (animal_sort > 0) {
                accessData()
            }
        }

        if (animal_sort == 1){
            rdBtnOne.isChecked = true
        }else if (animal_sort == 2){
            rdBtnTwo.isChecked = true
        }else if (animal_sort == 3){
            rdBtnThree.isChecked = true
        }else if (animal_sort == 4){
            rdBtnFour.isChecked = true
        }else{
        }

        dialog.show()
    }

    fun filterDialog(){
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_animal_filter)
        dialog.show()

        val layBelow30k = dialog.findViewById(R.id.layBelow30k) as LinearLayout
        val layP3050k = dialog.findViewById(R.id.layP3050k) as LinearLayout
        val layP5060k = dialog.findViewById(R.id.layP5070k) as LinearLayout
        val layP7090k = dialog.findViewById(R.id.layP7090k) as LinearLayout
        val layP90to1L = dialog.findViewById(R.id.layP90to1L) as LinearLayout
        val layP1Lto12L = dialog.findViewById(R.id.layP1Lto12L) as LinearLayout

        val imgBelow30K = dialog.findViewById(R.id.imgBelow30K) as ImageView
        val imgP3050k = dialog.findViewById(R.id.imgP3050k) as ImageView
        val imgP5070k = dialog.findViewById(R.id.imgP5070k) as ImageView
        val imgP7090k = dialog.findViewById(R.id.imgP7090k) as ImageView
        val imgP90to1L = dialog.findViewById(R.id.imgP90to1L) as ImageView
        val imgP1Lto12L = dialog.findViewById(R.id.imgP1Lto12L) as ImageView

        val tvCancel = dialog.findViewById(R.id.tvCancel) as TextView
        val tvApply = dialog.findViewById(R.id.tvApply) as TextView

        tvCancel.setOnClickListener {
            dialog.cancel()
            pricerange = 0
            imgBelow30K.setImageResource(R.drawable.ic_done_tick)
            imgP3050k.setImageResource(R.drawable.ic_done_tick)
            imgP5070k.setImageResource(R.drawable.ic_done_tick)
            imgP7090k.setImageResource(R.drawable.ic_done_tick)
            imgP90to1L.setImageResource(R.drawable.ic_done_tick)
            imgP1Lto12L.setImageResource(R.drawable.ic_done_tick)
            accessData()
        }
        tvApply.setOnClickListener {
            dialog.cancel()
            accessData()
        }

        when (pricerange) {
            1 -> imgBelow30K.setImageResource(R.drawable.ic_done_tick_green)
            2 -> imgP3050k.setImageResource(R.drawable.ic_done_tick_green)
            3 -> imgP5070k.setImageResource(R.drawable.ic_done_tick_green)
            4 -> imgP7090k.setImageResource(R.drawable.ic_done_tick_green)
            5 -> imgP90to1L.setImageResource(R.drawable.ic_done_tick_green)
            6 -> imgP1Lto12L.setImageResource(R.drawable.ic_done_tick_green)
            else -> {}
        }

        layBelow30k.setOnClickListener {
            if (pricerange == 1){
                imgBelow30K.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                imgBelow30K.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 1
                imgP3050k.setImageResource(R.drawable.ic_done_tick)
                imgP5070k.setImageResource(R.drawable.ic_done_tick)
                imgP7090k.setImageResource(R.drawable.ic_done_tick)
                imgP90to1L.setImageResource(R.drawable.ic_done_tick)
                imgP1Lto12L.setImageResource(R.drawable.ic_done_tick)
            }
        }

        layP3050k.setOnClickListener {
            if (pricerange == 2){
                imgP3050k.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                imgP3050k.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 2
                imgBelow30K.setImageResource(R.drawable.ic_done_tick)
                imgP5070k.setImageResource(R.drawable.ic_done_tick)
                imgP7090k.setImageResource(R.drawable.ic_done_tick)
                imgP90to1L.setImageResource(R.drawable.ic_done_tick)
                imgP1Lto12L.setImageResource(R.drawable.ic_done_tick)
            }
        }

        layP5060k.setOnClickListener {
            if (pricerange == 3){
                imgP5070k.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                imgP5070k.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 3
                imgBelow30K.setImageResource(R.drawable.ic_done_tick)
                imgP3050k.setImageResource(R.drawable.ic_done_tick)
                imgP7090k.setImageResource(R.drawable.ic_done_tick)
                imgP90to1L.setImageResource(R.drawable.ic_done_tick)
                imgP1Lto12L.setImageResource(R.drawable.ic_done_tick)
            }

        }

        layP7090k.setOnClickListener {
            if (pricerange == 4){
                imgP7090k.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                imgP7090k.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 4
                imgBelow30K.setImageResource(R.drawable.ic_done_tick)
                imgP3050k.setImageResource(R.drawable.ic_done_tick)
                imgP5070k.setImageResource(R.drawable.ic_done_tick)
                imgP90to1L.setImageResource(R.drawable.ic_done_tick)
                imgP1Lto12L.setImageResource(R.drawable.ic_done_tick)
            }

        }

        layP90to1L.setOnClickListener {
            if (pricerange == 5){
                imgP90to1L.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                imgP90to1L.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 5
                imgBelow30K.setImageResource(R.drawable.ic_done_tick)
                imgP3050k.setImageResource(R.drawable.ic_done_tick)
                imgP5070k.setImageResource(R.drawable.ic_done_tick)
                imgP7090k.setImageResource(R.drawable.ic_done_tick)
                imgP1Lto12L.setImageResource(R.drawable.ic_done_tick)
            }

        }

        layP1Lto12L.setOnClickListener {
            if (pricerange == 6){
                imgP1Lto12L.setImageResource(R.drawable.ic_done_tick)
                pricerange = 0
            }else{
                imgP1Lto12L.setImageResource(R.drawable.ic_done_tick_green)
                pricerange = 6
                imgBelow30K.setImageResource(R.drawable.ic_done_tick)
                imgP3050k.setImageResource(R.drawable.ic_done_tick)
                imgP5070k.setImageResource(R.drawable.ic_done_tick)
                imgP7090k.setImageResource(R.drawable.ic_done_tick)
                imgP90to1L.setImageResource(R.drawable.ic_done_tick)
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
            var buySaleBody = BuySaleTypeBody(user_id!!,animal_type,animal_sort,pricerange,latitude!!,longitude!!)
            homeViewModel.accessBuySaleAnimal(buySaleBody).observe(this, observer)
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

