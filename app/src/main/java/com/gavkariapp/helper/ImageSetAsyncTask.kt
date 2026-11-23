package com.gavkariapp.helper

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.AsyncTask
import android.view.Window
import com.gavkariapp.R
import com.gavkariapp.utility.Util
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class ImageSetAsyncTask(private val mContext: Context, private val imageCallback: ImageCallback, private val imageNo: Int) : AsyncTask<String, Void, Bitmap>() {
    private lateinit var scaledBitmap: Bitmap
    private lateinit var fileUri: Uri
    private lateinit var out: FileOutputStream
    private lateinit var dialog: Dialog

    interface ImageCallback {

        fun setBitmapExecute(bitmap: String)
        fun setImage(imageNo:Int,bitmap: Bitmap)
    }

    override fun onPreExecute() {
        super.onPreExecute()

        dialog = Dialog(mContext)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.layout_progressbar)
        dialog!!.setCancelable(false)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog!!.show()

    }

    override fun doInBackground(vararg params: String): Bitmap {

        // bitmap factory
        val options = BitmapFactory.Options()

        // downsizing image as it throws OutOfMemory Exception for larger
        // images
        BitmapFactory.decodeFile(params[0], options)
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth


        if (imageHeight > 5000 || imageWidth > 5000) {

            options.inSampleSize = 9

        } else if (imageHeight > 4000 || imageWidth > 4000) {

            options.inSampleSize = 8

        } else if (imageHeight > 3000 || imageWidth > 3000) {

            options.inSampleSize = 7

        } else if (imageHeight > 2000 || imageWidth > 2000) {

            options.inSampleSize = 0

        } else if (imageHeight > 1000 || imageWidth > 1000) {

            options.inSampleSize = 0

        } else {

            options.inSampleSize = 0
        }


        //save bitmap for further use
        scaledBitmap = BitmapFactory.decodeFile(params[0], options)

        //check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface("" + params[0])

            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)

            val matrix = Matrix()
            when (orientation) {
                6 -> matrix.postRotate(90f)
                3 -> matrix.postRotate(180f)
                8 -> matrix.postRotate(270f)
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap!!, 0, 0, scaledBitmap!!.width,
                    scaledBitmap!!.height, matrix, true)

        } catch (e: IOException) {
            e.printStackTrace()
        }

        //save file at this path
        fileUri = Util.getOutputMediaFileUri(mContext, "GavkariApp/upload")

        try {

            out = FileOutputStream(fileUri!!.path)
            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, out)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }


        return scaledBitmap
    }

    override fun onPostExecute(bitmap: Bitmap) {
        super.onPostExecute(bitmap)

        dialog!!.dismiss()

        imageCallback.setImage(imageNo,bitmap)

        imageCallback.setBitmapExecute(fileUri.path!!)
    }
}
