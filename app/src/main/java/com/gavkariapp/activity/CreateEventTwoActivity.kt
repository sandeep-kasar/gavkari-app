package com.gavkariapp.activity

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
import com.gavkariapp.Model.CreateAdBody
import com.gavkariapp.Model.Media
import com.gavkariapp.Model.UploadFile
import com.gavkariapp.R
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.AppConstant.DASHKRIYA_VIDHI
import com.gavkariapp.constant.AppConstant.ENGAGEMENT
import com.gavkariapp.constant.AppConstant.FIRST_MEMORIAL
import com.gavkariapp.constant.AppConstant.HOUSE_WARMING
import com.gavkariapp.constant.AppConstant.IMAGE_1_SELECTED
import com.gavkariapp.constant.AppConstant.IMAGE_2_SELECTED
import com.gavkariapp.constant.AppConstant.IMAGE_3_SELECTED
import com.gavkariapp.constant.AppConstant.IMAGE_NOT_SELECTED
import com.gavkariapp.constant.AppConstant.JAGARAN_GONDHAL
import com.gavkariapp.constant.AppConstant.WEDDING
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.helper.ImageSetAsyncTask
import com.gavkariapp.helper.PlacesFieldSelector
import com.gavkariapp.helper.SelectImageFragment
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
import kotlinx.android.synthetic.main.activity_create_event_two.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set


open class CreateEventTwoActivity : BaseActivity(), View.OnClickListener,
        AlertMessageCallback, ImageSetAsyncTask.ImageCallback {


    private val PLACE_PICKER_REQUEST = 1
    private val LOCATION_PERMISSION = 2
    private val IMAGE_DIRECTORY_NAME = "GavkariApp"
    private val REQUEST_IMAGE_CAPTURE = 3
    private val REQUEST_IMAGE_FROM_GALLERY = 4
    private var latitude = "19.9004433"
    private var longitude = "73.840436"
    private lateinit var createAdBody: CreateAdBody
    private lateinit var selectImageFragment: SelectImageFragment
    private lateinit var fileUri: Uri
    private var IMAGE_SELECTED = IMAGE_NOT_SELECTED
    private lateinit var hashMap: HashMap<String, String>

    open lateinit var viewImage1: View
    open lateinit var img_event1: ImageView
    open lateinit var tv_add_photo: TextView
    open lateinit var rel_top1: RelativeLayout

    open lateinit var viewImage2: View
    open lateinit var img_event2: ImageView
    open lateinit var tv_add_photo1: TextView
    open lateinit var rel_top2: RelativeLayout

    open lateinit var viewImage3: View
    open lateinit var img_event3: ImageView
    open lateinit var tv_add_photo2: TextView
    open lateinit var rel_top3: RelativeLayout

    var placesFieldSelector = PlacesFieldSelector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_create_event_two)
        setupToolbar(R.id.toolbarHome, getString(R.string.title_create_ad))
        setUpView()
        btnContinueTwo.setOnClickListener(this)
        edtMapLocation.setOnClickListener(this)
        img_event_1.setOnClickListener(this)
        img_event_2.setOnClickListener(this)
        img_event_3.setOnClickListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onClick(v: View?) {

        when (v) {
            btnContinueTwo -> continueTwo()
            edtMapLocation -> checkLocationPermission()
            img_event_1 -> selectImage(IMAGE_1_SELECTED)
            img_event_2 -> selectImage(IMAGE_2_SELECTED)
            img_event_3 -> selectImage(IMAGE_3_SELECTED)
        }
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
            //val place = PlacePicker.getPlace(this@CreateEventTwoActivity, data)
            var place = data?.let { Autocomplete.getPlaceFromIntent(it) }
            val placeName = String.format(getString(R.string.lbl_place), place?.name)
            val address = String.format(getString(R.string.lbl_address_patta), place?.address)
            edtMapLocation.text = placeName + "\n" + address

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

    private fun setUpView() {

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.place_picker_api))
        }

        //init
        selectImageFragment = SelectImageFragment(this)
        hashMap = HashMap()

        //view
        viewImage1 = findViewById<View>(R.id.img_event_1)
        img_event1 = viewImage1.findViewById(R.id.img_event) as ImageView
        tv_add_photo = viewImage1.findViewById(R.id.tv_add_photo) as TextView
        rel_top1 = viewImage1.findViewById(R.id.rel_top) as RelativeLayout
        viewImage2 = findViewById<View>(R.id.img_event_2)
        img_event2 = viewImage2.findViewById(R.id.img_event) as ImageView
        tv_add_photo1 = viewImage2.findViewById(R.id.tv_add_photo) as TextView
        rel_top2 = viewImage2.findViewById(R.id.rel_top) as RelativeLayout
        viewImage3 = findViewById<View>(R.id.img_event_3)
        img_event3 = viewImage3.findViewById(R.id.img_event) as ImageView
        tv_add_photo2 = viewImage3.findViewById(R.id.tv_add_photo) as TextView
        rel_top3 = viewImage3.findViewById(R.id.rel_top) as RelativeLayout

        //data
        createAdBody = intent.getSerializableExtra("createAdBody") as CreateAdBody

        if(createAdBody!=null){
            edtNimantrak.setText(createAdBody.description)
            edtMoreInformation.setText(createAdBody.description_one)
            edtContactNo.setText(createAdBody.contact_no)
            edtTips.setText(createAdBody.note)
            edtNimantrak.hint = createAdBody.description
            edtMoreInformation.hint = createAdBody.description_one
            edtContactNo.hint = createAdBody.contact_no
            edtTips.hint = createAdBody.note

            if(createAdBody.type == WEDDING || createAdBody.type == ENGAGEMENT ){
                tvRelInfo.text = getString(R.string.nimantrak)
            }


            if(createAdBody.type == DASHKRIYA_VIDHI || createAdBody.type == FIRST_MEMORIAL ){
                tvRelInfo.text = getString(R.string.vinit)
            }


            if(createAdBody.type == JAGARAN_GONDHAL || createAdBody.type == HOUSE_WARMING ){
                tvRelInfo.text = getString(R.string.snehankit)
            }


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
            tv_add_photo.visibility = View.VISIBLE

            //remove image url
            if (hashMap.containsKey("IMAGE_1_SELECTED"))
                hashMap.remove("IMAGE_1_SELECTED")
        })

        rel_top2.setOnClickListener(View.OnClickListener {
            img_event2.setImageBitmap(null)
            rel_top2.visibility = View.GONE
            tv_add_photo1.visibility = View.VISIBLE
            //remove image url
            if (hashMap.containsKey("IMAGE_2_SELECTED"))
                hashMap.remove("IMAGE_2_SELECTED")
        })

        rel_top3.setOnClickListener(View.OnClickListener {
            img_event3.setImageBitmap(null)
            rel_top3.visibility = View.GONE
            tv_add_photo2.visibility = View.VISIBLE

            //remove image url
            if (hashMap.containsKey("IMAGE_3_SELECTED"))
                hashMap.remove("IMAGE_3_SELECTED")
        })

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
        showAlert(this@CreateEventTwoActivity, this@CreateEventTwoActivity,
                getString(R.string.title_need_permission), getString(R.string.message_permission_location),
                getString(R.string.lbl_grant), getString(R.string.lbl_cancel))
    }

    fun cameraIntent() {
        selectImageFragment.dismiss()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        fileUri = Util.getOutputMediaFileUri(this@CreateEventTwoActivity, IMAGE_DIRECTORY_NAME)
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

        //get default prefs
        val prefs = PreferenceHelper.customPrefs(this, "user_info")

        val useId: String? = prefs[ApiConstant.USER_ID, "-1"]
        val villageId: String? = prefs[ApiConstant.VILLAGE_ID, "-1"]

        //take data
        var mapLocation = edtMapLocation.text.toString().trim()
        var moreNimantrak = edtNimantrak.text.toString().trim()
        var moreInformation = edtMoreInformation.text.toString().trim()
        var contactNo = edtContactNo.text.toString().trim()
        var tips = edtTips.text.toString().trim()


        var isTrue = validate(mapLocation, moreNimantrak,moreInformation, contactNo, tips)

        if (isTrue) {

            var eventMediaList = ArrayList<Media>()

            var keys = hashMap.keys

            for ((i, key) in keys.withIndex()) {
                var eventMedia = Media(i, hashMap[key]!!)
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

            val createAdBody = CreateAdBody(useId!!, villageId!!,createAdBody.type,
                    createAdBody.title,
                    createAdBody.subtitle,
                    createAdBody.subtitle_one,
                    createAdBody.subtitle_two,
                    createAdBody.subtitle_three,
                    createAdBody.subtitle_four,
                    createAdBody.subtitle_five,
                    createAdBody.family,
                    moreNimantrak,
                    moreInformation,
                    createAdBody.event_date, createAdBody.event_date_ms,
                    createAdBody.muhurt, eventMediaList,
                    createAdBody.address,
                    mapLocation,
                    latitude, longitude, contactNo,
                    tips, photo,
                    "0","0",0)

            startActivity(Intent(applicationContext, CreateEventThreeActivity::class.java)
                    .putExtra("createAdBody",createAdBody))

        }

    }

    private fun setImage(filePath: String) {
        val imageSetAsyncTask = ImageSetAsyncTask(this@CreateEventTwoActivity,
                this@CreateEventTwoActivity, IMAGE_SELECTED)
        imageSetAsyncTask.execute(filePath)
    }

    override fun setBitmapExecute(savedFileUrl: String) {
        //upload file
        uploadFile(savedFileUrl)
    }

    private fun validate(mapLocation: String, relatives: String, moreInformation: String, contactNo: String, tips: String): Boolean {


        when {

            InputValidatorHelper.isNullOrEmpty(mapLocation) -> {

                showWarning(getString(R.string.warning_valid_map_location))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(relatives) -> {

                showError(getString(R.string.lbl_relative_info))

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

                            Log.e("", "raw" + response.raw())
                            Log.e("", "body " + response.body()!!.status)

                            if (IMAGE_SELECTED == AppConstant.IMAGE_1_SELECTED) {
                                hashMap["IMAGE_1_SELECTED"] = response.body()!!.status
                            }

                            if (IMAGE_SELECTED == AppConstant.IMAGE_2_SELECTED) {
                                hashMap["IMAGE_2_SELECTED"] = response.body()!!.status
                            }

                            if (IMAGE_SELECTED == AppConstant.IMAGE_3_SELECTED) {
                                hashMap["IMAGE_3_SELECTED"] = response.body()!!.status
                            }

                            if (IMAGE_SELECTED == AppConstant.IMAGE_4_SELECTED) {
                                hashMap["IMAGE_4_SELECTED"] = response.body()!!.status
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
                                tv_add_photo.visibility = View.VISIBLE

                                //remove image url
                                if (hashMap.containsKey("IMAGE_1_SELECTED"))
                                    hashMap.remove("IMAGE_1_SELECTED")
                            }

                            if (IMAGE_SELECTED == IMAGE_2_SELECTED) {
                                img_event2.setImageBitmap(null)
                                rel_top2.visibility = View.GONE
                                tv_add_photo1.visibility = View.VISIBLE
                                //remove image url
                                if (hashMap.containsKey("IMAGE_2_SELECTED"))
                                    hashMap.remove("IMAGE_2_SELECTED")
                            }

                            if (IMAGE_SELECTED == IMAGE_3_SELECTED) {
                                img_event3.setImageBitmap(null)
                                rel_top3.visibility = View.GONE
                                tv_add_photo2.visibility = View.VISIBLE

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
            tv_add_photo.setVisibility(View.GONE)
            rel_top1.setVisibility(View.VISIBLE)
        }

        if (imageNo === IMAGE_2_SELECTED) {

            img_event2.setImageBitmap(bitmap)
            tv_add_photo1.setVisibility(View.GONE)
            rel_top2.setVisibility(View.VISIBLE)
        }

        if (imageNo === IMAGE_3_SELECTED) {

            img_event3.setImageBitmap(bitmap)
            tv_add_photo2.setVisibility(View.GONE)
            rel_top3.setVisibility(View.VISIBLE)
        }
    }

}
