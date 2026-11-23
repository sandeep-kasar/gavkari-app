package  com.gavkariapp.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.gavkariapp.constant.AppConstant.LATITUDE
import com.gavkariapp.constant.AppConstant.LONGITUDE
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.set
import com.google.android.gms.maps.model.LatLng


class LocationService : Service() {

    lateinit var mLastLocation: Location
    private lateinit var mLocationManager: LocationManager
    private lateinit var mLatLang: LatLng

    //check location provider
    private var mLocationListeners = arrayOf(LocationListener(LocationManager.GPS_PROVIDER),
            LocationListener(LocationManager.NETWORK_PROVIDER))


    /**
     * access live location
     */
    inner class LocationListener(provider: String) : android.location.LocationListener {
        init {
            mLastLocation = Location(provider)
        }

        override fun onLocationChanged(location: Location) {

            mLastLocation.set(location)
            mLatLang = LatLng(mLastLocation.latitude, mLastLocation.longitude)
            val pref = PreferenceHelper.defaultPrefs(this@LocationService)
            pref[LATITUDE] = mLatLang.latitude.toString()
            pref[LONGITUDE] = mLatLang.longitude.toString()
            Log.e("Location",mLatLang.latitude.toString())
            if (mLatLang != null) {
                this@LocationService.stopSelf()
            }
        }
        override fun onProviderDisabled(provider: String) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return Service.START_STICKY
    }

    override fun onCreate() {
        initializeLocationManager()
        requestLocation()

    }

    override fun onDestroy() {
        super.onDestroy()
        if (mLocationManager != null) {
            for (i in mLocationListeners.indices) {
                try {
                    mLocationManager!!.removeUpdates(mLocationListeners[i])
                } catch (ex: SecurityException) {
                }

            }
        }
    }

    private fun initializeLocationManager() {
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

    }

    private fun requestLocation() {

        try {
            mLocationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f,
                    mLocationListeners[0])
        } catch (ex: java.lang.SecurityException) {
            Log.i(TAG, "fail to request location update, ignore", ex)
        } catch (ex: IllegalArgumentException) {
            Log.d(TAG, "gps provider does not exist " + ex.message)
        }

        try {
            mLocationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f,
                    mLocationListeners[0])
        } catch (ex: java.lang.SecurityException) {
            Log.i(TAG, "fail to request location update, ignore", ex)
        } catch (ex: IllegalArgumentException) {
            Log.d(TAG, "gps provider does not exist " + ex.message)
        }

    }

    companion object {
        private val TAG = "GPS"
    }


}