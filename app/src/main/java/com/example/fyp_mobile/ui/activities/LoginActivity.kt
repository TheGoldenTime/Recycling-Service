package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.fyp_mobile.databinding.ActivityLoginBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.User
import com.example.fyp_mobile.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            loginUser()
        }

        binding.lnkForgotPass.setOnClickListener{
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.lnkRegister.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun validLogin():Boolean{
        return when{
            TextUtils.isEmpty(binding.emailEditText.text.toString().trim{ it <= ' ' }) -> {
                Toast.makeText(applicationContext,"Email Required!.",Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(binding.passwordEditText.text.toString().trim{ it <= ' ' }) -> {
                Toast.makeText(applicationContext,"Password Required!.",Toast.LENGTH_SHORT).show()
                false
            }
            else ->{
                true
            }
        }
    }

    private fun loginUser(){

        if(validLogin()){
            Log.e("Login","Logging In")
            val email = binding.emailEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener{ task ->
                    Log.e("Login","Inside Login")
                    if(task.isSuccessful){
                        Log.e("Login","Login Yes!")
                        Toast.makeText(applicationContext,"Login Successful!.",Toast.LENGTH_SHORT).show()

                        FirestoreClass().getUserDetails(this@LoginActivity)
                    }
                    else{
                        Log.e("Login","Login Fail")
                        Toast.makeText(applicationContext,"Incorrect Email or Password!.",Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    fun userLoggedInSuccess(user: User){

        Log.e("Login Success", user.username)
        Log.e("Login Success", user.email)

        if(user.role == "user"){
            if(user.profileCompleted == 0){
                val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
                intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
                startActivity(intent)
                finish()
            }
            else{
                startActivity(Intent(this@LoginActivity, RewardsActivity::class.java))
                finish()
            }

        }
        else{
            if(user.profileCompleted == 0){
                val intent = Intent(this@LoginActivity, AdminProfileActivity::class.java)
                intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
                startActivity(intent)
                finish()
            }
            else{

                startActivity(Intent(this@LoginActivity, AdminRewardsActivity::class.java))
                finish()
            }
        }


    }


}