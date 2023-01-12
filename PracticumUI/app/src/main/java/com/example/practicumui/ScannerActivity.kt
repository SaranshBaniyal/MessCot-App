package com.example.practicumui

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.example.practicumui.databinding.ActivityScannerBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.schedule


class ScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScannerBinding
    private lateinit var databaseOTP: DatabaseReference
    private lateinit var databaseUser: DatabaseReference
    private lateinit var databaseAdmin: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scanBtn.setOnClickListener {
            val intentIntegrator = IntentIntegrator(this)
            intentIntegrator.setPrompt("Scan the Mess QR code")
            intentIntegrator.setOrientationLocked(true)
            intentIntegrator.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.contents == null) {
                Toast.makeText(baseContext, "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                var scannedOTP = intentResult.contents.toString()
                databaseOTP = FirebaseDatabase.getInstance().getReference("OTP")
                databaseOTP.get().addOnSuccessListener {
                    if (scannedOTP.equals(it.value.toString())) {
                        databaseUser = FirebaseDatabase.getInstance().getReference("User")
                        val Year = Calendar.getInstance().get(Calendar.YEAR)
                        val Month = Calendar.getInstance().get(Calendar.MONTH)+1
                        val Day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                        val Hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
//                        val Date = Calendar.getInstance().get(Calendar.DATE)
                        val current = LocalDateTime.now()

                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val Date = current.format(formatter)

                        lateinit var Meal: String
                        if (Hour>=7 && Hour<=9) {
                            Meal = "Breakfast"
                        }
                        else if (Hour>=12 && Hour<=14) {
                            Meal = "Lunch"
                        }
                        else if (Hour>=17 && Hour<=18) {
                            Meal = "Snacks"
                        }
                        else if (Hour>=19 && Hour<=21) {
                            Meal = "Dinner"
                        }
                        else{
                            Meal = "Testdata"
                        }
                        //entry into User records
//                        databaseUser.child(Firebase.auth.currentUser!!.uid).child("records").child(Year.toString())
//                            .child(Month.toString()).child(Day.toString()).child(Meal).get().addOnSuccessListener {
                        databaseUser.child(Firebase.auth.currentUser!!.uid).child("records").child(Date.toString())
                                    .child(Meal).get().addOnSuccessListener {
                                if(it.exists()){//if attendance already exists
                                    Toast.makeText(this,"Not Allowed. Attendance already exists",Toast.LENGTH_SHORT).show()

                                    //randomizing next OTP
                                    databaseOTP.setValue(getRandomNumberString())
                                    //setting status to communicate to admin app
                                   var databaseFlag=FirebaseDatabase.getInstance().getReference("StatusFlag")
                                    databaseFlag.setValue(true)
                                }
                                else{
//                                    databaseUser.child(Firebase.auth.currentUser!!.uid).child("records").child(Year.toString())
//                            .child(Month.toString()).child(Day.toString()).child(Meal).get().addOnSuccessListener {
                                    databaseUser.child(Firebase.auth.currentUser!!.uid).child("records").child(Date.toString())
                                            .child(Meal).setValue(true)

                                    //retriving roll number
                                    lateinit var rollno:String
                                    databaseUser.child(Firebase.auth.currentUser!!.uid.toString()).child("personalData").child("rollno").get().addOnSuccessListener {
                                        rollno = it.value.toString()
//                                        binding.textContent.setText(it.value.toString())
                                    }
                                    //entry into Admin records
                                    Timer().schedule(500){
                                        databaseAdmin = FirebaseDatabase.getInstance().getReference("Admin")
                                        databaseAdmin.child(Year.toString()).child(Month.toString()).child(Day.toString()).child(Meal)
                                            .child(rollno).setValue(true)

                                        //randomizing next OTP
                                        databaseOTP.setValue(getRandomNumberString())

                                        Toast.makeText(baseContext, "Attendance Registered", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this@ScannerActivity,MainActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                            }


                        //randomizing next OTP
//                        databaseOTP.setValue(getRandomNumberString())
//
//                        Toast.makeText(baseContext, "Attendance Registered", Toast.LENGTH_SHORT).show()
//                        val intent = Intent(this,MainActivity::class.java)
//                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(baseContext, "Invalid QR Code", Toast.LENGTH_SHORT).show()
                    }
//                    binding.textContent.setText(intentResult.contents)
//                    binding.textFormat.setText(it.value.toString())
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    fun getRandomNumberString(): String? {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        val rnd = Random()
        val number: Int = rnd.nextInt(999999)

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number)
    }
}