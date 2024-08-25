package com.gavkariapp.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.gavkariapp.Model.*
import com.gavkariapp.R
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant.IMAGE_NOT_SELECTED
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.constant.HttpConstant.SUCCESS
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.helper.ImageSetAsyncTask
import com.gavkariapp.helper.SelectImageFragmentDirectory
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import com.gavkariapp.utility.InputValidatorHelper
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.Util
import kotlinx.android.synthetic.main.activity_create_direcory.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CreateDirectoryActivity : BaseActivity(), View.OnClickListener,
        ImageSetAsyncTask.ImageCallback {


    private var avatar: String? = ""
    var userId: String? = "-1"
    var villageId: String? = "-1"
    private lateinit var selectImageFragment: SelectImageFragmentDirectory
    private val IMAGE_DIRECTORY_NAME = "GavkariApp"
    private val REQUEST_IMAGE_CAPTURE = 3
    private val REQUEST_IMAGE_FROM_GALLERY = 4
    private lateinit var fileUri: Uri
    private var IMAGE_SELECTED = IMAGE_NOT_SELECTED
    private val SAVE_DIR = 1
    private val UPDATE_DIR = 2
    private var API_CALL_TYPE : Int = SAVE_DIR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_create_direcory)
        setupToolbar(R.id.toolbarHome, getString(R.string.create_dir_contact))
        setupView()
    }

    private fun setupView() {
        selectImageFragment = SelectImageFragmentDirectory(this)

        var bundleData = intent.hasExtra("directory")
        if (bundleData){
            var myDir = intent.getSerializableExtra("directory") as DirectoryList
            Log.e("==",myDir.b_description)
            edtBusiness.setText(myDir?.business)
            edtDirTitle.setText(myDir?.b_name)
            edtMobileDir.setText(myDir?.mobile)
            edtDirDescription.setText(myDir?.b_description)
            avatar = myDir?.avatar
            if (myDir?.avatar != "") {
                imgAvatarDir.setBackgroundResource(android.R.color.transparent)
                Glide.with(imgAvatarDir)
                        .load(HttpConstant.BASE_AVATAR_DOWNLOAD_URL + myDir?.avatar)
                        .thumbnail(0.5f)
                        .into(imgAvatarDir)
            } else {
                imgAvatarDir.setBackgroundResource(R.drawable.ic_user_profile)
            }
            API_CALL_TYPE = UPDATE_DIR
        }

        var business = intent.hasExtra("business")
        if (business){
            var business = intent.getStringExtra("business")
            edtBusiness.setText(business)
            val prefs = PreferenceHelper.customPrefs(this, "user_info")
            var mobile = prefs[ApiConstant.MOBILE, "-1"]
            edtMobileDir.setText(mobile)
        }

        btnDirSave.setOnClickListener(this)
        imgAvatarDir.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v) {

            imgAvatarDir -> {
                selectImage()
            }

            btnDirSave -> saveChanges()

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
        fileUri = Util.getOutputMediaFileUri(this@CreateDirectoryActivity, IMAGE_DIRECTORY_NAME)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    fun galleryIntent() {
        selectImageFragment.dismiss()
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_FROM_GALLERY)
    }

    private fun saveChanges() {
        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        userId = prefs[ApiConstant.USER_ID, "-1"]
        villageId = prefs[ApiConstant.VILLAGE_ID, "-1"]
        val business = edtBusiness.text.toString().trim()
        val title = edtDirTitle.text.toString().trim()
        val mobile = edtMobileDir.text.toString().trim()
        var description = edtDirDescription.text.toString().trim()
        val isValidInput: Boolean = validate(business, title, mobile, description)

        if (isValidInput) {

            if (InternetUtil.isInternetOn()) {

                showProgress()
                var createDirectoryBody : CreateDirectoryBody? = null
                if (API_CALL_TYPE == SAVE_DIR){
                     createDirectoryBody = CreateDirectoryBody(userId!!,villageId!!,"0",business,
                            title,description,mobile,avatar,SAVE_DIR)
                }
                if (API_CALL_TYPE == UPDATE_DIR){
                    createDirectoryBody = CreateDirectoryBody(userId!!,villageId!!,"0",business,
                            title,description,mobile,avatar,UPDATE_DIR)
                }
                createDirectoryBody?.let {
                    ApiClient.get().create(ApiInterface::class.java)
                            .createDirectory(it)
                            .enqueue(object : Callback<CommonResponse> {
                                override fun onResponse(call: Call<CommonResponse>?, response: Response<CommonResponse>?) {
                                    if (response!!.code() == 200) {
                                        if (response.body()!!.status == SUCCESS){
                                            runOnUiThread {
                                                showSuccess(getString(R.string.new_directory_contact))
                                                dismissProgress()
                                                startActivity(Intent(this@CreateDirectoryActivity,DirectoryActivity::class.java))
                                                finish()
                                            }
                                        }else{
                                            runOnUiThread {
                                                showSuccess(response.body()!!.message)
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


            } else {
                waitForInternet()
            }
        }
    }

    private fun validate(business: String, title: String, mobile: String, description: String): Boolean {

        if (InputValidatorHelper.isNullOrEmpty(business)) {

            showError(getString(R.string.b_name))

            return false

        } else if (InputValidatorHelper.isNullOrEmpty(title)) {

            showError(getString(R.string.b_title))

            return false

        } else if (InputValidatorHelper.isNullOrEmpty(mobile)) {

            showError(getString(R.string.b_contact))

            return false

        }else if (InputValidatorHelper.isNullOrEmpty(description)) {

            showError(getString(R.string.b_description))

            return false

        }else {

            return true
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Log.e("REQUEST_IMAGE_CAPTURE", fileUri!!.path)

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

    private fun setImage(filePath: String) {
        val imageSetAsyncTask = ImageSetAsyncTask(this@CreateDirectoryActivity,
                this@CreateDirectoryActivity, IMAGE_SELECTED)
        imageSetAsyncTask.execute(filePath)
    }

    override fun setImage(imageNo: Int, bitmap: Bitmap) {
        imgAvatarDir.setBackgroundResource(android.R.color.transparent)
        imgAvatarDir.setImageBitmap(bitmap)
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
                                if (response.body()!!.status=="") {
                                    imgAvatarDir.setImageBitmap(null)
                                    imgAvatarDir.setBackgroundResource(R.drawable.ic_user_profile)
                                    showError(getString(R.string.warning_try_later))
                                }
                                dismissProgress()
                            }

                        } else {
                            if (response.errorBody() != null) {
                                runOnUiThread {
                                    dismissProgress()
                                    imgAvatarDir.setImageBitmap(null)
                                    imgAvatarDir.setBackgroundResource(R.drawable.ic_user_profile)
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
                            imgAvatarDir.setImageBitmap(null)
                            imgAvatarDir.setBackgroundResource(R.drawable.ic_user_profile)
                            showError(getString(R.string.warning_try_later))
                        }
                    }
                })


    }

    private fun enableAll() {
        edtBusiness.isEnabled = true
        edtDirTitle.isEnabled = true
        edtDirDescription.isEnabled = true
        edtMobileDir.isEnabled = true
        btnDirSave.visibility = View.VISIBLE
        imgAvatarDir.isEnabled = true
    }

    fun disableAll() {
        edtBusiness.isEnabled = false
        edtDirTitle.isEnabled = false
        edtDirDescription.isEnabled = false
        edtMobileDir.isEnabled = false
        imgAvatarDir.isEnabled = false
        btnDirSave.visibility = View.GONE
    }
}
