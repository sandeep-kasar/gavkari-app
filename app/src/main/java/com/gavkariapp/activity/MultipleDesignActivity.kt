package com.gavkariapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.gavkariapp.BuildConfig
import com.gavkariapp.Model.Design
import com.gavkariapp.R
import com.gavkariapp.adapter.ViewPagerAdapter
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.AppConstant.ENGAGEMENT
import com.gavkariapp.constant.AppConstant.WEDDING
import com.gavkariapp.utility.Util
import com.gavkariapp.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayout
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference

class MultipleDesignActivity : BaseActivity() {

    private var sharedViewModel: HomeViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_multiple_design)
        sharedViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        var bundleData = intent.getSerializableExtra("event_obj")
        var design = bundleData as Design
        sharedViewModel!!.designResponse.postValue(design)
        setupNavigationTab(design)
    }

    private fun setupNavigationTab(design: Design) {

        //init ViewPager
        val viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        if (viewPager != null) {
            setupViewPager(viewPager,design)
        }

        //init TabLayout and set viewpager
        val tabLayout = findViewById<View>(R.id.tabsWed) as TabLayout
        tabLayout.setupWithViewPager(viewPager)

    }

    private fun setupViewPager(viewPager: ViewPager,design: Design) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        if (design.type == WEDDING || design.type == ENGAGEMENT ) {

            /*adapter.addFragment(WedCardOneFragment(), getString(R.string.lbl_card_one))
            adapter.addFragment(WedCardTwoFragment(), getString(R.string.lbl_card_two))
            adapter.addFragment(WedCardThreeFragment(), getString(R.string.lbl_card_three))
            adapter.addFragment(WedCardFourFragment(), getString(R.string.lbl_card_four))
            adapter.addFragment(WedCardFiveFragment(), getString(R.string.lbl_card_five))*/

        }else if (design.type == AppConstant.FIRST_MEMORIAL || design.type == AppConstant.DASHKRIYA_VIDHI){

           /* adapter.addFragment(FMCardOneFragment(), getString(R.string.lbl_card_one))
            adapter.addFragment(FMCardOneFragment(), getString(R.string.lbl_card_two))
            adapter.addFragment(FMCardOneFragment(), getString(R.string.lbl_card_three))*/

        }else if (design.type == AppConstant.HOUSE_WARMING){

            /*adapter.addFragment(HWCardOneFragment(), getString(R.string.lbl_card_one))
            adapter.addFragment(HWCardOneFragment(), getString(R.string.lbl_card_two))
            adapter.addFragment(HWCardOneFragment(), getString(R.string.lbl_card_three))*/

        }else if (design.type == AppConstant.JAGARAN_GONDHAL){

            /*adapter.addFragment(JagCardOneFragment(), getString(R.string.lbl_card_one))
            adapter.addFragment(JagCardOneFragment(), getString(R.string.lbl_card_two))
            adapter.addFragment(JagCardOneFragment(), getString(R.string.lbl_card_three))*/

        }else if (design.type == AppConstant.BIRTHDAY){

        }else if (design.type == AppConstant.SATYANARAYAN_POOJA){

        }else if (design.type == AppConstant.MAHAPRASAD){

        }else{

        }

        viewPager.adapter = adapter
        val pageChangeListener = object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(newPosition: Int) {}
            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        }
        viewPager.addOnPageChangeListener(pageChangeListener)
    }

    open fun checkGalleryPermission(context: LinearLayout) {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            MyAsyncTask(this@MultipleDesignActivity,context).execute()
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // permission is denied permenantly, navigate user to app settings
                            Util.showPermissionAlert(this@MultipleDesignActivity!!,
                                    getString(R.string.message_enable_permission), getString(R.string.app_name))
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
    }

    companion object {
        class MyAsyncTask internal constructor(context: MultipleDesignActivity,layout: LinearLayout) : AsyncTask<Int, String, String?>() {

            var content= layout
            private val activityReference: WeakReference<MultipleDesignActivity> = WeakReference(context)
            val activity = activityReference.get()
            override fun onPreExecute() {
                if (activity == null || activity.isFinishing) return
                (activity as MultipleDesignActivity).showProgress()
            }

            @SuppressLint("WrongThread")
            override fun doInBackground(vararg params: Int?): String? {

                content?.isDrawingCacheEnabled = true
                var bitmap = content?.drawingCache
                val root = Environment.getExternalStorageDirectory()
                val cachePath = File(root.absolutePath + "/DCIM/Camera/image.jpg")

                try {
                    cachePath.createNewFile()
                    val ostream = FileOutputStream(cachePath)
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, ostream)
                    ostream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                var uri: Uri? = null
                uri = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    FileProvider.getUriForFile(activity!!.application, BuildConfig.APPLICATION_ID+".provider",
                            cachePath)
                } else {
                    Uri.fromFile(cachePath)
                }

                val share = Intent(Intent.ACTION_SEND)
                share.putExtra(Intent.EXTRA_STREAM, uri)
                share.putExtra(Intent.EXTRA_TEXT,
                        activity?.getString(R.string.msg_share_more_info) + "\n"
                                + activity?.getString(R.string.url_gavkari_app_play_store));
                share.type = "image/*"
                activity?.startActivity(Intent.createChooser(share, activity?.getString(R.string.tittle_share_via)))

                return "done"
            }

            override fun onPostExecute(result: String?) {
                if (activity == null || activity.isFinishing) return
                activity.dismissProgress()
            }

        }
    }
}
