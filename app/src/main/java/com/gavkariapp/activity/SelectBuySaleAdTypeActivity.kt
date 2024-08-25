package com.gavkariapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.gavkariapp.Model.BuySaleType
import com.gavkariapp.R
import com.gavkariapp.constant.AppConstant.ANIMAL
import com.gavkariapp.constant.AppConstant.BUFFALO
import com.gavkariapp.constant.AppConstant.CAR
import com.gavkariapp.constant.AppConstant.COW
import com.gavkariapp.constant.AppConstant.CULTIVATOR
import com.gavkariapp.constant.AppConstant.EQUIPMENTS
import com.gavkariapp.constant.AppConstant.GOAT
import com.gavkariapp.constant.AppConstant.HEIFER_BUFFALO
import com.gavkariapp.constant.AppConstant.HEIFER_COW
import com.gavkariapp.constant.AppConstant.IRRIGATION_MATERIAL
import com.gavkariapp.constant.AppConstant.KUTTI_MACHINE
import com.gavkariapp.constant.AppConstant.MACHINERY
import com.gavkariapp.constant.AppConstant.MALE_BUFFALO
import com.gavkariapp.constant.AppConstant.MALE_GOAT
import com.gavkariapp.constant.AppConstant.OTHER
import com.gavkariapp.constant.AppConstant.OTHER_DOMESTIC_ANIMALS
import com.gavkariapp.constant.AppConstant.OTHER_MACHINE
import com.gavkariapp.constant.AppConstant.OX
import com.gavkariapp.constant.AppConstant.PICKUP
import com.gavkariapp.constant.AppConstant.PLOUGH
import com.gavkariapp.constant.AppConstant.ROTOVATOR
import com.gavkariapp.constant.AppConstant.SEED_DRILL
import com.gavkariapp.constant.AppConstant.SPRAY_BLOWER
import com.gavkariapp.constant.AppConstant.SPRAY_PUMP
import com.gavkariapp.constant.AppConstant.STEEL
import com.gavkariapp.constant.AppConstant.TEMPO_TRUCK
import com.gavkariapp.constant.AppConstant.THRESHER
import com.gavkariapp.constant.AppConstant.TRACTOR
import com.gavkariapp.constant.AppConstant.TROLLEY
import com.gavkariapp.constant.AppConstant.TWO_WHEELER
import com.gavkariapp.constant.AppConstant.WATER_MOTOR_PUMP
import kotlinx.android.synthetic.main.activity_select_buy_sale_ad_type.*

class SelectBuySaleAdTypeActivity : BaseActivity() {

    lateinit var buySaleType : BuySaleType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_select_buy_sale_ad_type)
        setupToolbar(R.id.toolbarHome, getString(R.string.lbl_sale))
        setUpView()
    }

    private fun setUpView(){

        val adType = intent.getIntExtra("adType",ANIMAL)
        when (adType) {
            ANIMAL -> {

                layAnimal.visibility = View.VISIBLE

                buySaleType = BuySaleType(ANIMAL,0,"")

                tvCow.setOnClickListener {
                    buySaleType.type = COW
                    buySaleType.name = tvCow.text.toString()
                    openCreateAd()
                }

                tvBuffalo.setOnClickListener {
                    buySaleType.type = BUFFALO
                    buySaleType.name = tvBuffalo.text.toString()
                    openCreateAd()
                }

                tvHeiferCow.setOnClickListener {
                    buySaleType.type = HEIFER_COW
                    buySaleType.name = tvHeiferCow.text.toString()
                    openCreateAd()
                }

                tvHeiferBuffalo.setOnClickListener {
                    buySaleType.type = HEIFER_BUFFALO
                    buySaleType.name = tvHeiferBuffalo.text.toString()
                    openCreateAd()
                }

                tvOx.setOnClickListener {
                    buySaleType.type = OX
                    buySaleType.name = tvOx.text.toString()
                    openCreateAd()
                }

                tvMaleBUffalo.setOnClickListener {
                    buySaleType.type = MALE_BUFFALO
                    buySaleType.name = tvMaleBUffalo.text.toString()
                    openCreateAd()
                }

                tvGoat.setOnClickListener {
                    buySaleType.type = GOAT
                    buySaleType.name = tvGoat.text.toString()
                    openCreateAd()
                }

                tvMaleGoat.setOnClickListener {
                    buySaleType.type = MALE_GOAT
                    buySaleType.name = tvMaleGoat.text.toString()
                    openCreateAd()
                }

                tvOtherDomesticAnimal.setOnClickListener {
                    buySaleType.type = OTHER_DOMESTIC_ANIMALS
                    buySaleType.name = tvOtherDomesticAnimal.text.toString()
                    openCreateAd()
                }

            }
            MACHINERY -> {

                layMachinery.visibility = View.VISIBLE
                tvSelectType.text = getString(R.string.lbl_select_mc)

                buySaleType = BuySaleType(MACHINERY,0,"")

                tvTractor.setOnClickListener {
                    buySaleType.type = TRACTOR
                    buySaleType.name = tvTractor.text.toString()
                    openCreateAd()
                }

                tvPickup.setOnClickListener {
                    buySaleType.type = PICKUP
                    buySaleType.name = tvPickup.text.toString()
                    openCreateAd()
                }

                tvTempoTruck.setOnClickListener {
                    buySaleType.type = TEMPO_TRUCK
                    buySaleType.name = tvTempoTruck.text.toString()
                    openCreateAd()
                }

                tvCar.setOnClickListener {
                    buySaleType.type = CAR
                    buySaleType.name = tvCar.text.toString()
                    openCreateAd()
                }

                tvTwoWheeler.setOnClickListener {
                    buySaleType.type = TWO_WHEELER
                    buySaleType.name = tvTwoWheeler.text.toString()
                    openCreateAd()
                }

                tvThresher.setOnClickListener {
                    buySaleType.type = THRESHER
                    buySaleType.name = tvThresher.text.toString()
                    openCreateAd()
                }

                tvKuttiMachine.setOnClickListener {
                    buySaleType.type = KUTTI_MACHINE
                    buySaleType.name = tvKuttiMachine.text.toString()
                    openCreateAd()
                }

                tvSprayBlower.setOnClickListener {
                    buySaleType.type = SPRAY_BLOWER
                    buySaleType.name = tvSprayBlower.text.toString()
                    openCreateAd()
                }

                tvOtherMc.setOnClickListener {
                    buySaleType.type = OTHER_MACHINE
                    buySaleType.name = tvOtherMc.text.toString()
                    openCreateAd()
                }

            }
            EQUIPMENTS -> {

                layEquipment.visibility = View.VISIBLE
                tvSelectType.text = getString(R.string.lbl_select_eq)

                buySaleType = BuySaleType(EQUIPMENTS,0,"")

                tvCultivator.setOnClickListener {
                    buySaleType.type = CULTIVATOR
                    buySaleType.name = tvCultivator.text.toString()
                    openCreateAd()
                }

                tvRotovator.setOnClickListener {
                    buySaleType.type = ROTOVATOR
                    buySaleType.name = tvRotovator.text.toString()
                    openCreateAd()
                }

                tvPlough.setOnClickListener {
                    buySaleType.type = PLOUGH
                    buySaleType.name = tvPlough.text.toString()
                    openCreateAd()
                }

                tvTrolley.setOnClickListener {
                    buySaleType.type = TROLLEY
                    buySaleType.name = tvTrolley.text.toString()
                    openCreateAd()
                }

                tvSeedDrill.setOnClickListener {
                    buySaleType.type = SEED_DRILL
                    buySaleType.name = tvSeedDrill.text.toString()
                    openCreateAd()
                }

                tvPump.setOnClickListener {
                    buySaleType.type = WATER_MOTOR_PUMP
                    buySaleType.name = tvPump.text.toString()
                    openCreateAd()
                }

                tvSprayPump.setOnClickListener {
                    buySaleType.type = SPRAY_PUMP
                    buySaleType.name = tvSprayPump.text.toString()
                    openCreateAd()
                }

                tvIrrigation.setOnClickListener {
                    buySaleType.type = IRRIGATION_MATERIAL
                    buySaleType.name = tvIrrigation.text.toString()
                    openCreateAd()
                }

                tvSteel.setOnClickListener {
                    buySaleType.type = STEEL
                    buySaleType.name = tvSteel.text.toString()
                    openCreateAd()
                }

                tvOtherEqui.setOnClickListener {
                    buySaleType.type = OTHER
                    buySaleType.name = tvOtherEqui.text.toString()
                    openCreateAd()
                }
            }
        }
    }

    private fun openCreateAd(){
        startActivity(Intent(this,CreateSellAdActivity::class.java).putExtra("buySaleType",buySaleType))
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
