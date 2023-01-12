package com.example.practicumui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.practicumui.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth //for accessing Firebase features

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //to take you to Sign Up Activity
            binding.tvSignUp.setOnClickListener {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }

            // Initialize Firebase Auth
            auth = Firebase.auth

            binding.btnSignIn.setOnClickListener { // to sign in
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                        Log.d("SignInActivity", "signInWithEmail:success")
                            Toast.makeText(
                                baseContext, "Authentication Successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            goToMain()
                        } else {
                            // If sign in fails, display a message to the user.
                        Log.w("SignInActivity", "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication Failed",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

            }
        }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        goToMain()
    }

    fun goToMain() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}