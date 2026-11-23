package com.gavkariapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.FileProvider
import com.gavkariapp.BuildConfig
import com.gavkariapp.Model.Design
import com.gavkariapp.Model.EventMediaResp
import com.gavkariapp.Model.MyEvent
import com.gavkariapp.Model.MyVillageEvent
import com.gavkariapp.R
import com.gavkariapp.adapter.MainSliderAdapter
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.AppConstant.DASHKRIYA_VIDHI
import com.gavkariapp.constant.AppConstant.ENGAGEMENT
import com.gavkariapp.constant.AppConstant.FIRST_MEMORIAL
import com.gavkariapp.constant.AppConstant.HOUSE_WARMING
import com.gavkariapp.constant.AppConstant.JAGARAN_GONDHAL
import com.gavkariapp.constant.AppConstant.PUBLISHED
import com.gavkariapp.constant.AppConstant.WEDDING
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.constant.HttpConstant.BASE_EVENT_DOWNLOAD_URL
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
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.layout_content_main_event.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ss.com.bannerslider.Slider
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference
import com.google.firebase.crashlytics.FirebaseCrashlytics



class EventDetailActivity : BaseActivity(), OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,View.OnClickListener {

    private lateinit var bundleData: Any

    private lateinit var mMap: GoogleMap

    private lateinit var myVillageEvent: MyVillageEvent

    private lateinit var myEvent: MyEvent

    private var eventId: String = "-1"

    private var latitude: Double = 0.0

    private var longitude: Double = 0.0

    private var address: String = ""

    private var photo = ""

    private var Title = ""

    private var SubTitle = ""

    private var EventDate = ""

    private var type = 0

    private var status = 0

    private lateinit var design: Design

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_event_detail)
        Slider.init(GlideImageLoadingService(applicationContext))
        setupViews()

    }

    private fun setupViews() {

        //set toolbar
        setupToolbar(R.id.toolbarHome, getString(R.string.title_event_details))

        //toolbar
        initCollapsingToolbar()

        //data
        bundleData = intent.getSerializableExtra("VillageResponse")


        //set data
        setData()

        tvContactNo.setOnClickListener(this)
        tvViewPatrika.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
            when(v){
                tvContactNo ->{call()}

                tvViewPatrika->{
                    startActivity(Intent(this,SingleDesignActivity::class.java)
                            .putExtra("event_obj", design))

                }
            }
    }

    private fun call(){
        var mobileNos= tvContactNo.text.toString().trim()
        val mobile = mobileNos.substring(0,Math.min(mobileNos.length,10))
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile))
        startActivity(intent)
    }

    private fun getEventMedia(eventId:String) {

        eventId?.let {
            ApiClient.get().create(ApiInterface::class.java)
                    .getEventMedia(it)
                    .enqueue(object : Callback<EventMediaResp> {
                        override fun onResponse(call: Call<EventMediaResp>?, response: Response<EventMediaResp>?) {

                            if (response!!.code() == 200) {
                                var response = response!!.body()
                                if (response?.status == HttpConstant.SUCCESS) {
                                    runOnUiThread {
                                        var imageList= response.photos
                                        slider.setAdapter(MainSliderAdapter(imageList,BASE_EVENT_DOWNLOAD_URL))
                                    }
                                }

                            }
                        }

                        override fun onFailure(call: Call<EventMediaResp>?, t: Throwable?) {
                            runOnUiThread {
                                showError(getString(R.string.warning_try_later))
                            }
                        }
                    })
        }
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
                    collapsing_toolbar.title = getString(R.string.title_event_details)
                    isShow = true
                } else if (isShow) {
                    collapsing_toolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    private fun setData() {

        if (bundleData is MyVillageEvent) {
            myVillageEvent = bundleData as MyVillageEvent


            if(myVillageEvent.status == "3"){
                tvViewPatrika.visibility = View.VISIBLE
                if (myVillageEvent.type == AppConstant.BIRTHDAY ||myVillageEvent.type == AppConstant.RETIREMENT ||myVillageEvent.type == AppConstant.SATYANARAYAN_POOJA ||
                        myVillageEvent.type == AppConstant.MAHAPRASAD ||myVillageEvent.type == AppConstant.OTHER_EVENT){
                    tvViewPatrika.visibility = View.GONE
                }else{
                    tvViewPatrika.visibility = View.VISIBLE
                }
            }else{
                tvViewPatrika.visibility = View.GONE
            }

            EventDate = Util.getFormatedDate(myVillageEvent.event_date,
                    "yyyy-MM-dd HH:mm:ss", "EEEE, MMMM d, HH:mm",resources).toString()
            tvEventType.text = myVillageEvent?.title
            tvFamily.text = myVillageEvent?.family
            tvDateTime.text = myVillageEvent?.muhurt
            tvAddress.text = myVillageEvent?.address
            tvContactNo.text = myVillageEvent?.contact_no
            tvTip.text = myVillageEvent?.note


            if(myVillageEvent.type== WEDDING || myVillageEvent.type== ENGAGEMENT){
                var subtitle = myVillageEvent.subtitle +" "+ myVillageEvent.subtitle_one +" "+getString(R.string.lbl_and)+" "+
                        myVillageEvent.subtitle_three +" "+myVillageEvent.subtitle_four +" "+getString(R.string.lbl_there_wedding)
                SubTitle =  subtitle
                tvEventSubTitle.text = subtitle
            }else{
                SubTitle =  myVillageEvent.subtitle
                tvEventSubTitle.text = myVillageEvent.subtitle
            }

            if(myVillageEvent.type == WEDDING || myVillageEvent.type == ENGAGEMENT ){
                tvRelInfo.text = getString(R.string.nimantrak)
            }


            if(myVillageEvent.type == DASHKRIYA_VIDHI || myVillageEvent.type == FIRST_MEMORIAL ){
                tvRelInfo.text = getString(R.string.vinit)
            }

            if(myVillageEvent.type == JAGARAN_GONDHAL || myVillageEvent.type == HOUSE_WARMING ){
                tvRelInfo.text = getString(R.string.snehankit)
            }

            tvRelInfoDetails.text = myVillageEvent.description

            tvMoreInfo.text = myVillageEvent.description_one

            latitude = myVillageEvent.latitude.toDouble()

            longitude = myVillageEvent.longitude.toDouble()

            address = myVillageEvent.address

            photo = myVillageEvent.photo

            Title = myVillageEvent.title

            eventId=myVillageEvent.id

            type  = myVillageEvent.type

            status = myVillageEvent.status.toInt()

            getEventMedia(eventId)

            design = Design(type,Title,myVillageEvent.family,myVillageEvent.muhurt,myVillageEvent.subtitle,
                    myVillageEvent.subtitle_one,myVillageEvent.subtitle_two,myVillageEvent.subtitle_three,
                    myVillageEvent.subtitle_four,myVillageEvent.subtitle_five,myVillageEvent.note,
                    myVillageEvent.description,myVillageEvent.description_one,myVillageEvent.address,myVillageEvent.photo)

        }

        if (bundleData is MyEvent) {

            myEvent = bundleData as MyEvent

            if(myEvent.status == PUBLISHED){
                tvViewPatrika.visibility = View.VISIBLE
                if (myEvent.type == AppConstant.BIRTHDAY ||myEvent.type == AppConstant.RETIREMENT ||myEvent.type == AppConstant.SATYANARAYAN_POOJA ||
                        myEvent.type == AppConstant.MAHAPRASAD ||myEvent.type == AppConstant.OTHER_EVENT){
                    tvViewPatrika.visibility = View.GONE
                }else{
                    tvViewPatrika.visibility = View.VISIBLE
                }
            }else{
                tvViewPatrika.visibility = View.GONE
            }

            EventDate = Util.getFormatedDate(myEvent.event_date,
                    "yyyy-MM-dd HH:mm:ss", "EEEE, MMMM d, HH:mm",resources).toString()
            tvEventType.text = myEvent?.title
            tvFamily.text = myEvent?.family
            tvDateTime.text = myEvent?.muhurt
            tvAddress.text = myEvent?.address
            tvContactNo.text = myEvent?.contact_no
            tvTip.text = myEvent?.note

            if(myEvent.type== WEDDING || myEvent.type== ENGAGEMENT){
                var subtitle = myEvent.subtitle +" "+ myEvent.subtitle_one +" "+getString(R.string.lbl_and)+" "+
                        myEvent.subtitle_three +" "+myEvent.subtitle_four +" "+getString(R.string.lbl_there_wedding)
                SubTitle =  subtitle
                tvEventSubTitle.text = subtitle
            }else{
                SubTitle =  myEvent.subtitle
                tvEventSubTitle.text = myEvent.subtitle
            }

            if(myEvent.type == WEDDING || myEvent.type == ENGAGEMENT ){
                tvRelInfo.text = getString(R.string.nimantrak)
            }


            if(myEvent.type == DASHKRIYA_VIDHI || myEvent.type == FIRST_MEMORIAL ){
                tvRelInfo.text = getString(R.string.vinit)
            }

            if(myEvent.type == JAGARAN_GONDHAL || myEvent.type == HOUSE_WARMING ){
                tvRelInfo.text = getString(R.string.snehankit)
            }

            tvRelInfoDetails.text = myEvent.description

            tvMoreInfo.text = myEvent.description_one

            latitude = myEvent.latitude.toDouble()

            longitude = myEvent.longitude.toDouble()

            address = myEvent.address

            photo = myEvent.photo

            Title = myEvent.title

            SubTitle = myEvent.subtitle

            eventId=myEvent.id

            type  = myEvent.type

            status = myEvent.status.toInt()

            getEventMedia(eventId)

            design = Design(type,Title,myEvent.family,myEvent.muhurt,SubTitle,
                    myEvent.subtitle_one,myEvent.subtitle_two,myEvent.subtitle_three,
                    myEvent.subtitle_four,myEvent.subtitle_five,myEvent.note,
                    myEvent.description,myEvent.description_one,myEvent.address,myEvent.photo)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {

        //init map object
        mMap = googleMap

        // Add a marker and zoom the camera.
        val addressLocal = LatLng(latitude, longitude)
        val cameraPosition = CameraPosition.Builder().target(addressLocal).zoom(15f).build()
        mMap.addMarker(MarkerOptions().position(addressLocal)
                .title(address))
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
                            MyAsyncTask(this@EventDetailActivity).execute()
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // permission is denied permenantly, navigate user to app settings
                            Util.showPermissionAlert(this@EventDetailActivity!!,
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
        class MyAsyncTask internal constructor(context: EventDetailActivity) : AsyncTask<Int, String, String?>() {

            private val activityReference: WeakReference<EventDetailActivity> = WeakReference(context)
            val activity = activityReference.get()
            override fun onPreExecute() {
                if (activity == null || activity.isFinishing) return
                (MultipleDesignActivity())
            }


            @SuppressLint("WrongThread")
            override fun doInBackground(vararg params: Int?): String? {

                var content = activity?.findViewById<CoordinatorLayout>(R.id.layScreen)
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
                                activity?.getString(R.string.msg_share_more_info) + "\n" + activity?.getString(R.string.url_gavkari_app_play_store));
                share.type = "image/*"
                activity?.startActivity(Intent.createChooser(share, activity?.getString(R.string.tittle_share_via)))

                return "done"
            }


            override fun onPostExecute(result: String?) {
                if (activity == null || activity.isFinishing) return
                //activity.dismissProgress()
            }

        }
    }

}
