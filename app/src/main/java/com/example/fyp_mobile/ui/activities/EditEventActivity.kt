package com.example.fyp_mobile.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import com.example.fyp_mobile.R
import com.example.fyp_mobile.databinding.ActivityEditEventBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.Event
import com.example.fyp_mobile.model.Rewards
import com.example.fyp_mobile.utils.Constants
import com.example.fyp_mobile.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_edit_event.*
import kotlinx.android.synthetic.main.activity_edit_reward.*
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

class EditEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditEventBinding

    private lateinit var mEventDetails: Event

    private var id = ""

    private var mSelectedImageFileUri: Uri? = null

    private var mEventImageURL: String = ""

    //Date
    private val DATE_PATTERN: Pattern = Pattern.compile(
        "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])\$"
    )

    private val TIME: Pattern = Pattern.compile(
        "(1[012]|[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)-(1[012]|[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //event ID
        id = intent.getStringExtra("id")!!

        FirestoreClass().getEventDetails(this, id)

        binding.backBtnEditEvent.setOnClickListener {
            onBackPressed()
        }

        binding.imageEditEvent.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
                //Toast.makeText(applicationContext,"You already has the storage permission!", Toast.LENGTH_SHORT).show()
            }
            else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        binding.btnEditEvent.setOnClickListener {
            if(validateEvent()){
                if(mSelectedImageFileUri != null) {
                    Log.e("update","I change pic")
                    FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri)
                }
                else{
                    Log.e("update","No change pic")
                    updateEventDetails()
                }
            }
        }

        binding.btnEditUser.setOnClickListener {
            //To Participant Activity
            val intent = Intent(this@EditEventActivity, ParticipantActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }

    private fun validateEvent(): Boolean{
        val name = binding.etEditEvent.text.toString().trim()
        val desc = binding.etEditEventDesc.text.toString().trim()
        val point = binding.etEditEventPoint.text.toString().trim()
        val date = binding.etEditEventDate.text.toString().trim()
        val time = binding.etEditEventTime.text.toString().trim()
        val venue = binding.etEditEventVenue.text.toString().trim()

        if(name.isEmpty()){
            Toast.makeText(applicationContext,"Please Enter Event Name!", Toast.LENGTH_SHORT).show()
            return false
        }

        else if(desc.isEmpty()){
            Toast.makeText(applicationContext,"Please Enter Description!", Toast.LENGTH_SHORT).show()
            return false
        }

        else if(point.isEmpty()){
            Toast.makeText(applicationContext,"Please Enter Point!", Toast.LENGTH_SHORT).show()
            return false
        }

        else if(point.isDigitsOnly()){
            if(point.toInt() <= 0){
                Toast.makeText(applicationContext,"Please Valid Point!", Toast.LENGTH_SHORT).show()
                return false
            }
            else{
                if(DATE_PATTERN.matcher(date).matches()){
                    if(TIME.matcher(time).matches()){
                        if(venue.isEmpty()){
                            Toast.makeText(applicationContext,"Please Enter Venue!", Toast.LENGTH_SHORT).show()
                            return false
                        }
                        else{
                            return true
                        }
                    }
                    else{
                        Toast.makeText(applicationContext,"Please Follow the Time Format XX:XX AM-XX:XX PM!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                }
                else{
                    Toast.makeText(applicationContext,"Please Follow the Date Format YYYY-MM-DD!", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
        }

        else{
            Toast.makeText(applicationContext,"Please Enter Valid Point!", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun updateEventDetails(){
        val eventHashMap = HashMap<String, Any>()

        //String
        val name = binding.etEditEvent.text.toString().trim()

        if(name != mEventDetails.name){
            eventHashMap["name"] = name
        }

        val desc = binding.etEditEventDesc.text.toString().trim()

        if(desc != mEventDetails.desc){
            eventHashMap["desc"] = desc
        }

        //Int
        val point = binding.etEditEventPoint.text.toString().trim()

        if(point.isNotEmpty()){
            eventHashMap["points"] = point.toInt()
        }

        val date = binding.etEditEventDate.text.toString().trim()

        if(date != mEventDetails.date){
            eventHashMap["date"] = date
        }

        val time = binding.etEditEventTime.text.toString().trim()

        if(time != mEventDetails.time){
            eventHashMap["time"] = time
        }

        val venue = binding.etEditEventVenue.text.toString().trim()

        if(venue != mEventDetails.venue){
            eventHashMap["venue"] = venue
        }

        //Image
        if(mEventImageURL.isNotEmpty()){
            eventHashMap[Constants.IMAGE] = mEventImageURL
        }

        Log.e("update", "updating event")
        FirestoreClass().updateEventData(this, eventHashMap, id)
    }

    fun eventDetailsUpdateSuccess(){
        Toast.makeText(applicationContext,"Event Updated!", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this@EditEventActivity, AdminEventActivity::class.java))
        finish()
    }

    fun eventDetailsSuccess(event: Event){

        mEventDetails = event

        GlideLoader(this@EditEventActivity).loadRewardPicture(event.image, imageEditEvent)

        binding.etEditEvent.setText(event.name)
        binding.etEditEventDesc.setText(event.desc)
        binding.etEditEventPoint.setText(event.points.toString())
        binding.etEditEventDate.setText(event.date)
        binding.etEditEventTime.setText(event.time)
        binding.etEditEventVenue.setText(event.venue)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
                //Toast.makeText(applicationContext,"Permission Granted!", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,resources.getString(R.string.read_storage_permission_denied), Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(data != null){
                try{
                    mSelectedImageFileUri = data.data!!

                    //GlideLoader Class
                    GlideLoader(this).loadRewardPicture(mSelectedImageFileUri!!, imageEditEvent)
                    //binding.ivUserPhoto.setImageURI(selectedImageFileUri)

                } catch(e: IOException){
                    e.printStackTrace()
                    Toast.makeText(this@EditEventActivity,resources.getString(R.string.image_selection_failed), Toast.LENGTH_SHORT).show()
                }
            }
        } else if(resultCode == Activity.RESULT_CANCELED){
            Log.e("update", "Image Select Cancelled")
        }
    }

    fun imageUploadSuccess(imageURL: String) {

        mEventImageURL = imageURL
        updateEventDetails()
    }
}