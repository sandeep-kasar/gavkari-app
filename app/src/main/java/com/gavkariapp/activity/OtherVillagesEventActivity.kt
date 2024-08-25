package com.gavkariapp.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.gavkariapp.Model.*
import com.gavkariapp.R
import com.gavkariapp.adapter.EventFragmentAdapter
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_other_villages_event.*
import java.util.*

class OtherVillagesEventActivity : BaseActivity(), View.OnClickListener,
EventFragmentAdapter.OnItemClickListener  {

    private var villageId: String = "0"

    lateinit var myVillageEventList: LinkedList<Any>

    lateinit var eventFragmentAdapter: EventFragmentAdapter

    lateinit var homeViewModel: HomeViewModel

    lateinit var observer: Observer<ApiResponse<MyVillageResponse, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_other_villages_event)
        setupToolbar(R.id.toolbarHome, getString(R.string.lbl_more_village))
        laySelectVillage.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            laySelectVillage -> {
                val prefs = PreferenceHelper.customPrefs(applicationContext, "user_info")
                var stateId = prefs[ApiConstant.STATE_ID, "-1"]
                var districtId = prefs[ApiConstant.DISTRICT_ID, "-1"]
                var talukaId = prefs[ApiConstant.TALUKA_ID, "-1"]
                var villageBody = VillageBody(stateId!!, districtId!!, talukaId!!,"District")
                startActivityForResult(Intent(applicationContext, SelectVillageByTalActivity::class.java)
                        .putExtra("villageBody",villageBody),
                        AppConstant.OPEN_EVENT_VILLAGE_SELECTION_BY_TAL_ACTIVITY)
            }
        }
    }

    override fun onItemClick(item: MyVillageEvent) {
        if (item is MyVillageEvent) {
            startActivity(Intent(applicationContext, EventDetailActivity::class.java)
                    .putExtra("VillageResponse", item))
        }
    }

    override fun onCardClick(item: MyVillageEvent) {
        var design = Design(item.type,item.title,item.family,item.muhurt,item.subtitle,
                item.subtitle_one,item.subtitle_two,item.subtitle_three,
                item.subtitle_four,item.subtitle_five,item.note,
                item.description,item.description_one,item.address,item.photo)
        val intent = Intent(this, SingleDesignActivity::class.java)
        intent.putExtra("event_obj", design)
        startActivity(intent)
    }

    private fun setVillage(entry: Village) {

        if (AppConstant.userLang == AppConstant.ENGLISH) {
            var village= entry.english
            tvSelectedVillage.text= village
            villageId = entry.id
        }

        if (AppConstant.userLang == AppConstant.MARATHI) {
            var village= entry.marathi
            tvSelectedVillage.text= village
            villageId = entry.id
        }

        homeViewModel = HomeViewModel()

        initObserver()

        accessData(villageId)

        villageId  = villageId
    }

    private fun initObserver() {
        observer = Observer { t ->

            if (t?.response != null) {

                var myVillageResponse = t.response

                if (myVillageResponse?.status == HttpConstant.SUCCESS) {
                    displayData(t?.response)
                } else {
                    dismissProgress()
                    Log.e("warning", myVillageResponse!!.message)
                    rvViewVillage.visibility = View.GONE
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

    private fun accessData(villageId: String) {
        showProgress()
        if (InternetUtil.isInternetOn()) {
            var arryEventType = ArrayList<Int>()
            arryEventType.add(0)
            var eventPeriod = 0
            var eventFilterBody = EventFilterBody(villageId.toString(),arryEventType,eventPeriod)
            homeViewModel.getMyVillageCall(eventFilterBody).observe(this, observer)
        } else {
            dismissProgress()
            waitForInternetConnection()
        }
    }

    private fun waitForInternetConnection() {

        InternetUtil.observe(this, Observer { status ->
            if (status!!) {
                rvViewVillage.visibility = View.VISIBLE
                layNoInternet.visibility = View.GONE
                layNoData.visibility = View.GONE

            } else {
                layNoInternet.visibility = View.VISIBLE
                rvViewVillage.visibility = View.GONE
                layNoData.visibility = View.GONE

            }
        })
    }

    private fun displayData(response: MyVillageResponse?) {

        rvViewVillage.visibility = View.VISIBLE
        layNoInternet.visibility = View.GONE
        layNoData.visibility = View.GONE

        myVillageEventList = LinkedList<Any>()

        for (entry in response!!.MyVillageEvent) {
            myVillageEventList.add(entry)
        }

        rvViewVillage.applyVerticalWithDividerLinearLayoutManager()
        eventFragmentAdapter = EventFragmentAdapter(myVillageEventList, this)
        rvViewVillage.adapter = eventFragmentAdapter
        dismissProgress()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AppConstant.OPEN_EVENT_VILLAGE_SELECTION_BY_TAL_ACTIVITY) {

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

        if (requestCode == AppConstant.OPEN_EVENT_FILTER_ACTIVITY) {

            if (resultCode == Activity.RESULT_OK) {

                var eventFB = data?.getSerializableExtra("eventFilterBody")
                        as EventFilterBody
                eventFB.village_id =  villageId
                initObserver()
                if (InternetUtil.isInternetOn()) {
                    homeViewModel.getMyVillageCall(eventFB).observe(this, observer)
                } else {
                    waitForInternetConnection()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.other_village_event_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {

            R.id.action_search -> {
                if (villageId != "0"){
                    startActivity(Intent(this, SearchVillageActivity::class.java)
                            .putExtra("villageId", villageId))
                }
                true
            }

            R.id.action_show_filter->{
                if (villageId != "0"){
                    startActivityForResult(Intent(this, FilterMyVillageActivity::class.java),
                            AppConstant.OPEN_EVENT_FILTER_ACTIVITY)
                }

                true
            }

            R.id.action_show_directory -> {
                if (villageId != "0"){
                    var directoryIntent =DirectoryIntent(villageId!!, AppConstant.NO)
                    startActivity(Intent(applicationContext, DirectoryActivity::class.java)
                            .putExtra("directoryIntent", directoryIntent))
                }
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
