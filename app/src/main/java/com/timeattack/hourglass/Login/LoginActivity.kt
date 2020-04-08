package com.timeattack.hourglass.Login

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.timeattack.hourglass.BottomNavigation
import com.timeattack.hourglass.MainActivity
import com.timeattack.hourglass.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity() {

    // Firebase Auth
    private lateinit var auth:FirebaseAuth

    private val TAG : String = "LoginActivity"
    //뒤로가기 연속 클릭 대기 시간
    var mBackWait:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.idLoginEtv)
        val password = findViewById<EditText>(R.id.pwLoginEtv)

        // 로그인
        loginButton.setOnClickListener{
            if(email.text.toString().isEmpty() || password.text.toString().isEmpty()){
                Toast.makeText(this,"'Email' 혹은 'Password'를 반드시 입력하세요.",Toast.LENGTH_LONG).show()
            }
            else{
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this){task ->
                        if(task.isSuccessful){
                            // 로그인 성공하면, 유저의 정보와 함께 UI를 업데이트한다.
                            Log.d(TAG, "signInWithEmail:Success")
                            val user = auth.currentUser
                            updateUI(user)

                        }
                        else {
                            // 만약에 로그인 실패하면 유저에게 메시지를 보여준다.
                            Log.w(TAG, "signInWithEmail:failure",task.exception)
                            Toast.makeText(baseContext,"아이디와 비밀번호 확인해주세요.", Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }
                    }
            }
        }

        // 회원가입
        registerLoginTv.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }


    override fun onResume() {
        super.onResume()

    }

    override fun onStart() {
        super.onStart()

        // 앱 시작 단계에서 사용자가 현재 로그인 되어 있는지 확인하고 처리 해 준다.
        val currentUser= auth.currentUser
        updateUI(currentUser) // 원하는 대로 사용자 설정해 주는 부분??
    }

    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        if(System.currentTimeMillis() - mBackWait >=2000 ) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show()
        } else {
            finish()
        }
    }


    fun updateUI(cUser: FirebaseUser? = null) {
        if(cUser != null){
            Toast.makeText(this,"로그인 성공",Toast.LENGTH_SHORT).show()
            val mainIntent = Intent(this, BottomNavigation::class.java)
            startActivity(mainIntent)
            finish()
        }
    }


}


