package com.timeattack.hourglass.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.timeattack.hourglass.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val TAG : String = "CreateAccount"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        // 회원가입 나열되어 있는 것
        val name = findViewById<EditText>(R.id.nameRegisterEtv)
        val email = findViewById<EditText>(R.id.emailRegisterEtv)
        val nickname = findViewById<EditText>(R.id.nickNameRegisterEtv)
        val password = findViewById<EditText>(R.id.pwRegisterEtv)
        val passwordCheck = findViewById<EditText>(R.id.pwCheckRegisterEtv)

        registerButton.setOnClickListener {
            if(email.text.toString().isEmpty() || password.text.toString().isEmpty() ||
                    name.text.toString().isEmpty() || nickname.text.toString().isEmpty() || passwordCheck.text.toString().isEmpty())
            {
                Toast.makeText(this, "번거로우시겠지만 모두 작성해주세요 ^^", Toast.LENGTH_SHORT).show()
            } else if (password.text.toString().length < 6)
            {
                Toast.makeText(this, "비밀번호는 7자리 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
            }
            else if (password.text.toString() != passwordCheck.text.toString())
            {
                Toast.makeText(this, "두 비밀번호가 일치하여야 합니다.", Toast.LENGTH_SHORT).show()
            }
            else{
                //여기서 막힌듯
                auth.createUserWithEmailAndPassword(email.text.toString(),password.text.toString())
                    .addOnCompleteListener(this){task ->
                        if (task.isSuccessful){
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            //updateUI(user)
                            // 아니면 액티비티를 닫아 버린다.
                            finish()
                        }else
                        {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
            }



        }

        backbuttonRegister.setOnClickListener {
            super.onBackPressed()
        }

    }
}
