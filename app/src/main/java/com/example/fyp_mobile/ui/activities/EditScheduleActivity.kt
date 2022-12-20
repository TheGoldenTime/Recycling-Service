package com.example.fyp_mobile.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fyp_mobile.R
import com.example.fyp_mobile.databinding.ActivityEditScheduleBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.Rewards
import com.example.fyp_mobile.model.Schedule
import com.example.fyp_mobile.utils.Constants
import com.example.fyp_mobile.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_edit_reward.*
import java.util.regex.Pattern


class EditScheduleActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener{

    private lateinit var binding: ActivityEditScheduleBinding

    private lateinit var mScheduleDetails: Schedule

    private var areaID = ""

    private var scheduleID = ""

    private val TIME: Pattern = Pattern.compile(
        "(1[012]|[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)-(1[012]|[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)"
    )

    private var day = ""
    private var time = "12:00PM-2:PM"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val extras = intent.extras
        areaID = extras!!.getString("areaID").toString()
        scheduleID = extras.getString("scheduleID").toString()

        FirestoreClass().getScheduleDetail(this, areaID, scheduleID)

        val spinnerDay: Spinner = binding.spinnerDay
        val spinnerTime: Spinner = binding.spinnerTime
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.day_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerDay.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.time_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerTime.adapter = adapter
        }

        binding.backBtnEditSchedule.setOnClickListener {
            onBackPressed()
        }

        binding.btnEditSchedule.setOnClickListener {
            updateScheduleDetails()
        }

        binding.btnEditUserList.setOnClickListener {

            //if(validateSchedule()){
                val intent = Intent(this@EditScheduleActivity, UserListActivity::class.java)
                val extras = Bundle()
                extras.putString("areaID", areaID)
                extras.putString("scheduleID", scheduleID)
                intent.putExtras(extras)
                startActivity(intent)
            //}
        }

    }

//    private fun validateSchedule(): Boolean{

//        val day = binding.etEditScheduleDay.text.toString().trim()
//        val time = binding.etEditScheduleTime.text.toString().trim()
//
//        if((day == "Monday") || (day == "Tuesday") || (day == "Wednesday") || (day == "Thursday") || (day == "Friday") || (day == "Saturday") || (day == "Sunday")){
//            if(TIME.matcher(time).matches()){
//                return true
//            }
//
//            Toast.makeText(applicationContext,"Please Follow the Time Format XX:XX AM-XX:XX PM!", Toast.LENGTH_SHORT).show()
//            return false
//
//        }
//
//        else{
//            Toast.makeText(applicationContext,"Please Enter A correct Day!", Toast.LENGTH_SHORT).show()
//            return false
//        }
//    }

    private fun updateScheduleDetails(){
        val scheduleHashMap = HashMap<String, Any>()

        val day = day

        if(day != mScheduleDetails.day){
            scheduleHashMap["day"] = day
        }

        //val time = binding.etEditScheduleTime.text.toString().trim()

        if(time != mScheduleDetails.time){
            scheduleHashMap["time"] = time
        }

        Log.e("update", "updating schedule")
        FirestoreClass().updateSchedule(this, scheduleHashMap, areaID, scheduleID)
    }

    fun scheduleUpdateSuccess(){
        Toast.makeText(applicationContext,"Schedule Updated!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this@EditScheduleActivity, AdminScheduleActivity::class.java)
        intent.putExtra("id", areaID)
        startActivity(intent)
        finish()
    }

    fun scheduleDetailsSuccess(schedule: Schedule){

        mScheduleDetails = schedule

//        binding.etEditScheduleDay.setText(schedule.day)
//        binding.etEditScheduleTime.setText(schedule.time)

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        day = parent.getItemAtPosition(pos).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}