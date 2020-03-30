package com.timeattack.hourglass.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.timeattack.hourglass.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var passwordCheck: EditText
    private lateinit var registerButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        dbRef = database.reference

        name = findViewById<EditText>(R.id.idRegisterEtv)
        email = findViewById<EditText>(R.id.emailRegisterEtv)
        password = findViewById<EditText>(R.id.pwRegisterEtv)
        passwordCheck = findViewById<EditText>(R.id.pwCheckRegisterEtv)
        registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener(){
            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).
                    addOnCompleteListener { task: Task<AuthResult> ->
                        if(task.isSuccessful){
                            val userId = auth.currentUser?.uid


                        }

                    }
        }
    }
}
