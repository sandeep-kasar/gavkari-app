package com.gavkariapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.gavkariapp.Model.Village
import com.gavkariapp.Model.VillageBody
import com.gavkariapp.Model.VillageResponse
import com.gavkariapp.R
import com.gavkariapp.adapter.SelectVillageByTalAdapter
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.applyVerticalWithDividerLinearLayoutManager
import kotlinx.android.synthetic.main.activity_village_selection.*
import kotlinx.android.synthetic.main.layout_toolbar_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectVillageByTalActivity : BaseActivity(), SelectVillageByTalAdapter.addVillage {

    private var village : Village? = null

    private lateinit var villageList: ArrayList<Village>

    private lateinit var villageBody :VillageBody


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_village_selection)
        setupView()
        setupToolbar(R.id.toolbarHome, getString(com.gavkariapp.R.string.title_select_village))

    }

    private fun setupView() {

        //set back navigation
        toolbarHome.setNavigationIcon(R.drawable.ic_back_black)
        toolbarHome.setNavigationOnClickListener { finish() }

        villageBody = intent.getSerializableExtra("villageBody") as VillageBody

        //set rv
        rvVillageList.applyVerticalWithDividerLinearLayoutManager()

        //onClickRow data
        getVillage()

        //add searching functionality
        ed_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //adding search functionality
                //val query = s.toString().toLowerCase().trim { it <= ' ' }//get query
                val query= s.toString().toLowerCase().trim { it <= ' ' }

                if (query != "") { //if query is not null

                    //take data from SearchExpObj and add into new filteredList
                    val filteredList = java.util.ArrayList<Village>()
                    for (i in villageList.indices) {

                        //find out entered keyword is present in customer list or not
                        //val english = villageList[i].english.toLowerCase()
                        //val marathi = villageList[i].marathi.toLowerCase()
                        //val hindi = villageList[i].hindi.toLowerCase()

                        val english = villageList[i].english.toLowerCase()
                        val marathi = villageList[i].marathi
                        val hindi = villageList[i].hindi

                        //add sorted list in filteredList
                        if (english.contains(query) || marathi.contains(query)) {
                            filteredList.add(villageList.get(i))
                        }
                    }
                    //set new list to adapter and show in recyclerview
                    displayData(filteredList)

                } else {//if query is null
                    displayData(villageList)
                }

            }

            override fun afterTextChanged(s: Editable) {

            }
        })

    }

    private fun getVillage() {

        if (InternetUtil.isInternetOn()) {
            showProgress()
            var villageBody = VillageBody(villageBody.state_id, villageBody.district_id, villageBody.taluka_id,villageBody.village_by)

            ApiClient.get().create(ApiInterface::class.java)
                    .getVillage(villageBody)
                    .enqueue(object : Callback<VillageResponse> {
                        override fun onResponse(call: Call<VillageResponse>?, response: Response<VillageResponse>?) {

                            var villageResponse = response!!.body()

                            if (response!!.code() == 200) {
                                villageList= villageResponse!!.villageList
                                displayData(villageResponse!!.villageList)
                            }else {
                                runOnUiThread {
                                    dismissProgress()
                                    Log.e("warning", villageResponse!!.message)
                                    rvVillageList.visibility = View.GONE
                                    layNoInternet.visibility = View.GONE
                                    layNoData.visibility = View.VISIBLE
                                }

                            }
                        }

                        override fun onFailure(call: Call<VillageResponse>?, t: Throwable?) {
                            showError(getString(R.string.warning_try_later))
                            dismissProgress()
                        }
                    })
        }else{
            //observe internet connection
            dismissProgress()
            waitForInternetConnection()
        }

    }


    private fun displayData(response: ArrayList<Village>) {

        runOnUiThread {
            dismissProgress()
            rvVillageList.visibility = View.VISIBLE
            layNoInternet.visibility = View.GONE
            layNoData.visibility = View.GONE
            if(response.isNotEmpty()) {
                var villageListAdapter = SelectVillageByTalAdapter(response, this)
                rvVillageList.adapter = villageListAdapter
            }
        }
    }

    override fun onClickAddButton(villages: Village) {
        Log.e("","list="+villages.id +"=="+villages.english)
        village = villages
        onBackPressed()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        var intent = Intent(applicationContext, LocationActivity::class.java)
        intent.putExtra("village", village)
        setResult(Activity.RESULT_OK, intent)
        finish()
        super.onBackPressed()
    }

    private fun waitForInternetConnection() {

        InternetUtil.observe(this, Observer { status ->
            if (status!!) {
                rvVillageList.visibility = View.VISIBLE
                layNoInternet.visibility = View.GONE
                layNoData.visibility = View.GONE

            } else {
                layNoInternet.visibility = View.VISIBLE
                rvVillageList.visibility = View.GONE
                layNoData.visibility = View.GONE

            }
        })
    }


}

