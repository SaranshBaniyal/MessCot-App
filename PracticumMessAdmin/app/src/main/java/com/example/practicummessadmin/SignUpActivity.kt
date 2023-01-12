package com.example.practicummessadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.practicummessadmin.MainActivity
import com.example.practicummessadmin.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth //for accessing Firebase features
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.tvSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignUp.setOnClickListener{
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirmpassword = binding.etConfirmPassword.text.toString()

            if(validateInput(name,email,password,confirmpassword)){
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignUpActivity", "createUserWithEmail:success")
                            Toast.makeText(baseContext, "Sign Up and Authentication Successful",
                                Toast.LENGTH_SHORT).show()
                            val user = auth.currentUser

//                        val profileUpdates = userProfileChangeRequest {
//                            displayName = binding.etName.text.toString()
////                            photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
//                        }
//                        user!!.updateProfile(profileUpdates)
//                            .addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    Log.d(TAG, "User profile updated.")
//
//                                }
//                            }

                            updateUI()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignUpActivity", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication Failed",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
    fun updateUI(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun validateInput(name: String, email: String, password: String, confirmpassword: String): Boolean{
        if(TextUtils.isEmpty(name)){
            binding.etName.setError("Please Enter your Name")
            return false
        }
        if(TextUtils.isEmpty(email)){
            binding.etEmail.setError("Please Enter your Email")
            return false
        }
        if(TextUtils.isEmpty(password)){
            binding.etPassword.setError("Please Enter a password")
            return false
        }
        if(TextUtils.isEmpty(confirmpassword)){
            binding.etConfirmPassword.setError("Please Re-enter your password")
            return false
        }
        if(!password.equals(confirmpassword)){
            binding.etConfirmPassword.setError("Doesn't match with the entered password")
            return false
        }
        return true
    }
}