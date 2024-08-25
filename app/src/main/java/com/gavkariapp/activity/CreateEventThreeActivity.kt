package com.gavkariapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.gavkariapp.Model.*
import com.gavkariapp.R
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.AppConstant.CREATED
import com.gavkariapp.constant.AppConstant.OTHER_EVENT
import com.gavkariapp.constant.AppConstant.PUBLISH_LATER
import com.gavkariapp.constant.AppConstant.PUBLISH_NOW
import com.gavkariapp.constant.AppConstant.UNDER_REVIEW
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import kotlinx.android.synthetic.main.activity_create_event_three.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateEventThreeActivity : BaseActivity(),View.OnClickListener {

    private lateinit var createAdBody: CreateAdBody

    private var publish_type = PUBLISH_NOW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_create_event_three)
        setupToolbar(R.id.toolbarHome, getString(R.string.title_create_ad))
        setUpView()
    }

    private fun setUpView() {
        btnContinueThree.setOnClickListener(this)
        tvTerms.setOnClickListener(this)
        layPublishNow.setOnClickListener(this)
        rdPublishNow.setOnClickListener(this)
        layPublishLater.setOnClickListener(this)
        rdPublishLater.setOnClickListener(this)
        createAdBody = intent.getSerializableExtra("createAdBody") as CreateAdBody
        if (createAdBody.type == AppConstant.BIRTHDAY ||createAdBody.type == AppConstant.RETIREMENT ||createAdBody.type == AppConstant.SATYANARAYAN_POOJA ||
                createAdBody.type == AppConstant.MAHAPRASAD ||createAdBody.type == OTHER_EVENT){
            tvInfo.text = getString(R.string.lbl_share_over_social)
        }
        publishNow()
    }

    override fun onClick(v: View?) {

        when (v) {
            layPublishNow -> { publishNow()}
            rdPublishNow -> { publishNow()}
            layPublishLater -> {publishLater()}
            rdPublishLater -> {publishLater()}
            btnContinueThree -> continueThree()
            tvTerms->{
                startActivity(Intent(applicationContext, LegalActivity::class.java))
            }
        }
    }

    private fun publishNow(){

        publish_type = PUBLISH_NOW
        createAdBody.status = UNDER_REVIEW

        rdPublishNow.isChecked = true
        layPublishNow.setBackgroundResource(R.drawable.border_select_plan)
        rdPublishNow.setTextColor(resources.getColor(R.color.colorPrimary))

        rdPublishLater.isChecked = false
        layPublishLater.setBackgroundResource(R.drawable.border_square_gray)
        rdPublishLater.setTextColor(resources.getColor(R.color.gray))

        btnContinueThree.text = getString(R.string.lbl_publish)

    }

    private fun publishLater(){

        publish_type = PUBLISH_LATER
        createAdBody.status = CREATED

        rdPublishLater.isChecked = true
        layPublishLater.setBackgroundResource(R.drawable.border_select_plan)
        rdPublishLater.setTextColor(resources.getColor(R.color.colorPrimary))

        rdPublishNow.isChecked = false
        layPublishNow.setBackgroundResource(R.drawable.border_square_gray)
        rdPublishNow.setTextColor(resources.getColor(R.color.gray))

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun continueThree() {

        val createAdBody = CreateAdBody(
                createAdBody.user_id,
                createAdBody.village_id,
                createAdBody.type,
                createAdBody.title,
                createAdBody.subtitle,
                createAdBody.subtitle_one,
                createAdBody.subtitle_two,
                createAdBody.subtitle_three,
                createAdBody.subtitle_four,
                createAdBody.subtitle_five,
                createAdBody.family,
                createAdBody.description,
                createAdBody.description_one,
                createAdBody.event_date,
                createAdBody.event_date_ms,
                createAdBody.muhurt,
                createAdBody.event_media,
                createAdBody.address,
                createAdBody.location,
                createAdBody.latitude,
                createAdBody.longitude,
                createAdBody.contact_no,
                createAdBody.note,
                createAdBody.photo,
                createAdBody.event_id,
                createAdBody.event_aid,
                createAdBody.status)

        if (checkBox.isChecked){
            publishEvent(createAdBody)
        }else{
            showWarning(getString(R.string.war_accept_tems))
        }


    }

    private fun publishEvent(createAdBody: CreateAdBody) {

        showProgress()

        ApiClient.get().create(ApiInterface::class.java)
                .createEvent(createAdBody)
                .enqueue(object : Callback<SaveEventResponse> {
                    override fun onResponse(call: Call<SaveEventResponse>?, response: Response<SaveEventResponse>?) {
                        if (response!!.code() == 200) {
                            if (response.body()?.status == HttpConstant.SUCCESS) {
                                var saveEventResponse = response.body()
                                runOnUiThread {
                                    dismissProgress()
                                    when (publish_type) {

                                        PUBLISH_NOW -> {
                                            startActivity(Intent(this@CreateEventThreeActivity,SuccessActivity::class.java)
                                                    .putExtra("screen_type","event"))
                                            ActivityCompat.finishAffinity(this@CreateEventThreeActivity)
                                        }
                                        else -> {
                                            showSuccess(getString(R.string.msg_event_created))
                                            startActivity(Intent(this@CreateEventThreeActivity,MyEventActivity::class.java))
                                            ActivityCompat.finishAffinity(this@CreateEventThreeActivity)
                                        }
                                    }
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

                    override fun onFailure(call: Call<SaveEventResponse>?, t: Throwable?) {
                        runOnUiThread {
                            dismissProgress()
                            showError(t.toString())
                        }
                    }
                })

    }

}
