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
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.gavkariapp.BuildConfig
import com.gavkariapp.Model.Design
import com.gavkariapp.R
import com.gavkariapp.constant.AppConstant.BIRTHDAY
import com.gavkariapp.constant.AppConstant.DASHKRIYA_VIDHI
import com.gavkariapp.constant.AppConstant.ENGAGEMENT
import com.gavkariapp.constant.AppConstant.FIRST_MEMORIAL
import com.gavkariapp.constant.AppConstant.HOUSE_WARMING
import com.gavkariapp.constant.AppConstant.JAGARAN_GONDHAL
import com.gavkariapp.constant.AppConstant.RETIREMENT
import com.gavkariapp.constant.AppConstant.SATYANARAYAN_POOJA
import com.gavkariapp.constant.AppConstant.WEDDING
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.utility.Util
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_single_design.*
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference

class SingleDesignActivity : BaseActivity() ,View.OnClickListener{

    private lateinit var design: Design

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_single_design)
        design = intent.getSerializableExtra("event_obj") as Design

        if(design.type == WEDDING || design.type == ENGAGEMENT){
            laywedding.visibility = View.VISIBLE
            layFirstMemorial.visibility = View.GONE
            layHouseWarm.visibility = View.GONE
            layJagaran.visibility = View.GONE
            tvTitleWed.text = design.title
            tvNameWed.text = design.subtitle.trim()
            tvParentWed.text = design.subtitle_two.trim()
            tvNameVadhuWed.text =  design.subtitle_three.trim()
            tvVadhuParent.text = design.subtitle_five.trim()
            tvMuhurtWed.text = design.muhurt
            tvAddressWed.text = design.address
            tvNimantrakWed.text = design.description

        }else if (design.type == FIRST_MEMORIAL || design.type == DASHKRIYA_VIDHI){
            layFirstMemorial.visibility = View.VISIBLE
            laywedding.visibility = View.GONE
            layHouseWarm.visibility = View.GONE
            layJagaran.visibility = View.GONE
            tvTitleMem.text = "|| " + design?.title + " ||"
            tvNameMem.text = design?.subtitle_one.trim()
            tvfamilyMem.text = design?.family
            tvMuhurtMem.text = design?.muhurt
            tvAddressMem.text = design?.address
            tvMoreInfoMem.text = design?.description_one
            tvMoreInfoOneMem.text = design?.description
            tvTipMem.text = design?.note

            Glide.with(imgphoto)
                    .load(HttpConstant.BASE_EVENT_DOWNLOAD_URL + design.photo)
                    .into(imgphoto)

        }else if (design.type == HOUSE_WARMING){
            layHouseWarm.visibility = View.VISIBLE
            laywedding.visibility = View.GONE
            layFirstMemorial.visibility = View.GONE
            layJagaran.visibility = View.GONE
            tvTitleHw.text = design?.title
            tvfamilyHw.text = design?.family
            tvMuhurtHw.text = design?.muhurt
            tvAddressHw.text = design?.address
            tvMoreInfoHw.text = design?.description_one
            tvMoreInfoOneHw.text = design?.description
            tvTipHw.text = design?.note

        }else if (design.type == JAGARAN_GONDHAL){
            layJagaran.visibility = View.VISIBLE
            laywedding.visibility = View.GONE
            layFirstMemorial.visibility = View.GONE
            layHouseWarm.visibility = View.GONE
            tvTitleJag.text = design?.title
            tvfamilyJag.text = design?.family
            tvMuhurtJag.text = design?.muhurt
            tvAddressJag.text = design?.address
            tvMoreInfoJag.text = design?.description_one
            tvMoreInfoOneJag.text = design?.description
            tvTipJag.text = design?.note

        }else if (design.type == BIRTHDAY){


        }else if (design.type == SATYANARAYAN_POOJA){

        }else{

        }

        imgShareWed.setOnClickListener(this)
        imgShareMem.setOnClickListener(this)
        imgShareHw.setOnClickListener(this)
        imgShareJag.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v){
            imgShareWed -> {checkGalleryPermission(laywedding)}
            imgShareMem -> {checkGalleryPermission(layFirstMemorial)}
            imgShareHw -> {checkGalleryPermission(layHouseWarm)}
            imgShareJag -> {checkGalleryPermission(layJagaran)}
        }
    }

    fun checkGalleryPermission(context: RelativeLayout) {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            imgShareWed.visibility = View.GONE
                            imgShareMem.visibility = View.GONE
                            imgShareHw.visibility = View.GONE
                            imgShareJag.visibility = View.GONE
                            MyAsyncTask(this@SingleDesignActivity,context).execute()
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // permission is denied permenantly, navigate user to app settings
                            Util.showPermissionAlert(this@SingleDesignActivity!!,
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
        class MyAsyncTask internal constructor(context: SingleDesignActivity,layout: RelativeLayout) : AsyncTask<Int, String, String?>() {

            var content= layout
            private val activityReference: WeakReference<SingleDesignActivity> = WeakReference(context)
            val activity = activityReference.get()
            override fun onPreExecute() {
                if (activity == null || activity.isFinishing) return
                (activity as SingleDesignActivity).showProgress()
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
