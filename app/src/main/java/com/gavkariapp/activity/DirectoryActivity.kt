package com.gavkariapp.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gavkariapp.Model.ApiResponse
import com.gavkariapp.Model.DirectoryIntent
import com.gavkariapp.Model.DirectoryList
import com.gavkariapp.Model.DirectoryListResponse
import com.gavkariapp.R
import com.gavkariapp.adapter.DirectoryAdapter
import com.gavkariapp.adapter.onClickCall
import com.gavkariapp.base.MyApplication
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.interfaces.AlertMessageCallback
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.viewmodel.HomeViewModel
import com.vatsal.imagezoomer.ZoomAnimation
import kotlinx.android.synthetic.main.activity_directory.*


class DirectoryActivity : BaseActivity(), onClickCall, AlertMessageCallback {

    private val CALL_PERMISSION = 102

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var observer: Observer<ApiResponse<DirectoryListResponse, String>>

    private lateinit var directoryListResponse: DirectoryListResponse

    lateinit var directoryIntent : DirectoryIntent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_directory)
        setupToolbar(R.id.toolbarHome, getString(R.string.title_directory))

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        rvDirectory.layoutManager = LinearLayoutManager(this)

        initObserver()

        directoryIntent = intent.getSerializableExtra("directoryIntent") as DirectoryIntent


    }

    override fun onResume() {
        super.onResume()
        accessDirectory()
    }

    private fun accessDirectory() {

        showProgress()

        if (InternetUtil.isInternetOn()) {
            //pass data to view model
            homeViewModel.accessDirectoryCall(directoryIntent.village_id!!).observe(this, observer)
        } else {
            //observe internet connection
            dismissProgress()
            waitForInternetConnection()
        }


    }

    private fun initObserver() {

        observer = Observer { t ->

            //hide progress bar
            dismissProgress()

            if (t?.response != null) {

                directoryListResponse = t.response!!

                if (directoryListResponse?.status == HttpConstant.SUCCESS) {
                    displayData(t.response!!.directoryList)
                } else {
                    dismissProgress()
                    Log.e("warning", directoryListResponse!!.message)
                    rvDirectory.visibility = View.GONE
                    layNoInternet.visibility = View.GONE
                    layNoData.visibility = View.VISIBLE

                }

            } else {
                Log.e("warning", t?.error)
                dismissProgress()
                showWarning(getString(R.string.msg_unexpected_error))
            }
        }
    }

    private fun displayData(response: ArrayList<DirectoryList>) {
        rvDirectory.visibility = View.VISIBLE
        layNoInternet.visibility = View.GONE
        layNoData.visibility = View.GONE
        var directoryAdapter = DirectoryAdapter(response, this)
        rvDirectory.adapter = directoryAdapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun waitForInternetConnection() {

        InternetUtil.observe(this, Observer { status ->
            if (status!!) {
                rvDirectory.visibility = View.VISIBLE
                layNoInternet.visibility = View.GONE
                layNoData.visibility = View.GONE

            } else {
                layNoInternet.visibility = View.VISIBLE
                rvDirectory.visibility = View.GONE
                layNoData.visibility = View.GONE

            }
        })
    }

    override fun zoom(v :View) {
        var zoomAnimation = ZoomAnimation(this)
        zoomAnimation.zoom(v, 200)
    }

    override fun updateDir(item: DirectoryList) {
        startActivity(Intent(applicationContext, CreateDirectoryActivity::class.java)
                .putExtra("directory", item))
    }

    override fun call(item: DirectoryList) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.mobile))
        startActivity(intent)
    }

    private fun showSettingsDialog() {
        showAlert(this@DirectoryActivity, this@DirectoryActivity,
                getString(R.string.title_need_permission), getString(R.string.message_permission_call),
                getString(R.string.lbl_grant), getString(R.string.lbl_cancel))
    }

    override fun setPositiveButton(message: String) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, CALL_PERMISSION)
    }

    override fun setNegativeButton(message: String) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CALL_PERMISSION) {
            if (ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.msg_calling), Toast.LENGTH_SHORT).show()
            } else {
                showError(getString(R.string.warning_location_permission))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        doAsync().execute()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.direcory_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_busi -> {
                startActivity(Intent(applicationContext, AddBusinessActivity::class.java))
                return true
                /*val prefs = PreferenceHelper.customPrefs(this, "user_info")
                var isVerified = prefs[ApiConstant.IS_VERIFIED, "-1"]
                if (isVerified == "0") {
                    startActivity(Intent(applicationContext, ProfileEditActivity::class.java)
                            .putExtra("editable",true))
                    showWarning(getString(R.string.msg_edit_profile))
                    return true
                }else{
                    startActivity(Intent(applicationContext, AddBusinessActivity::class.java))
                    return true
                }*/
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        var item = menu!!.findItem(R.id.action_add_busi)
        item.isVisible = directoryIntent.is_my_village == AppConstant.YES
        return super.onPrepareOptionsMenu(menu)
    }

    class doAsync: AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            Glide.get(MyApplication.instance).clearDiskCache()
            return null
        }
    }

}

