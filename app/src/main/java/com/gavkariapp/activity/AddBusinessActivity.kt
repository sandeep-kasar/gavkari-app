package com.gavkariapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.gavkariapp.R
import kotlinx.android.synthetic.main.activity_add_business.*

class AddBusinessActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_add_business)
        setupToolbar(R.id.toolbarHome, getString(R.string.add_business))
        tvKirana.setOnClickListener(this)
        tvClinic.setOnClickListener(this)
        tvTractor.setOnClickListener(this)
        tvAgriStore.setOnClickListener(this)
        tvMill.setOnClickListener(this)
        tvHotel.setOnClickListener(this)
        tvWireman.setOnClickListener(this)
        tvPostman.setOnClickListener(this)
        tvCarpenter.setOnClickListener(this)
        tvPainter.setOnClickListener(this)
        tvFarmWorker.setOnClickListener(this)
        tvGovtOffice.setOnClickListener(this)
        tvTravel.setOnClickListener(this)
        tvDecoration.setOnClickListener(this)
        tvConstruction.setOnClickListener(this)
        tvOtherbusiness.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v) {
            tvKirana->{
                var business = tvKirana.text.toString().trim()
                openCreateAdOneActivity(business)
            }

            tvClinic->{
                var business = tvClinic.text.toString().trim()
                openCreateAdOneActivity(business)
            }

            tvTractor->{
                var business = tvTractor.text.toString().trim()
                openCreateAdOneActivity(business)
            }

            tvAgriStore->{
                var business = tvAgriStore.text.toString().trim()
                openCreateAdOneActivity(business)
            }

            tvMill->{
                var business = tvMill.text.toString().trim()
                openCreateAdOneActivity(business)
            }

            tvHotel->{
                var business = tvHotel.text.toString().trim()
                openCreateAdOneActivity(business)
            }

            tvWireman->{
                var business = tvWireman.text.toString().trim()
                openCreateAdOneActivity(business)
            }

            tvHotel->{
                var business = tvHotel.text.toString().trim()
                openCreateAdOneActivity(business)
            }

            tvPostman->{
                var business = tvPostman.text.toString().trim()
                openCreateAdOneActivity(business)
            }

            tvCarpenter->{
                var business = tvCarpenter.text.toString().trim()
                openCreateAdOneActivity(business)
            }

            tvPainter->{
                var business = tvPainter.text.toString().trim()
                openCreateAdOneActivity(business)
            }

            tvFarmWorker->{
                var business = tvFarmWorker.text.toString().trim()
                openCreateAdOneActivity(business)
            }

            tvGovtOffice->{
                var business = tvGovtOffice.text.toString().trim()
                openCreateAdOneActivity(business)
            }

            tvTravel->{
                var business = tvTravel.text.toString().trim()
                openCreateAdOneActivity(business)
            }

            tvDecoration->{
                var business = tvDecoration.text.toString().trim()
                openCreateAdOneActivity(business)
            }

            tvConstruction->{
                var business = tvConstruction.text.toString().trim()
                openCreateAdOneActivity(business)
            }

            tvOtherbusiness->{
                var business = tvOtherbusiness.text.toString().trim()
                openCreateAdOneActivity(business)
            }
        }
    }



    private fun openCreateAdOneActivity(business:String) {
        startActivity(Intent(applicationContext, CreateDirectoryActivity::class.java)
                    .putExtra("business", business))
        finish()

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
