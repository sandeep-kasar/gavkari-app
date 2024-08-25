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
import android.widget.*
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.gavkariapp.Model.*
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
import com.gavkariapp.constant.AppConstant.IMAGE_2_SELECTED
import com.gavkariapp.constant.AppConstant.IMAGE_3_SELECTED
import com.gavkariapp.constant.AppConstant.IMAGE_NOT_SELECTED
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
import com.gavkariapp.constant.AppConstant.SPRAY_PUMP
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
import com.gavkariapp.helper.ImageSetAsyncTask
import com.gavkariapp.helper.PlacesFieldSelector
import com.gavkariapp.helper.SelectImageMySaleAdFragment
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
import kotlinx.android.synthetic.main.activity_create_sell_ad.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.collections.set
import com.gavkariapp.constant.AppConstant.IMAGE_1_SELECTED as IMAGE_1_SELECTED1


open class EditSaleAdActivity : BaseActivity(), View.OnClickListener,
        AlertMessageCallback, ImageSetAsyncTask.ImageCallback {

    private val PLACE_PICKER_REQUEST = 1
    private val LOCATION_PERMISSION = 2
    private val IMAGE_DIRECTORY_NAME = "GavkariApp"
    private val REQUEST_IMAGE_CAPTURE = 3
    private val REQUEST_IMAGE_FROM_GALLERY = 4
    private var latitude = "19.9004433"
    private var longitude = "73.840436"
    private lateinit var buySale: MySaleAd
    private lateinit var buySaleObj: SaleCreateBody
    private lateinit var selectImageFragment: SelectImageMySaleAdFragment
    private lateinit var fileUri: Uri
    private var IMAGE_SELECTED = IMAGE_NOT_SELECTED
    private lateinit var hashMap: HashMap<String, String>
    var saleMediaList = java.util.ArrayList<Media>()

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
        setContentView(R.layout.activity_create_sell_ad)
        setupToolbar(R.id.toolbarHome, getString(R.string.lbl_edit))
        setUpView()
        edtMapLocation.setOnClickListener(this)
        img_event_1.setOnClickListener(this)
        img_event_2.setOnClickListener(this)
        img_event_3.setOnClickListener(this)
        btnSale.setOnClickListener(this)
    }

    private fun setUpView() {

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.place_picker_api))
        }

        selectImageFragment = SelectImageMySaleAdFragment(this)
        hashMap = HashMap()

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

        buySale = intent.getSerializableExtra("mySaleAd") as MySaleAd

        if(buySale!=null){

            buySaleObj = SaleCreateBody(buySale.id,buySale.user_id,buySale.village_id,buySale.status,buySale.tab_type,
                    buySale.type,buySale.name,buySale.price,buySale.breed,buySale.pregnancies_count.toString(),
                    buySale.pregnancy_status.toString(),buySale.milk.toString(),buySale.weight,buySale.company,buySale.model,
                    buySale.year,buySale.km_driven,buySale.power,buySale.capacity,buySale.material,
                    buySale.tynes_count,buySale.size,buySale.phase,buySale.latitude,buySale.longitude,
                    buySale.village_en,buySale.village_mr,buySale.miliseconds,buySale.photo,buySale.title,
                    buySale.description,buySale.buysale_media)

            setData(buySale)

            edtSellingEntity.setText(buySale.name)

            when {
                buySale.tab_type == ANIMAL -> {

                    layAnimal.visibility = View.VISIBLE
                    layMachine.visibility = View.GONE
                    layEquipment.visibility = View.GONE

                    when {
                        buySale.type == COW -> setSpinner(COW)
                        buySale.type == BUFFALO -> setSpinner(BUFFALO)
                        buySale.type == HEIFER_COW -> setSpinner(HEIFER_COW)
                        buySale.type == HEIFER_BUFFALO -> setSpinner(HEIFER_BUFFALO)
                        buySale.type == OX || buySale.type == MALE_BUFFALO -> layAnimal.visibility = View.GONE
                        buySale.type == GOAT -> setSpinner(GOAT)
                        buySale.type == MALE_GOAT -> setSpinner(MALE_GOAT)
                        buySale.type == OTHER_DOMESTIC_ANIMALS -> {
                            layAnimal.visibility = View.GONE
                            edtSellingEntity.setText("")
                            edtSellingEntity.setEnabled(true)
                        }
                        else -> layAnimal.visibility = View.GONE
                    }
                }
                buySale.tab_type == MACHINERY -> {
                    layAnimal.visibility = View.GONE
                    layMachine.visibility = View.VISIBLE
                    layEquipment.visibility = View.GONE

                    when {
                        buySale.type == TRACTOR || buySale.type == PICKUP || buySale.type == TEMPO_TRUCK || buySale.type == CAR || buySale.type == TWO_WHEELER -> {

                            layPower.visibility = View.GONE
                            layCapacity.visibility = View.GONE

                        }
                        buySale.type == THRESHER || buySale.type == KUTTI_MACHINE -> {

                            layPower.visibility = View.VISIBLE
                            layKmDriven.visibility = View.GONE
                            layModel.visibility = View.GONE
                            layCapacity.visibility = View.GONE

                        }
                        buySale.type == SPRAY_BLOWER -> {
                            layCapacity.visibility = View.VISIBLE
                            layKmDriven.visibility = View.GONE
                            layModel.visibility = View.GONE

                        }
                        else -> {
                            layKmDriven.visibility = View.GONE
                            layModel.visibility = View.GONE
                            edtSellingEntity.setText("")
                            edtSellingEntity.setEnabled(true)
                        }
                    }
                }
                buySale.tab_type == EQUIPMENTS -> {

                    layAnimal.visibility = View.GONE
                    layMachine.visibility = View.GONE
                    layEquipment.visibility = View.VISIBLE

                    when {
                        buySale.type == CULTIVATOR || buySale.type == SEED_DRILL -> {

                            layCompanyEq.visibility = View.VISIBLE
                            layBuyYearEq.visibility = View.VISIBLE
                            layMaterial.visibility = View.VISIBLE
                            layTynes.visibility = View.VISIBLE
                        }
                        buySale.type == ROTOVATOR -> {
                            layCompanyEq.visibility = View.VISIBLE
                            layBuyYearEq.visibility = View.VISIBLE
                            layMaterial.visibility = View.VISIBLE

                        }
                        buySale.type == PLOUGH -> {
                            layCompanyEq.visibility = View.VISIBLE
                            layBuyYearEq.visibility = View.VISIBLE
                            layMaterial.visibility = View.VISIBLE
                            layWeightEq.visibility = View.VISIBLE
                        }
                        buySale.type == TROLLEY -> {
                            layCompanyEq.visibility = View.VISIBLE
                            layBuyYearEq.visibility = View.VISIBLE
                            layCapacityEq.visibility = View.VISIBLE
                            laySize.visibility = View.VISIBLE

                        }
                        buySale.type == WATER_MOTOR_PUMP -> {
                            layCompanyEq.visibility = View.VISIBLE
                            layBuyYearEq.visibility = View.VISIBLE
                            layPowerEq.visibility = View.VISIBLE
                            layPhase.visibility = View.VISIBLE

                        }
                        buySale.type == SPRAY_PUMP -> {
                            layCompanyEq.visibility = View.VISIBLE
                            layBuyYearEq.visibility = View.VISIBLE
                            layCapacityEq.visibility = View.VISIBLE

                        }
                        buySale.type == IRRIGATION_MATERIAL -> {
                            layCompanyEq.visibility = View.VISIBLE
                            layBuyYearEq.visibility = View.VISIBLE
                        }
                        buySale.type == STEEL -> {
                            layCompanyEq.visibility = View.VISIBLE
                            layBuyYearEq.visibility = View.VISIBLE
                            layMaterial.visibility = View.VISIBLE
                            layWeightEq.visibility = View.VISIBLE
                        }
                        else -> {
                            layCompanyEq.visibility = View.VISIBLE
                            layBuyYearEq.visibility = View.VISIBLE
                            edtSellingEntity.setText("")
                            edtSellingEntity.setEnabled(true)
                        }
                    }
                }
                else -> {

                }
            }
        }

        viewImage1.setOnClickListener(View.OnClickListener {
            //set image number
            selectImage(IMAGE_1_SELECTED1)
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

        btnSale.text = getString(R.string.lbl_save)

    }

    fun setData(buySale: MySaleAd){

        edtSellingEntity.setText(buySale.name)
        edtPrice.setText(buySale.price)
        setSpinner(buySale.type)
        edtMilk.setText(buySale.milk.toString())
        edtWeight.setText(buySale.weight)

        edtCompany.setText(buySale.company)
        edtBuyYear.setText(buySale.year)
        edtKmDriven.setText(buySale.km_driven)
        edtModel.setText(buySale.model)
        edtPower.setText(buySale.power)
        edtCapacity.setText(buySale.capacity)

        edtCompanyEq.setText(buySale.company)
        edtBuyYearEq.setText(buySale.year)
        edtMaterial.setText(buySale.material)
        edtTynes.setText(buySale.tynes_count)
        edtWeightEq.setText(buySale.weight)
        edtCapacityEq.setText(buySale.capacity)
        edtSize.setText(buySale.size)
        edtPowerEq.setText(buySale.power)
        edtPhase.setText(buySale.phase)
        edtMapLocation.setText(buySale.village_mr)
        edtTitle.setText(buySale.title)
        edtMoreInformation.setText(buySale.description)

        var arrayList = buySale.buysale_media
        var i = AppConstant.IMAGE_1_SELECTED
        for (entry in arrayList) {
            if (i == AppConstant.IMAGE_1_SELECTED) {
                Glide.with(applicationContext)
                        .load(HttpConstant.BASE_BUYSALE_DOWNLOAD_URL + entry.photo)
                        .thumbnail(0.5f)
                        .into(img_event1)
                tv_add_photo.visibility = View.GONE
                rel_top1.visibility = View.VISIBLE
                hashMap["IMAGE_1_SELECTED"] = entry.photo
            }

            if (i == IMAGE_2_SELECTED) {
                Glide.with(applicationContext)
                        .load(HttpConstant.BASE_BUYSALE_DOWNLOAD_URL + entry.photo)
                        .thumbnail(0.5f)
                        .into(img_event2)
                tv_add_photo1.visibility = View.GONE
                rel_top2.visibility = View.VISIBLE
                hashMap["IMAGE_2_SELECTED"] = entry.photo
            }

            if (i == IMAGE_3_SELECTED) {
                Glide.with(applicationContext)
                        .load(HttpConstant.BASE_BUYSALE_DOWNLOAD_URL + entry.photo)
                        .thumbnail(0.5f)
                        .into(img_event3)
                tv_add_photo2.visibility = View.GONE
                rel_top3.visibility = View.VISIBLE
                hashMap["IMAGE_3_SELECTED"] = entry.photo
            }
            i++
        }
    }

    private fun setSpinner(type:Int){

        when (type) {
            BUFFALO -> layBreed.visibility = View.GONE
            HEIFER_COW -> layPregCount.visibility = View.GONE
            HEIFER_BUFFALO -> {
                layBreed.visibility = View.GONE
                layPregCount.visibility = View.GONE
            }
            GOAT -> layBreed.visibility = View.GONE
            MALE_GOAT -> {
                layBreed.visibility = View.GONE
                layPregCount.visibility = View.GONE
                layPregStatus.visibility = View.GONE
                layPregStatus.visibility = View.GONE
                layWeight.visibility = View.VISIBLE
            }
        }


        val breedList = arrayOf("",getString(R.string.lbl_sahiwal),getString(R.string.lbl_jercy),
                getString(R.string.lbl_rathi),getString(R.string.lbl_desi),getString(R.string.lbl_takkar),
                getString(R.string.lbl_red_si),getString(R.string.lbl_git),getString(R.string.lbl_malvi),
                getString(R.string.lbl_deoni),getString(R.string.lbl_other_breed))
        var adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, breedList)
        spnBreed.adapter = adapter
        spnBreed.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,view: View, position: Int, id: Long) {
                //Toast.makeText(this@EditSaleAdActivity,breedList[position], Toast.LENGTH_SHORT).show()
                buySaleObj.breed = breedList[position]

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        if (buySale.breed != null) {
            var spinnerPosition = adapter.getPosition(buySale.breed)
            spnBreed.setSelection(spinnerPosition)
        }

        val pregCount = arrayOf("",getString(R.string.lbl_never),getString(R.string.lbl_1_time),
                getString(R.string.lbl_2_times),getString(R.string.lbl_3_times),
                getString(R.string.lbl_4_times),getString(R.string.lbl_5_times),
                getString(R.string.lbl_6_times),getString(R.string.lbl_7_times),
                getString(R.string.lbl_8_times),getString(R.string.lbl_9_times),
                getString(R.string.lbl_10_times),getString(R.string.lbl_more_10))
        val adapter_1 = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, pregCount)
        spnPregCount.adapter = adapter_1
        spnPregCount.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,view: View, position: Int, id: Long) {
                //Toast.makeText(this@EditSaleAdActivity,pregCount[position], Toast.LENGTH_SHORT).show()
                when {
                    position > 1 -> {
                        layMilk.visibility = View.VISIBLE
                        buySaleObj.pregnancies_count = (position-1).toString()
                    }
                    position == 1 -> {
                        layMilk.visibility = View.GONE
                        buySaleObj.pregnancies_count = (position-1).toString()
                    }
                    position == 0 -> {
                        layMilk.visibility = View.GONE
                        buySaleObj.pregnancies_count = ""
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        if (buySale.pregnancies_count != null) {
            spnPregCount.setSelection(buySale.pregnancies_count + 1)
        }

        val pregStatus = arrayOf("",getString(R.string.lbl_not_pregnant),getString(R.string.lbl_first_m),
                getString(R.string.lbl_second_m),getString(R.string.lbl_third),
                getString(R.string.lbl_fourth),getString(R.string.lbl_fifth),
                getString(R.string.lbl_sixth),getString(R.string.lbl_seven),
                getString(R.string.lbl_eight),getString(R.string.lbl_nine),
                getString(R.string.lbl_ten))
        val adapter_2 = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, pregStatus)
        spnPregStatus.adapter = adapter_2
        spnPregStatus.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,view: View, position: Int, id: Long) {
                //Toast.makeText(this@EditSaleAdActivity,pregStatus[position], Toast.LENGTH_SHORT).show()
                if(position>0){
                    buySaleObj.pregnancy_status = (position-1).toString()
                }else{
                    buySaleObj.pregnancy_status = ""
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        if (buySale.pregnancy_status != null || buySale.breed!="") {
            spnPregStatus.setSelection(buySale.pregnancy_status+1)
        }

    }

    override fun onClick(v: View?) {

        when (v) {
            btnSale -> continueTwo()
            edtMapLocation -> checkLocationPermission()
            img_event_1 -> selectImage(IMAGE_1_SELECTED1)
            img_event_2 -> selectImage(IMAGE_2_SELECTED)
            img_event_3 -> selectImage(IMAGE_3_SELECTED)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
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

            Log.e("REQUEST_IMAGE_CAPTURE", fileUri!!.path)

            setImage(fileUri.path!!)

        } else if (requestCode == REQUEST_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {

            if (data != null) {

                Log.e("IMAGE_FROM_GALLERY", data.dataString)

                var filePath = Util.getRealPathFromUri(this, data.dataString.toString())

                Log.e("IMAGE_FROM_GALLERY", filePath)

                setImage(filePath)

            }

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
        showAlert(this@EditSaleAdActivity, this@EditSaleAdActivity,
                getString(R.string.title_need_permission), getString(R.string.message_permission_location),
                getString(R.string.lbl_grant), getString(R.string.lbl_cancel))
    }

    fun cameraIntent() {
        selectImageFragment.dismiss()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        fileUri = Util.getOutputMediaFileUri(this@EditSaleAdActivity, IMAGE_DIRECTORY_NAME)
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

        val prefs = PreferenceHelper.customPrefs(this, "user_info")

        buySaleObj.user_id = prefs[ApiConstant.USER_ID, "-1"]!!
        buySaleObj.village_id = prefs[ApiConstant.VILLAGE_ID, "-1"]!!
        buySaleObj.status = UNDER_REVIEW
        buySaleObj.tab_type = buySale.tab_type
        buySaleObj.type = buySale.type
        buySaleObj.name = edtSellingEntity.text.toString().trim()
        buySaleObj.price = edtPrice.text.toString().trim()
        buySaleObj.milk = edtMilk.text.toString().trim()
        buySaleObj.weight = edtWeight.text.toString().trim()
        buySaleObj.company = edtCompany.text.toString().trim()
        buySaleObj.model = edtModel.text.toString().trim()
        buySaleObj.year = edtBuyYear.text.toString().trim()
        buySaleObj.km_driven = edtKmDriven.text.toString().trim()
        buySaleObj.power = edtPower.text.toString().trim()
        buySaleObj.capacity = edtCapacity.text.toString().trim()
        buySaleObj.material = edtMaterial.text.toString().trim()
        buySaleObj.tynes_count = edtTynes.text.toString().trim()
        buySaleObj.size = edtSize.text.toString().trim()
        buySaleObj.phase = edtPhase.text.toString().trim()
        buySaleObj.latitude = latitude
        buySaleObj.longitude = longitude
        buySaleObj.longitude = longitude
        buySaleObj.village_en = prefs[ApiConstant.VN_ENGLISH, ""]!!
        buySaleObj.village_mr = prefs[ApiConstant.VN_MARATHI, ""]!!
        buySaleObj.village_mr = prefs[ApiConstant.VN_MARATHI, ""]!!
        buySaleObj.miliseconds= Util.getDateNow()
        buySaleObj.title= edtTitle.text.toString().trim()
        buySaleObj.description= edtMoreInformation.text.toString().trim()


        var isTrue = validate(buySaleObj)

        if (isTrue) {
            var isTrueType = true
            when{
                buySale.tab_type == ANIMAL -> {
                    when {
                        buySale.type == COW -> {
                            isTrueType = validateCow(buySaleObj)
                        }
                        buySale.type == BUFFALO -> {
                            isTrueType = validateBuffalo(buySaleObj)
                        }
                        buySale.type == HEIFER_COW -> {
                            isTrueType = validateHeiferCow(buySaleObj)
                        }
                        buySale.type == HEIFER_BUFFALO -> {
                            isTrueType = validateHeiferBuffalo(buySaleObj)
                        }
                        buySale.type == GOAT -> {
                            isTrueType = validateBuffalo(buySaleObj)
                        }
                    }
                }
                buySale.tab_type == MACHINERY -> {
                    when {
                        buySale.type == TRACTOR || buySale.type == PICKUP || buySale.type == TEMPO_TRUCK || buySale.type == CAR || buySale.type == TWO_WHEELER -> {
                            isTrueType = validateMachine(buySaleObj)
                        }
                        else -> {
                            isTrueType = validateOtherMachine(buySaleObj)
                        }
                    }
                }
                buySale.tab_type == EQUIPMENTS -> {
                    isTrueType = validateOtherMachine(buySaleObj)
                }
            }

            if (isTrueType) {
                var isTrueLast = validateLast(buySaleObj)
                if (isTrueLast) {
                    var keys = hashMap.keys
                    for ((i, key) in keys.withIndex()) {
                        var saleMedia = Media(i + 1, hashMap[key]!!)
                        saleMediaList.add(saleMedia)
                    }
                    buySaleObj.sale_media = saleMediaList
                    if(saleMediaList.isNotEmpty()){
                        buySaleObj.photo = when {
                            saleMediaList[0].photo.isNotEmpty() -> saleMediaList[0].photo
                            saleMediaList[1].photo.isNotEmpty() -> saleMediaList[1].photo
                            saleMediaList[2].photo.isNotEmpty() -> saleMediaList[2].photo
                            else -> saleMediaList[3].photo
                        }
                    }
                    //Toast.makeText(this@EditSaleAdActivity, buySale.name + buySale.tab_type + buySale.type, Toast.LENGTH_SHORT).show()
                    createSale(buySaleObj)

                }
            }
        }
    }

    private fun createSale(saleCreateBody: SaleCreateBody) {

        showProgress()

        ApiClient.get().create(ApiInterface::class.java)
                .editBuySaleAd(saleCreateBody)
                .enqueue(object : Callback<CommonResponse> {
                    override fun onResponse(call: Call<CommonResponse>?, response: Response<CommonResponse>?) {
                        if (response!!.code() == 200) {
                            if (response.body()?.status == HttpConstant.SUCCESS) {
                                runOnUiThread {
                                    dismissProgress()
                                    showSuccess(getString(R.string.msg_ad_saved_succes))
                                    finish()
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
        val imageSetAsyncTask = ImageSetAsyncTask(this@EditSaleAdActivity,
                this@EditSaleAdActivity, IMAGE_SELECTED)
        imageSetAsyncTask.execute(filePath)
    }

    override fun setBitmapExecute(savedFileUrl: String) {
        uploadFile(savedFileUrl)
    }

    private fun validate(buySale: SaleCreateBody): Boolean {

        when {

            InputValidatorHelper.isNullOrEmpty(buySale.name) -> {

                showWarning(getString(R.string.war_sale_entiry))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(buySale.price) -> {

                showWarning(getString(R.string.war_enter_price))

                return false

            }

            /*(hashMap.size == 0 || hashMap == null) -> {

                showWarning(getString(R.string.war_sale_photo))

                return false
            }*/

            else -> return true
        }
    }

    private fun validateLast(buySale: SaleCreateBody): Boolean {

        val mapAddress = edtMapLocation.text.toString().trim()

        when {

            InputValidatorHelper.isNullOrEmpty(mapAddress) -> {

                showWarning(getString(R.string.war_address))

                return false

            }

            /*InputValidatorHelper.isNullOrEmpty(buySale.title) -> {

                showWarning(getString(R.string.war_short_title))

                return false

            }*/

            else -> return true
        }
    }

    private fun validateCow(buySale: SaleCreateBody): Boolean {

        when {

            InputValidatorHelper.isNullOrEmpty(buySale.breed) -> {
                showWarning(getString(R.string.war_breed))
                return false
            }

            InputValidatorHelper.isNullOrEmpty(buySale.pregnancies_count) -> {
                showWarning(getString(R.string.war_preg_count))
                return false
            }

            InputValidatorHelper.isNullOrEmpty(buySale.pregnancy_status) -> {
                showWarning(getString(R.string.war_preg_status))
                return false
            }

            InputValidatorHelper.isNullOrEmpty(buySale.milk) -> {
                if (buySale.pregnancies_count.toInt() > 1){
                    showWarning(getString(R.string.war_milk))
                    return false
                }
                return true
            }
            else -> return true
        }

    }

    private fun validateBuffalo(buySale: SaleCreateBody): Boolean {

        when {

            InputValidatorHelper.isNullOrEmpty(buySale.pregnancies_count.toString()) -> {
                showWarning(getString(R.string.war_preg_count))
                return false
            }

            InputValidatorHelper.isNullOrEmpty(buySale.pregnancy_status.toString()) -> {
                showWarning(getString(R.string.war_preg_status))
                return false
            }

            InputValidatorHelper.isNullOrEmpty(buySale.milk.toString()) -> {
                if (buySale.pregnancies_count.toInt() > 1){
                    showWarning(getString(R.string.war_milk))
                    return false
                }
                return true
            }

            else -> return true
        }

    }

    private fun validateHeiferCow(buySale: SaleCreateBody): Boolean {

        when {

            InputValidatorHelper.isNullOrEmpty(buySale.breed) -> {
                showWarning(getString(R.string.war_breed))
                return false
            }

            InputValidatorHelper.isNullOrEmpty(buySale.pregnancy_status.toString()) -> {
                showWarning(getString(R.string.war_preg_status))
                return false
            }

            else -> return true
        }

    }

    private fun validateHeiferBuffalo(buySale: SaleCreateBody): Boolean {

        return return when {

            InputValidatorHelper.isNullOrEmpty(buySale.pregnancy_status.toString()) -> {
                showWarning(getString(R.string.war_preg_status))
                false
            }

            else -> true

        }

    }

    private fun validateMachine(buySale: SaleCreateBody): Boolean {

        return when {

            InputValidatorHelper.isNullOrEmpty(buySale.company) -> {
                showWarning(getString(R.string.war_company))
                false
            }

            InputValidatorHelper.isNullOrEmpty(buySale.year) -> {
                showWarning(getString(R.string.war_buying_year))
                false
            }

            InputValidatorHelper.isNullOrEmpty(buySale.km_driven) -> {
                showWarning(getString(R.string.war_km_driven))
                false
            }

            else -> true
        }

    }

    private fun validateOtherMachine(buySale: SaleCreateBody): Boolean {

        return when {

            InputValidatorHelper.isNullOrEmpty(buySale.company) -> {
                showWarning(getString(R.string.war_company))
                false
            }

            InputValidatorHelper.isNullOrEmpty(buySale.year) -> {
                showWarning(getString(R.string.war_buying_year))
                false
            }

            else -> true
        }

    }

    private fun uploadFile(imagePath: String) {

        showProgress()

        val file = File(imagePath)
        Log.d("", "Filename " + file.name)
        val mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val fileToUpload = MultipartBody.Part.createFormData("userfile", file.name, mFile)

        ApiClient.get().create(ApiInterface::class.java)
                .uploadSale(fileToUpload)
                .enqueue(object : Callback<UploadFile> {
                    override fun onResponse(call: Call<UploadFile>?, response: Response<UploadFile>?) {

                        if (response!!.code() == 200) {

                            Log.e("", "raw" + response.raw())
                            Log.e("", "body " + response.body()!!.status)

                            if (IMAGE_SELECTED == IMAGE_1_SELECTED1) {
                                hashMap["IMAGE_1_SELECTED"] = response.body()!!.status
                            }

                            if (IMAGE_SELECTED == IMAGE_2_SELECTED) {
                                hashMap["IMAGE_2_SELECTED"] = response.body()!!.status
                            }

                            if (IMAGE_SELECTED == IMAGE_3_SELECTED) {
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

                            if (IMAGE_SELECTED == IMAGE_1_SELECTED1) {

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
                        }
                    }
                })


    }

    override fun setImage(imageNo: Int, bitmap: Bitmap) {

        if (imageNo === IMAGE_1_SELECTED1) {

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
