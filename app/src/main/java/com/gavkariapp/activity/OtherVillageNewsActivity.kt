package com.gavkariapp.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gavkariapp.Model.*
import com.gavkariapp.R
import com.gavkariapp.adapter.NewsAdapter
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.AppConstant.ENGLISH
import com.gavkariapp.constant.AppConstant.MARATHI
import com.gavkariapp.constant.AppConstant.userLang
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_other_villages_event.*

class OtherVillageNewsActivity : BaseActivity(), View.OnClickListener, NewsAdapter.OnItemClickListener {

    private var villageId: String = "0"

    private var sharedViewModel: HomeViewModel? = null

    lateinit var newsAdapterAdapter: NewsAdapter

    lateinit var  newsFilterBody : NewsFilterBody

    lateinit var newsList: ArrayList<News>

    lateinit var homeViewModel: HomeViewModel

    lateinit var observer: Observer<ApiResponse<NewsResponse, String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_other_villages_event)
        setUpView()
        sharedViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

    }

    fun setUpView() {
        setupToolbar(R.id.toolbarHome, getString(R.string.lbl_more_village))
        laySelectVillage.setOnClickListener(this)
        homeViewModel = HomeViewModel()
        rvViewVillage.applyVerticalWithDividerLinearLayoutManager()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AppConstant.OPEN_EVENT_VILLAGE_SELECTION_BY_TAL_ACTIVITY) {

            if (resultCode == Activity.RESULT_OK) {

                if (data?.getSerializableExtra("village") !=null){
                    var village = data?.getSerializableExtra("village") as Village
                    setVillage(village)
                }else{
                    showWarning(getString(R.string.warning_no_village_selected))
                }


            } else {
                showWarning(getString(R.string.warning_no_village_selected))
            }
        }

        if (requestCode == AppConstant.OPEN_NEWS_FILTER_ACTIVITY) {

            if (resultCode == Activity.RESULT_OK) {
                var newsFilterBody = data?.getSerializableExtra("newsFilterBody") as NewsFilterBody
                newsFilterBody.village_id = villageId
                callNewsApi(newsFilterBody)
            }
        }
    }

    fun callNewsApi(newsFilterBody: NewsFilterBody){
        if (InternetUtil.isInternetOn()) {
            homeViewModel.getNewsData(newsFilterBody).observe(this, observer)
        } else {
            dismissProgress()
            waitForInternetConnection()
        }
    }

    private fun setVillage(entry: Village) {

        if (userLang == ENGLISH) {
            var village= entry.english
            tvSelectedVillage.text= village
            villageId = entry.id
        }

        if (userLang == MARATHI) {
            var village= entry.marathi
            tvSelectedVillage.text= village
            villageId = entry.id
        }

        homeViewModel = HomeViewModel()

        initObserver()
        
        setData(entry.id)
    }

    fun setData(villageId  :String){
        showProgress()
        var arryNewsType = ArrayList<Int>()
        arryNewsType.add(0)
        var newsPeriod = 0
        newsFilterBody = NewsFilterBody(villageId!!,arryNewsType,newsPeriod)
        callNewsApi(newsFilterBody)
    }

    @SuppressLint("SetTextI18n")
    fun initObserver() {
        observer = Observer { t ->

            if (t?.response != null) {
                var NewsResponse = t.response
                dismissProgress()
                if (NewsResponse?.status == HttpConstant.SUCCESS) {
                    setData(t?.response)
                }else if (NewsResponse?.status == HttpConstant.NO_DATA_AVAILABLE){
                    rvViewVillage.visibility = View.GONE
                    layNoInternet.visibility = View.GONE
                    layNoData.visibility = View.VISIBLE
                }

            } else {
                dismissProgress()
                Log.e("warning", t?.error!!)
                showError(getString(R.string.msg_unexpected_error))
            }
        }
    }

    fun setData(response: NewsResponse?) {

        rvViewVillage.visibility = View.VISIBLE
        layNoInternet.visibility = View.GONE
        layNoData.visibility = View.GONE

        newsAdapterAdapter = NewsAdapter(response!!.News, this)
        rvViewVillage.adapter = newsAdapterAdapter

        newsList = response.News

    }

    override fun onItemClick(item: Any) {
        if (item is News) {
            startActivity(Intent(this, NewsDetailActivity::class.java)
                    .putExtra("news_data", item))
        }
    }

    private fun waitForInternetConnection() {

        InternetUtil.observe(this, Observer { status ->
            if (status!!) {
                setData(villageId)
            } else {
                layNoInternet.visibility = View.VISIBLE
                rvViewVillage.visibility = View.GONE
                layNoData.visibility = View.GONE

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.other_village_news_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

       return when (item.itemId) {
            R.id.action_search -> {
                if (villageId != "0"){
                    startActivity(Intent(this, SearchNewsActivity::class.java)
                            .putExtra("newsFilterBody",newsFilterBody))
                }
                true
            }

            R.id.action_show_filter->{
                if (villageId != "0"){
                    startActivityForResult(Intent(this, NewsFilterActivity::class.java),
                            AppConstant.OPEN_NEWS_FILTER_ACTIVITY)
                }
                true
            }

           else -> super.onOptionsItemSelected(item)
       }

    }
}
