package com.gavkariapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.gavkariapp.Model.News
import com.gavkariapp.Model.NewsFilterBody
import com.gavkariapp.R
import kotlinx.android.synthetic.main.activity_news_filter.*

class NewsFilterActivity : BaseActivity(),View.OnClickListener {

    var arryNewsType = ArrayList<Int>()
    var newsPeriod = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_news_filter)
        setupToolbar(R.id.toolbarHome, getString(R.string.title_filter))
        arryNewsType.add(0)
        newsPeriod.add(0)

        btnApply.setOnClickListener(this)
        layAll.setOnClickListener(this)
        layPanchayat.setOnClickListener(this)
        layHealthService.setOnClickListener(this)
        laySadNews.setOnClickListener(this)
        laySchool.setOnClickListener(this)
        laySarpanch.setOnClickListener(this)
        layPolicePatil.setOnClickListener(this)
        layVillageOther.setOnClickListener(this)
        layMP.setOnClickListener(this)
        layMLA.setOnClickListener(this)
        layAgri.setOnClickListener(this)
        layYojana.setOnClickListener(this)
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
                if (arryNewsType.contains(0)){
                    Log.e("All","present")
                    imgTickAll.setImageResource(R.drawable.ic_done_tick)
                    arryNewsType.remove(0)

                }else{
                    Log.e("All","absent")
                    imgTickAll.setImageResource(R.drawable.ic_done_tick_green)
                    arryNewsType.add(0)
                    arryNewsType.remove(1)
                    arryNewsType.remove(2)
                    arryNewsType.remove(3)
                    arryNewsType.remove(4)
                    arryNewsType.remove(5)
                    arryNewsType.remove(6)
                    arryNewsType.remove(7)
                    arryNewsType.remove(8)
                    arryNewsType.remove(9)
                    arryNewsType.remove(10)
                    arryNewsType.remove(11)
                    imgTickPanchayat.setImageResource(R.drawable.ic_done_tick)
                    imgTickHealthServ.setImageResource(R.drawable.ic_done_tick)
                    imgTickSchool.setImageResource(R.drawable.ic_done_tick)
                    imgTickSadNews.setImageResource(R.drawable.ic_done_tick)
                    imgTickSarpanch.setImageResource(R.drawable.ic_done_tick)
                    imgTickPolicePatil.setImageResource(R.drawable.ic_done_tick)
                    imgTickVillageOther.setImageResource(R.drawable.ic_done_tick)
                    imgTickMLA.setImageResource(R.drawable.ic_done_tick)
                    imgTickMP.setImageResource(R.drawable.ic_done_tick)
                    imgTickAgri.setImageResource(R.drawable.ic_done_tick)
                    imgTickyojana.setImageResource(R.drawable.ic_done_tick)
                }
            }

            layPanchayat ->{

                unselectAll()

                if (arryNewsType.contains(1)){
                    Log.e("layWedding","present")
                    imgTickPanchayat.setImageResource(R.drawable.ic_done_tick)
                    arryNewsType.remove(1)

                }else{
                    Log.e("layWedding","absent")
                    imgTickPanchayat.setImageResource(R.drawable.ic_done_tick_green)
                    arryNewsType.add(1)
                    arryNewsType.remove(8)
                    arryNewsType.remove(9)
                    arryNewsType.remove(10)
                    arryNewsType.remove(11)
                    imgTickMLA.setImageResource(R.drawable.ic_done_tick)
                    imgTickMP.setImageResource(R.drawable.ic_done_tick)
                    imgTickAgri.setImageResource(R.drawable.ic_done_tick)
                    imgTickyojana.setImageResource(R.drawable.ic_done_tick)
                }
            }


            layHealthService ->{

                unselectAll()

                if (arryNewsType.contains(2)){
                    Log.e("imgTickEngage","present")
                    imgTickHealthServ.setImageResource(R.drawable.ic_done_tick)
                    arryNewsType.remove(2)

                }else{
                    Log.e("imgTickEngage","absent")
                    imgTickHealthServ.setImageResource(R.drawable.ic_done_tick_green)
                    arryNewsType.add(2)
                    arryNewsType.remove(8)
                    arryNewsType.remove(9)
                    arryNewsType.remove(10)
                    arryNewsType.remove(11)
                    imgTickMLA.setImageResource(R.drawable.ic_done_tick)
                    imgTickMP.setImageResource(R.drawable.ic_done_tick)
                    imgTickAgri.setImageResource(R.drawable.ic_done_tick)
                    imgTickyojana.setImageResource(R.drawable.ic_done_tick)
                }
            }

            laySchool ->{
                unselectAll()
                if (arryNewsType.contains(3)){
                    Log.e("imgTickFrstMem","present")
                    imgTickSchool.setImageResource(R.drawable.ic_done_tick)
                    arryNewsType.remove(3)

                }else{
                    Log.e("imgTickFrstMem","absent")
                    imgTickSchool.setImageResource(R.drawable.ic_done_tick_green)
                    arryNewsType.add(3)
                    arryNewsType.remove(8)
                    arryNewsType.remove(9)
                    arryNewsType.remove(10)
                    arryNewsType.remove(11)
                    imgTickMLA.setImageResource(R.drawable.ic_done_tick)
                    imgTickMP.setImageResource(R.drawable.ic_done_tick)
                    imgTickAgri.setImageResource(R.drawable.ic_done_tick)
                    imgTickyojana.setImageResource(R.drawable.ic_done_tick)
                }
            }

            laySadNews ->{
                unselectAll()
                if (arryNewsType.contains(4)){
                    Log.e("imgTickFrstMem","present")
                    imgTickSadNews.setImageResource(R.drawable.ic_done_tick)
                    arryNewsType.remove(4)

                }else{
                    Log.e("imgTickFrstMem","absent")
                    imgTickSadNews.setImageResource(R.drawable.ic_done_tick_green)
                    arryNewsType.add(4)
                    arryNewsType.remove(8)
                    arryNewsType.remove(9)
                    arryNewsType.remove(10)
                    arryNewsType.remove(11)
                    imgTickMLA.setImageResource(R.drawable.ic_done_tick)
                    imgTickMP.setImageResource(R.drawable.ic_done_tick)
                    imgTickAgri.setImageResource(R.drawable.ic_done_tick)
                    imgTickyojana.setImageResource(R.drawable.ic_done_tick)
                }
            }

            laySarpanch ->{

                unselectAll()

                if (arryNewsType.contains(5)){
                    Log.e("imgTickHousewarm","present")
                    imgTickSarpanch.setImageResource(R.drawable.ic_done_tick)
                    arryNewsType.remove(5)

                }else{
                    Log.e("imgTickHousewarm","absent")
                    imgTickSarpanch.setImageResource(R.drawable.ic_done_tick_green)
                    arryNewsType.add(5)
                    arryNewsType.remove(8)
                    arryNewsType.remove(9)
                    arryNewsType.remove(10)
                    arryNewsType.remove(11)
                    imgTickMLA.setImageResource(R.drawable.ic_done_tick)
                    imgTickMP.setImageResource(R.drawable.ic_done_tick)
                    imgTickAgri.setImageResource(R.drawable.ic_done_tick)
                    imgTickyojana.setImageResource(R.drawable.ic_done_tick)
                }
            }

            layPolicePatil ->{
                unselectAll()
                if (arryNewsType.contains(6)){
                    Log.e("imgTickDashkriya","present")
                    imgTickPolicePatil.setImageResource(R.drawable.ic_done_tick)
                    arryNewsType.remove(6)

                }else{
                    Log.e("imgTickDashkriya","absent")
                    imgTickPolicePatil.setImageResource(R.drawable.ic_done_tick_green)
                    arryNewsType.add(6)
                    arryNewsType.remove(8)
                    arryNewsType.remove(9)
                    arryNewsType.remove(10)
                    arryNewsType.remove(11)
                    imgTickMLA.setImageResource(R.drawable.ic_done_tick)
                    imgTickMP.setImageResource(R.drawable.ic_done_tick)
                    imgTickAgri.setImageResource(R.drawable.ic_done_tick)
                    imgTickyojana.setImageResource(R.drawable.ic_done_tick)
                }
            }


            layVillageOther ->{

                unselectAll()

                if (arryNewsType.contains(7)){
                    Log.e("imgTickJagaran","present")
                    imgTickVillageOther.setImageResource(R.drawable.ic_done_tick)
                    arryNewsType.remove(7)

                }else{
                    Log.e("imgTickJagaran","absent")
                    imgTickVillageOther.setImageResource(R.drawable.ic_done_tick_green)
                    arryNewsType.add(7)
                    arryNewsType.remove(8)
                    arryNewsType.remove(9)
                    arryNewsType.remove(10)
                    arryNewsType.remove(11)
                    imgTickMLA.setImageResource(R.drawable.ic_done_tick)
                    imgTickMP.setImageResource(R.drawable.ic_done_tick)
                    imgTickAgri.setImageResource(R.drawable.ic_done_tick)
                    imgTickyojana.setImageResource(R.drawable.ic_done_tick)

                }
            }

            layMP ->{
                unselectAll()
                if (arryNewsType.contains(8)){
                    Log.e("imgTickBirthday","present")
                    imgTickMP.setImageResource(R.drawable.ic_done_tick)
                    arryNewsType.remove(8)

                }else{
                    Log.e("imgTickBirthday","absent")
                    imgTickMP.setImageResource(R.drawable.ic_done_tick_green)
                    arryNewsType.add(8)
                    arryNewsType.remove(1)
                    arryNewsType.remove(2)
                    arryNewsType.remove(3)
                    arryNewsType.remove(4)
                    arryNewsType.remove(5)
                    arryNewsType.remove(6)
                    arryNewsType.remove(7)
                    arryNewsType.remove(10)
                    arryNewsType.remove(11)
                    imgTickPanchayat.setImageResource(R.drawable.ic_done_tick)
                    imgTickHealthServ.setImageResource(R.drawable.ic_done_tick)
                    imgTickSchool.setImageResource(R.drawable.ic_done_tick)
                    imgTickSadNews.setImageResource(R.drawable.ic_done_tick)
                    imgTickSarpanch.setImageResource(R.drawable.ic_done_tick)
                    imgTickPolicePatil.setImageResource(R.drawable.ic_done_tick)
                    imgTickVillageOther.setImageResource(R.drawable.ic_done_tick)
                    imgTickAgri.setImageResource(R.drawable.ic_done_tick)
                    imgTickyojana.setImageResource(R.drawable.ic_done_tick)
                }
            }


            layMLA ->{
                unselectAll()
                if (arryNewsType.contains(9)){
                    Log.e("imgTickRetirement","present")
                    imgTickMLA.setImageResource(R.drawable.ic_done_tick)
                    arryNewsType.remove(9)

                }else{
                    Log.e("imgTickRetirement","absent")
                    imgTickMLA.setImageResource(R.drawable.ic_done_tick_green)
                    arryNewsType.add(9)
                    arryNewsType.remove(1)
                    arryNewsType.remove(2)
                    arryNewsType.remove(3)
                    arryNewsType.remove(4)
                    arryNewsType.remove(5)
                    arryNewsType.remove(6)
                    arryNewsType.remove(7)
                    arryNewsType.remove(10)
                    arryNewsType.remove(11)
                    imgTickPanchayat.setImageResource(R.drawable.ic_done_tick)
                    imgTickHealthServ.setImageResource(R.drawable.ic_done_tick)
                    imgTickSchool.setImageResource(R.drawable.ic_done_tick)
                    imgTickSadNews.setImageResource(R.drawable.ic_done_tick)
                    imgTickSarpanch.setImageResource(R.drawable.ic_done_tick)
                    imgTickPolicePatil.setImageResource(R.drawable.ic_done_tick)
                    imgTickVillageOther.setImageResource(R.drawable.ic_done_tick)
                    imgTickAgri.setImageResource(R.drawable.ic_done_tick)
                    imgTickyojana.setImageResource(R.drawable.ic_done_tick)
                }
            }



            layAgri ->{
                unselectAll()
                if (arryNewsType.contains(10)){
                    Log.e("imgTickSatyanarayan","present")
                    imgTickAgri.setImageResource(R.drawable.ic_done_tick)
                    arryNewsType.remove(10)

                }else{
                    Log.e("imgTickSatyanarayan","absent")
                    imgTickAgri.setImageResource(R.drawable.ic_done_tick_green)
                    arryNewsType.add(10)
                    arryNewsType.remove(1)
                    arryNewsType.remove(2)
                    arryNewsType.remove(3)
                    arryNewsType.remove(4)
                    arryNewsType.remove(5)
                    arryNewsType.remove(6)
                    arryNewsType.remove(7)
                    arryNewsType.remove(8)
                    arryNewsType.remove(9)
                    imgTickPanchayat.setImageResource(R.drawable.ic_done_tick)
                    imgTickHealthServ.setImageResource(R.drawable.ic_done_tick)
                    imgTickSchool.setImageResource(R.drawable.ic_done_tick)
                    imgTickSadNews.setImageResource(R.drawable.ic_done_tick)
                    imgTickSarpanch.setImageResource(R.drawable.ic_done_tick)
                    imgTickPolicePatil.setImageResource(R.drawable.ic_done_tick)
                    imgTickVillageOther.setImageResource(R.drawable.ic_done_tick)
                    imgTickMLA.setImageResource(R.drawable.ic_done_tick)
                    imgTickMP.setImageResource(R.drawable.ic_done_tick)
                }
            }

            layYojana ->{
                unselectAll()
                if (arryNewsType.contains(11)){
                    Log.e("imgTickMahaP","present")
                    imgTickyojana.setImageResource(R.drawable.ic_done_tick)
                    arryNewsType.remove(11)

                }else{
                    Log.e("imgTickMahaP","absent")
                    imgTickyojana.setImageResource(R.drawable.ic_done_tick_green)
                    arryNewsType.add(11)
                    arryNewsType.remove(1)
                    arryNewsType.remove(2)
                    arryNewsType.remove(3)
                    arryNewsType.remove(4)
                    arryNewsType.remove(5)
                    arryNewsType.remove(6)
                    arryNewsType.remove(7)
                    arryNewsType.remove(8)
                    arryNewsType.remove(9)
                    imgTickPanchayat.setImageResource(R.drawable.ic_done_tick)
                    imgTickHealthServ.setImageResource(R.drawable.ic_done_tick)
                    imgTickSchool.setImageResource(R.drawable.ic_done_tick)
                    imgTickSadNews.setImageResource(R.drawable.ic_done_tick)
                    imgTickSarpanch.setImageResource(R.drawable.ic_done_tick)
                    imgTickPolicePatil.setImageResource(R.drawable.ic_done_tick)
                    imgTickVillageOther.setImageResource(R.drawable.ic_done_tick)
                    imgTickMLA.setImageResource(R.drawable.ic_done_tick)
                    imgTickMP.setImageResource(R.drawable.ic_done_tick)
                }
            }


            layAllPeriod ->{
                if (newsPeriod.contains(0)){
                    Log.e("imgAllPeriod","present")
                    imgAllPeriod.setImageResource(R.drawable.ic_done_tick)
                    newsPeriod.remove(0)

                }else{
                    Log.e("imgAllPeriod","absent")
                    imgAllPeriod.setImageResource(R.drawable.ic_done_tick_green)
                    newsPeriod.add(0)
                    img8days.setImageResource(R.drawable.ic_done_tick)
                    img15days.setImageResource(R.drawable.ic_done_tick)
                    img30days.setImageResource(R.drawable.ic_done_tick)
                    newsPeriod.remove(8)
                    newsPeriod.remove(15)
                    newsPeriod.remove(30)
                }
            }

            lay8days ->{
                if (newsPeriod.contains(7)){
                    Log.e("img8days","present")
                    img8days.setImageResource(R.drawable.ic_done_tick)
                    newsPeriod.remove(7)

                }else{
                    Log.e("img8days","absent")
                    img8days.setImageResource(R.drawable.ic_done_tick_green)
                    newsPeriod.add(7)

                    imgAllPeriod.setImageResource(R.drawable.ic_done_tick)
                    img15days.setImageResource(R.drawable.ic_done_tick)
                    img30days.setImageResource(R.drawable.ic_done_tick)
                    newsPeriod.remove(0)
                    newsPeriod.remove(15)
                    newsPeriod.remove(30)
                }
            }

            lay15days ->{
                if (newsPeriod.contains(15)){
                    Log.e("img15days","present")
                    img15days.setImageResource(R.drawable.ic_done_tick)
                    newsPeriod.remove(15)

                }else{
                    Log.e("img15days","absent")
                    img15days.setImageResource(R.drawable.ic_done_tick_green)
                    newsPeriod.add(15)

                    imgAllPeriod.setImageResource(R.drawable.ic_done_tick)
                    img8days.setImageResource(R.drawable.ic_done_tick)
                    img30days.setImageResource(R.drawable.ic_done_tick)
                    newsPeriod.remove(0)
                    newsPeriod.remove(7)
                    newsPeriod.remove(30)
                }
            }

            lay30days ->{
                if (newsPeriod.contains(30)){
                    Log.e("img30days","present")
                    img30days.setImageResource(R.drawable.ic_done_tick)
                    newsPeriod.remove(30)

                }else{
                    Log.e("img30days","absent")
                    img30days.setImageResource(R.drawable.ic_done_tick_green)
                    newsPeriod.add(30)

                    imgAllPeriod.setImageResource(R.drawable.ic_done_tick)
                    img8days.setImageResource(R.drawable.ic_done_tick)
                    img15days.setImageResource(R.drawable.ic_done_tick)
                    newsPeriod.remove(0)
                    newsPeriod.remove(7)
                    newsPeriod.remove(15)
                }
            }
        }
    }

    fun unselectAll(){
        if (arryNewsType.contains(0)){
            imgTickAll.setImageResource(R.drawable.ic_done_tick)
            arryNewsType.remove(0)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        var intent = Intent(applicationContext, News::class.java)
        if (arryNewsType.isEmpty()){
            arryNewsType.add(0)
        }
        if (newsPeriod.isEmpty()){
            newsPeriod.add(0)
        }
        var newsFilterBody = NewsFilterBody("0",arryNewsType,newsPeriod[0])
        intent.putExtra("newsFilterBody", newsFilterBody)
        setResult(Activity.RESULT_OK, intent)
        finish()
        super.onBackPressed()
    }
}