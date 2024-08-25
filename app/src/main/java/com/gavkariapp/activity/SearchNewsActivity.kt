package com.gavkariapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.gavkariapp.Model.*
import com.gavkariapp.R
import com.gavkariapp.adapter.NewsAdapter
import com.gavkariapp.base.MyApplication
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_search_news.*
import kotlinx.android.synthetic.main.activity_search_news.layNoData
import kotlinx.android.synthetic.main.layout_no_data.*
import kotlinx.android.synthetic.main.layout_toolbar_search.*

class SearchNewsActivity : BaseActivity(), NewsAdapter.OnItemClickListener {

    lateinit var news: News

    lateinit var newsList: ArrayList<News>

    lateinit var newsAdapter: NewsAdapter

    lateinit var homeViewModel: HomeViewModel

    lateinit var observer: Observer<ApiResponse<NewsResponse, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_search_news)
        setUpView()
    }

    private fun setUpView() {

        homeViewModel = HomeViewModel()
        initObserver()
        accessData()
        search()
        toolbarHome.setNavigationIcon(R.drawable.ic_back_black)
        toolbarHome.setNavigationOnClickListener { finish() }
        rvSearchNewsbyList.applyVerticalWithDividerLinearLayoutManager()
    }

    private fun accessData() {

        showProgress()

        var newsFilterBody = intent.getSerializableExtra("newsFilterBody") as NewsFilterBody

        if (InternetUtil.isInternetOn()) {
            homeViewModel.getNewsData(newsFilterBody).observe(this, observer)

        } else {
            waitForInternetConnection()
        }

    }

    @SuppressLint("SetTextI18n")
    fun initObserver() {
        observer = Observer { t ->

            if (t?.response != null) {
                var NewsResponse = t.response
                if (NewsResponse?.status == HttpConstant.SUCCESS) {
                    displayData(t.response)
                } else {
                    if (InternetUtil.isInternetOn()) {
                        accessData()

                    } else {
                        waitForInternet()
                    }
                    dismissProgress()
                    rvSearchNewsbyList.visibility = View.GONE
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

    private fun displayData(response: NewsResponse?) {

        dismissProgress()

        newsList = response!!.News
        if (newsList.isEmpty()){
            rvSearchNewsbyList.visibility = View.GONE
            layNoInternet.visibility = View.GONE
            layNoData.visibility = View.VISIBLE
            layNoDataText.text = getString(R.string.lbl_no_data_to_search)
        }else{
            rvSearchNewsbyList.visibility = View.VISIBLE
            layNoInternet.visibility = View.GONE
            layNoData.visibility = View.GONE
            newsAdapter = NewsAdapter(newsList, this)
            rvSearchNewsbyList.adapter = newsAdapter
        }

    }


    override fun onItemClick(item: Any) {
        if (item is News) {
            startActivity(Intent(applicationContext, NewsDetailActivity::class.java)
                    .putExtra("news_data", item))
        }
    }


    private fun search() {

        ed_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val query=s.toString().toLowerCase().trim { it <= ' ' }

                if (query != "") { //if query is not null

                    //take data from SearchExpObj and add into new filteredList
                    val filteredList = ArrayList<News>()


                    for (i in newsList.indices) {

                        //find out entered keyword is present in list or not
                        //val title = event[i].title.toLowerCase()
                        val title =newsList[i].title
                        //val subtitle = event[i].subtitle.toLowerCase()
                        val subtitle =newsList[i].description

                        //add sorted list in filteredList
                        if (title.contains(query) || subtitle.contains(query)) {
                            filteredList.add(newsList[i])
                        }
                    }

                    //set new list to adapter and show in recyclerview
                    displaySearchData(filteredList)

                } else {//if query is null
                    displaySearchData(newsList)
                }

            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    private fun displaySearchData(filteredList: ArrayList<News>) {

        rvSearchNewsbyList.adapter = null
        newsAdapter = NewsAdapter(filteredList, this)
        rvSearchNewsbyList.adapter = newsAdapter
        rvSearchNewsbyList.adapter?.notifyDataSetChanged()

    }

    private fun waitForInternetConnection() {

        InternetUtil.observe(this, Observer { status ->
            if (status!!) {
                rvSearchNewsbyList.visibility = View.VISIBLE
                layNoInternet.visibility = View.GONE
                layNoData.visibility = View.GONE

            } else {
                layNoInternet.visibility = View.VISIBLE
                rvSearchNewsbyList.visibility = View.GONE
                layNoData.visibility = View.GONE

            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        doAsync().execute()
    }

    class doAsync: AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            Glide.get(MyApplication.instance).clearDiskCache()
            return null
        }
    }

}
