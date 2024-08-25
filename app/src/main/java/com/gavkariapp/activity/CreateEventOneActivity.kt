package com.gavkariapp.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.gavkariapp.Model.CreateAdBody
import com.gavkariapp.Model.Media
import com.gavkariapp.Model.Matter
import com.gavkariapp.R
import com.gavkariapp.constant.AppConstant.BIRTHDAY
import com.gavkariapp.constant.AppConstant.DASHKRIYA_VIDHI
import com.gavkariapp.constant.AppConstant.ENGAGEMENT
import com.gavkariapp.constant.AppConstant.FIRST_MEMORIAL
import com.gavkariapp.constant.AppConstant.RETIREMENT
import com.gavkariapp.constant.AppConstant.WEDDING
import com.gavkariapp.helper.DatePickerEvent
import com.gavkariapp.utility.InputValidatorHelper
import com.gavkariapp.utility.Util
import kotlinx.android.synthetic.main.activity_create_event_one.*
import java.util.*
import kotlin.collections.ArrayList


class CreateEventOneActivity : BaseActivity(), View.OnClickListener {

    private lateinit var matter: Matter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event_one)
        applyLocale(this)
        setupToolbar(R.id.toolbarHome, getString(R.string.title_create_ad))
        btnContinueOne.setOnClickListener(this)
        tvDate.setOnClickListener(this)
        tvTime.setOnClickListener(this)
        edtAdTitle.setOnClickListener(this)
        matter = intent.getSerializableExtra("eventMatter") as Matter
        if (matter.type == WEDDING || matter.type == ENGAGEMENT){
            tvDateTime.text = getString(R.string.lbl_muhurt)
            laySubtitle.visibility =View.GONE
            laySubtitlePerson.visibility =View.GONE
            layLagnSubtitle.visibility = View.VISIBLE
            setEvent(matter)
        }else if(matter.type == FIRST_MEMORIAL || matter.type == DASHKRIYA_VIDHI){
            laySubtitle.visibility =View.GONE
            layLagnSubtitle.visibility = View.GONE
            laySubtitlePerson.visibility =View.VISIBLE
            tvSubtitlePerson.text = getString(R.string.late_person_name)
            setEvent(matter)

        }else if(matter.type == BIRTHDAY || matter.type == RETIREMENT){
            laySubtitle.visibility =View.GONE
            layLagnSubtitle.visibility = View.GONE
            laySubtitlePerson.visibility =View.VISIBLE
            tvSubtitlePerson.text = getString(R.string.lbl_person_name)
            setEvent(matter)
        }else{
            layLagnSubtitle.visibility = View.GONE
            laySubtitlePerson.visibility =View.GONE
            laySubtitle.visibility =View.VISIBLE
            setEvent(matter)
        }

    }

    fun setEvent(matter: Matter){


        if (matter.type == WEDDING || matter.type == ENGAGEMENT){

            edtAdTitle.setText(matter.title)
            edtSubTitleOne.setText(matter.subtitle)
            edtSubTitleTwo.setText(matter.subtitle_one)
            edtSubTitleThree.setText(matter.subtitle_two)
            edtSubTitleFour.setText(matter.subtitle_three)
            edtSubTitleFive.setText(matter.subtitle_four)
            edtSubTitleSix.setText(matter.subtitle_five)
            edtFamily.setText(matter.family)
            edtMuhurt.setText(matter.muhurt)
            edtAddress.setText(matter.place)

            edtSubTitleOne.hint = matter.subtitle
            edtSubTitleTwo.hint = matter.subtitle_one
            edtSubTitleThree.hint = matter.subtitle_two
            edtSubTitleFour.hint = matter.subtitle_three
            edtSubTitleFive.hint = matter.subtitle_four
            edtSubTitleSix.hint = matter.subtitle_five
            edtFamily.hint = matter.family
            edtMuhurt.hint = matter.muhurt
            edtAddress.hint = matter.place

        }else if(matter.type == FIRST_MEMORIAL || matter.type == DASHKRIYA_VIDHI ||
                matter.type == BIRTHDAY || matter.type == RETIREMENT){

            edtAdTitle.setText(matter.title)
            edtSubTitleDetails.setText(matter.subtitle)
            edtSubTitlePerson.setText(matter.subtitle_one)
            edtFamily.setText(matter.family)
            edtMuhurt.setText(matter.muhurt)
            edtAddress.setText(matter.place)

            edtSubTitleDetails.hint = matter.subtitle
            edtSubTitlePerson.hint = matter.subtitle_one
            edtFamily.hint = matter.family
            edtMuhurt.hint = matter.muhurt
            edtAddress.hint = matter.place


        }else{
            edtAdTitle.setText(matter.title)
            edtSubTitle.setText(matter.subtitle)
            edtFamily.setText(matter.family)
            edtMuhurt.setText(matter.muhurt)
            edtAddress.setText(matter.place)

            edtSubTitle.hint = matter.subtitle
            edtFamily.hint = matter.family
            edtMuhurt.hint = matter.muhurt
            edtAddress.hint = matter.place
        }
    }


    override fun onClick(v: View?) {

        when (v) {
            btnContinueOne -> continueOne()
            tvDate -> selectDate()
            tvTime -> selectTime()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun selectDate() {
        val date = DatePickerEvent()
        val calender = Calendar.getInstance()
        val args = Bundle()
        args.putInt("year", calender.get(Calendar.YEAR))
        args.putInt("month", calender.get(Calendar.MONTH))
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH))
        date.arguments = args
        date.setCallBack(ondate)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            date.show(fragmentManager, "Date Picker")
        }
    }

    private var ondate: DatePickerDialog.OnDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                tvDate.text = (dayOfMonth).toString() + "/" + (monthOfYear + 1).toString() + "/" + (year).toString()
            }

    private fun selectTime() {
        val calendar = Calendar.getInstance()
        val tpd = TimePickerDialog(this, R.style.DatePickerDialogTheme,
                TimePickerDialog.OnTimeSetListener { _, i, i1 ->
                    tvTime.text = "$i:$i1"
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
        tpd.show()
    }

    private fun continueOne() {


        if (matter.type == WEDDING || matter.type == ENGAGEMENT){

            var adTitle = edtAdTitle.text.toString().trim()
            var subTitleOne = edtSubTitleOne.text.toString().trim()
            var subTitletwo = edtSubTitleTwo.text.toString().trim()
            var subTitleThree = edtSubTitleThree.text.toString().trim()
            var subTitleFour = edtSubTitleFour.text.toString().trim()
            var subTitleFive = edtSubTitleFive.text.toString().trim()
            var subTitleSix = edtSubTitleSix.text.toString().trim()
            var family = edtFamily.text.toString().trim()
            var date = tvDate.text.toString().trim()
            var time = tvTime.text.toString().trim()
            var muhurt = edtMuhurt.text.toString().trim()
            var address = edtAddress.text.toString().trim()

            var isTrue = validateVivah(adTitle, subTitleOne,subTitletwo,subTitleThree,subTitleFour,subTitleFive,subTitleSix,
                    family, date, time, muhurt, address)

            if (isTrue) {

                var inputDate = Util.getFormatedDateEnglish("$date $time",
                        "dd/MM/yyyy HH:mm", "yyyy-MM-dd HH:mm:ss")

                var millisecondsFromNow = Util.getInMilliSecond(inputDate!!)

                var eventMediaList = ArrayList<Media>()


                val createAdBody = CreateAdBody("", "", matter.type,adTitle,
                        subTitleOne,subTitletwo,subTitleThree,subTitleFour,subTitleFive,subTitleSix, family,
                        matter.description,matter.description_one, inputDate, millisecondsFromNow.toString(), muhurt,
                        eventMediaList, address,"", "", "", matter.mobile,matter.note,
                        matter.amount,"","",0)

                openCreateAdOneActivity(createAdBody)
            }
        }else if(matter.type == FIRST_MEMORIAL || matter.type == DASHKRIYA_VIDHI ||
                matter.type == BIRTHDAY || matter.type == RETIREMENT){

            var adTitle = edtAdTitle.text.toString().trim()
            var subTitlePerson = edtSubTitlePerson.text.toString().trim()
            var subTitleDetails = edtSubTitleDetails.text.toString().trim()
            var family = edtFamily.text.toString().trim()
            var date = tvDate.text.toString().trim()
            var time = tvTime.text.toString().trim()
            var muhurt = edtMuhurt.text.toString().trim()
            var address = edtAddress.text.toString().trim()


            var isTrue = validate(adTitle, subTitlePerson,subTitleDetails, family, date, time, muhurt, address)

            if (isTrue) {

                var inputDate = Util.getFormatedDateEnglish("$date $time",
                        "dd/MM/yyyy HH:mm", "yyyy-MM-dd HH:mm:ss")

                var millisecondsFromNow = Util.getInMilliSecond(inputDate!!)

                var eventMediaList = ArrayList<Media>()


                val createAdBody = CreateAdBody("", "", matter.type, adTitle,
                        subTitleDetails,subTitlePerson,"","","","",
                        family, matter.description,matter.description_one, inputDate, millisecondsFromNow.toString(),
                        muhurt, eventMediaList, address, "", "", "", matter.mobile,
                        matter.note, matter.amount, "", "", 0)

                openCreateAdOneActivity(createAdBody)
            }


        }else{
            var adTitle = edtAdTitle.text.toString().trim()
            var subTitle = edtSubTitle.text.toString().trim()
            var family = edtFamily.text.toString().trim()
            var date = tvDate.text.toString().trim()
            var time = tvTime.text.toString().trim()
            var muhurt = edtMuhurt.text.toString().trim()
            var address = edtAddress.text.toString().trim()


            var isTrue = validate(adTitle, "Not Avail",subTitle, family, date, time, muhurt, address)

            if (isTrue) {

                var inputDate = Util.getFormatedDateEnglish("$date $time",
                        "dd/MM/yyyy HH:mm", "yyyy-MM-dd HH:mm:ss")

                var millisecondsFromNow = Util.getInMilliSecond(inputDate!!)

                var eventMediaList = ArrayList<Media>()


                val createAdBody = CreateAdBody("", "", matter.type, adTitle,
                        subTitle,"","","","","",
                        family, matter.description,matter.description_one, inputDate, millisecondsFromNow.toString(),
                        muhurt, eventMediaList, address, "", "", "", matter.mobile,
                        matter.note, matter.amount, "", "", 0)

                openCreateAdOneActivity(createAdBody)
            }
        }





    }

    private fun openCreateAdOneActivity(createAdBodyLocal: CreateAdBody) {
            startActivity(Intent(applicationContext, CreateEventTwoActivity::class.java)
                    .putExtra("createAdBody", createAdBodyLocal))
    }

    private fun validate(adTitle: String, subTitle: String, subTitleDetails: String, family: String, date: String,
                         time: String, muhurt: String, address: String): Boolean {


        when {
            InputValidatorHelper.isNullOrEmpty(adTitle) -> {

                showError(getString(R.string.warning_empty_ad_title))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(subTitle) -> {

                showError(getString(R.string.warning_empty_sub_title_person))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(subTitleDetails) -> {

                showError(getString(R.string.warning_empty_sub_title))

                return false

            }


            InputValidatorHelper.isNullOrEmpty(family) -> {

                showError(getString(R.string.warning_empty_family_info))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(date) -> {

                showError(getString(R.string.warning_empty_date))

                return false

            }


            InputValidatorHelper.isNullOrEmpty(time) -> {

                showError(getString(R.string.warning_empty_time))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(muhurt) -> {

                showError(getString(R.string.warning_empty_muhurt))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(address) -> {

                showError(getString(R.string.warning_empty_address))

                return false

            }

            else -> return true
        }


    }

    private fun validateVivah(adTitle: String, subTitleOne: String,subTitleTwo: String,subTitleThree: String,
                              subTitleFour: String,subTitleFive: String,subTitleSix: String,
                              family: String, date: String, time: String, muhurt: String, address: String): Boolean {


        when {
            InputValidatorHelper.isNullOrEmpty(adTitle) -> {

                showError(getString(R.string.warning_empty_ad_title))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(subTitleOne) -> {

                showError(getString(R.string.lbl_grooms_name))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(subTitleTwo) -> {

                showError(getString(R.string.lbl_surname))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(subTitleThree) -> {

                showError(getString(R.string.lbl_parents_name))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(subTitleFour) -> {

                showError(getString(R.string.lbl_brides_name))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(subTitleFive) -> {

                showError(getString(R.string.lbl_surname))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(subTitleSix) -> {

                showError(getString(R.string.lbl_parents_name))

                return false

            }


            InputValidatorHelper.isNullOrEmpty(family) -> {

                showError(getString(R.string.warning_empty_family_info))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(date) -> {

                showError(getString(R.string.warning_empty_date))

                return false

            }


            InputValidatorHelper.isNullOrEmpty(time) -> {

                showError(getString(R.string.warning_empty_time))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(muhurt) -> {

                showError(getString(R.string.warning_empty_muhurt))

                return false

            }

            InputValidatorHelper.isNullOrEmpty(address) -> {

                showError(getString(R.string.warning_empty_address))

                return false

            }

            else -> return true
        }
    }
}
