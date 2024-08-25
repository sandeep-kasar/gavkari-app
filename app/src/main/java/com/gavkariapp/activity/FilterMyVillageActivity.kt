package com.gavkariapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.gavkariapp.Model.EventFilterBody
import com.gavkariapp.Model.MyVillageEvent
import com.gavkariapp.R
import com.gavkariapp.base.MyApplication
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import kotlinx.android.synthetic.main.activity_filter_my_village.*

class FilterMyVillageActivity : BaseActivity(),View.OnClickListener {

    var arryEventType = ArrayList<Int>()
    var eventPeriod = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_filter_my_village)
        setupToolbar(R.id.toolbarHome, getString(R.string.title_filter))
        arryEventType.add(0)
        eventPeriod.add(0)

        btnApply.setOnClickListener(this)
        layAll.setOnClickListener(this)
        layWedding.setOnClickListener(this)
        layEngage.setOnClickListener(this)
        layHouseWarm.setOnClickListener(this)
        layJagaran.setOnClickListener(this)
        layDashkriya.setOnClickListener(this)
        layFirstMaemory.setOnClickListener(this)
        layRetirements.setOnClickListener(this)
        layBirthday.setOnClickListener(this)
        laySataynaraya.setOnClickListener(this)
        layMahaP.setOnClickListener(this)
        layOther.setOnClickListener(this)
        layAllPeriod.setOnClickListener(this)
        lay8days.setOnClickListener(this)
        lay15days.setOnClickListener(this)
        lay30days.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when (v) {

            btnApply->{
                onBackPressed()
            }

            layAll ->{
                if (arryEventType.contains(0)){
                    Log.e("All","present")
                    imgTickAll.setImageResource(R.drawable.ic_done_tick)
                    arryEventType.remove(0)

                }else{
                    Log.e("All","absent")
                    imgTickAll.setImageResource(R.drawable.ic_done_tick_green)
                    arryEventType.add(0)
                    arryEventType.remove(1)
                    arryEventType.remove(2)
                    arryEventType.remove(3)
                    arryEventType.remove(4)
                    arryEventType.remove(5)
                    arryEventType.remove(6)
                    arryEventType.remove(7)
                    arryEventType.remove(8)
                    arryEventType.remove(9)
                    arryEventType.remove(10)
                    arryEventType.remove(11)
                    imgTickWedding.setImageResource(R.drawable.ic_done_tick)
                    imgTickEngage.setImageResource(R.drawable.ic_done_tick)
                    imgTickHousewarm.setImageResource(R.drawable.ic_done_tick)
                    imgTickJagaran.setImageResource(R.drawable.ic_done_tick)
                    imgTickDashkriya.setImageResource(R.drawable.ic_done_tick)
                    imgTickFrstMem.setImageResource(R.drawable.ic_done_tick)
                    imgTickRetirement.setImageResource(R.drawable.ic_done_tick)
                    imgTickBirthday.setImageResource(R.drawable.ic_done_tick)
                    imgTickSatyanarayan.setImageResource(R.drawable.ic_done_tick)
                    imgTickMahaP.setImageResource(R.drawable.ic_done_tick)
                    imgTickOther.setImageResource(R.drawable.ic_done_tick)
                }
            }

            layWedding ->{

                unselectAll()

                if (arryEventType.contains(1)){
                    Log.e("layWedding","present")
                    imgTickWedding.setImageResource(R.drawable.ic_done_tick)
                    arryEventType.remove(1)

                }else{
                    Log.e("layWedding","absent")
                    imgTickWedding.setImageResource(R.drawable.ic_done_tick_green)
                    arryEventType.add(1)
                }
            }


            layEngage ->{

                unselectAll()

                if (arryEventType.contains(2)){
                    Log.e("imgTickEngage","present")
                    imgTickEngage.setImageResource(R.drawable.ic_done_tick)
                    arryEventType.remove(2)

                }else{
                    Log.e("imgTickEngage","absent")
                    imgTickEngage.setImageResource(R.drawable.ic_done_tick_green)
                    arryEventType.add(2)
                }
            }

            layFirstMaemory ->{
                unselectAll()
                if (arryEventType.contains(3)){
                    Log.e("imgTickFrstMem","present")
                    imgTickFrstMem.setImageResource(R.drawable.ic_done_tick)
                    arryEventType.remove(3)

                }else{
                    Log.e("imgTickFrstMem","absent")
                    imgTickFrstMem.setImageResource(R.drawable.ic_done_tick_green)
                    arryEventType.add(3)
                }
            }

            layHouseWarm ->{

                unselectAll()

                if (arryEventType.contains(4)){
                    Log.e("imgTickHousewarm","present")
                    imgTickHousewarm.setImageResource(R.drawable.ic_done_tick)
                    arryEventType.remove(4)

                }else{
                    Log.e("imgTickHousewarm","absent")
                    imgTickHousewarm.setImageResource(R.drawable.ic_done_tick_green)
                    arryEventType.add(4)
                }
            }

            layDashkriya ->{
                unselectAll()
                if (arryEventType.contains(5)){
                    Log.e("imgTickDashkriya","present")
                    imgTickDashkriya.setImageResource(R.drawable.ic_done_tick)
                    arryEventType.remove(5)

                }else{
                    Log.e("imgTickDashkriya","absent")
                    imgTickDashkriya.setImageResource(R.drawable.ic_done_tick_green)
                    arryEventType.add(5)
                }
            }


            layJagaran ->{

                unselectAll()

                if (arryEventType.contains(6)){
                    Log.e("imgTickJagaran","present")
                    imgTickJagaran.setImageResource(R.drawable.ic_done_tick)
                    arryEventType.remove(6)

                }else{
                    Log.e("imgTickJagaran","absent")
                    imgTickJagaran.setImageResource(R.drawable.ic_done_tick_green)
                    arryEventType.add(6)
                }
            }

            layBirthday ->{
                unselectAll()
                if (arryEventType.contains(7)){
                    Log.e("imgTickBirthday","present")
                    imgTickBirthday.setImageResource(R.drawable.ic_done_tick)
                    arryEventType.remove(7)

                }else{
                    Log.e("imgTickBirthday","absent")
                    imgTickBirthday.setImageResource(R.drawable.ic_done_tick_green)
                    arryEventType.add(7)
                }
            }


            layRetirements ->{
                unselectAll()
                if (arryEventType.contains(8)){
                    Log.e("imgTickRetirement","present")
                    imgTickRetirement.setImageResource(R.drawable.ic_done_tick)
                    arryEventType.remove(8)

                }else{
                    Log.e("imgTickRetirement","absent")
                    imgTickRetirement.setImageResource(R.drawable.ic_done_tick_green)
                    arryEventType.add(8)
                }
            }



            laySataynaraya ->{
                unselectAll()
                if (arryEventType.contains(9)){
                    Log.e("imgTickSatyanarayan","present")
                    imgTickSatyanarayan.setImageResource(R.drawable.ic_done_tick)
                    arryEventType.remove(9)

                }else{
                    Log.e("imgTickSatyanarayan","absent")
                    imgTickSatyanarayan.setImageResource(R.drawable.ic_done_tick_green)
                    arryEventType.add(9)
                }
            }

            layMahaP ->{
                unselectAll()
                if (arryEventType.contains(10)){
                    Log.e("imgTickMahaP","present")
                    imgTickMahaP.setImageResource(R.drawable.ic_done_tick)
                    arryEventType.remove(10)

                }else{
                    Log.e("imgTickMahaP","absent")
                    imgTickMahaP.setImageResource(R.drawable.ic_done_tick_green)
                    arryEventType.add(10)
                }
            }

            layOther ->{
                unselectAll()
                if (arryEventType.contains(11)){
                    Log.e("imgTickOther","present")
                    imgTickOther.setImageResource(R.drawable.ic_done_tick)
                    arryEventType.remove(11)

                }else{
                    Log.e("imgTickOther","absent")
                    imgTickOther.setImageResource(R.drawable.ic_done_tick_green)
                    arryEventType.add(11)
                }
            }

            layAllPeriod ->{
                if (eventPeriod.contains(0)){
                    Log.e("imgAllPeriod","present")
                    imgAllPeriod.setImageResource(R.drawable.ic_done_tick)
                    eventPeriod.remove(0)

                }else{
                    Log.e("imgAllPeriod","absent")
                    imgAllPeriod.setImageResource(R.drawable.ic_done_tick_green)
                    eventPeriod.add(0)
                    img8days.setImageResource(R.drawable.ic_done_tick)
                    img15days.setImageResource(R.drawable.ic_done_tick)
                    img30days.setImageResource(R.drawable.ic_done_tick)
                    eventPeriod.remove(8)
                    eventPeriod.remove(15)
                    eventPeriod.remove(30)
                }
            }

            lay8days ->{
                if (eventPeriod.contains(8)){
                    Log.e("img8days","present")
                    img8days.setImageResource(R.drawable.ic_done_tick)

                }else{
                    Log.e("img8days","absent")
                    img8days.setImageResource(R.drawable.ic_done_tick_green)
                    eventPeriod.add(8)

                    imgAllPeriod.setImageResource(R.drawable.ic_done_tick)
                    img15days.setImageResource(R.drawable.ic_done_tick)
                    img30days.setImageResource(R.drawable.ic_done_tick)
                    eventPeriod.remove(0)
                    eventPeriod.remove(15)
                    eventPeriod.remove(30)
                }
            }

            lay15days ->{
                if (eventPeriod.contains(15)){
                    Log.e("img15days","present")
                    img15days.setImageResource(R.drawable.ic_done_tick)
                    eventPeriod.remove(15)

                }else{
                    Log.e("img15days","absent")
                    img15days.setImageResource(R.drawable.ic_done_tick_green)
                    eventPeriod.add(15)

                    imgAllPeriod.setImageResource(R.drawable.ic_done_tick)
                    img8days.setImageResource(R.drawable.ic_done_tick)
                    img30days.setImageResource(R.drawable.ic_done_tick)
                    eventPeriod.remove(0)
                    eventPeriod.remove(8)
                    eventPeriod.remove(30)
                }
            }

            lay30days ->{
                if (eventPeriod.contains(30)){
                    Log.e("img30days","present")
                    img30days.setImageResource(R.drawable.ic_done_tick)
                    eventPeriod.remove(30)

                }else{
                    Log.e("img30days","absent")
                    img30days.setImageResource(R.drawable.ic_done_tick_green)
                    eventPeriod.add(30)

                    imgAllPeriod.setImageResource(R.drawable.ic_done_tick)
                    img8days.setImageResource(R.drawable.ic_done_tick)
                    img15days.setImageResource(R.drawable.ic_done_tick)
                    eventPeriod.remove(0)
                    eventPeriod.remove(8)
                    eventPeriod.remove(15)
                }
            }
        }
    }

    fun unselectAll(){
        if (arryEventType.contains(0)){
            imgTickAll.setImageResource(R.drawable.ic_done_tick)
            arryEventType.remove(0)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val preference = PreferenceHelper.customPrefs(MyApplication.instance, "user_info")
        var villageId: String? = preference[ApiConstant.VILLAGE_ID, "-1"]
        var intent = Intent(applicationContext, MyVillageEvent::class.java)

        if (arryEventType.isEmpty()){
            arryEventType.add(0)
        }
        if (eventPeriod.isEmpty()){
            eventPeriod.add(0)
        }

        var eventFilterBody =EventFilterBody(villageId.toString(),arryEventType, eventPeriod[0])
        Log.e("eventFilterBody",eventFilterBody.event_type.toString()+eventFilterBody.event_period.toString())
        intent.putExtra("eventFilterBody", eventFilterBody)
        setResult(Activity.RESULT_OK, intent)
        finish()
        super.onBackPressed()
    }
}