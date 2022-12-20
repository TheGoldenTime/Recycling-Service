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
import com.example.fyp_mobile.databinding.ActivityEditRewardBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.Rewards
import com.example.fyp_mobile.utils.Constants
import com.example.fyp_mobile.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_edit_reward.*
import kotlinx.android.synthetic.main.activity_user_prof.*
import kotlinx.android.synthetic.main.activity_user_prof.iv_user_photo
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class EditRewardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRewardBinding

    private lateinit var mRewardDetails: Rewards

    private var id = ""

    private var mSelectedImageFileUri: Uri? = null

    private var mRewardImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityEditRewardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id")!!

        FirestoreClass().getRewardDetails(this, id)

        binding.backBtnEditReward.setOnClickListener {
            onBackPressed()
        }

        binding.imageEditReward.setOnClickListener {
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

        binding.btnSubmitEditReward.setOnClickListener {
            if(validateRewardDetails()){
                if(mSelectedImageFileUri != null) {
                    Log.e("update","I change pic")
                    FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri)
                }
                else{
                    Log.e("update","No change pic")
                    updateRewardDetails()
                }
            }
        }

    }

    private fun validateRewardDetails(): Boolean{
        val name = binding.etEditRewards.text.toString().trim()
        val desc = binding.etEditDesc.text.toString().trim()
        val point = binding.etEditPoint.text.toString().trim()
        val quantity = binding.etEditQuantity.text.toString().trim()

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

    private fun updateRewardDetails(){
        val rewardHashMap = HashMap<String, Any>()

        val name = binding.etEditRewards.text.toString().trim()

        if(name != mRewardDetails.name){
            rewardHashMap["name"] = name
        }

        val desc = binding.etEditDesc.text.toString().trim()

        if(desc != mRewardDetails.desc){
            rewardHashMap["desc"] = desc
        }

        val point = binding.etEditPoint.text.toString().trim()

        if(point.isNotEmpty()){
            rewardHashMap["points"] = point.toInt()
        }

        val quantity = binding.etEditQuantity.text.toString().trim()

        if(quantity.isNotEmpty()){
            rewardHashMap["quantity"] = quantity.toInt()
        }

        if(mRewardImageURL.isNotEmpty()){
            rewardHashMap[Constants.IMAGE] = mRewardImageURL
        }

        Log.e("update", "updating reward")
        FirestoreClass().updateRewardData(this, rewardHashMap, id)
    }

    fun rewardDetailsUpdateSuccess(){
        Toast.makeText(applicationContext,"Reward Updated!", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this@EditRewardActivity, AdminRewardsActivity::class.java))
        finish()
    }

    fun rewardDetailsSuccess(rewards: Rewards){

        mRewardDetails = rewards

        GlideLoader(this@EditRewardActivity).loadRewardPicture(rewards.image, imageEditReward)

        binding.etEditRewards.setText(rewards.name)
        binding.etEditDesc.setText(rewards.desc)
        binding.etEditPoint.setText(rewards.points.toString())
        binding.etEditQuantity.setText(rewards.quantity.toString())

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
                    GlideLoader(this).loadRewardPicture(mSelectedImageFileUri!!, imageEditReward)
                    //binding.ivUserPhoto.setImageURI(selectedImageFileUri)

                } catch(e: IOException){
                    e.printStackTrace()
                    Toast.makeText(this@EditRewardActivity,resources.getString(R.string.image_selection_failed), Toast.LENGTH_SHORT).show()
                }
            }
        } else if(resultCode == Activity.RESULT_CANCELED){
            Log.e("update", "Image Select Cancelled")
        }
    }

    fun imageUploadSuccess(imageURL: String) {

        mRewardImageURL = imageURL
        updateRewardDetails()
    }
}