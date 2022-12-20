package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.fyp_mobile.databinding.ActivityRegisterBinding
import com.example.fyp_mobile.firestore.FirestoreClass
import com.example.fyp_mobile.model.Cart
import com.example.fyp_mobile.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    var usernameValid: Boolean = false
    var emailValid: Boolean = false
    var passwordValid: Boolean = false
    var cfnpasswordValid: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                    Toast.makeText(applicationContext,"Password is not the same!",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Log.e("fail","Register Fail")
            }
        }

        binding.lnkLogin.setOnClickListener{
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

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
        val role = "user"

        val cartName = "Welcome Gift Eco Bag"
        val points = 0
        val status = "Pending"
        val image = "https://firebasestorage.googleapis.com/v0/b/recyclingapp-b5eb4.appspot.com/o/ecoBag.jpg?alt=media&token=f9bdc42b-d911-46bd-9ccb-50f4194a316c"

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

                        val cart = Cart(
                            System.currentTimeMillis().toString(),
                            cartName,
                            points,
                            status,
                            image
                        )

                        //Store inside user collection through FirestoreClass
                        FirestoreClass().registerUser(this@RegisterActivity, user)
                        FirestoreClass().registerCart(this@RegisterActivity, cart, firebaseUser.uid)
                        Toast.makeText(applicationContext,"Register Successful!.",Toast.LENGTH_SHORT).show()

                    } else{
                        Log.e("register","Register Failed!")
                        Toast.makeText(applicationContext, task.exception.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            )
    }

}