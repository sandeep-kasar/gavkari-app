package com.gavkariapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gavkariapp.Model.*
import com.gavkariapp.R
import com.gavkariapp.adapter.ViewPagerAdapter
import com.gavkariapp.base.MyApplication
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.ApiConstant.VILLAGE_ID
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.HttpConstant.BASE_AVATAR_DOWNLOAD_URL
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.fragment.*
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.viewmodel.HomeViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference


class HomeActivity : BaseActivity() {

    private var navigationView: NavigationView? = null
    private var drawer: DrawerLayout? = null
    private lateinit var toolbar: Toolbar
    lateinit var observer: Observer<ApiResponse<BuySaleResponse, String>>
    private var sharedViewModel: HomeViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applyLocale(this)

        setContentView(R.layout.activity_home)

        setUpView()

        setUpNavigationView()

        setupNavigationTab()

        initObserver()

        accessData()

    }

    fun initObserver() {
        observer = Observer { t ->
            if (t?.response != null) {
                sharedViewModel!!.buySaleResponse.postValue(t.response)
                checkForAppUpdate()
            }else{
                showError(getString(R.string.msg_unexpected_error))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        drawer!!.closeDrawers()
        Log.e("Home activity","onResume")
        loadNavHeader()
    }

    private fun setUpView() {

        switchNotification(intent)

        // initialize widgets
        toolbar = findViewById<View>(R.id.toolbarHome) as Toolbar
        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        navigationView = findViewById<View>(R.id.nav_view) as NavigationView

        //setup view
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.app_name)

        /*val view = layoutInflater.inflate(R.layout.layout_select_village_toolbar, null)
        toolbarHome.addView(view,Toolbar.LayoutParams(Gravity.RIGHT))*/

        sharedViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)


        val navHeader = navigationView!!.getHeaderView(0)
        val imgHomeAvatar = navHeader.findViewById<ImageView>(R.id.imgHomeAvatar)
        imgHomeAvatar.setOnClickListener {
            startActivity(Intent(applicationContext, ProfileActivity::class.java))
        }

    }

    private fun switchNotification(intent: Intent){
        if (intent.extras != null)
        {
            var notificationBody = NotificationBody("","")

            for (key in intent.extras!!.keySet())
            {
                var value = intent.extras!!.getString(key)
                if(key =="type"){
                    notificationBody.type = value.toString()
                }
                if(key =="id"){
                    notificationBody.id = value.toString()
                }
            }
            if (notificationBody.type == "notification"){
                startActivity(Intent(applicationContext, MyNotificationActivity::class.java))
            }else{
                startActivity(Intent(applicationContext, NotificationActivity::class.java)
                        .putExtra("notificationBody",notificationBody))
            }
        }
    }

    private fun loadNavHeader() {

        val navHeader = navigationView!!.getHeaderView(0)
        val tvUserName = navHeader.findViewById<TextView>(R.id.tvUserName)
        val tvEmail = navHeader.findViewById<TextView>(R.id.tvEmail)
        val imgHomeAvatar = navHeader.findViewById<ImageView>(R.id.imgHomeAvatar)
        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        var userId: String? = prefs[ApiConstant.USER_ID, "-1"]
        var userAvatar: String? = prefs[ApiConstant.AVATAR, ""]
        var userMobile: String? = prefs[ApiConstant.MOBILE, ""]
        var userName: String? = prefs[ApiConstant.NAME, ""]
        if (userId !="-1") {
            tvUserName.text = userName
            tvEmail.text = userMobile
            if (!userAvatar.run { equals("") }) {
                val requestOptions = RequestOptions()
                requestOptions.placeholder(R.drawable.ic_user_drawer)
                requestOptions.error(R.drawable.ic_user_drawer)
                Glide.with(imgHomeAvatar)
                        .load(BASE_AVATAR_DOWNLOAD_URL + userAvatar)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imgHomeAvatar)

            }
        }

    }

    private fun setUpNavigationView() {

        navigationView!!.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            // This method will trigger on item Click of navigation menu
            when (menuItem.itemId) {
                R.id.nav_my_ad -> {
                    startActivity(Intent(applicationContext, MyEventActivity::class.java))
                    finish()
                    drawer!!.closeDrawers()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_my_sale_ad -> {
                    startActivity(Intent(applicationContext, MySaleAdActivity::class.java))
                    drawer!!.closeDrawers()
                    return@OnNavigationItemSelectedListener true
                }

                R.id.nav_notification -> {
                    startActivity(Intent(applicationContext, MyNotificationActivity::class.java))
                    drawer!!.closeDrawers()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_setting -> {
                    startActivity(Intent(applicationContext, Settings::class.java))
                    drawer!!.closeDrawers()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_help -> {
                    startActivity(Intent(applicationContext, HelpActivity::class.java))
                    drawer!!.closeDrawers()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_share -> {
                    MyAsyncTask(this).execute()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_my_account -> {
                    startActivity(Intent(applicationContext, MyAccountActivity::class.java))
                    drawer!!.closeDrawers()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_legal -> {
                    startActivity(Intent(applicationContext, LegalActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }
                else -> drawer!!.closeDrawers()
            }

            //Checking if the item is in checked state or not, if not make it in checked state
            menuItem.isChecked = !menuItem.isChecked
            menuItem.isChecked = true

            true
        })


        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.opendrawer, R.string.closedrawer) {}

        drawer!!.addDrawerListener(actionBarDrawerToggle)

        actionBarDrawerToggle.isDrawerIndicatorEnabled = false

        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu)

        actionBarDrawerToggle.syncState()

        actionBarDrawerToggle.toolbarNavigationClickListener = View.OnClickListener { drawer!!.openDrawer(GravityCompat.START) }

    }

    private fun setupNavigationTab() {

        //init ViewPager
        val viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        if (viewPager != null) {
            setupViewPager(viewPager)
        }

        //init TabLayout and set viewpager
        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(viewPager)

    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(BuySaleFragment(), getString(R.string.title_buy_sale))
        adapter.addFragment(EventFragment(), getString(R.string.hint_my_village))
        adapter.addFragment(NewsFragment(), getString(R.string.title_news))
        viewPager.adapter = adapter

        val pageChangeListener = object : ViewPager.OnPageChangeListener {
            var currentPosition = 0
            override fun onPageSelected(newPosition: Int) {
//                val fragmentToShow = adapter.getItem(newPosition) as FragmentLifecycle
//                fragmentToShow.onResumeFragment()
//                currentPosition = newPosition
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        }
        viewPager.addOnPageChangeListener(pageChangeListener)

    }

    fun accessData() {
        if (InternetUtil.isInternetOn()) {
            val prefs = PreferenceHelper.customPrefs(applicationContext, "user_info")
            var userId : String? = prefs[ApiConstant.USER_ID, ""]
            var villageId : String? = prefs[VILLAGE_ID, ""]
            var latitude : String? = prefs[ApiConstant.LATITUDE, ""]
            var longitude : String? = prefs[ApiConstant.LONGITUDE, ""]
            var buySaleBody = BuySaleBody(userId!!,villageId!!,latitude!!,longitude!!)
            sharedViewModel!!.getBuySaleData(buySaleBody).observe(this, observer)
        } else {
            waitForInternet()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DoAsync().execute()
    }

    private val localBitmapUri: Uri? get() {
            var bmpUri: Uri? = null
            val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png")
            var out: FileOutputStream?
            try {
                val bmp = BitmapFactory.decodeResource(resources, R.drawable.gavkari_banner)
                out = FileOutputStream(file)
                bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
                try {
                    out.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                bmpUri = Uri.fromFile(file)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            return bmpUri
        }

    class DoAsync: AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            Glide.get(MyApplication.instance).clearDiskCache()
            return null
        }
    }

    companion object {
        class MyAsyncTask internal constructor(context: HomeActivity) : AsyncTask<Int, String, String?>() {

            private val activityReference: WeakReference<HomeActivity> = WeakReference(context)
            val activity = activityReference.get()
            override fun onPreExecute() {
                if (activity == null || activity.isFinishing) return
                activity.showProgress()
            }

            override fun doInBackground(vararg params: Int?): String? {

                val bmpUri = activity?.localBitmapUri
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_TEXT, activity?.getString(R.string.url_gavkari_app_play_store))
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
                shareIntent.type = "image/png"
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                activity?.startActivity(Intent.createChooser(shareIntent, "Share"))

                return "done"
            }


            override fun onPostExecute(result: String?) {
                if (activity == null || activity.isFinishing) return
                activity.dismissProgress()
            }

        }
    }

    private fun checkForAppUpdate() {
        // Creates instance of the manager.
        val appUpdateManager = AppUpdateManagerFactory.create(applicationContext)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                // Request the update.
                appUpdateManager.startUpdateFlowForResult(
                        // Pass the intent that is returned by 'getAppUpdateInfo()'.
                        appUpdateInfo,
                        // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                        AppUpdateType.FLEXIBLE,
                        // The current activity making the update request.
                        this,
                        // Include a request code to later monitor this update request.
                        AppConstant.REQ_CODE_VERSION_UPDATE)
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AppConstant.REQ_CODE_VERSION_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e("UPDATE","Update flow failed! Result code: $resultCode")
                // If the update is cancelled or fails,
                // you can request to start the update again.
                Toast.makeText(this,"Update flow failed",Toast.LENGTH_LONG).show()
            }else{
                Log.e("UPDATE","Update flow success! Result code: $resultCode")
                Toast.makeText(this,"Update flow success",Toast.LENGTH_LONG).show()
            }
        }
    }

}


//app:layout_scrollFlags="scroll|enterAlways"