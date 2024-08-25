package com.gavkariapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gavkariapp.R
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        applyLocale(this)
        setupToolbar(R.id.toolbarHome, getString(R.string.title_profile_details))
        setupView()
    }

    private fun setupView(){
        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        var userName: String? = prefs[ApiConstant.NAME, "-1"]
        var userEmail: String? = prefs[ApiConstant.EMAIL, "-1"]
        var bio: String? = prefs[ApiConstant.BIO, "Farmer"]
        var userMobile: String? = prefs[ApiConstant.MOBILE, "-1"]
        var userLang = prefs[AppConstant.LANGUAGE, "-1"]
        var userAvatar: String? = prefs[ApiConstant.AVATAR, ""]
        var vnEnglish: String? = prefs[ApiConstant.VN_ENGLISH, "-1"]
        var vnMarathi: String? = prefs[ApiConstant.VN_MARATHI, "-1"]

        var villageName = ""
        if (userLang == AppConstant.ENGLISH) {
            villageName = vnEnglish.toString()
        }

        if (userLang == AppConstant.MARATHI) {
            villageName = vnMarathi.toString()
        }

        tvName.text = userName
        tvEmail.text = userEmail
        tvBio.text = bio
        tvMobile.text = userMobile
        tvVillage.text = villageName

        imgAvatar.setBackgroundResource(android.R.color.transparent)
        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.ic_user_info)
        requestOptions.error(R.drawable.ic_user_info)
        Glide.with(imgAvatar)
                .setDefaultRequestOptions(requestOptions)
                .load(HttpConstant.BASE_AVATAR_DOWNLOAD_URL + userAvatar)
                .apply(RequestOptions.circleCropTransform())
                .into(imgAvatar)

        btnEdit.setOnClickListener {
            startActivity(Intent(this,ProfileEditActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item?.itemId) {
            R.id.action_edit -> {
                startActivity(Intent(this,ProfileEditActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
