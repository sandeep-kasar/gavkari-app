package com.gavkariapp.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.gavkariapp.Model.*
import com.gavkariapp.R
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.AppConstant.IMAGE_1_SELECTED
import com.gavkariapp.constant.AppConstant.IMAGE_2_SELECTED
import com.gavkariapp.constant.AppConstant.IMAGE_3_SELECTED
import com.gavkariapp.constant.AppConstant.IMAGE_NOT_SELECTED
import com.gavkariapp.constant.AppConstant.UNDER_REVIEW
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.constant.HttpConstant.BASE_NEWS_DOWNLOAD_URL
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.helper.DatePicker
import com.gavkariapp.helper.ImageSetAsyncTask
import com.gavkariapp.helper.SelectImageEditNews
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import com.gavkariapp.utility.InputValidatorHelper
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.Util
import kotlinx.android.synthetic.main.activity_create_news.*
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


open class EditNewsActivity : BaseActivity(), View.OnClickListener,ImageSetAsyncTask.ImageCallback {


    private val IMAGE_DIRECTORY_NAME = "GavkariApp"
    private val REQUEST_IMAGE_CAPTURE = 3
    private val REQUEST_IMAGE_FROM_GALLERY = 4
    private lateinit var selectImageFragment: SelectImageEditNews
    private lateinit var fileUri: Uri
    private var IMAGE_SELECTED = IMAGE_NOT_SELECTED
    private lateinit var hashMap: HashMap<String, String>
    private lateinit var news: MyNews

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_create_news)
        setupToolbar(R.id.toolbarHome, getString(R.string.title_create_news))
        setUpView()
        btnNewsContinueTwo.setOnClickListener(this)
        img_event_1.setOnClickListener(this)
        img_event_2.setOnClickListener(this)
        img_event_3.setOnClickListener(this)
        tvNewsDate.setOnClickListener(this)
        tvNewsTime.setOnClickListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onClick(v: View?) {

        when (v) {
            btnNewsContinueTwo -> continueTwo()
            tvNewsDate -> selectDate()
            tvNewsTime -> selectTime()
            img_event_1 -> selectImage(IMAGE_1_SELECTED)
            img_event_2 -> selectImage(IMAGE_2_SELECTED)
            img_event_3 -> selectImage(IMAGE_3_SELECTED)
        }
    }

    private fun selectDate() {
        val date = DatePicker()
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
                tvNewsDate.text = (dayOfMonth).toString() + "/" + (monthOfYear + 1).toString() + "/" + (year).toString()
            }

    private fun selectTime() {
        val calendar = Calendar.getInstance()
        val tpd = TimePickerDialog(this, R.style.DatePickerDialogTheme,
                TimePickerDialog.OnTimeSetListener { _, i, i1 ->
                    tvNewsTime.text = "$i:$i1"
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
        tpd.show()
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

    private fun setUpView() {

        selectImageFragment = SelectImageEditNews(this)
        hashMap = HashMap()

        viewImage1 = findViewById<View>(R.id.img_event_1)
        img_event1 = viewImage1.findViewById(R.id.img_event) as ImageView
        img_cam1 = viewImage1.findViewById(R.id.tv_add_photo) as TextView
        rel_top1 = viewImage1.findViewById(R.id.rel_top) as RelativeLayout
        viewImage2 = findViewById<View>(R.id.img_event_2)
        img_event2 = viewImage2.findViewById(R.id.img_event) as ImageView
        img_cam2 = viewImage2.findViewById(R.id.tv_add_photo) as TextView
        rel_top2 = viewImage2.findViewById(R.id.rel_top) as RelativeLayout
        viewImage3 = findViewById<View>(R.id.img_event_3)
        img_event3 = viewImage3.findViewById(R.id.img_event) as ImageView
        img_cam3 = viewImage3.findViewById(R.id.tv_add_photo) as TextView
        rel_top3 = viewImage3.findViewById(R.id.rel_top) as RelativeLayout

        news = intent.getSerializableExtra("news") as MyNews

        if(news!=null){
            edtNewsTitle.setText(news.title)
            edtNewsTitle.hint = news.title
            edtNewsSource.setText(news.source)
            edtNewsSource.hint = news.source
            edtNewsDescription.setText(news.description)
            edtNewsDescription.hint = news.description

            var date = Util.getFormatedDateEnglish(news.news_date, "yyyy-MM-dd HH:mm:ss", "dd/MM/YYYY")
            var time = Util.getFormatedDateEnglish(news.news_date, "yyyy-MM-dd HH:mm:ss", "HH:mm")
            tvNewsDate.text = date
            tvNewsTime.text = time
        }

        var arrayList = news.news_media

        var i = IMAGE_1_SELECTED

        for (entry in arrayList) {
            if (i == IMAGE_1_SELECTED) {
                Glide.with(applicationContext)
                        .load(HttpConstant.BASE_NEWS_DOWNLOAD_URL + entry.photo)
                        .thumbnail(0.5f)
                        .into(img_event1)
                img_cam1.visibility = View.GONE
                rel_top1.visibility = View.VISIBLE
                hashMap["IMAGE_1_SELECTED"] = entry.photo
            }

            if (i == IMAGE_2_SELECTED) {
                Glide.with(applicationContext)
                        .load(BASE_NEWS_DOWNLOAD_URL + entry.photo)
                        .thumbnail(0.5f)
                        .into(img_event2)
                img_cam2.visibility = View.GONE
                rel_top2.visibility = View.VISIBLE
                hashMap["IMAGE_2_SELECTED"] = entry.photo
            }

            if (i == IMAGE_3_SELECTED) {
                Glide.with(applicationContext)
                        .load(BASE_NEWS_DOWNLOAD_URL + entry.photo)
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

    }


    fun cameraIntent() {
        selectImageFragment.dismiss()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        fileUri = Util.getOutputMediaFileUri(this@EditNewsActivity, IMAGE_DIRECTORY_NAME)
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

    private fun continueTwo() {

        //get default prefs
        val prefs = PreferenceHelper.customPrefs(this, "user_info")

        val useId: String? = prefs[ApiConstant.USER_ID, "-1"]
        val villageId: String? = prefs[ApiConstant.VILLAGE_ID, "-1"]

        //take data
        var title = edtNewsTitle.text.toString()
        var source = edtNewsSource.text.toString()
        var short_description = edtNewsSource.text.toString()
        var description = edtNewsDescription.text.toString()
        var date = tvNewsDate.text.toString().trim()
        var time = tvNewsTime.text.toString().trim()


        var isTrue = validate(title,source,description)

        if (isTrue) {

            var inputDate = Util.getFormatedDateEnglish("$date $time",
                    "dd/MM/yyyy HH:mm", "yyyy-MM-dd HH:mm:ss")

            var millisecondsFromNow = System.currentTimeMillis()

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

            val createNewsBody = CreateNewsBody(news.id,useId!!, villageId!!,"0","0",news.news_type,
                    UNDER_REVIEW, inputDate.toString(),millisecondsFromNow,title,source,photo,description,eventMediaList,false)

            saveAndContinue(createNewsBody)

        }

    }
    
    private fun saveAndContinue(cad: CreateNewsBody) {

        showProgress()

        ApiClient.get().create(ApiInterface::class.java)
                .editNews(cad)
                .enqueue(object : Callback<CommonResponse> {
                    override fun onResponse(call: Call<CommonResponse>?, response: Response<CommonResponse>?) {
                        if (response!!.code() == 200) {
                            if (response.body()?.status == HttpConstant.SUCCESS) {
                                runOnUiThread {
                                    dismissProgress()
                                    startActivity(Intent(this@EditNewsActivity,SuccessActivity::class.java).
                                            putExtra("screen_type","news"))
                                    ActivityCompat.finishAffinity(this@EditNewsActivity)
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
        val imageSetAsyncTask = ImageSetAsyncTask(this@EditNewsActivity,
                this@EditNewsActivity, IMAGE_SELECTED)
        imageSetAsyncTask.execute(filePath)
    }

    override fun setBitmapExecute(savedFileUrl: String) {
        uploadFile(savedFileUrl)
    }

    private fun validate(title: String,source: String, description: String): Boolean {


        when {

            InputValidatorHelper.isNullOrEmpty(title) -> {

                showWarning(getString(R.string.warning_news_titles))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(title) -> {

                showWarning(getString(R.string.lbl_source))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(description) -> {

                showError(getString(R.string.warning_news_description))

                return false

            }

            /*(hashMap.size == 0 || hashMap == null) -> {

                showWarning(getString(R.string.upload_news_pic))

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
                .uploadNews(fileToUpload)
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
                        }
                    }
                })
        
    }

    override fun setImage(imageNo: Int, bitmap: Bitmap) {

        if (imageNo === IMAGE_1_SELECTED) {

            img_event1.setImageBitmap(bitmap)
            img_cam1.setVisibility(View.GONE)
            rel_top1.setVisibility(View.VISIBLE)
        }

        if (imageNo === IMAGE_2_SELECTED) {

            img_event2.setImageBitmap(bitmap)
            img_cam2.setVisibility(View.GONE)
            rel_top2.setVisibility(View.VISIBLE)
        }

        if (imageNo === IMAGE_3_SELECTED) {

            img_event3.setImageBitmap(bitmap)
            img_cam3.setVisibility(View.GONE)
            rel_top3.setVisibility(View.VISIBLE)
        }
    }

}
