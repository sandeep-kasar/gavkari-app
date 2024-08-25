package com.gavkariapp.helper

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.gavkariapp.R
import com.gavkariapp.activity.ProfileEditActivity
import com.gavkariapp.utility.Util
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


@SuppressLint("ValidFragment")
class SelectImageFragmentProfile(private val profileEditActivity: ProfileEditActivity) :
        BottomSheetDialogFragment() {

    // declare widgets
    private var tv_select_from_gal: TextView? = null
    private var tv_take_photo: TextView? = null
    private var img_take_photo: ImageView? = null
    private var img_select_from_gal: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.fragment_select_image, container, false)
        setupUI(contentView)
        onClickListener()
        return contentView
    }

    private fun setupUI(view: View) {
        tv_select_from_gal = view.findViewById(R.id.tv_select_from_gal) as TextView
        tv_take_photo = view.findViewById(R.id.tv_take_photo) as TextView
        img_select_from_gal = view.findViewById(R.id.img_select_from_gal) as ImageView
        img_take_photo = view.findViewById(R.id.img_take_photo) as ImageView
    }

    private fun onClickListener() {

        tv_take_photo!!.setOnClickListener {
            checkCameraPermission()
        }

        tv_select_from_gal!!.setOnClickListener {
            checkGalleryPermission()
        }

        img_take_photo!!.setOnClickListener {
            checkCameraPermission()
        }

        img_select_from_gal!!.setOnClickListener {
            checkGalleryPermission()
        }
    }

    private fun checkCameraPermission() {

        Dexter.withActivity(profileEditActivity)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            profileEditActivity.cameraIntent()
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // permission is denied permenantly, navigate user to app settings
                            Util.showPermissionAlert(context!!,
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

    private fun checkGalleryPermission() {

        Dexter.withActivity(profileEditActivity)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            profileEditActivity.galleryIntent()
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // permission is denied permenantly, navigate user to app settings
                            Util.showPermissionAlert(context!!,
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

}
