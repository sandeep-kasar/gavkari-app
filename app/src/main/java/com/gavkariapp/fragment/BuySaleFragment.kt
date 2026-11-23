package com.gavkariapp.fragment


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gavkariapp.Model.ApiResponse
import com.gavkariapp.Model.BuySale
import com.gavkariapp.Model.BuySaleResponse
import com.gavkariapp.R
import com.gavkariapp.activity.*
import com.gavkariapp.adapter.BuySaleAdapter
import com.gavkariapp.constant.AppConstant.ANIMAL
import com.gavkariapp.constant.AppConstant.EQUIPMENTS
import com.gavkariapp.constant.AppConstant.MACHINERY
import com.gavkariapp.constant.HttpConstant.*
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_buy_sale.*


class BuySaleFragment : Fragment(), BuySaleAdapter.OnItemClickListener,View.OnClickListener {

    lateinit var buySaleAdapter: BuySaleAdapter

    lateinit var homeViewModel: HomeViewModel

    lateinit var observer: Observer<ApiResponse<BuySaleResponse, String>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_buy_sale, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onClick(v: View?) {

        when(v){
            layAnimal -> {startActivity(Intent(activity?.applicationContext,AnimalActivity::class.java))}
            layMachinary -> {startActivity(Intent(activity?.applicationContext,MachinaryActivity::class.java))}
            layEquipment -> {startActivity(Intent(activity?.applicationContext,EquipmentActivity::class.java))}
            layCreateAd -> {dialogSelectType()}
        }
    }

    override fun onItemClick(buysale: BuySale){
        startActivity(Intent(activity?.applicationContext,BuySaleDetailActivity::class.java).putExtra("buySaleData",buysale))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    fun setupView(){
        homeViewModel = HomeViewModel()
        rvBuy.applyVerticalWithDividerLinearLayoutManager()
        activity?.let {
            homeViewModel = ViewModelProviders.of(it).get(HomeViewModel::class.java)
            observeInput(homeViewModel!!)
        }
        layAnimal.setOnClickListener(this)
        layMachinary.setOnClickListener(this)
        layEquipment.setOnClickListener(this)
        layCreateAd.setOnClickListener(this)
    }

    private fun observeInput(sharedViewModel: HomeViewModel) {
        sharedViewModel.buySaleResponse.observe(viewLifecycleOwner, Observer { it ->
            it?.let {
                accessData(it)
            }
        })

        sharedViewModel.isInternetAvail.observe(viewLifecycleOwner, Observer {
            it?.let {
                waitForInternet()
            }
        })
    }

    fun accessData(buySaleResponse: BuySaleResponse){
        if (buySaleResponse?.status == SUCCESS) {
            displayData(buySaleResponse)
        }else if (buySaleResponse?.status == NO_DATA_AVAILABLE ||
                buySaleResponse?.status == EMPTY_REQUEST ||
                buySaleResponse?.status == FIELD_IS_EMPTY ||
                buySaleResponse?.status == FAIL_TO_INSERT ||
                buySaleResponse?.status == DUPLICATE_DATA){
            nvBuy.visibility = View.GONE
            layNoInternetBuy.visibility = View.GONE
            proBuy.visibility=View.GONE
            layNoDataBuy.visibility = View.VISIBLE
        }
    }

    fun displayData(buySale: BuySaleResponse){
        proBuy.visibility=View.GONE
        nvBuy.visibility = View.VISIBLE
        layNoInternetBuy.visibility = View.GONE
        layNoDataBuy.visibility = View.GONE
        buySaleAdapter = BuySaleAdapter(buySale.BuySaleAds, this)
        rvBuy.adapter = buySaleAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.buy_sale_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item!!.itemId) {

            R.id.action_favourite -> {
                startActivity(Intent(activity?.applicationContext, MyFavAdActivity::class.java)
                        .putExtra("tab_type",0))
                return true
            }


            R.id.action_animal -> {
                startActivity(Intent(activity?.applicationContext,AnimalActivity::class.java))
                return true
            }


            R.id.action_machinery -> {
                startActivity(Intent(activity?.applicationContext,MachinaryActivity::class.java))
                return true
            }


            R.id.action_equipment -> {
                startActivity(Intent(activity?.applicationContext,EquipmentActivity::class.java))
                return true
            }

            R.id.action_create_ad -> {
                dialogSelectType()
                return true
            }

            else -> {
                false
            }
        }
    }

    private fun dialogSelectType(){
        val dialog = Dialog((activity as HomeActivity))
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_select_sale_type)
        dialog.show()

        val tvAnimal = dialog.findViewById(R.id.tvAnimal) as TextView
        val tvMachinery = dialog.findViewById(R.id.tvMachinery) as TextView
        val tvEquipment = dialog.findViewById(R.id.tvEquipment) as TextView

        tvAnimal.setOnClickListener {
            dialog.cancel()
            startActivity(Intent(activity?.applicationContext, SelectBuySaleAdTypeActivity::class.java)
                    .putExtra("adType",ANIMAL))
        }

        tvMachinery.setOnClickListener {
            dialog.cancel()
            startActivity(Intent(activity?.applicationContext, SelectBuySaleAdTypeActivity::class.java)
                    .putExtra("adType", MACHINERY))
        }

        tvEquipment.setOnClickListener {
            dialog.cancel()
            startActivity(Intent(activity?.applicationContext, SelectBuySaleAdTypeActivity::class.java)
                    .putExtra("adType",EQUIPMENTS))
        }

    }

    private fun waitForInternet() {
        InternetUtil.observe(viewLifecycleOwner, Observer { status ->
            if (status!!) {
                activity?.let {
                    homeViewModel = ViewModelProviders.of(it).get(HomeViewModel::class.java)
                    observeInput(homeViewModel!!)
                }
            } else {
                layNoInternetBuy.visibility = View.VISIBLE
                rvBuy.visibility = View.GONE
                layNoDataBuy.visibility = View.GONE
                proBuy.visibility=View.GONE
            }
        })
    }
}
