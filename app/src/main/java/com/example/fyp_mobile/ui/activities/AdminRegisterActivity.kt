package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.fyp_mobile.R
import com.example.fyp_mobile.databinding.ActivityAdminRegisterBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_user_prof.*

class AdminRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminRegisterBinding

    var usernameValid: Boolean = false
    var emailValid: Boolean = false
    var passwordValid: Boolean = false
    var cfnpasswordValid: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setupActionBar()

        usernameFocusListener()
        emailFocusListener()
        passwordFocusListener()
        cfnpasswordFocusListener()

        binding.btnRegister.setOnClickListener{
            if(usernameValid == true && emailValid == true && passwordValid == true){
                if(binding.passwordEditText.text.toString() == binding.cfnpasswordEditText.text.toString()){
                    registerUser()
                }
                else{
                    Toast.makeText(applicationContext,"Password is not the same!", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Log.e("fail","Register Fail")
            }
        }

        binding.lnkBack.setOnClickListener{
            val intent = Intent(this@AdminRegisterActivity, AdminProfActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

//    private fun setupActionBar(){
//        setSupportActionBar(toolbar_settings_activity)
//
//        val actionBar = supportActionBar
//        if (actionBar != null){
//            actionBar.setDisplayHomeAsUpEnabled(true)
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
//        }
//
//        toolbar_settings_activity.setNavigationOnClickListener{onBackPressed()}
//    }

    private fun usernameFocusListener()
    {
        binding.usernameEditText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.usernameContainer.helperText = validUsername()
            }
        }
    }

    private fun validUsername(): String?
    {
        val usernameText = binding.usernameEditText.text.toString()
        if(usernameText.length < 4){
            usernameValid = false
            return "Username cannot less than 4 words!"
        }

        else if (usernameText.length > 20){
            usernameValid = false
            return "Username cannot more than 20 words!"

        }
        usernameValid = true
        return null
    }

    private fun emailFocusListener()
    {
        binding.emailEditText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.emailContainer.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String?
    {
        val emailText = binding.emailEditText.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            emailValid = false
            return "Invalid Email Address"
        }
        emailValid = true
        return null
    }

    private fun passwordFocusListener()
    {
        binding.passwordEditText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.passwordContainer.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String?
    {
        val passwordText = binding.passwordEditText.text.toString()
        if(passwordText.length < 4)
        {
            passwordValid = false
            return "Minimum 4 Character Password"
        }
        if (passwordText.length > 20)
        {
            passwordValid = false
            return "Username cannot more than 20 words!"
        }
        if(!passwordText.matches(".*[A-Z].*".toRegex()))
        {
            passwordValid = false
            return "Must Contain 1 Upper-case Character"
        }
        if(!passwordText.matches(".*[a-z].*".toRegex()))
        {
            passwordValid = false
            return "Must Contain 1 Lower-case Character"
        }

        passwordValid = true
        return null
    }

    private fun cfnpasswordFocusListener()
    {
        binding.cfnpasswordEditText.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.cfnpasswordContainer.helperText = validCfnPassword()
            }
        }
    }

    private fun validCfnPassword(): String?
    {
        val cfnpasswordText = binding.cfnpasswordEditText.text.toString()
        if(cfnpasswordText.length < 4)
        {
            cfnpasswordValid = false
            return "Minimum 4 Character Password"
        }
        if (cfnpasswordText.length > 20)
        {
            cfnpasswordValid = false
            return "Username cannot more than 20 words!"
        }

        cfnpasswordValid = true
        return null
    }

    private fun registerUser(){
        Log.e("register","Im in!")
        val username = binding.usernameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val pass = binding.passwordEditText.text.toString()
        val role = "admin"

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(
                OnCompleteListener<AuthResult>{ task ->
                    Log.e("register","Did I get in?")
                    if(task.isSuccessful){
                        Log.e("register","Im inside!")
                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        val user = User(
                            firebaseUser.uid,
                            username,
                            email,
                            role
                        )

                        //Store inside user collection through FirestoreClass
                        FirestoreClass().registerUser(this@AdminRegisterActivity, user)
                        Toast.makeText(applicationContext,"Register Successful!.",Toast.LENGTH_SHORT).show()

                    } else{
                        Log.e("register","Register Failed!")
                        Toast.makeText(applicationContext, task.exception.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            )
    }
}