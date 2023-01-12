package com.example.practicummessadmin

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.practicummessadmin.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var databaseotp:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignOut.setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnRecords.setOnClickListener {
            val intent = Intent(this, RecordsActivity::class.java)
            startActivity(intent)
        }

        databaseotp = FirebaseDatabase.getInstance().getReference("OTP")
        databaseotp.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var content = snapshot.value.toString()
                    var bitmap: Bitmap? = null
                    val barcodeEncoder = BarcodeEncoder()
                    bitmap = barcodeEncoder.encodeBitmap(content,
                        BarcodeFormat.QR_CODE, 900, 900);

                    binding.ivQr.setImageBitmap(bitmap)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        FirebaseDatabase.getInstance().getReference("StatusFlag").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value==true){//double attendance attempt
                Toast.makeText(this@MainActivity, "Not Allowed. Attendance already exists",Toast.LENGTH_SHORT).show()
                Log.d("StatusFlag","Not Allowed attendance already exists")
                FirebaseDatabase.getInstance().getReference("StatusFlag").setValue(false)
            }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}