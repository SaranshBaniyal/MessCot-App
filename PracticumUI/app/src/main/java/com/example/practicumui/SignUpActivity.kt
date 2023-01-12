package com.example.practicumui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.practicumui.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth //for accessing Firebase features
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var databaseUser: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.tvSignIn.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignUp.setOnClickListener{
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val rollno = binding.etRollno.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val branch = binding.etBranch.text.toString().trim()
            val semester = binding.etSemester.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirmpassword = binding.etConfirmPassword.text.toString()

            if(validateInput(name, email, rollno, phone, branch, semester, password, confirmpassword)){
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignUpActivity", "createUserWithEmail:success")
                            Toast.makeText(baseContext, "Sign Up and Authentication Successful",
                                Toast.LENGTH_SHORT).show()
                            val user = auth.currentUser
                            //updating user table in realtime database
                            databaseUser = FirebaseDatabase.getInstance().getReference("User")
                            val personalDataObject = PersonalData(name, rollno, phone, branch, semester, "Student")
                            val userObject = User(personalDataObject,"")
                            if (user != null) {
                                databaseUser.child(user.uid).setValue(userObject)
                            }

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
    fun validateInput(name: String, email: String, rollno: String, phone:String, branch:String, semester:String, password: String, confirmpassword: String): Boolean{
        if(TextUtils.isEmpty(name)){
            binding.etName.setError("Please Enter your Name")
            return false
        }
        if(TextUtils.isEmpty(email)){
            binding.etEmail.setError("Please Enter your Email")
            return false
        }
        if(TextUtils.isEmpty(rollno)){
            binding.etRollno.setError("Please Enter your Rollno")
            return false
        }
        if(TextUtils.isEmpty(phone)){
            binding.etPhone.setError("Please Enter your Phone Number")
            return false
        }
        if(phone.length !=10){
            binding.etPhone.setError("Invaild Phone Number")
            return false
        }
        if(TextUtils.isEmpty(branch)){
            binding.etBranch.setError("Please Enter your Branch")
            return false
        }
        if(TextUtils.isEmpty(semester)){
            binding.etSemester.setError("Please Enter your Semester")
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