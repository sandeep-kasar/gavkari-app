package com.gavkariapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.FileProvider
import com.gavkariapp.BuildConfig
import com.gavkariapp.Model.*
import com.gavkariapp.R
import com.gavkariapp.adapter.MainSliderAdapter
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant
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
import com.gavkariapp.constant.AppConstant.MACHINERY
import com.gavkariapp.constant.AppConstant.MALE_BUFFALO
import com.gavkariapp.constant.AppConstant.MALE_GOAT
import com.gavkariapp.constant.AppConstant.MARATHI
import com.gavkariapp.constant.AppConstant.OTHER_DOMESTIC_ANIMALS
import com.gavkariapp.constant.AppConstant.OX
import com.gavkariapp.constant.AppConstant.PICKUP
import com.gavkariapp.constant.AppConstant.PLOUGH
import com.gavkariapp.constant.AppConstant.PUBLISHED
import com.gavkariapp.constant.AppConstant.ROTOVATOR
import com.gavkariapp.constant.AppConstant.SEED_DRILL
import com.gavkariapp.constant.AppConstant.SPRAY_BLOWER
import com.gavkariapp.constant.AppConstant.SPRAY_PUMP
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
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import com.gavkariapp.service.GlideImageLoadingService
import com.gavkariapp.utility.Util
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.AppBarLayout
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_buy_sale_detail.*
import kotlinx.android.synthetic.main.layout_content_buysale.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ss.com.bannerslider.Slider
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference

class BuySaleDetailActivity : BaseActivity(), OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, View.OnClickListener {

    lateinit var buysaleData : BuySale

    private lateinit var mMap: GoogleMap

    private lateinit var mobile: String
    
    private lateinit var  userId :String

    private var  status = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_buy_sale_detail)
        Slider.init(GlideImageLoadingService(applicationContext))
        setUpView()
    }

    fun setUpView(){
        setupToolbar(R.id.toolbarHome, getString(R.string.lbl_ad_details))
        initCollapsingToolbar()
        buysaleData = intent.getSerializableExtra("buySaleData") as BuySale
        setData()
        layShare.setOnClickListener(this)
        mobCall.setOnClickListener(this)
        imgWhatsApp.setOnClickListener(this)
        imgFav.setOnClickListener(this)
        imgFavFill.setOnClickListener(this)
    }

    private fun setData(){

        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        val userLang = prefs[AppConstant.LANGUAGE, "-1"]!!
        userId = prefs[ApiConstant.USER_ID, "0"]!!

        getBuySaleDetails(buysaleData.id,buysaleData.user_id)

        var villageName = ""

        if (userLang == ENGLISH) {
            villageName = buysaleData.village_en
        }
        if (userLang == MARATHI) {
            villageName =  buysaleData.village_mr
        }
        tvAddress.text = villageName
        tvPrice.text = buysaleData.price

        val distance = String.format("%.0f", buysaleData.distance)
        tvDistance.text = "( " + distance +" " + getString(R.string.lbl_km) + " )"

        if (buysaleData.distance == 0.0){
            tvDistance.visibility = View.GONE
        }

        if (buysaleData.title.isNotEmpty()){
            tvTitle.text = buysaleData.title
        }else{
            tvTitle.visibility = View.GONE
        }

        if (buysaleData.description.isNotEmpty()){
            tvDescription.text = buysaleData.description
        }else{
            layDescription.visibility = View.GONE
            tvDescriptionLine.visibility = View.GONE
        }

        var inputDate = Util.getFormatedDate(buysaleData.created_at,
                "yyyy-MM-dd", "d MMMM",resources)

        tvDate.text = inputDate

        tvName.text = getString(R.string.lbl_selling_entiry)
        tvName1.text = buysaleData.name

        if (buysaleData.fromActivity == "MySaleAdActivity"){
            tvDistance.visibility = View.INVISIBLE
        }

        status = buysaleData.status

        if (buysaleData.status == PUBLISHED){
            layShare.visibility = View.VISIBLE

        }else{
            layShare.visibility = View.GONE
        }

        when {
            buysaleData.tab_type == ANIMAL -> {

                if (buysaleData.type == COW){

                    tvOne.text = getString(R.string.lbl_breed)
                    tvOne1.text = buysaleData.breed

                    tvTwo.text = getString(R.string.lbl_milk)
                    if (buysaleData.milk == 0){
                        tvTwo2.text = getString(R.string.lbl_no_milk)
                    }else{
                        tvTwo2.text = buysaleData.milk.toString() +" "+ getString(R.string.lbl_milk_liter)
                    }

                    tvThree.text = getString(R.string.lbl_preg_status)
                    if (buysaleData.pregnancy_status == 0){
                        tvThree3.text = getString(R.string.lbl_not_pregnant)
                    }else{
                        tvThree3.text = getCount(buysaleData.pregnancy_status)
                    }

                    tvFour.text = getString(R.string.lbl_preg)
                    tvFour4.text = buysaleData.pregnancies_count.toString() + " " + getString(R.string.lbl_times)

                }else if (buysaleData.type == BUFFALO ){

                    tvTwo.text = getString(R.string.lbl_milk)
                    if (buysaleData.milk == 0){
                        tvTwo2.text = getString(R.string.lbl_no_milk)
                    }else{
                        tvTwo2.text = buysaleData.milk.toString() +" "+ getString(R.string.lbl_milk_liter)
                    }

                    tvThree.text = getString(R.string.lbl_preg_status)
                    if (buysaleData.pregnancy_status == 0){
                        tvThree3.text = getString(R.string.lbl_not_pregnant)
                    }else{
                        tvThree3.text = getCount(buysaleData.pregnancy_status)
                    }

                    tvFour.text = getString(R.string.lbl_preg)
                    tvFour4.text = buysaleData.pregnancies_count.toString() + " " + getString(R.string.lbl_times)

                    layOne.visibility = View.GONE

                }else if (buysaleData.type == HEIFER_COW){

                    tvOne.text = getString(R.string.lbl_breed)
                    tvOne1.text = buysaleData.breed

                    tvTwo.text = getString(R.string.lbl_milk)
                    if (buysaleData.milk == 0){
                        tvTwo2.text = getString(R.string.lbl_no_milk)
                    }else{
                        tvTwo2.text = buysaleData.milk.toString() +" "+ getString(R.string.lbl_milk_liter)
                    }

                    tvThree.text = getString(R.string.lbl_preg_status)
                    if (buysaleData.pregnancy_status == 0){
                        tvThree3.text = getString(R.string.lbl_not_pregnant)
                    }else{
                        tvThree3.text = getCount(buysaleData.pregnancy_status)
                    }

                    layFour.visibility = View.GONE

                }else if (buysaleData.type == HEIFER_BUFFALO){

                    tvTwo.text = getString(R.string.lbl_milk)
                    if (buysaleData.milk == 0){
                        tvTwo2.text = getString(R.string.lbl_no_milk)
                    }else{
                        tvTwo2.text = buysaleData.milk.toString() +" "+ getString(R.string.lbl_milk_liter)
                    }

                    tvThree.text = getString(R.string.lbl_preg_status)
                    if (buysaleData.pregnancy_status == 0){
                        tvThree3.text = getString(R.string.lbl_not_pregnant)
                    }else{
                        tvThree3.text = getCount(buysaleData.pregnancy_status)
                    }

                    layOne.visibility = View.GONE
                    layFour.visibility = View.GONE

                }else if (buysaleData.type == OX || buysaleData.type == MALE_BUFFALO){

                    layOne.visibility = View.GONE
                    layTwo.visibility = View.GONE
                    layThree.visibility = View.GONE
                    layFour.visibility = View.GONE

                }else if (buysaleData.type == GOAT){

                    layOne.visibility = View.GONE
                    layFour.visibility = View.GONE

                    tvTwo.text = getString(R.string.lbl_milk)
                    if (buysaleData.milk == 0){
                        tvTwo2.text = getString(R.string.lbl_no_milk)
                    }else{
                        tvTwo2.text = buysaleData.milk.toString() +" "+ getString(R.string.lbl_milk_liter)
                    }

                    tvThree.text = getString(R.string.lbl_preg_status)
                    if (buysaleData.pregnancy_status == 0){
                        tvThree3.text = getString(R.string.lbl_not_pregnant)
                    }else{
                        tvThree3.text = getCount(buysaleData.pregnancy_status)
                    }

                }else if (buysaleData.type == MALE_GOAT){

                    layOne.visibility = View.GONE
                    layTwo.visibility = View.GONE
                    layThree.visibility = View.GONE

                    tvFour.text = getString(R.string.lbl_weight)
                    tvFour4.text = buysaleData.weight + " " + getString(R.string.lbl_kg)

                }else if (buysaleData.type == OTHER_DOMESTIC_ANIMALS){
                    layOne.visibility = View.GONE
                    layTwo.visibility = View.GONE
                    layThree.visibility = View.GONE
                    layFour.visibility = View.GONE

                }else{
                    layOne.visibility = View.GONE
                    layTwo.visibility = View.GONE
                    layThree.visibility = View.GONE
                    layFour.visibility = View.GONE

                }

            }
            buysaleData.tab_type == MACHINERY -> {

                if (buysaleData.type == TRACTOR || buysaleData.type == PICKUP || buysaleData.type == TEMPO_TRUCK ||
                        buysaleData.type == CAR || buysaleData.type == TWO_WHEELER){

                    tvOne.text = getString(R.string.lbl_companny)
                    tvOne1.text = buysaleData.company

                    tvTwo.text = getString(R.string.lbl_buying_year)
                    tvTwo2.text = buysaleData.year

                    tvThree.text = getString(R.string.km_driven)
                    tvThree3.text = buysaleData.km_driven

                    if (buysaleData.model.isNullOrEmpty()){
                        layFour.visibility = View.GONE
                    }else{
                        tvFour.text = getString(R.string.lbl_model)
                        tvFour4.text = buysaleData.model
                    }


                }else if (buysaleData.type == THRESHER || buysaleData.type == KUTTI_MACHINE){

                    tvOne.text = getString(R.string.lbl_companny)
                    tvOne1.text = buysaleData.company

                    tvTwo.text = getString(R.string.lbl_buying_year)
                    tvTwo2.text = buysaleData.year

                    if (buysaleData.power.isNullOrEmpty()){
                        layThree.visibility = View.GONE
                    }else{
                        tvThree.text = getString(R.string.lbl_power)
                        tvThree3.text = buysaleData.power
                    }

                    layFour.visibility = View.GONE

                }else if (buysaleData.type == SPRAY_BLOWER){
                    tvOne.text = getString(R.string.lbl_companny)
                    tvOne1.text = buysaleData.company

                    tvTwo.text = getString(R.string.lbl_buying_year)
                    tvTwo2.text = buysaleData.year

                    if (buysaleData.capacity.isNullOrEmpty()){
                        layThree.visibility = View.GONE
                    }else{
                        tvThree.text = getString(R.string.lbl_capacity)
                        tvThree3.text = buysaleData.capacity
                    }

                    layFour.visibility = View.GONE

                }else{
                    tvOne.text = getString(R.string.lbl_companny)
                    tvOne1.text = buysaleData.company

                    tvTwo.text = getString(R.string.lbl_buying_year)
                    tvTwo2.text = buysaleData.year

                    layThree.visibility = View.GONE
                    layFour.visibility = View.GONE
                }

            }
            buysaleData.tab_type == EQUIPMENTS -> {

                if (buysaleData.type == CULTIVATOR || buysaleData.type == SEED_DRILL){

                    tvOne.text = getString(R.string.lbl_companny)
                    tvOne1.text = buysaleData.company

                    tvTwo.text = getString(R.string.lbl_buying_year)
                    tvTwo2.text = buysaleData.year

                    if (buysaleData.material.isNullOrEmpty()){
                        layThree.visibility = View.GONE
                    }else{
                        tvThree.text = getString(R.string.lbl_material)
                        tvThree3.text = buysaleData.material
                    }

                    if (buysaleData.tynes_count.isNullOrEmpty()){
                        layFour.visibility = View.GONE
                    }else{
                        tvFour.text = getString(R.string.lbl_no_of_tynes)
                        tvFour4.text = buysaleData.tynes_count
                    }

                }else if (buysaleData.type == ROTOVATOR){

                    tvOne.text = getString(R.string.lbl_companny)
                    tvOne1.text = buysaleData.company

                    tvTwo.text = getString(R.string.lbl_buying_year)
                    tvTwo2.text = buysaleData.year

                    if (buysaleData.material.isNullOrEmpty()){
                        layThree.visibility = View.GONE
                    }else{
                        tvThree.text = getString(R.string.lbl_material)
                        tvThree3.text = buysaleData.material
                    }

                    layFour.visibility = View.GONE

                }else if (buysaleData.type == PLOUGH){

                    tvOne.text = getString(R.string.lbl_companny)
                    tvOne1.text = buysaleData.company

                    tvTwo.text = getString(R.string.lbl_buying_year)
                    tvTwo2.text = buysaleData.year

                    if (buysaleData.material.isNullOrEmpty()){
                        layThree.visibility = View.GONE
                    }else{
                        tvThree.text = getString(R.string.lbl_material)
                        tvThree3.text = buysaleData.material
                    }

                    if (buysaleData.weight.isNullOrEmpty()){
                        layFour.visibility = View.GONE
                    }else{
                        tvFour.text = getString(R.string.lbl_weight)
                        tvFour4.text = buysaleData.weight + " " + getString(R.string.lbl_kg)
                    }

                }else if (buysaleData.type == TROLLEY){
                    tvOne.text = getString(R.string.lbl_companny)
                    tvOne1.text = buysaleData.company

                    tvTwo.text = getString(R.string.lbl_buying_year)
                    tvTwo2.text = buysaleData.year

                    if (buysaleData.capacity.isNullOrEmpty()){
                        layThree.visibility = View.GONE
                    }else{
                        tvThree.text = getString(R.string.lbl_capacity)
                        tvThree3.text = buysaleData.capacity
                    }

                    if (buysaleData.size.isNullOrEmpty()){
                        layFour.visibility = View.GONE
                    }else{
                        tvFour.text = getString(R.string.lbl_size)
                        tvFour4.text = buysaleData.size
                    }

                }else if (buysaleData.type == WATER_MOTOR_PUMP){
                    tvOne.text = getString(R.string.lbl_companny)
                    tvOne1.text = buysaleData.company

                    tvTwo.text = getString(R.string.lbl_buying_year)
                    tvTwo2.text = buysaleData.year


                    if (buysaleData.power.isNullOrEmpty()){
                        layThree.visibility = View.GONE
                    }else{
                        tvThree.text = getString(R.string.lbl_power)
                        tvThree3.text = buysaleData.power
                    }

                    if (buysaleData.phase.isNullOrEmpty()){
                        layFour.visibility = View.GONE
                    }else{
                        tvFour.text = getString(R.string.lbl_phase)
                        tvFour4.text = buysaleData.phase
                    }

                }else if (buysaleData.type == SPRAY_PUMP ){

                    tvOne.text = getString(R.string.lbl_companny)
                    tvOne1.text = buysaleData.company

                    tvTwo.text = getString(R.string.lbl_buying_year)
                    tvTwo2.text = buysaleData.year

                    if (buysaleData.capacity.isNullOrEmpty()){
                        layThree.visibility = View.GONE
                    }else{
                        tvThree.text = getString(R.string.lbl_capacity)
                        tvThree3.text = buysaleData.capacity
                    }

                    layFour.visibility = View.GONE

                }else if (buysaleData.type == IRRIGATION_MATERIAL ){

                    tvOne.text = getString(R.string.lbl_companny)
                    tvOne1.text = buysaleData.company

                    tvTwo.text = getString(R.string.lbl_buying_year)
                    tvTwo2.text = buysaleData.year

                    layThree.visibility = View.GONE
                    layFour.visibility = View.GONE

                }else if (buysaleData.type == STEEL){
                    tvOne.text = getString(R.string.lbl_companny)
                    tvOne1.text = buysaleData.company

                    tvTwo.text = getString(R.string.lbl_buying_year)
                    tvTwo2.text = buysaleData.year

                    if (buysaleData.material.isNullOrEmpty()){
                        layThree.visibility = View.GONE
                    }else{
                        tvThree.text = getString(R.string.lbl_material)
                        tvThree3.text = buysaleData.material
                    }

                    if (buysaleData.weight.isNullOrEmpty()){
                        layFour.visibility = View.GONE
                    }else{
                        tvFour.text = getString(R.string.lbl_weight)
                        tvFour4.text = buysaleData.weight + " " + getString(R.string.lbl_kg)
                    }

                }else {

                    tvOne.text = getString(R.string.lbl_companny)
                    tvOne1.text = buysaleData.company

                    tvTwo.text = getString(R.string.lbl_buying_year)
                    tvTwo2.text = buysaleData.year

                    layThree.visibility = View.GONE
                    layFour.visibility = View.GONE
                }

            }
            else -> {

            }
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getCount(number:Int) : String{
        return when (number) {
            1 -> getString(R.string.lbl_first_m)
            2 -> getString(R.string.lbl_second_m)
            3 -> getString(R.string.lbl_third)
            4 -> getString(R.string.lbl_fourth)
            5 -> getString(R.string.lbl_fifth)
            6 -> getString(R.string.lbl_sixth)
            7 -> getString(R.string.lbl_seven)
            8 -> getString(R.string.lbl_eight)
            9 -> getString(R.string.lbl_nine)
            10 -> getString(R.string.lbl_ten)
            else -> ""
        }
    }

    private fun getBuySaleDetails(buySaleId:String,user_id:String) {
            var buySaleMediaBody = BuySaleMediaBody(buySaleId,user_id,userId)
            ApiClient.get().create(ApiInterface::class.java)
                    .getBuySaleMedia(buySaleMediaBody)
                    .enqueue(object : Callback<BuySaleMedia> {
                        override fun onResponse(call: Call<BuySaleMedia>?, response: Response<BuySaleMedia>?) {

                            if (response!!.code() == 200) {
                                var response = response!!.body()
                                if (response?.status == HttpConstant.SUCCESS) {
                                    runOnUiThread {
                                        var imageList= response.photos
                                        slider.setAdapter(MainSliderAdapter(imageList, HttpConstant.BASE_BUYSALE_DOWNLOAD_URL))

                                        var user = response.user
                                        layUser.visibility = View.VISIBLE
                                        tvUserName.text = user.name
                                        tvMobile.text = user.mobile

                                        mobile = user.mobile

                                        var fav = response.fav
                                        if(fav.isNotEmpty()){
                                            imgFav.visibility = View.GONE
                                            imgFavFill.visibility = View.VISIBLE
                                        }else{
                                            imgFavFill.visibility = View.GONE
                                            imgFav.visibility = View.VISIBLE
                                        }

                                        layContact.visibility = View.VISIBLE
                                    }
                                }else{
                                    tvDescriptionLine.visibility = View.GONE
                                    layUser.visibility = View.GONE
                                    layContact.visibility = View.GONE
                                }

                            }
                        }

                        override fun onFailure(call: Call<BuySaleMedia>?, t: Throwable?) {
                            runOnUiThread {
                                showError(getString(R.string.warning_try_later))
                            }
                        }
                    })
    }

    override fun onClick(v: View?) {
        when(v){
            layShare -> {
                checkGalleryPermission()
            }

            mobCall ->{
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile))
                startActivity(intent)
            }

            imgFav ->{
                addFav(1)
            }

            imgFavFill ->{
                addFav(2)
            }

            imgWhatsApp ->{
                if (mobile.isNotEmpty()){
                    val uri = Uri.parse("https://api.whatsapp.com/send?phone=91$mobile&text=Hello")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }
        }
    }

    private fun addFav(type : Int){


        var addFavBody = AddFavBody(buysaleData.id,userId,type)

        ApiClient.get().create(ApiInterface::class.java)
                .deleteFav(addFavBody)
                .enqueue(object : Callback<CommonResponse> {
                    override fun onResponse(call: Call<CommonResponse>?, response: Response<CommonResponse>?) {
                        if (response!!.code() == 200) {

                            var commonResponse = response.body() as CommonResponse
                            if (commonResponse.status == HttpConstant.SUCCESS){
                                runOnUiThread {
                                    if (addFavBody.type == 1){
                                        imgFav.visibility = View.GONE
                                        imgFavFill.visibility = View.VISIBLE
                                    }else{
                                        imgFavFill.visibility = View.GONE
                                        imgFav.visibility = View.VISIBLE
                                    }
                                }
                            }
                        } else {
                            if (response.errorBody() != null) {
                                runOnUiThread {
                                    showError(response.errorBody().toString())
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<CommonResponse>?, t: Throwable?) {
                        runOnUiThread {
                            showError(t.toString())
                        }
                    }
                })
    }

    private fun initCollapsingToolbar() {

        collapsing_toolbar.title = " "
        appbar.setExpanded(true)

        // hiding & showing the title when toolbar expanded & collapsed
        appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            internal var isShow = false
            internal var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsing_toolbar.title = getString(R.string.lbl_ad_details)
                    isShow = true
                } else if (isShow) {
                    collapsing_toolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val addressLocal = LatLng(buysaleData.latitude.toDouble(), buysaleData.longitude.toDouble())
        val cameraPosition = CameraPosition.Builder().target(addressLocal).zoom(15f).build()
        mMap.addMarker(MarkerOptions().position(addressLocal).title(buysaleData.village_mr))
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        mMap.uiSettings.isMapToolbarEnabled = true
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.event_details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item?.itemId) {
            R.id.action_share -> {
                if (status == PUBLISHED){
                    checkGalleryPermission()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkGalleryPermission() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            MyAsyncTask(this@BuySaleDetailActivity,layBuySale).execute()
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // permission is denied permenantly, navigate user to app settings
                            Util.showPermissionAlert(this@BuySaleDetailActivity!!,
                                    getString(R.string.message_enable_permission), getString(R.string.app_name))
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
    }

    companion object {
        class MyAsyncTask internal constructor(context: BuySaleDetailActivity,layout: CoordinatorLayout) : AsyncTask<Int, String, String?>() {

            var content= layout
            private val activityReference: WeakReference<BuySaleDetailActivity> = WeakReference(context)
            val activity = activityReference.get()
            override fun onPreExecute() {
                if (activity == null || activity.isFinishing) return
                (activity as BuySaleDetailActivity).showProgress()
            }


            @SuppressLint("WrongThread")
            override fun doInBackground(vararg params: Int?): String? {

                content?.isDrawingCacheEnabled = true
                var bitmap = content?.drawingCache
                val root = Environment.getExternalStorageDirectory()
                val cachePath = File(root.absolutePath + "/DCIM/Camera/image.jpg")

                try {
                    cachePath.createNewFile()
                    val ostream = FileOutputStream(cachePath)
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, ostream)
                    ostream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                var uri: Uri? = null
                uri = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    FileProvider.getUriForFile(activity!!.application, BuildConfig.APPLICATION_ID+".provider",
                            cachePath)
                } else {
                    Uri.fromFile(cachePath)
                }

                val share = Intent(Intent.ACTION_SEND)
                share.putExtra(Intent.EXTRA_STREAM, uri)
                share.putExtra(Intent.EXTRA_TEXT,
                        activity?.getString(R.string.msg_share_more_info) + "\n"
                                + activity?.getString(R.string.url_gavkari_app_play_store));
                share.type = "image/*"
                activity?.startActivity(Intent.createChooser(share, activity?.getString(R.string.tittle_share_via)))

                return "done"
            }


            override fun onPostExecute(result: String?) {
                if (activity == null || activity.isFinishing) return
                activity.dismissProgress()
            }

        }
    }
}

