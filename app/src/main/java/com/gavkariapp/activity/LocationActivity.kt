package com.gavkariapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gavkariapp.Model.*
import com.gavkariapp.R
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.AppConstant.ENGLISH
import com.gavkariapp.constant.AppConstant.MARATHI
import com.gavkariapp.constant.AppConstant.OPEN_EVENT_VILLAGE_SELECTION_BY_TAL_ACTIVITY
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.helper.UserData
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import com.gavkariapp.utility.DeviceInfo
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.set

class LocationActivity : BaseActivity(), View.OnClickListener {

    private lateinit var loginViewmodel: LoginViewModel

    private lateinit var signUpobserver: Observer<ApiResponse<SignInResponse, String>>

    private lateinit var userData: UserData

    private lateinit var signUpInput: SignUpInput

    var stateListIds = mutableMapOf<String, String>()
    var distListIds = mutableMapOf<String, String>()
    var talListIds = mutableMapOf<String, String>()
    var villageListIds = mutableMapOf<String, String>()

    private var stateId: String = "0"

    private var districtId: String = "0"

    private var talukaId: String = "0"

    private var villageId: String = "0"

    var deviceId: String? = "-1"

    var userLang: String? = "en"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_location)

        //preferences
        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        userLang = prefs[AppConstant.LANGUAGE, "-1"]

        val prefs_ = PreferenceHelper.customPrefs(this, "device_id")
        deviceId = prefs_[ApiConstant.DEVICE_ID, "-1"]

        //init view model
        loginViewmodel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        //save userdata class
        userData = UserData(this)

        //signup input
        signUpInput = intent.getSerializableExtra("signUpInput") as SignUpInput

        //init observer
        initSignUpObserver()

        //get state
        getState()

        //click
        btnSignUp.setOnClickListener(this)
        laySignIn_.setOnClickListener(this)
        laySelectVillage.setOnClickListener(this)

        //spinner item selection
        spnState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showWarning(getString(R.string.warning_select_state))
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                stateId = stateListIds.get(parent?.getItemAtPosition(position))!!
                getDistrict(stateId)
            }
        }

        spnDist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showWarning(getString(R.string.warning_select_district))
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                districtId = distListIds.get(parent?.getItemAtPosition(position))!!
                getTaluka(districtId)
                Log.e("", parent?.getItemAtPosition(position).toString())


            }

        }

        spnTal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showWarning(getString(R.string.warning_select_district))
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                talukaId = talListIds.get(parent?.getItemAtPosition(position))!!
                Log.e("", parent?.getItemAtPosition(position).toString())
            }

        }

    }

    override fun onClick(v: View?) {

        when (v) {

            btnSignUp -> signUp()

            laySignIn_ -> {
                startActivity(Intent(this.applicationContext, SignInActivity::class.java))
                finish()
            }

            laySelectVillage ->{
                var villageBody = VillageBody(stateId, districtId, talukaId,"Taluka")
                startActivityForResult(Intent(applicationContext, SelectVillageByTalActivity::class.java)
                        .putExtra("villageBody",villageBody),
                        OPEN_EVENT_VILLAGE_SELECTION_BY_TAL_ACTIVITY)
            }

        }
    }

    private fun signUp() {

        val deviceCompany = DeviceInfo.getDeviceManufacturer()
        val imei = DeviceInfo.getIMEI(this)
        val platformVersion = DeviceInfo.getPlatformVersion()

        //if input is valid then onClickRow register api
        if (deviceId != null && deviceCompany != null && imei != null && platformVersion != null && villageId!="0") {

            //create user body
            val userBody = SignUpBody(signUpInput.name, signUpInput.mobile,
                    signUpInput.password, villageId, deviceId!!, deviceCompany, imei, platformVersion)

            //showProgress()

            if (InternetUtil.isInternetOn()) {
                //pass data to view model
                loginViewmodel.signUpCall(userBody).observe(this, signUpobserver)
            } else {
                //observe internet connection
                //dismissProgress()
                waitForInternet()
            }
        }else{
            showError(getString(R.string.war_select_village))
        }

    }

    private fun initSignUpObserver() {

        signUpobserver = Observer { t ->

            //hide progress bar
            //dismissProgress()

            if (t?.response != null) {

                var userSignUpResponse = t.response

                if (userSignUpResponse?.status == HttpConstant.SUCCESS) {
                    //save user info in shared pref
                    userData.saveUserSignInInfo(t.response!!,true)
                } else {
                    if (userSignUpResponse!!.message == "User already present !"){
                        showSuccess(getString(R.string.msg_duplicate_user))
                    }else{
                        showError(userSignUpResponse.message)
                    }
                }

            } else {
                showError(t?.error!!)
            }
        }
    }

    private fun getState() {

        //showProgress()

        ApiClient.get().create(ApiInterface::class.java)
                .getState()
                .enqueue(object : Callback<StateResponse> {
                    override fun onResponse(call: Call<StateResponse>?, response: Response<StateResponse>?) {
                        if (response!!.code() == 200) {
                            var stateResponse = response.body()
                            setState(stateResponse!!.state)
                        }
                    }

                    override fun onFailure(call: Call<StateResponse>?, t: Throwable?) {
                        showError(getString(R.string.warning_try_later))
                        //dismissProgress()
                    }
                })

    }

    private fun setState(arrayList: List<State>) {

        var stateList = ArrayList<String>()

        for (entry in arrayList) {

            if (userLang == ENGLISH) {
                var state = entry.english
                stateList.add(state)
                stateListIds[state] = entry.id
            }

            if (userLang == MARATHI) {
                var state = entry.marathi
                stateList.add(state)
                stateListIds[state] = entry.id
            }
        }

        runOnUiThread {
            var arrayAdapter = ArrayAdapter(this, R.layout.layout_spinner, stateList)
            spnState.adapter = arrayAdapter
            //dismissProgress()
        }
    }

    private fun getDistrict(stateId: String) {

        //showProgress()
        var districtBody = DistrictBody(stateId.toString())

        ApiClient.get().create(ApiInterface::class.java)
                .getDistrict(districtBody)
                .enqueue(object : Callback<DistrictResponse> {
                    override fun onResponse(call: Call<DistrictResponse>?, response: Response<DistrictResponse>?) {
                        if (response!!.code() == 200) {
                            var districtResponse = response.body()
                            setDistrict(districtResponse!!.districtList)
                        }
                    }

                    override fun onFailure(call: Call<DistrictResponse>?, t: Throwable?) {
                        showError(getString(R.string.warning_try_later))
                        //dismissProgress()
                    }
                })

    }

    private fun setDistrict(arrayList: List<District>) {

        var districtList = ArrayList<String>()

        for (entry in arrayList) {

            if (userLang == ENGLISH) {
                var district = entry.english
                districtList.add(district)
                distListIds[district] = entry.id
            }

            if (userLang == MARATHI) {
                var district = entry.marathi
                districtList.add(district)
                distListIds[district] = entry.id
            }
        }

        runOnUiThread {
            var arrayAdapter = ArrayAdapter(this, R.layout.layout_spinner, districtList)
            spnDist.adapter = arrayAdapter
            //dismissProgress()
        }
    }

    private fun getTaluka(districtId: String) {

        //showProgress()
        var talukaBody = TalukaBody(stateId, districtId)

        ApiClient.get().create(ApiInterface::class.java)
                .getTaluka(talukaBody)
                .enqueue(object : Callback<TalukaResponse> {
                    override fun onResponse(call: Call<TalukaResponse>?, response: Response<TalukaResponse>?) {
                        if (response!!.code() == 200) {
                            var talukaResponse = response.body()
                            setTaluka(talukaResponse!!.talukaList)
                        }
                    }

                    override fun onFailure(call: Call<TalukaResponse>?, t: Throwable?) {
                        showError(getString(R.string.warning_try_later))
                        //dismissProgress()
                    }
                })

    }

    private fun setTaluka(arrayList: List<Taluka>) {

        if (arrayList != null) {

            var talukaList = ArrayList<String>()

            for (entry in arrayList) {

                if (userLang == ENGLISH) {
                    var taluka = entry.english
                    talukaList.add(taluka)
                    talListIds[taluka] = entry.id
                }

                if (userLang == MARATHI) {
                    var taluka = entry.marathi
                    talukaList.add(taluka)
                    talListIds[taluka] = entry.id
                }
            }

            runOnUiThread {
                var arrayAdapter = ArrayAdapter(this, R.layout.layout_spinner, talukaList)
                spnTal.adapter = arrayAdapter
                tvVillage.text = getString(R.string.select_village)
                //dismissProgress()
            }
        } else {
            //dismissProgress()
        }

    }

    private fun setVillage(entry: Village) {

        if (userLang == ENGLISH) {
            var village= entry.english
            villageListIds[village] = entry.id
            tvVillage.text= village
            villageId = entry.id
        }

        if (userLang == MARATHI) {
            var village= entry.marathi
            villageListIds[village] = entry.id
            tvVillage.text= village
            villageId = entry.id
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == OPEN_EVENT_VILLAGE_SELECTION_BY_TAL_ACTIVITY) {

            if (resultCode == Activity.RESULT_OK) {

                if (data?.getSerializableExtra("village") !=null){
                    var village = data?.getSerializableExtra("village")
                            as Village
                    setVillage(village)
                }else{
                    showWarning(getString(R.string.warning_no_village_selected))
                }


            } else {
                showWarning(getString(R.string.warning_no_village_selected))
            }
        }
    }


}
