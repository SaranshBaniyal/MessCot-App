package com.example.practicummessadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.practicummessadmin.databinding.ActivityRecordsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RecordsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecordsBinding
    private lateinit var databaseAdmin: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = Firebase.auth.currentUser

        binding.iconSearch.setOnClickListener {
            var Year = binding.etYear.text.toString()
            var Month = binding.etMonth.text.toString()
            var Day = binding.etDay.text.toString()
            databaseAdmin = FirebaseDatabase.getInstance().getReference("Admin")
            databaseAdmin.child(Year).child(Month).child(Day).get().addOnSuccessListener {
                if(it.value!=null){
                    binding.tvSearchOutput.setText("Attendance marked for: \n"+it.value.toString())
                }
                else{
                    Toast.makeText(this@RecordsActivity, "No records found for the given date", Toast.LENGTH_SHORT).show()
                    binding.tvSearchOutput.setText("")
                }
            }
        }
    }
}