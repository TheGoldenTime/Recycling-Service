package com.example.fyp_mobile.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.fyp_mobile.R
import com.example.fyp_mobile.databinding.ActivityUserProfileBinding
import com.example.fyp_mobile.model.User
import com.example.fyp_mobile.utils.Constants
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.text.isDigitsOnly
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class UserProfileActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var binding: ActivityUserProfileBinding

    private lateinit var mUserDetails: User

    private var mSelectedImageFileUri: Uri? = null

    private var mUserProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        binding.etUsername.setText(mUserDetails.username)

        binding.etEmail.isEnabled = false
        binding.etEmail.setText(mUserDetails.email)

        if(mUserDetails.profileCompleted == 0){
            binding.etUsername.isEnabled = false
        }
        else{
            GlideLoader(this@UserProfileActivity).loadUserPicture(mUserDetails.image, iv_user_photo)

            if(mUserDetails.mobile != 0L){
                binding.etMobile.setText(mUserDetails.mobile.toString())
            }

            binding.etAddress.setText(mUserDetails.address)
        }

        binding.ivUserPhoto.setOnClickListener(this@UserProfileActivity)
        binding.btnSubmit.setOnClickListener(this@UserProfileActivity)
        binding.btnLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@UserProfileActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onClick(v: View?) {
        if(v != null){
            when (v.id){

                R.id.iv_user_photo -> {
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

                R.id.btn_submit ->{
                    Log.e("update","hello")

                    if(validateUserProfileDetails()){
                        Log.e("update","hello am in a little")
                        if(mSelectedImageFileUri != null) {
                            Log.e("update","I change pic")
                            FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri)
                        }
                        else{
                            Log.e("update","No change pic")
                            updateUserProfileDetails()
                        }

                    }

                }
            }
        }
    }

    private fun updateUserProfileDetails(){
        val userHashMap = HashMap<String, Any>()

        val username = binding.etUsername.text.toString().trim()

        if(username != mUserDetails.username){
            userHashMap[Constants.USERNAME] = username
        }

        val address = binding.etAddress.text.toString().trim()

        if(address != mUserDetails.address){
            userHashMap[Constants.ADDRESS] = address
        }

        if(mUserProfileImageURL.isNotEmpty()){
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        val mobileNumber = binding.etMobile.text.toString().trim()

        if(mobileNumber.isNotEmpty()){
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        userHashMap[Constants.COMPLETE_PROFILE] = 1
        Log.e("update", "updating profile")
        FirestoreClass().updateUserProfileData(this, userHashMap)
    }

    fun userProfileUpdateSuccess(){
        Toast.makeText(applicationContext,"Profile Updated!", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this@UserProfileActivity, UserProfActivity::class.java))
        finish()
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
                    GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, iv_user_photo)

                } catch(e: IOException){
                    e.printStackTrace()
                    Toast.makeText(this@UserProfileActivity,resources.getString(R.string.image_selection_failed), Toast.LENGTH_SHORT).show()
                }
            }
        } else if(resultCode == Activity.RESULT_CANCELED){
            Log.e("update", "Image Select Cancelled")
        }
    }

    private fun validateUserProfileDetails(): Boolean{
        val mobile = binding.etMobile.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        if(mobile.isEmpty()){
            Toast.makeText(applicationContext,"Please Enter Mobile Number!", Toast.LENGTH_SHORT).show()
            return false
        }
        else if(mobile.isDigitsOnly()){
            if(mobile.length < 9 || mobile.length > 10 ){
                Toast.makeText(applicationContext,"Only 9 - 10 digits are allowed!", Toast.LENGTH_SHORT).show()
                return false
            }
            else{
                if(address.isEmpty()){
                    Toast.makeText(applicationContext,"Please Enter Address!", Toast.LENGTH_SHORT).show()
                    return false
                }
                else{
                    return true
                }
            }
        }
        else{
            Toast.makeText(applicationContext,"Only Digits for Mobile Number!", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    fun imageUploadSuccess(imageURL: String){

        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }
}