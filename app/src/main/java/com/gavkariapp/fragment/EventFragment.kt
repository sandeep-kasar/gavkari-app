package com.gavkariapp.fragment


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.gavkariapp.Model.*
import com.gavkariapp.R
import com.gavkariapp.activity.*
import com.gavkariapp.adapter.EventFragmentAdapter
import com.gavkariapp.base.MyApplication
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.AppConstant.OPEN_SEARCH_VILLAGE_ACTIVITY
import com.gavkariapp.constant.AppConstant.YES
import com.gavkariapp.constant.AppConstant.villageId
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.interfaces.FragmentLifecycle
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_event.*
import java.util.*


class EventFragment : Fragment(),
        EventFragmentAdapter.OnItemClickListener, FragmentLifecycle {
    
    lateinit var myVillageEventList: LinkedList<Any>

    lateinit var eventFragmentAdapter: EventFragmentAdapter

    lateinit var homeViewModel: HomeViewModel

    lateinit var observer: Observer<ApiResponse<MyVillageResponse, String>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    fun setUpView() {
        myVillageEventList = LinkedList()
        homeViewModel = HomeViewModel()
        rvMyVillageFragment.applyVerticalWithDividerLinearLayoutManager()
        initObserver()
        accessData()
        layCreateEvent.setOnClickListener {
            createEvent()
        }
    }

    fun initObserver() {
        observer = Observer { t ->
            if (t?.response != null) {
                var myVillageResponse = t.response
                if (myVillageResponse?.status == HttpConstant.SUCCESS) {
                    displayData(myVillageResponse)
                } else {
                    layNoData.visibility = View.VISIBLE
                    proMyVillage.visibility=View.GONE
                    rvMyVillageFragment.visibility = View.GONE
                    layNoInternet.visibility = View.GONE
                }
            } else {
                (activity as HomeActivity).showError(getString(R.string.msg_unexpected_error))
                layNoData.visibility = View.VISIBLE
                proMyVillage.visibility=View.GONE
                rvMyVillageFragment.visibility = View.GONE
                layNoInternet.visibility = View.GONE
            }
        }
    }

    private fun accessData() {
        val preference = PreferenceHelper.customPrefs(MyApplication.instance, "user_info")
        var villageId: String? = preference[ApiConstant.VILLAGE_ID, "-1"]
        var arryEventType = ArrayList<Int>()
        arryEventType.add(0)
        var eventPeriod = 0
        var eventFilterBody = EventFilterBody(villageId.toString(),arryEventType,eventPeriod)

        if (InternetUtil.isInternetOn()) {
            homeViewModel!!.getMyVillageCall(eventFilterBody).observe(viewLifecycleOwner, observer)
        } else {
            waitForInternet()
        }
    }

    fun displayData(response: MyVillageResponse?) {
        if (response!!.status == HttpConstant.SUCCESS){
            rvMyVillageFragment.visibility = View.VISIBLE
            layNoData.visibility = View.GONE
            layNoInternet.visibility = View.GONE
            proMyVillage.visibility = View.GONE
            myVillageEventList = LinkedList<Any>()
            for (entry in response!!.MyVillageEvent) {
                myVillageEventList.add(entry)
            }
            eventFragmentAdapter = EventFragmentAdapter(myVillageEventList, this)
            rvMyVillageFragment.adapter = eventFragmentAdapter
        }else{
            layNoData.visibility = View.VISIBLE
            rvMyVillageFragment.visibility = View.GONE
            layNoInternet.visibility = View.GONE
            proMyVillage.visibility = View.GONE
        }
    }

    override fun onItemClick(item: MyVillageEvent) {
        startActivity(Intent(activity, EventDetailActivity::class.java)
                .putExtra("VillageResponse", item))
    }

    override fun onCardClick(item: MyVillageEvent) {

        var design = Design(item.type,item.title,item.family,item.muhurt,item.subtitle,
                item.subtitle_one,item.subtitle_two,item.subtitle_three,
                item.subtitle_four,item.subtitle_five,item.note,
                item.description,item.description_one,item.address,item.photo)
        val intent = Intent(activity, SingleDesignActivity::class.java)
            intent.putExtra("event_obj", design)
            startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.event_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                startActivityForResult(Intent(activity?.applicationContext, SearchVillageActivity::class.java)
                        .putExtra("villageId", villageId), OPEN_SEARCH_VILLAGE_ACTIVITY)

                return true
            }

            R.id.action_show_directory -> {
                var directoryIntent =DirectoryIntent(villageId!!, YES)
                startActivity(Intent(activity?.applicationContext, DirectoryActivity::class.java)
                        .putExtra("directoryIntent", directoryIntent))

                return true
            }

            R.id.action_show_create->{
                createEvent()
            }

            R.id.action_show_more->{
                startActivity(Intent(activity, OtherVillagesEventActivity::class.java))
            }

            R.id.action_show_filter->{
                startActivityForResult(Intent(activity?.applicationContext, FilterMyVillageActivity::class.java),
                        AppConstant.OPEN_EVENT_FILTER_ACTIVITY)
                return true
            }
        }
        return true
    }

    fun createEvent(){

        startActivity(Intent(activity?.applicationContext, SelectEventTypeActivity::class.java))

        /*val prefs = activity?.applicationContext?.let { PreferenceHelper.customPrefs(it, "user_info") }
        var isVerified = prefs!![ApiConstant.IS_VERIFIED, "-1"]
        if (isVerified == "0") {
            startActivity(Intent(activity?.applicationContext, ProfileEditActivity::class.java).putExtra("editable",true))
            (activity as HomeActivity).showWarning(getString(R.string.msg_edit_profile))
        }else{
            startActivity(Intent(activity?.applicationContext, SelectEventTypeActivity::class.java))
        }*/
    }

    private fun waitForInternet() {

        InternetUtil.observe(viewLifecycleOwner, Observer { status ->
            if (status!!) {
                rvMyVillageFragment.visibility = View.VISIBLE
                layNoInternet.visibility = View.GONE
                layNoData.visibility = View.GONE
                proMyVillage.visibility = View.GONE
                accessData()
            } else {
                layNoInternet.visibility = View.VISIBLE
                rvMyVillageFragment.visibility = View.GONE
                layNoData.visibility = View.GONE
                proMyVillage.visibility = View.GONE

            }
        })
    }

    override fun onResumeFragment() {
        Log.e("Home activity","onResumeFragment")
        if(myVillageEventList.isEmpty() && InternetUtil.isInternetOn()){
            layNoData.visibility = View.VISIBLE
            rvMyVillageFragment.visibility = View.GONE
            layNoInternet.visibility = View.GONE
            proMyVillage.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AppConstant.OPEN_EVENT_FILTER_ACTIVITY) {

            if (resultCode == Activity.RESULT_OK) {

                var eventFB = data?.getSerializableExtra("eventFilterBody")
                        as EventFilterBody

                initObserver()

                if (InternetUtil.isInternetOn()) {
                    homeViewModel!!.getMyVillageCall(eventFB).observe(this, observer)
                } else {
                    waitForInternet()
                }

            }
        }
    }


}


