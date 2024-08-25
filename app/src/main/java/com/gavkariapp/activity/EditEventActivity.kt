package com.gavkariapp.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.gavkariapp.Model.CommonResponse
import com.gavkariapp.Model.CreateAdBody
import com.gavkariapp.Model.Media
import com.gavkariapp.Model.UploadFile
import com.gavkariapp.R
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.AppConstant.BIRTHDAY
import com.gavkariapp.constant.AppConstant.DASHKRIYA_VIDHI
import com.gavkariapp.constant.AppConstant.ENGAGEMENT
import com.gavkariapp.constant.AppConstant.FIRST_MEMORIAL
import com.gavkariapp.constant.AppConstant.IMAGE_1_SELECTED
import com.gavkariapp.constant.AppConstant.IMAGE_2_SELECTED
import com.gavkariapp.constant.AppConstant.IMAGE_3_SELECTED
import com.gavkariapp.constant.AppConstant.RETIREMENT
import com.gavkariapp.constant.AppConstant.UNDER_REVIEW
import com.gavkariapp.constant.AppConstant.WEDDING
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.constant.HttpConstant.BASE_EVENT_DOWNLOAD_URL
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.helper.*
import com.gavkariapp.interfaces.AlertMessageCallback
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import com.gavkariapp.utility.InputValidatorHelper
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.Util
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_edit_event.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.collections.set

class EditEventActivity : BaseActivity(), View.OnClickListener,
        AlertMessageCallback, ImageSetAsyncTask.ImageCallback {

    private val PLACE_PICKER_REQUEST = 1
    private val LOCATION_PERMISSION = 2
    private val IMAGE_DIRECTORY_NAME = "GavkariApp"
    private val REQUEST_IMAGE_CAPTURE = 3
    private val REQUEST_IMAGE_FROM_GALLERY = 4
    private var latitude = ""
    private var longitude = ""
    private lateinit var createAdBody: CreateAdBody
    private lateinit var selectImageFragment: SelectImageFragmentAd
    private lateinit var fileUri: Uri
    private var IMAGE_SELECTED = AppConstant.IMAGE_NOT_SELECTED
    private lateinit var hashMap: HashMap<String, String>

    open lateinit var viewImage1: View
    open lateinit var img_event1: ImageView
    open lateinit var img_cam1: TextView
    open lateinit var rel_top1: RelativeLayout

    open lateinit var viewImage2: View
    open lateinit var img_event2: ImageView
    open lateinit var img_cam2: TextView
    open lateinit var rel_top2: RelativeLayout

    open lateinit var viewImage3: View
    open lateinit var img_event3: ImageView
    open lateinit var img_cam3: TextView
    open lateinit var rel_top3: RelativeLayout

    var placesFieldSelector = PlacesFieldSelector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_edit_event)
        setupToolbar(R.id.toolbarHome, getString(R.string.title_edit_ad))
        setupView()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onClick(v: View?) {

        when (v) {
            btnContinueOneAd -> continueTwo()
            tvDateAd -> selectDate()
            tvTimeAd -> selectTime()
            edtMapLocationAd -> checkLocationPermission()
            img_event_1_ad -> selectImage(IMAGE_1_SELECTED)
            img_event_2_ad -> selectImage(IMAGE_2_SELECTED)
            img_event_3_ad -> selectImage(IMAGE_3_SELECTED)
        }
    }

    private fun selectDate() {
        val date = DatePickerEvent()
        val calender = Calendar.getInstance()
        val args = Bundle()
        args.putInt("year", calender.get(Calendar.YEAR))
        args.putInt("month", calender.get(Calendar.MONTH))
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH))
        date.arguments = args
        date.setCallBack(ondate)
        date.show(fragmentManager, "Date Picker")
    }

    private var ondate: DatePickerDialog.OnDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                tvDateAd.text = (dayOfMonth).toString() + "/" + (monthOfYear + 1).toString() + "/" + (year).toString()
            }

    private fun selectTime() {
        val calendar = Calendar.getInstance()
        val tpd = TimePickerDialog(this, R.style.DatePickerDialogTheme,
                TimePickerDialog.OnTimeSetListener { timePicker, i, i1 ->
                    tvTimeAd.text = i.toString() + ":" + i1
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
        tpd.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOCATION_PERMISSION) {
            if (ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                showPlacePicker()
            } else {
                showError(getString(R.string.warning_location_permission))
            }
        } else if (requestCode === PLACE_PICKER_REQUEST && resultCode === RESULT_OK) {
            var place = data?.let { Autocomplete.getPlaceFromIntent(it) }
            val placeName = String.format("Place: %s", place?.name)
            val address = String.format("Address: %s", place?.address)
            edtMapLocationAd.text = placeName + "\n" + address

            //get latitude and longitude
            val latlong = place?.latLng
            latitude = latlong!!.latitude.toString()
            longitude = latlong!!.longitude.toString()


        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Log.e("REQUEST_IMAGE_CAPTURE", fileUri.path!!)

            setImage(fileUri.path!!)

        } else if (requestCode == REQUEST_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {

            if (data != null) {

                Log.e("IMAGE_FROM_GALLERY", data.dataString)

                var filePath = Util.getRealPathFromUri(this, data.dataString!!)

                Log.e("IMAGE_FROM_GALLERY", filePath)

                setImage(filePath)

            }

        }
    }

    fun setupView() {

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.place_picker_api))
        }

        createAdBody = intent.getSerializableExtra("createAdBody") as CreateAdBody

        //init
        selectImageFragment = SelectImageFragmentAd(this)
        hashMap = HashMap()

        //view
        viewImage1 = findViewById<View>(R.id.img_event_1_ad)
        img_event1 = viewImage1.findViewById(R.id.img_event) as ImageView
        img_cam1 = viewImage1.findViewById(R.id.tv_add_photo) as TextView
        rel_top1 = viewImage1.findViewById(R.id.rel_top) as RelativeLayout
        viewImage2 = findViewById<View>(R.id.img_event_2_ad)
        img_event2 = viewImage2.findViewById(R.id.img_event) as ImageView
        img_cam2 = viewImage2.findViewById(R.id.tv_add_photo) as TextView
        rel_top2 = viewImage2.findViewById(R.id.rel_top) as RelativeLayout
        viewImage3 = findViewById<View>(R.id.img_event_3_ad)
        img_event3 = viewImage3.findViewById(R.id.img_event) as ImageView
        img_cam3 = viewImage3.findViewById(R.id.tv_add_photo) as TextView
        rel_top3 = viewImage3.findViewById(R.id.rel_top) as RelativeLayout

        if (createAdBody.type == WEDDING || createAdBody.type == ENGAGEMENT){
            laySubtitle.visibility =View.GONE
            laySubtitlePerson.visibility =View.GONE
            layLagnSubtitle.visibility = View.VISIBLE
            tvDateTime.text = getString(R.string.lbl_muhurt)

            edtSubTitleOne.setText(createAdBody.subtitle)
            edtSubTitleTwo.setText(createAdBody.subtitle_one)
            edtSubTitleThree.setText(createAdBody.subtitle_two)
            edtSubTitleFour.setText(createAdBody.subtitle_three)
            edtSubTitleFive.setText(createAdBody.subtitle_four)
            edtSubTitleSix.setText(createAdBody.subtitle_five)
            edtNimantrak.setText(createAdBody.description)
            edtMoreInformationAd.setText(createAdBody.description_one)

        }else if(createAdBody.type == FIRST_MEMORIAL || createAdBody.type == DASHKRIYA_VIDHI){
            laySubtitle.visibility =View.GONE
            layLagnSubtitle.visibility = View.GONE
            laySubtitlePerson.visibility =View.VISIBLE
            tvDateTime.text = getString(R.string.lbl_date_time)
            tvSubtitlePerson.text = getString(R.string.late_person_name)
            tvRelInfo.text = getString(R.string.vinit)

            edtSubTitleDetails.setText(createAdBody.subtitle)
            edtSubTitlePerson.setText(createAdBody.subtitle_one)
            edtNimantrak.setText(createAdBody.description)
            edtMoreInformationAd.setText(createAdBody.description_one)

        }else if(createAdBody.type == BIRTHDAY || createAdBody.type == RETIREMENT){
            laySubtitle.visibility =View.GONE
            layLagnSubtitle.visibility = View.GONE
            laySubtitlePerson.visibility =View.VISIBLE
            tvSubtitlePerson.text = getString(R.string.lbl_person_name)

            edtSubTitleDetails.setText(createAdBody.subtitle)
            edtSubTitlePerson.setText(createAdBody.subtitle_one)
            edtNimantrak.setText(createAdBody.description)
            edtMoreInformationAd.setText(createAdBody.description_one)
            
        }else{
            layLagnSubtitle.visibility = View.GONE
            laySubtitlePerson.visibility =View.GONE
            laySubtitle.visibility =View.VISIBLE
            edtSubTitle.setText(createAdBody.subtitle)
            edtNimantrak.setText(createAdBody.description)
            edtMoreInformationAd.setText(createAdBody.description_one)
        }

        edtAdTitleAd.setText(createAdBody.title)
        edtFamilyAd.setText(createAdBody.family)
        var date = Util.getFormatedDateEnglish(createAdBody.event_date, "yyyy-MM-dd HH:mm:ss", "dd/MM/YYYY")
        var time = Util.getFormatedDateEnglish(createAdBody.event_date, "yyyy-MM-dd HH:mm:ss", "HH:mm")
        tvDateAd.text = date
        tvTimeAd.text = time
        edtMuhurtAd.setText(createAdBody.muhurt)
        edtAddressAd.setText(createAdBody.address)
        edtMapLocationAd.text = createAdBody.location
        latitude = createAdBody.latitude
        longitude = createAdBody.longitude
        edtContactNoAd.setText(createAdBody.contact_no)
        edtTipsAd.setText(createAdBody.note)

        var arrayList = createAdBody.event_media

        var i = IMAGE_1_SELECTED

        for (entry in arrayList) {
            if (i == IMAGE_1_SELECTED) {
                Glide.with(applicationContext)
                        .load(HttpConstant.BASE_EVENT_DOWNLOAD_URL + entry.photo)
                        .thumbnail(0.5f)
                        .into(img_event1)
                img_cam1.visibility = View.GONE
                rel_top1.visibility = View.VISIBLE
                hashMap["IMAGE_1_SELECTED"] = entry.photo
            }

            if (i == IMAGE_2_SELECTED) {
                Glide.with(applicationContext)
                        .load(BASE_EVENT_DOWNLOAD_URL + entry.photo)
                        .thumbnail(0.5f)
                        .into(img_event2)
                img_cam2.visibility = View.GONE
                rel_top2.visibility = View.VISIBLE
                hashMap["IMAGE_2_SELECTED"] = entry.photo
            }

            if (i == IMAGE_3_SELECTED) {
                Glide.with(applicationContext)
                        .load(BASE_EVENT_DOWNLOAD_URL + entry.photo)
                        .thumbnail(0.5f)
                        .into(img_event3)
                img_cam3.visibility = View.GONE
                rel_top3.visibility = View.VISIBLE
                hashMap["IMAGE_3_SELECTED"] = entry.photo
            }


            i++
        }


        //click
        viewImage1.setOnClickListener(View.OnClickListener {
            //set image number
            selectImage(IMAGE_1_SELECTED)
        })

        viewImage2.setOnClickListener(View.OnClickListener {
            //set image number
            selectImage(IMAGE_2_SELECTED)
        })

        viewImage3.setOnClickListener(View.OnClickListener {
            //set image number
            selectImage(IMAGE_3_SELECTED)
        })


        rel_top1.setOnClickListener(View.OnClickListener {
            img_event1.setImageBitmap(null)
            rel_top1.visibility = View.GONE
            img_cam1.visibility = View.VISIBLE

            //remove image url
            if (hashMap.containsKey("IMAGE_1_SELECTED"))
                hashMap.remove("IMAGE_1_SELECTED")
        })

        rel_top2.setOnClickListener(View.OnClickListener {
            img_event2.setImageBitmap(null)
            rel_top2.visibility = View.GONE
            img_cam2.visibility = View.VISIBLE
            //remove image url
            if (hashMap.containsKey("IMAGE_2_SELECTED"))
                hashMap.remove("IMAGE_2_SELECTED")
        })

        rel_top3.setOnClickListener(View.OnClickListener {
            img_event3.setImageBitmap(null)
            rel_top3.visibility = View.GONE
            img_cam3.visibility = View.VISIBLE

            //remove image url
            if (hashMap.containsKey("IMAGE_3_SELECTED"))
                hashMap.remove("IMAGE_3_SELECTED")
        })


        btnContinueOneAd.setOnClickListener(this)
        tvDateAd.setOnClickListener(this)
        tvTimeAd.setOnClickListener(this)
        edtMapLocationAd.setOnClickListener(this)

        if (createAdBody.status.equals("0")) {
            btnContinueOneAd.text = getString(R.string.lbl_save_continue)
        }

    }

    private fun checkLocationPermission() {
        Dexter.withActivity(this)
                .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        // permission is granted
                        showPlacePicker()

                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied) {
                            showSettingsDialog()
                        }

                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest()

                    }
                }).check()
    }

    private fun showPlacePicker() {
        var autocompleteIntent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,
                placesFieldSelector.allFields)
                .build(this)
        //requestCode in INT
        startActivityForResult(autocompleteIntent, PLACE_PICKER_REQUEST)

    }

    private fun showSettingsDialog() {
        showAlert(this@EditEventActivity, this@EditEventActivity,
                getString(R.string.title_need_permission), getString(R.string.message_permission_location),
                getString(R.string.lbl_grant), getString(R.string.lbl_cancel))
    }

    fun cameraIntent() {
        selectImageFragment.dismiss()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        fileUri = Util.getOutputMediaFileUri(this@EditEventActivity, IMAGE_DIRECTORY_NAME)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    fun galleryIntent() {
        selectImageFragment.dismiss()
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_FROM_GALLERY)
    }

    private fun selectImage(imageNo: Int) {
        if (InternetUtil.isInternetOn()) {
            IMAGE_SELECTED = imageNo
            selectImageFragment.show(supportFragmentManager, selectImageFragment.tag)
        } else {
            waitForInternet()
        }
    }

    override fun setPositiveButton(message: String) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, LOCATION_PERMISSION)
    }

    override fun setNegativeButton(message: String) {}

    private fun continueTwo() {

        if(createAdBody.type == WEDDING || createAdBody.type == ENGAGEMENT){
            //take data
            var adTitle = edtAdTitleAd.text.toString()
            var subTitle = edtSubTitleOne.text.toString()
            var subTitle_two = edtSubTitleTwo.text.toString()
            var subTitle_three = edtSubTitleThree.text.toString()
            var subTitle_four = edtSubTitleFour.text.toString()
            var subTitle_five = edtSubTitleFive.text.toString()
            var subTitle_six = edtSubTitleSix.text.toString()
            var family = edtFamilyAd.text.toString()
            var date = tvDateAd.text.toString().trim()
            var time = tvTimeAd.text.toString().trim()
            var muhurt = edtMuhurtAd.text.toString()
            var address = edtAddressAd.text.toString()
            var mapLocation = edtMapLocationAd.text.toString()
            var moreInformation = edtNimantrak.text.toString()
            var moreInformation_one = edtMoreInformationAd.text.toString()
            var contactNo = edtContactNoAd.text.toString()
            var tips = edtTipsAd.text.toString()


            var isTrue = validateVivah(adTitle, subTitle,subTitle_two,subTitle_three,subTitle_four,subTitle_five,
                    subTitle_six,family, date, time, muhurt, address,
                    mapLocation, moreInformation,moreInformation_one, contactNo,tips)

            if (isTrue) {

                var inputDate = Util.getFormatedDateEnglish("$date $time",
                        "dd/MM/yyyy HH:mm", "yyyy-MM-dd HH:mm:ss")

                var millisecondsFromNow = Util.getInMilliSecond(inputDate!!)

                var eventMediaList = ArrayList<Media>()

                var keys = hashMap.keys

                for ((i, key) in keys.withIndex()) {
                    var eventMedia = Media(i + 1, hashMap[key]!!)
                    eventMediaList.add(eventMedia)
                }

                var photo = ""
                if (eventMediaList.isNotEmpty()){
                    photo = when {
                        eventMediaList[0].photo.isNotEmpty() -> eventMediaList[0].photo
                        eventMediaList[1].photo.isNotEmpty() -> eventMediaList[1].photo
                        eventMediaList[2].photo.isNotEmpty() -> eventMediaList[2].photo
                        else -> eventMediaList[3].photo
                    }
                }

                val prefs = PreferenceHelper.customPrefs(this, "user_info")
                var userId: String? = prefs[ApiConstant.USER_ID, "-1"]
                var villageId: String? = prefs[ApiConstant.VILLAGE_ID, "-1"]

                val createAdBody = CreateAdBody(
                        userId!!,
                        villageId!!,
                        createAdBody.type,
                        adTitle,subTitle,subTitle_two,subTitle_three,subTitle_four,subTitle_five,subTitle_six,
                        family, moreInformation,moreInformation_one,inputDate,
                        millisecondsFromNow.toString(), muhurt, eventMediaList, address, mapLocation,
                        latitude, longitude, contactNo, tips, photo,
                        createAdBody.event_id,createAdBody.event_id, UNDER_REVIEW)

                saveAndContinue(createAdBody)

            }
        }
        else if(createAdBody.type == FIRST_MEMORIAL || createAdBody.type == DASHKRIYA_VIDHI ||
                createAdBody.type == BIRTHDAY || createAdBody.type == RETIREMENT){
            var adTitle = edtAdTitleAd.text.toString()
            var subTitlePeson = edtSubTitlePerson.text.toString()
            var subTitleDetails = edtSubTitleDetails.text.toString()
            var family = edtFamilyAd.text.toString()
            var date = tvDateAd.text.toString().trim()
            var time = tvTimeAd.text.toString().trim()
            var muhurt = edtMuhurtAd.text.toString()
            var address = edtAddressAd.text.toString()
            var mapLocation = edtMapLocationAd.text.toString()
            var moreInformation = edtNimantrak.text.toString()
            var moreInformation_one = edtMoreInformationAd.text.toString()
            var contactNo = edtContactNoAd.text.toString()
            var tips = edtTipsAd.text.toString()


            var isTrue = validate(adTitle, subTitlePeson,subTitleDetails ,family, date, time, muhurt, address,
                    mapLocation, moreInformation, contactNo, tips)

            if (isTrue) {

                var inputDate = Util.getFormatedDateEnglish("$date $time",
                        "dd/MM/yyyy HH:mm", "yyyy-MM-dd HH:mm:ss")

                var millisecondsFromNow = Util.getInMilliSecond(inputDate!!)

                var eventMediaList = ArrayList<Media>()

                var keys = hashMap.keys

                for ((i, key) in keys.withIndex()) {
                    var eventMedia = Media(i + 1, hashMap[key]!!)
                    eventMediaList.add(eventMedia)
                }

                var photo = ""
                if (eventMediaList.isNotEmpty()){
                    photo = when {
                        eventMediaList[0].photo.isNotEmpty() -> eventMediaList[0].photo
                        eventMediaList[1].photo.isNotEmpty() -> eventMediaList[1].photo
                        eventMediaList[2].photo.isNotEmpty() -> eventMediaList[2].photo
                        else -> eventMediaList[3].photo
                    }
                }

                val prefs = PreferenceHelper.customPrefs(this, "user_info")
                var userId: String? = prefs[ApiConstant.USER_ID, "-1"]
                var villageId: String? = prefs[ApiConstant.VILLAGE_ID, "-1"]

                val createAdBody = CreateAdBody(
                        userId!!,
                        villageId!!,
                        createAdBody.type,
                        adTitle,subTitleDetails,subTitlePeson,"","","","",
                        family, moreInformation,moreInformation_one,inputDate,
                        millisecondsFromNow.toString(), muhurt, eventMediaList, address, mapLocation,
                        latitude, longitude, contactNo, tips, photo,
                        createAdBody.event_id,createAdBody.event_id,UNDER_REVIEW)

                saveAndContinue(createAdBody)
            }
        }else{

            var adTitle = edtAdTitleAd.text.toString()
            var subTitle = edtSubTitle.text.toString()
            var family = edtFamilyAd.text.toString()
            var date = tvDateAd.text.toString().trim()
            var time = tvTimeAd.text.toString().trim()
            var muhurt = edtMuhurtAd.text.toString()
            var address = edtAddressAd.text.toString()
            var mapLocation = edtMapLocationAd.text.toString()
            var moreInformation = edtNimantrak.text.toString()
            var moreInformation_one = edtMoreInformationAd.text.toString()
            var contactNo = edtContactNoAd.text.toString()
            var tips = edtTipsAd.text.toString()


            var isTrue = validate(adTitle, "Not Avail",subTitle,family, date, time, muhurt, address,
                    mapLocation, moreInformation, contactNo, tips)

            if (isTrue) {

                var inputDate = Util.getFormatedDateEnglish("$date $time",
                        "dd/MM/yyyy HH:mm", "yyyy-MM-dd HH:mm:ss")

                var millisecondsFromNow = Util.getInMilliSecond(inputDate!!)

                var eventMediaList = ArrayList<Media>()

                var keys = hashMap.keys

                for ((i, key) in keys.withIndex()) {
                    var eventMedia = Media(i + 1, hashMap[key]!!)
                    eventMediaList.add(eventMedia)
                }

                var photo = ""
                if (eventMediaList.isNotEmpty()){
                    photo = when {
                        eventMediaList[0].photo.isNotEmpty() -> eventMediaList[0].photo
                        eventMediaList[1].photo.isNotEmpty() -> eventMediaList[1].photo
                        eventMediaList[2].photo.isNotEmpty() -> eventMediaList[2].photo
                        else -> eventMediaList[3].photo
                    }
                }

                val prefs = PreferenceHelper.customPrefs(this, "user_info")
                var userId: String? = prefs[ApiConstant.USER_ID, "-1"]
                var villageId: String? = prefs[ApiConstant.VILLAGE_ID, "-1"]

                val createAdBody = CreateAdBody(
                        userId!!,
                        villageId!!,
                        createAdBody.type,
                        adTitle,subTitle,"","","","","",
                        family, moreInformation,moreInformation_one,inputDate,
                        millisecondsFromNow.toString(), muhurt, eventMediaList, address, mapLocation,
                        latitude, longitude, contactNo, tips, photo,
                        createAdBody.event_id,createAdBody.event_id,UNDER_REVIEW)

                saveAndContinue(createAdBody)
            }
        }

    }

    fun saveAndContinue(cad: CreateAdBody) {

        showProgress()

        ApiClient.get().create(ApiInterface::class.java)
                .editMyAd(cad)
                .enqueue(object : Callback<CommonResponse> {
                    override fun onResponse(call: Call<CommonResponse>?, response: Response<CommonResponse>?) {
                        if (response!!.code() == 200) {
                            if (response.body()?.status == HttpConstant.SUCCESS) {
                                var commonResponse = response.body()
                                runOnUiThread {
                                    dismissProgress()
                                    showSuccess(getString(R.string.msg_event_updated))
                                    startActivity(Intent(applicationContext, MyEventActivity::class.java))
                                    ActivityCompat.finishAffinity(this@EditEventActivity)

                                }
                            } else if (response.body()?.status == HttpConstant.DUPLICATE_DATA ||
                                    response.body()?.status == HttpConstant.EMPTY_REQUEST ||
                                    response.body()?.status == HttpConstant.FIELD_IS_EMPTY ||
                                    response.body()?.status == HttpConstant.FAIL_TO_INSERT ||
                                    response.body()?.status == HttpConstant.NO_DATA_AVAILABLE) {
                                runOnUiThread {
                                    dismissProgress()
                                    showWarning(response.body()!!.message!!)
                                }
                            }

                        } else {
                            if (response.errorBody() != null) {
                                runOnUiThread {
                                    dismissProgress()
                                    showError(response.errorBody().toString())
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<CommonResponse>?, t: Throwable?) {
                        runOnUiThread {
                            dismissProgress()
                            showError(t.toString())
                        }
                    }
                })

    }

    private fun setImage(filePath: String) {
        val imageSetAsyncTask = ImageSetAsyncTask(this@EditEventActivity,
                this@EditEventActivity, IMAGE_SELECTED)
        imageSetAsyncTask.execute(filePath)
    }

    override fun setBitmapExecute(savedFileUrl: String) {
        //upload file
        uploadFile(savedFileUrl)
    }

    private fun validateVivah(adTitle: String, subTitleOne: String,subTitleTwo: String,subTitleThree: String,
                              subTitleFour: String,subTitleFive: String,subTitleSix: String,
                              family: String, date: String, time: String, muhurt: String, address: String,
                              mapLocation: String, moreInformation: String,moreInformationOne: String, contactNo: String, tips: String): Boolean {


        when {
            InputValidatorHelper.isNullOrEmpty(adTitle) -> {

                showError(getString(R.string.warning_empty_ad_title))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(subTitleOne) -> {

                showError(getString(R.string.lbl_grooms_name))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(subTitleTwo) -> {

                showError(getString(R.string.lbl_surname))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(subTitleThree) -> {

                showError(getString(R.string.lbl_parents_name))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(subTitleFour) -> {

                showError(getString(R.string.lbl_brides_name))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(subTitleFive) -> {

                showError(getString(R.string.lbl_surname))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(subTitleSix) -> {

                showError(getString(R.string.lbl_parents_name))

                return false

            }


            InputValidatorHelper.isNullOrEmpty(family) -> {

                showError(getString(R.string.warning_empty_family_info))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(date) -> {

                showError(getString(R.string.warning_empty_date))

                return false

            }


            InputValidatorHelper.isNullOrEmpty(time) -> {

                showError(getString(R.string.warning_empty_time))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(muhurt) -> {

                showError(getString(R.string.warning_empty_muhurt))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(address) -> {

                showError(getString(R.string.warning_empty_address))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(mapLocation) -> {

                showWarning(getString(R.string.warning_valid_map_location))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(moreInformation) -> {

                showError(getString(R.string.warning_empty_more_info))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(moreInformationOne) -> {

                showError(getString(R.string.warning_empty_more_info))

                return false

            }
            InputValidatorHelper.isNullOrEmpty(contactNo) -> {

                showError(getString(R.string.warning_empty_cont_number))

                return false

            }


            /*(hashMap.size == 0 || hashMap == null) -> {

                showWarning(getString(R.string.warning_upload_photo))

                return false
            }*/


            else -> return true
        }


    }


    private fun validate(adTitle: String, subTitle: String,subTitleDeails: String, family: String, date: String,
                         time: String, muhurt: String, address: String,
                         mapLocation: String, moreInformation: String, contactNo: String, tips: String): Boolean {


        when {

            InputValidatorHelper.isNullOrEmpty(adTitle) -> {

                showError(getString(R.string.warning_empty_ad_title))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(subTitle) -> {

                showError(getString(R.string.warning_empty_sub_title_person))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(subTitleDeails) -> {

                showError(getString(R.string.warning_empty_sub_title))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(family) -> {

                showError(getString(R.string.warning_empty_family_info))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(date) -> {

                showError(getString(R.string.warning_empty_date))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(time) -> {

                showError(getString(R.string.warning_empty_time))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(muhurt) -> {

                showError(getString(R.string.warning_empty_muhurt))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(address) -> {

                showError(getString(R.string.warning_empty_address))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(mapLocation) -> {

                showWarning(getString(R.string.warning_valid_map_location))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(moreInformation) -> {

                showError(getString(R.string.warning_empty_more_info))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(contactNo) -> {

                showError(getString(R.string.warning_empty_cont_number))

                return false

            }


            /*(hashMap.size == 0 || hashMap == null) -> {

                showWarning(getString(R.string.warning_upload_photo))

                return false
            }*/

            else -> return true
        }


    }

    private fun uploadFile(imagePath: String) {

        showProgress()

        val file = File(imagePath)
        Log.d("", "Filename " + file.name)
        val mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val fileToUpload = MultipartBody.Part.createFormData("userfile", file.name, mFile)

        ApiClient.get().create(ApiInterface::class.java)
                .uploadEvent(fileToUpload)
                .enqueue(object : Callback<UploadFile> {
                    override fun onResponse(call: Call<UploadFile>?, response: Response<UploadFile>?) {

                        if (response!!.code() == 200) {

                            Log.e("", "response " + response.raw())
                            Log.e("", "response " + response.body()!!.status)

                            if (IMAGE_SELECTED == AppConstant.IMAGE_1_SELECTED) {
                                hashMap["IMAGE_1_SELECTED"] = response.body()!!.status
                            }

                            if (IMAGE_SELECTED == AppConstant.IMAGE_2_SELECTED) {
                                hashMap["IMAGE_2_SELECTED"] = response.body()!!.status
                            }

                            if (IMAGE_SELECTED == AppConstant.IMAGE_3_SELECTED) {
                                hashMap["IMAGE_3_SELECTED"] = response.body()!!.status
                            }

                            runOnUiThread {
                                dismissProgress()
                            }

                        } else {
                            if (response.errorBody() != null) {
                                runOnUiThread {
                                    dismissProgress()
                                    showError(response.errorBody().toString())
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<UploadFile>?, t: Throwable?) {
                        // showError(t.toString())

                        Log.d("", "Error " + t.toString())

                        runOnUiThread {

                            if (IMAGE_SELECTED == IMAGE_1_SELECTED) {

                                img_event1.setImageBitmap(null)
                                rel_top1.visibility = View.GONE
                                img_cam1.visibility = View.VISIBLE

                                //remove image url
                                if (hashMap.containsKey("IMAGE_1_SELECTED"))
                                    hashMap.remove("IMAGE_1_SELECTED")
                            }

                            if (IMAGE_SELECTED == IMAGE_2_SELECTED) {
                                img_event2.setImageBitmap(null)
                                rel_top2.visibility = View.GONE
                                img_cam2.visibility = View.VISIBLE
                                //remove image url
                                if (hashMap.containsKey("IMAGE_2_SELECTED"))
                                    hashMap.remove("IMAGE_2_SELECTED")
                            }

                            if (IMAGE_SELECTED == IMAGE_3_SELECTED) {
                                img_event3.setImageBitmap(null)
                                rel_top3.visibility = View.GONE
                                img_cam3.visibility = View.VISIBLE

                                //remove image url
                                if (hashMap.containsKey("IMAGE_3_SELECTED"))
                                    hashMap.remove("IMAGE_3_SELECTED")
                            }

                            dismissProgress()

                            showError(getString(R.string.warning_try_later))
                        }
                    }
                })


    }

    override fun setImage(imageNo: Int, bitmap: Bitmap) {

        if (imageNo === IMAGE_1_SELECTED) {

            img_event1.setImageBitmap(bitmap)
            img_cam1.visibility = View.GONE
            rel_top1.visibility = View.VISIBLE
        }

        if (imageNo === IMAGE_2_SELECTED) {

            img_event2.setImageBitmap(bitmap)
            img_cam2.visibility = View.GONE
            rel_top2.visibility = View.VISIBLE
        }

        if (imageNo === IMAGE_3_SELECTED) {

            img_event3.setImageBitmap(bitmap)
            img_cam3.visibility = View.GONE
            rel_top3.visibility = View.VISIBLE
        }
    }

}
