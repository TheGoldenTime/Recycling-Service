package com.example.fyp_mobile.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import com.example.fyp_mobile.R
import com.example.fyp_mobile.databinding.ActivityAddEventBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.Event
import com.example.fyp_mobile.model.Rewards
import com.example.fyp_mobile.utils.Constants
import com.example.fyp_mobile.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_add_event.*
import kotlinx.android.synthetic.main.activity_add_reward.*
import java.io.IOException
import java.util.regex.Pattern

class AddEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEventBinding

    private lateinit var firebaseAuth: FirebaseAuth

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
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.backBtnEvent.setOnClickListener {
            onBackPressed()
        }

        binding.imageEvent.setOnClickListener{
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

        binding.btnSubmitEvent.setOnClickListener{
            if(validateEvent()){
                if(mSelectedImageFileUri != null) {
                    Log.e("event","Upload Pic")
                    FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri)

                    Toast.makeText(applicationContext,"Event Created!", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this@AddEventActivity, AdminEventActivity::class.java))
                    finish()
                }
                else{
                    Log.e("event","Upload Pic Fail")
                    Toast.makeText(this,resources.getString(R.string.image_selection_failed), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun validateEvent(): Boolean{
        val name = binding.etEvent.text.toString().trim()
        val desc = binding.etEventDesc.text.toString().trim()
        val point = binding.etEventPoint.text.toString().trim()
        val date = binding.etEventDate.text.toString().trim()
        val time = binding.etEventTime.text.toString().trim()
        val venue = binding.etEventVenue.text.toString().trim()

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
                    GlideLoader(this).loadRewardPicture(mSelectedImageFileUri!!, imageEvent)

                } catch(e: IOException){
                    e.printStackTrace()
                    Toast.makeText(this@AddEventActivity,resources.getString(R.string.image_selection_failed), Toast.LENGTH_SHORT).show()
                }
            }
        } else if(resultCode == Activity.RESULT_CANCELED){
            Log.e("update", "Image Select Cancelled")
        }
    }

    private fun addEvent(){

        Log.e("event","Adding!")
        val id = System.currentTimeMillis().toString()
        Log.e("event",id)

        val name = binding.etEvent.text.toString().trim()
        val desc = binding.etEventDesc.text.toString().trim()
        val point = binding.etEventPoint.text.toString().trim()
        val date = binding.etEventDate.text.toString().trim()
        val time = binding.etEventTime.text.toString().trim()
        val venue = binding.etEventVenue.text.toString().trim()
        val image = mEventImageURL

        val event = Event(
            id,
            name,
            desc,
            point.toInt(),
            date,
            time,
            venue,
            image
        )

        FirestoreClass().addEvent(this@AddEventActivity, event)
        Log.e("event","Added?")

    }

    fun imageUploadSuccess(imageURL: String){

        mEventImageURL = imageURL
        addEvent()

    }
}