package com.example.fyp_mobile.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.fyp_mobile.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnForgot.setOnClickListener{
            val email = binding.emailEditText.text.toString()

            if (email.isEmpty()){
                Toast.makeText(applicationContext,"Email Required!",Toast.LENGTH_SHORT).show()
            }
            else{
                Log.e("Forgot","Forgot")
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{ task ->
                        if(task.isSuccessful){
                            Log.e("Forgot","Forgot Yes!")
                            Toast.makeText(applicationContext,"Email Sent Successfully!",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Log.e("Forgot","Forgot No!")
                            Toast.makeText(applicationContext,"Email Does Not Exist!",Toast.LENGTH_SHORT).show()
                        }

                    }
            }
        }

        binding.lnkLogin.setOnClickListener{
            val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}