package com.gavkariapp.fragment


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gavkariapp.Model.ApiResponse
import com.gavkariapp.Model.News
import com.gavkariapp.Model.NewsFilterBody
import com.gavkariapp.Model.NewsResponse
import com.gavkariapp.R
import com.gavkariapp.activity.*
import com.gavkariapp.adapter.NewsAdapter
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_news.*


open class NewsFragment() : Fragment(), NewsAdapter.OnItemClickListener {

    private var sharedViewModel: HomeViewModel? = null

    lateinit var newsAdapterAdapter: NewsAdapter

    lateinit var  newsFilterBody : NewsFilterBody

    lateinit var newsList: ArrayList<News>

    lateinit var homeViewModel: HomeViewModel

    lateinit var observer: Observer<ApiResponse<NewsResponse, String>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        activity?.let {
            sharedViewModel = ViewModelProviders.of(it).get(HomeViewModel::class.java)
        }
    }


    fun setUpView() {
        homeViewModel = HomeViewModel()
        rvHome.applyVerticalWithDividerLinearLayoutManager()
        initObserver()
        accessData()

    }

    fun accessData(){

        val prefs = PreferenceHelper.customPrefs(requireActivity().baseContext, "user_info")
        var  villageId = prefs[ApiConstant.VILLAGE_ID, "-1"]
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
                if (NewsResponse?.status == HttpConstant.SUCCESS) {
                    accessData(t?.response)
                }else if (NewsResponse?.status == HttpConstant.NO_DATA_AVAILABLE){
                    rvHome.visibility = View.GONE
                    layNoInternet.visibility = View.GONE
                    proNearBy.visibility=View.GONE
                    layNoData.visibility = View.VISIBLE
                }

            } else {
                Log.e("warning", t?.error!!)
                (activity as HomeActivity).showError(getString(R.string.msg_unexpected_error))
                //(activity as HomeActivity).dismissProgress()
            }
        }
    }

    fun callNewsApi(newsFilterBody: NewsFilterBody) {

        if (InternetUtil.isInternetOn()) {

            homeViewModel.getNewsData(newsFilterBody).observe(viewLifecycleOwner, observer)
        } else {
            waitForInternet()
        }
    }

    fun accessData(response: NewsResponse?) {


        proNearBy.visibility=View.GONE
        rvHome.visibility = View.VISIBLE
        layNoInternet.visibility = View.GONE
        layNoData.visibility = View.GONE

        newsAdapterAdapter = NewsAdapter(response!!.News, this)
        rvHome.adapter = newsAdapterAdapter

        newsList = response.News

    }

    override fun onItemClick(item: Any) {
        if (item is News) {
            startActivity(Intent(activity, NewsDetailActivity::class.java)
                    .putExtra("news_data", item))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.news_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val prefs = PreferenceHelper.customPrefs(requireActivity().applicationContext, "user_info")
        var is_village_boy: String? = prefs[ApiConstant.IS_VILLAGE_BOY, ""]
        if (is_village_boy == "1"){
            val item = menu!!.findItem(R.id.action_create_news)
            item.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item!!.itemId) {
            R.id.action_search -> {
                if (!newsFilterBody.news_type.isEmpty()){
                    startActivity(Intent(activity?.applicationContext, SearchNewsActivity::class.java)
                            .putExtra("newsFilterBody",newsFilterBody))
                }

                return true
            }

            R.id.action_news_filter -> {
                startActivityForResult(Intent(activity?.applicationContext, NewsFilterActivity::class.java),AppConstant.OPEN_NEWS_FILTER_ACTIVITY)
                return true
            }

            R.id.action_show_more -> {
                startActivity(Intent(activity?.applicationContext, OtherVillageNewsActivity::class.java))
                return true
            }

            R.id.action_create_news -> {
                startActivity(Intent(activity?.applicationContext, SelectNewsTypeActivity::class.java))
                return true
            }

            else -> {
                false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AppConstant.OPEN_NEWS_FILTER_ACTIVITY) {

            if (resultCode == Activity.RESULT_OK) {

                var newsFilterBody = data?.getSerializableExtra("newsFilterBody")
                        as NewsFilterBody


                val prefs = PreferenceHelper.customPrefs(requireActivity().baseContext, "user_info")
                var  villageId = prefs[ApiConstant.VILLAGE_ID, "-1"]

                newsFilterBody.village_id = villageId!!

                callNewsApi(newsFilterBody)

            }
        }
    }


    private fun waitForInternet() {

        InternetUtil.observe(viewLifecycleOwner, Observer { status ->
            if (status!!) {
                accessData()
            } else {
                layNoInternet.visibility = View.VISIBLE
                rvHome.visibility = View.GONE
                layNoData.visibility = View.GONE
                proNearBy.visibility=View.GONE

            }
        })
    }
}
