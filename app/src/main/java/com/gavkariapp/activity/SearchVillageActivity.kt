package com.gavkariapp.activity

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
import com.gavkariapp.adapter.EventFragmentAdapter
import com.gavkariapp.base.MyApplication
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_search_village.*
import kotlinx.android.synthetic.main.layout_toolbar_search.*
import java.util.*

class SearchVillageActivity : BaseActivity(), EventFragmentAdapter.OnItemClickListener {
    override fun onCardClick(item: MyVillageEvent) {
    }

    lateinit var myVillageEventList: LinkedList<Any>

    lateinit var villageResponse: MyVillageResponse

    lateinit var eventFragmentAdapter: EventFragmentAdapter

    lateinit var homeViewModel: HomeViewModel

    lateinit var observer: Observer<ApiResponse<MyVillageResponse, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_search_village)
        setupView()
    }

    private fun setupView() {

        //bundle
        var villageId = intent.getStringExtra("villageId")

        //init class
        homeViewModel = HomeViewModel()

        //init observer
        initObserver()

        //access data
        accessData(villageId)

        //searching function
        search()

        //set back navigation
        toolbarHome.setNavigationIcon(R.drawable.ic_back_black)
        toolbarHome.setNavigationOnClickListener { finish() }

        //set rv
        rvSearchList.applyVerticalWithDividerLinearLayoutManager()

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


    private fun initObserver() {
        observer = Observer { t ->


            if (t?.response != null) {

                villageResponse = t.response!!

                if (villageResponse?.status == HttpConstant.SUCCESS) {
                    displayData(t?.response)
                } else {
                    dismissProgress()
                    Log.e("warning",villageResponse!!.message)
                    rvSearchList.visibility=View.GONE
                    layNoInternet.visibility=View.GONE
                    layNoData.visibility=View.VISIBLE

                }

            } else {
                Log.e("warning",t?.error)
                dismissProgress()
                showWarning(getString(R.string.msg_unexpected_error))
            }
        }
    }


    private fun displayData(response: MyVillageResponse?) {

        rvSearchList.visibility=View.VISIBLE
        layNoInternet.visibility=View.GONE
        layNoData.visibility=View.GONE


        myVillageEventList = LinkedList<Any>()

        for (entry in response!!.MyVillageEvent) {
            myVillageEventList.add(entry)
        }

        eventFragmentAdapter = EventFragmentAdapter(myVillageEventList, this)
        rvSearchList.adapter = eventFragmentAdapter
        dismissProgress()

    }

    override fun onItemClick(item: MyVillageEvent) {

        if (item is MyVillageEvent) {
            startActivity(Intent(applicationContext, EventDetailActivity::class.java)
                    .putExtra("VillageResponse", item))
        }
    }

    private fun displaySearchData(filteredList: LinkedList<Any>) {

        rvSearchList.adapter = null
        eventFragmentAdapter = EventFragmentAdapter(filteredList, this)
        rvSearchList.adapter = eventFragmentAdapter
        rvSearchList.adapter?.notifyDataSetChanged()

    }

    private fun search() {

        //add searching functionality
        ed_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                //Util.hideKeyboard(this@SearchVillageActivity)

                //adding search functionality
                //val query = s.toString().toLowerCase().trim { it <= ' ' }//get query
                val query= s.toString().toLowerCase().trim { it <= ' ' }

                if (query != "") { //if query is not null

                    //take data from SearchExpObj and add into new filteredList
                    val filteredList = LinkedList<Any>()


                    var event = villageResponse.MyVillageEvent

                    for (i in event.indices) {

                        //find out entered keyword is present in list or not
                        //val title = event[i].title.toLowerCase()
                        //val subtitle = event[i].subtitle.toLowerCase()

                        val title = event[i].title
                        val subtitle = event[i].subtitle

                        //add sorted list in filteredList
                        if (title.contains(query) || subtitle.contains(query)) {
                            filteredList.add(event[i])
                        }
                    }

//                    for (j in news.indices) {
//
//                        //find out entered keyword is present in list or not
//                        val title = news[j].title.toLowerCase()
//                        val description = news[j].description.toLowerCase()
//
//                        //add sorted list in filteredList
//                        if (title.contains(query) || description.contains(query)) {
//                            filteredList.add(news[j])
//                        }
//                    }


                    //set new list to adapter and show in recyclerview
                    displaySearchData(filteredList)

                } else {//if query is null
                    displaySearchData(myVillageEventList)
                }

            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    private fun waitForInternetConnection() {

        InternetUtil.observe(this, Observer { status ->
            if (status!!) {
                rvSearchList.visibility= View.VISIBLE
                layNoInternet.visibility= View.GONE
                layNoData.visibility= View.GONE

            } else {
                layNoInternet.visibility= View.VISIBLE
                rvSearchList.visibility= View.GONE
                layNoData.visibility= View.GONE

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
