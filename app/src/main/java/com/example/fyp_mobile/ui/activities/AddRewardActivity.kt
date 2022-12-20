package com.example.fyp_mobile.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import com.example.fyp_mobile.R
import com.example.fyp_mobile.databinding.ActivityAddRewardBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.Rewards
import com.example.fyp_mobile.utils.Constants
import com.example.fyp_mobile.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_add_reward.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class AddRewardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRewardBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private var mSelectedImageFileUri: Uri? = null

    private var mRewardImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRewardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.imageReward.setOnClickListener{
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

        binding.btnSubmit.setOnClickListener{
            if(validateRewardDetails()){
                if(mSelectedImageFileUri != null) {
                    Log.e("reward","Upload Pic")
                    FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri)

                    Toast.makeText(applicationContext,"Reward Updated!", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this@AddRewardActivity, AdminRewardsActivity::class.java))
                    finish()
                }
                else{
                    Log.e("reward","Upload Pic Fail")
                    Toast.makeText(this,resources.getString(R.string.image_selection_failed), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateRewardDetails(): Boolean{
        val name = binding.etRewards.text.toString().trim()
        val desc = binding.etDesc.text.toString().trim()
        val point = binding.etPoint.text.toString().trim()
        val quantity = binding.etQuantity.text.toString().trim()

        if(name.isEmpty()){
            Toast.makeText(applicationContext,"Please Enter Reward Name!", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(applicationContext,"Please Enter Valid Point!", Toast.LENGTH_SHORT).show()
                return false
            }
            else{
                if(quantity.isEmpty()){
                    Toast.makeText(applicationContext,"Please Enter Quantity!", Toast.LENGTH_SHORT).show()
                    return false
                }

                else if(quantity.isDigitsOnly()){
                    if(quantity.toInt() <= 0){
                        Toast.makeText(applicationContext,"Please Valid Quantity!", Toast.LENGTH_SHORT).show()
                        return false
                    }
                }

                else{
                    Toast.makeText(applicationContext,"Please Valid Quantity!", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
        }

        else{
            Toast.makeText(applicationContext,"Please Enter Valid Point!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
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
                    GlideLoader(this).loadRewardPicture(mSelectedImageFileUri!!, imageReward)

                } catch(e: IOException){
                    e.printStackTrace()
                    Toast.makeText(this@AddRewardActivity,resources.getString(R.string.image_selection_failed), Toast.LENGTH_SHORT).show()
                }
            }
        } else if(resultCode == Activity.RESULT_CANCELED){
            Log.e("update", "Image Select Cancelled")
        }
    }

    private fun addReward(){

        Log.e("reward","Adding!")
        val id = System.currentTimeMillis().toString()
        Log.e("reward",id)

        val name = binding.etRewards.text.toString().trim()
        val desc = binding.etDesc.text.toString().trim()
        val point = binding.etPoint.text.toString().trim()
        val quantity = binding.etQuantity.text.toString().trim()
        val image = mRewardImageURL

        val reward = Rewards(
            id,
            name,
            desc,
            point.toInt(),
            quantity.toInt(),
            image
        )

        FirestoreClass().addReward(this@AddRewardActivity, reward)
        Log.e("reward","Added?")

    }

    fun imageUploadSuccess(imageURL: String){

        mRewardImageURL = imageURL
        addReward()

    }




}