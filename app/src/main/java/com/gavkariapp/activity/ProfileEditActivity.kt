package com.gavkariapp.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ScrollView
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gavkariapp.Model.*
import com.gavkariapp.R
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.ApiConstant.IS_VERIFIED
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.AppConstant.ENGLISH
import com.gavkariapp.constant.AppConstant.IMAGE_NOT_SELECTED
import com.gavkariapp.constant.AppConstant.MARATHI
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.constant.HttpConstant.BASE_AVATAR_DOWNLOAD_URL
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.data.PreferenceHelper.set
import com.gavkariapp.helper.ImageSetAsyncTask
import com.gavkariapp.helper.SelectImageFragmentProfile
import com.gavkariapp.helper.UserData
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import com.gavkariapp.utility.InputValidatorHelper
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.Util
import kotlinx.android.synthetic.main.activity_profile_edit.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.collections.set

class ProfileEditActivity : BaseActivity(), View.OnClickListener,
        ImageSetAsyncTask.ImageCallback {

    var stateListIds = mutableMapOf<String, String>()
    var distListIds = mutableMapOf<String, String>()
    var talListIds = mutableMapOf<String, String>()
    var villageListIds = mutableMapOf<String, String>()

    private var stateId: String = "0"

    private var districtId: String = "0"

    private var talukaId: String = "0"

    private var villageId: String = "0"

    var isVerified: String? = "0"

    private var avatar: String? = ""

    var userId: String? = "-1"

    var userLang: String? = "en"

    private lateinit var userData: UserData

    private lateinit var selectImageFragment: SelectImageFragmentProfile

    private val IMAGE_DIRECTORY_NAME = "GavkariApp"
    private val REQUEST_IMAGE_CAPTURE = 3
    private val REQUEST_IMAGE_FROM_GALLERY = 4
    private lateinit var fileUri: Uri
    private var IMAGE_SELECTED = IMAGE_NOT_SELECTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)
        applyLocale(this)
        setupToolbar(R.id.toolbarHome, getString(R.string.lbl_edit))
        setupView()
    }

    private fun setupView() {

        selectImageFragment = SelectImageFragmentProfile(this)

        userData = UserData(this)

        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        userId = prefs[ApiConstant.USER_ID, "-1"]
        var userName: String? = prefs[ApiConstant.NAME, "-1"]
        var userEmail: String? = prefs[ApiConstant.EMAIL, "-1"]
        var bio: String? = prefs[ApiConstant.BIO, "Farmer"]
        var userMobile: String? = prefs[ApiConstant.MOBILE, "-1"]
        userLang = prefs[AppConstant.LANGUAGE, "-1"]
        var userAvatar: String? = prefs[ApiConstant.AVATAR, ""]
        var vnEnglish: String? = prefs[ApiConstant.VN_ENGLISH, "-1"]
        var vnMarathi: String? = prefs[ApiConstant.VN_MARATHI, "-1"]
        var vnHindi: String? = prefs[ApiConstant.VN_HINDI, "-1"]
        isVerified = prefs[IS_VERIFIED, "-1"]
        villageId = prefs[ApiConstant.VILLAGE_ID, "-1"].toString()

        edtNameEd.setText(userName)
        edtEmailEd.setText(userEmail)
        edtBio.setText(bio)
        edtMobileEd.setText(userMobile)

        var villageName = ""
        if (userLang == ENGLISH) {
            villageName = vnEnglish.toString()
        }

        if (userLang == MARATHI) {
            villageName = vnMarathi.toString()
        }

        edtVillageName.setText(villageName)


        avatar = if (!userAvatar.equals("-1") && !userAvatar.equals("") ) {

            imgAvatar.setBackgroundResource(android.R.color.transparent)
            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.drawable.ic_user_drawer)
            requestOptions.error(R.drawable.ic_user_drawer)
            Glide.with(imgAvatar)
                    .setDefaultRequestOptions(requestOptions)
                    .load(BASE_AVATAR_DOWNLOAD_URL + userAvatar)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgAvatar)
            userAvatar
        } else {
            userAvatar
        }

        btnVerify.setOnClickListener(this)
        btnChange.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        imgAvatar.setOnClickListener(this)

        spnStateEdt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showWarning(getString(R.string.warning_select_state))
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                stateId = stateListIds.get(parent?.getItemAtPosition(position))!!
                getDistrict(stateId)
            }
        }

        spnDistEdt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showWarning(getString(R.string.warning_select_district))
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                districtId = distListIds.get(parent?.getItemAtPosition(position))!!
                getTaluka(districtId)
                Log.e("", parent?.getItemAtPosition(position).toString())


            }

        }

        spnTalukaEdt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showWarning(getString(R.string.warning_select_district))
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                talukaId = talListIds.get(parent?.getItemAtPosition(position))!!
                getVillage(talukaId)
                Log.e("", parent?.getItemAtPosition(position).toString())
            }

        }

        spnVillageEdt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showWarning(getString(R.string.warning_select_district))
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.e("", parent?.getItemAtPosition(position).toString())
                villageId = villageListIds.get(parent?.getItemAtPosition(position))!!
                edtVillageName.setText(parent?.getItemAtPosition(position).toString())
            }

        }

        edtMobileEd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                btnVerified.visibility=View.GONE
                btnVerify.visibility=View.VISIBLE
                isVerified = "0"
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        enableAll()

    }

    override fun onClick(v: View?) {
        when (v) {

            btnVerify -> {
                layIsVerified.visibility = View.VISIBLE
                requestOtp()
            }

            btnChange -> {
                layChangeVillage.visibility = View.VISIBLE
                getState()
            }

            btnSubmit -> {
                if (edtotpEdt.text.toString().trim().equals("")) {
                    showWarning(getString(R.string.warning_otp))
                } else {
                    submitOtp()
                }
            }

            imgAvatar -> {
                selectImage()
            }

            btnSave -> saveChanges()

        }
    }

    private fun selectImage() {
        if (InternetUtil.isInternetOn()) {
            selectImageFragment.show(supportFragmentManager, selectImageFragment.tag)
        } else {
            waitForInternet()
        }
    }

    fun cameraIntent() {
        selectImageFragment.dismiss()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        fileUri = Util.getOutputMediaFileUri(this@ProfileEditActivity, IMAGE_DIRECTORY_NAME)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    fun galleryIntent() {
        selectImageFragment.dismiss()
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_FROM_GALLERY)
    }

    private fun requestOtp() {

        var mobileNo=edtMobileEd.text.toString().trim()

        if (mobileNo!="" && mobileNo!=null){

            val requestOtp = RequestOtp(mobileNo, userId!!)

            showProgress()

            ApiClient.get().create(ApiInterface::class.java)
                    .requestOtp(requestOtp)
                    .enqueue(object : Callback<OtpResponse> {
                        override fun onResponse(call: Call<OtpResponse>?, response: Response<OtpResponse>?) {
                            if (response!!.code() == 200) {
                                var response = response!!.body()
                                runOnUiThread {
                                    dismissProgress()
                                    tvOtpWait.visibility=View.VISIBLE
                                }
                            }
                        }

                        override fun onFailure(call: Call<OtpResponse>?, t: Throwable?) {
                            runOnUiThread {
                                showError(getString(R.string.warning_try_later))
                                dismissProgress()
                            }
                        }
                    })
        }else{
            showError(getString(R.string.warning_empty_mobile))
        }


    }

    private fun submitOtp() {

        val otp = edtotpEdt.text.toString().trim()

        val verifyMobile = VerifyMobile(userId!!, otp)

        showProgress()

        ApiClient.get().create(ApiInterface::class.java)
                .verifyMobile(verifyMobile)
                .enqueue(object : Callback<CommonResponse> {
                    override fun onResponse(call: Call<CommonResponse>?, response: Response<CommonResponse>?) {
                        if (response!!.code() == 200) {
                            var response = response!!.body()
                            runOnUiThread {
                                if (response?.status == HttpConstant.EMPTY_REQUEST) {
                                    showError(response.message)
                                    dismissProgress()
                                } else {
                                    layIsVerified.visibility = View.GONE
                                    btnVerify.visibility = View.GONE
                                    btnVerified.visibility = View.VISIBLE
                                    edtMobileEd.isEnabled=false

                                    //get custom prefs and save data
                                    val prefs = PreferenceHelper.customPrefs(this@ProfileEditActivity,
                                            "user_info")
                                    prefs[IS_VERIFIED] = "1"
                                    isVerified="1"

                                    tvOtpWait.visibility=View.GONE

                                    dismissProgress()
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<CommonResponse>?, t: Throwable?) {
                        runOnUiThread {
                            showError(getString(R.string.warning_try_later))
                            dismissProgress()
                        }
                    }
                })
    }

    private fun saveChanges() {

        val userName = edtNameEd.text.toString()
        val userEmail = edtEmailEd.text.toString().trim()
        val userBio = edtBio.text.toString()
        val userMobile = edtMobileEd.text.toString().trim()
        val selectedVillageId = villageId

        //validate the input params
        val isValidInput: Boolean = validate(userName, userMobile, isVerified!!)

        if (isValidInput) {

            if (InternetUtil.isInternetOn()) {

                showProgress()

                val profileUpdateBody = ProfileUpdateBody(userId!!, userName, userEmail, userBio, userMobile, selectedVillageId,
                        avatar)

                ApiClient.get().create(ApiInterface::class.java)
                        .editProfile(profileUpdateBody)
                        .enqueue(object : Callback<SignInResponse> {
                            override fun onResponse(call: Call<SignInResponse>?, response: Response<SignInResponse>?) {
                                if (response!!.code() == 200) {
                                    runOnUiThread {
                                        response.body()?.let { userData.saveUserSignInInfo(it,false) }
                                        showSuccess(getString(R.string.msg_profile_update))
                                        dismissProgress()
                                        val prefs = PreferenceHelper.customPrefs(applicationContext, "user_info")
                                        prefs[ApiConstant.VILLAGE_ID] = villageId
                                        openProfileActivity()
                                    }
                                }
                            }

                            override fun onFailure(call: Call<SignInResponse>?, t: Throwable?) {
                                runOnUiThread {
                                    showError(getString(R.string.warning_try_later))
                                    dismissProgress()
                                }
                            }
                        })


            } else {
                waitForInternet()
            }
        }
    }

    private fun validate(name: String,mobile: String,
                         isVerified: String): Boolean {

        if (InputValidatorHelper.isNullOrEmpty(name)) {

            showError(getString(R.string.warning_empty_name))

            return false

        }else if (InputValidatorHelper.isNullOrEmpty(mobile)) {

            showError(getString(R.string.warning_empty_mobile))

            return false

        } else if (isVerified == "0") {

            showError(getString(R.string.warning_verify_mob_no))

            return false

        }  else {

            return true
        }


    }

    private fun getState() {

        ApiClient.get().create(ApiInterface::class.java)
                .getState()
                .enqueue(object : Callback<StateResponse> {
                    override fun onResponse(call: Call<StateResponse>?, response: Response<StateResponse>?) {
                        if (response!!.code() == 200) {
                            var stateResponse = response!!.body()
                            setState(stateResponse!!.state)
                        }
                    }

                    override fun onFailure(call: Call<StateResponse>?, t: Throwable?) {
                        runOnUiThread {
                            showError(getString(R.string.warning_try_later))
                        }

                    }
                })

    }

    private fun setState(arrayList: List<State>) {

        var stateList = ArrayList<String>()

        for (entry in arrayList) {

            if (userLang == ENGLISH) {
                var state = entry.english
                stateList.add(state)
                stateListIds[state] = entry.id
            }

            if (userLang == MARATHI) {
                var state = entry.marathi
                stateList.add(state)
                stateListIds[state] = entry.id
            }

        }

        runOnUiThread {
            var arrayAdapter = ArrayAdapter(this, R.layout.layout_spinner, stateList)
            spnStateEdt.adapter = arrayAdapter
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }


    }

    private fun getDistrict(stateId: String) {

        var districtBody = DistrictBody(stateId.toString())

        ApiClient.get().create(ApiInterface::class.java)
                .getDistrict(districtBody)
                .enqueue(object : Callback<DistrictResponse> {
                    override fun onResponse(call: Call<DistrictResponse>?, response: Response<DistrictResponse>?) {
                        if (response!!.code() == 200) {
                            var districtResponse = response!!.body()
                            setDistrict(districtResponse!!.districtList)
                        }
                    }

                    override fun onFailure(call: Call<DistrictResponse>?, t: Throwable?) {
                        runOnUiThread {
                            showError(getString(R.string.warning_try_later))
                        }
                    }
                })

    }

    private fun setDistrict(arrayList: List<District>) {

        var districtList = ArrayList<String>()

        for (entry in arrayList) {

            if (userLang == ENGLISH) {
                var district = entry.english
                districtList.add(district)
                distListIds[district] = entry.id
            }

            if (userLang == MARATHI) {
                var district = entry.marathi
                districtList.add(district)
                distListIds[district] = entry.id
            }

        }

        runOnUiThread {
            var arrayAdapter = ArrayAdapter(this, R.layout.layout_spinner, districtList)
            spnDistEdt.adapter = arrayAdapter
        }
    }

    private fun getTaluka(districtId: String) {

        var talukaBody = TalukaBody(stateId, districtId)

        ApiClient.get().create(ApiInterface::class.java)
                .getTaluka(talukaBody)
                .enqueue(object : Callback<TalukaResponse> {
                    override fun onResponse(call: Call<TalukaResponse>?, response: Response<TalukaResponse>?) {
                        if (response!!.code() == 200) {
                            var talukaResponse = response!!.body()
                            setTaluka(talukaResponse!!.talukaList)
                        }
                    }

                    override fun onFailure(call: Call<TalukaResponse>?, t: Throwable?) {
                        runOnUiThread {
                            showError(getString(R.string.warning_try_later))
                        }
                    }
                })

    }

    private fun setTaluka(arrayList: List<Taluka>) {

        if (arrayList != null) {

            var talukaList = ArrayList<String>()

            for (entry in arrayList) {

                if (userLang == ENGLISH) {
                    var taluka = entry.english
                    talukaList.add(taluka)
                    talListIds[taluka] = entry.id
                }

                if (userLang == MARATHI) {
                    var taluka = entry.marathi
                    talukaList.add(taluka)
                    talListIds[taluka] = entry.id
                }

            }

            runOnUiThread {
                var arrayAdapter = ArrayAdapter(this, R.layout.layout_spinner, talukaList)
                spnTalukaEdt.adapter = arrayAdapter
            }
        } else {
        }

    }

    private fun getVillage(talukaId: String) {

        var villageBody = VillageBody(stateId, districtId, talukaId,"Taluka")

        ApiClient.get().create(ApiInterface::class.java)
                .getVillage(villageBody)
                .enqueue(object : Callback<VillageResponse> {
                    override fun onResponse(call: Call<VillageResponse>?, response: Response<VillageResponse>?) {
                        if (response!!.code() == 200) {
                            var villageResponse = response!!.body()
                            setVillage(villageResponse!!.villageList)
                        }
                    }

                    override fun onFailure(call: Call<VillageResponse>?, t: Throwable?) {
                        runOnUiThread {
                            showError(getString(R.string.warning_try_later))
                        }
                    }
                })

    }

    private fun setVillage(arrayList: List<Village>) {

        if (arrayList != null) {

            var villageList = ArrayList<String>()

            for (entry in arrayList) {

                if (userLang == ENGLISH) {
                    var village= entry.english
                    villageList.add(village)
                    villageListIds[village] = entry.id
                }

                if (userLang == MARATHI) {
                    var village= entry.marathi
                    villageList.add(village)
                    villageListIds[village] = entry.id
                }

            }

            runOnUiThread {
                var arrayAdapter = ArrayAdapter(this, R.layout.layout_spinner, villageList)
                spnVillageEdt.adapter = arrayAdapter
            }
        } else {
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(applicationContext, HomeActivity::class.java))
        overridePendingTransition(0, 0)
        ActivityCompat.finishAffinity(this@ProfileEditActivity)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Log.e("REQUEST_IMAGE_CAPTURE", fileUri!!.path)

            setImage(fileUri.path.toString())

        } else if (requestCode == REQUEST_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {

            if (data != null) {

                Log.e("IMAGE_FROM_GALLERY", data.dataString)

                var filePath = Util.getRealPathFromUri(this, data.dataString.toString())

                Log.e("IMAGE_FROM_GALLERY", filePath)

                setImage(filePath)

            }

        }
    }

    private fun setImage(filePath: String) {
        val imageSetAsyncTask = ImageSetAsyncTask(this@ProfileEditActivity,
                this@ProfileEditActivity, IMAGE_SELECTED)
        imageSetAsyncTask.execute(filePath)
    }

    override fun setImage(imageNo: Int, bitmap: Bitmap) {
        imgAvatar.setBackgroundResource(android.R.color.transparent)
        imgAvatar.setImageBitmap(bitmap)
    }

    override fun setBitmapExecute(savedFileUrl: String) {
        //upload file
        uploadFile(savedFileUrl)
    }

    private fun uploadFile(imagePath: String) {

        showProgress()

        val file = File(imagePath)
        Log.d("", "Filename " + file.name)
        val mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val fileToUpload = MultipartBody.Part.createFormData("userfile", file.name, mFile)

        ApiClient.get().create(ApiInterface::class.java)
                .uploadAvatar(fileToUpload)
                .enqueue(object : Callback<UploadFile> {
                    override fun onResponse(call: Call<UploadFile>?, response: Response<UploadFile>?) {

                        if (response!!.code() == 200) {

                            Log.e("", "raw" + response.raw())
                            Log.e("", "body " + response.body()!!.status)
                            avatar = response.body()!!.status
                            Log.e("", "avatar :  $avatar")
                            runOnUiThread {
                                dismissProgress()
                                if (response.body()!!.status==""){
                                    imgAvatar.setImageBitmap(null)
                                    imgAvatar.setBackgroundResource(R.drawable.ic_user_profile)
                                    showError(getString(R.string.warning_try_later))
                                }
                            }

                        } else {
                            if (response.errorBody() != null) {
                                runOnUiThread {
                                    dismissProgress()
                                    imgAvatar.setImageBitmap(null)
                                    imgAvatar.setBackgroundResource(R.drawable.ic_user_profile)
                                    showError(getString(R.string.warning_try_later))
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<UploadFile>?, t: Throwable?) {
                        // showError(t.toString())

                        Log.d("", "Error " + t.toString())

                        runOnUiThread {
                            dismissProgress()
                            imgAvatar.setImageBitmap(null)
                            imgAvatar.setBackgroundResource(R.drawable.ic_user_profile)
                            showError(getString(R.string.warning_try_later))


                        }
                    }
                })


    }


    private fun enableAll() {
        btnSave.visibility = View.VISIBLE
        btnChange.visibility = View.VISIBLE

        if (isVerified.equals("1")){
            btnVerify.visibility = View.GONE
            btnVerified.visibility=View.VISIBLE
        }else{
            btnVerify.visibility = View.VISIBLE
            btnVerified.visibility=View.GONE
        }
    }

    fun openProfileActivity() {
        startActivity(Intent(this,ProfileActivity::class.java))
        finish()
    }
}
