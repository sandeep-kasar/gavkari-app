package com.gavkariapp.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.gavkariapp.Model.*
import com.gavkariapp.R
import com.gavkariapp.adapter.MyNewsAdapter
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.HttpConstant.SUCCESS
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_my_news.*
import kotlinx.android.synthetic.main.layout_no_data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyNewsActivity : BaseActivity(), MyNewsAdapter.OnItemClickListener {

    private lateinit var myNewsResponse: MyNewsResponse

    lateinit var news: ArrayList<MyNews>

    lateinit var myNewsAdapter: MyNewsAdapter

    lateinit var homeViewModel: HomeViewModel

    lateinit var observer: Observer<ApiResponse<MyNewsResponse, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_my_news)
        setupToolbar(R.id.toolbarHome, getString(R.string.lbl_my_news))
        setupView()

    }

    override fun onResume() {
        super.onResume()
        accessData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(applicationContext, HomeActivity::class.java))
        overridePendingTransition(0, 0)
        ActivityCompat.finishAffinity(this@MyNewsActivity)

    }

    private fun setupView() {

        homeViewModel = HomeViewModel()
        initObserver()
        rvMyNews.applyVerticalWithDividerLinearLayoutManager()
    }

    private fun initObserver() {
        observer = Observer { t ->

            if (t?.response != null) {

                myNewsResponse = t.response!!

                if (myNewsResponse?.status == SUCCESS) {
                    displayData(myNewsResponse)
                } else {
                    nvMyNews.visibility = View.GONE
                    layNoDataMyNews.visibility = View.VISIBLE
                    layNoInternetMyNews.visibility = View.GONE
                    layNoDataText.text=getString(R.string.lml_no_news_yet)
                    dismissProgress()
                }

            } else {
                showError(t?.error!!)
                dismissProgress()
            }
        }
    }

    private fun accessData() {

        showProgress()

        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        var userId: String? = prefs[ApiConstant.USER_ID, "-1"]

        if (InternetUtil.isInternetOn()) {
            homeViewModel.accessMyNewsCall(userId!!).observe(this, observer)
        } else {
            dismissProgress()
            waitForInternetConnection()
        }
    }

    private fun displayData(response: MyNewsResponse?) {
        news = response!!.News
        myNewsAdapter = MyNewsAdapter(news, this)
        nvMyNews.visibility = View.VISIBLE
        layNoDataMyNews.visibility = View.GONE
        rvMyNews.adapter = myNewsAdapter
        dismissProgress()

    }

    open fun deleteNews(item: MyNews) {
        showProgress()
        ApiClient.get().create(ApiInterface::class.java)
                .deleteMyNews(item.id)
                .enqueue(object : Callback<CommonResponse> {
                    override fun onResponse(call: Call<CommonResponse>?, response: Response<CommonResponse>?) {
                        if (response!!.code() == 200) {
                            runOnUiThread {
                                dismissProgress()
                                news.remove(item)
                                rvMyNews.adapter?.notifyDataSetChanged()
                                if (response.body()!!.message == "deleted") {
                                    showSuccess(getString(R.string.msg_delete_news))
                                }
                            }
                        } else {
                            if (response.errorBody() != null) {
                                runOnUiThread {
                                    dismissProgress()
                                    showError(response.errorBody().toString())
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<CommonResponse>?, t: Throwable?) {
                        runOnUiThread {
                            dismissProgress()
                            showError(t.toString())
                        }
                    }
                })
    }

    override fun onItemClick(myNews: MyNews) {
        var news = News(myNews.id,myNews.user_id,myNews.village_id,myNews.assembly_const_id,myNews.parliament_const_id,myNews.news_type,
                myNews.status,myNews.news_date,myNews.news_date_ms,myNews.title,myNews.source,myNews.photo,myNews.description)
        startActivity(Intent(applicationContext, NewsDetailActivity::class.java).putExtra("news_data", news))
    }

    override fun onEditClick(news: MyNews) {
        startActivity(Intent(applicationContext, EditNewsActivity::class.java)
                .putExtra("news", news))
    }

    override fun onDeleteAdClick(item: MyNews) {
        showDeleteAlert(item)
    }

    private fun showDeleteAlert(item: MyNews) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.msg_alert))
        builder.setMessage(getString(R.string.lbl_delete_news))
        builder.setPositiveButton(getString(R.string.msg_yes)) { dialog, which ->
            dialog.cancel()
            deleteNews(item)
        }
        builder.setNegativeButton(getString(R.string.msg_no)) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun waitForInternetConnection() {

        InternetUtil.observe(this, Observer { status ->
            if (status!!) {
                nvMyNews.visibility = View.VISIBLE
                layNoInternetMyNews.visibility = View.GONE
                layNoDataMyNews.visibility = View.GONE

            } else {
                layNoInternetMyNews.visibility = View.VISIBLE
                nvMyNews.visibility = View.GONE
                layNoDataMyNews.visibility = View.GONE

            }
        })
    }
}

