package com.gavkariapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gavkariapp.Model.SignOutBody
import com.gavkariapp.Model.SignOutResponse
import com.gavkariapp.R
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.AppConstant.ENGLISH
import com.gavkariapp.constant.AppConstant.MARATHI
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.constant.HttpConstant.BASE_AVATAR_DOWNLOAD_URL
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import kotlinx.android.synthetic.main.activity_my_account.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyAccountActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_my_account)
        setupToolbar(R.id.toolbarHome, "")
        btnLogout.setOnClickListener(this)
        tvPrifileDetails.setOnClickListener(this)
        tvMyNews.setOnClickListener(this)
        tvCreateNews.setOnClickListener(this)
        layAddBusiness.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        setupView()
    }

    @SuppressLint("RestrictedApi")
    override fun onClick(v: View?) {
        when (v) {

            btnLogout -> logout()

            tvPrifileDetails -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }

            tvCreateNews -> {
                startActivity(Intent(applicationContext, SelectNewsTypeActivity::class.java))
            }

            tvMyNews -> {
                startActivity(Intent(applicationContext, MyNewsActivity::class.java))
            }

            layAddBusiness -> {
                startActivity(Intent(applicationContext, AddBusinessActivity::class.java))
            }


        }
    }

    private fun setupView() {

        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        var userName: String? = prefs[ApiConstant.NAME, ""]
        var is_village_boy: String? = prefs[ApiConstant.IS_VILLAGE_BOY, ""]
        var userMobile: String? = prefs[ApiConstant.MOBILE, ""]
        var userAvatar: String? = prefs[ApiConstant.AVATAR, ""]
        var userLang: String? = prefs[AppConstant.LANGUAGE, "-1"]
        var vnEnglish: String? = prefs[ApiConstant.VN_ENGLISH, "-1"]
        var vnMarathi: String? = prefs[ApiConstant.VN_MARATHI, "-1"]
        var vnHindi: String? = prefs[ApiConstant.VN_HINDI, "-1"]

        if (is_village_boy == "1"){
            layCreateNews.visibility = View.VISIBLE
            layMyNews.visibility = View.VISIBLE
        }

        var villageName = ""
        if (userLang == ENGLISH) {
            villageName = vnEnglish.toString()
        }

        if (userLang == MARATHI) {
            villageName = vnMarathi.toString()
        }

        tvUserName.text = userName
        tvUserMob.text = userMobile
        tvUserVillage.text = villageName

        if (!userAvatar.equals("")) {
            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.drawable.ic_user_drawer)
            requestOptions.error(R.drawable.ic_user_drawer)
            Glide.with(imgAccoutAvatar)
                    .setDefaultRequestOptions(requestOptions)
                    .load(BASE_AVATAR_DOWNLOAD_URL + userAvatar)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgAccoutAvatar)
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

    private fun logout() {
        showProgress()

        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        var userId: String? = prefs[ApiConstant.USER_ID, "-1"]
        var deviceId: String? = prefs[ApiConstant.DEVICE_ID, "-1"]

        val signOutBody = SignOutBody(userId!!, deviceId!!)
        ApiClient.get().create(ApiInterface::class.java)
                .signOut(signOutBody)
                .enqueue(object : Callback<SignOutResponse> {
                    override fun onResponse(call: Call<SignOutResponse>?, response: Response<SignOutResponse>?) {
                        if (response!!.code() == 200) {
                            if (response.body()?.status == HttpConstant.SUCCESS) {
                                runOnUiThread {
                                    PreferenceHelper.ClearData(applicationContext, "user_info")

                                    showSuccess(getString(R.string.msg_logout))
                                    startActivity(Intent(this@MyAccountActivity, LanguageActivity::class.java))
                                    ActivityCompat.finishAffinity(this@MyAccountActivity)
                                    dismissProgress()
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

                    override fun onFailure(call: Call<SignOutResponse>?, t: Throwable?) {
                        runOnUiThread {
                            dismissProgress()
                            showError(t.toString())
                        }
                    }
                })

    }


}
