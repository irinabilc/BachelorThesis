package irinabilc.bachelorthesis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import irinabilc.bachelorthesis.image.CameraActivity
import irinabilc.bachelorthesis.utils.isValid
import irinabilc.bachelorthesis.utils.validate

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginUserActivity"

    // shared prefs
    private val PREFS_NAME = "user_pref"
    private val USER_STATUS_PREFS = "states"
    // UI elements
    private var _emailText: EditText? = null
    private var _passwordText: EditText? = null
    private var _loginButton: Button? = null
    private var _progressBar: ProgressBar? = null

    //Firebase
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<LoginActivityBinding>(this, R.layout.activity_login)
        //checkLogin()
        _emailText = binding.emailEditText
        _passwordText = binding.passwordEditText
        _loginButton = binding.loginButton
        _progressBar = binding.progressBat
        _progressBar!!.isVisible = false

        mAuth = FirebaseAuth.getInstance()

        _loginButton?.setOnClickListener {
            loginUser()
        }

        binding.registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterAccountActivity::class.java))
        }


    }

    private fun loginUser() {
        if (validateFields()) {
            _progressBar!!.visibility = View.VISIBLE

            mAuth!!.signInWithEmailAndPassword(_emailText!!.text.toString(), _passwordText!!.text.toString())
                .addOnCompleteListener { task ->
                    _progressBar!!.visibility = View.GONE

                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEMailAndPassword:success")
                        updateUI()
                    } else {
                        _progressBar!!.visibility = View.GONE
                        Log.e(TAG, "signInWithEMailAndPassword:failure", task.exception)
                        Toast.makeText(this, "Authentication failed!", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun updateUI() {
        _progressBar!!.visibility = View.GONE
        val intent = Intent(this, CameraActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }


    private fun validateFields(): Boolean {
        _emailText?.validate({ s -> s.isValid() }, "Invalid username!")
        _passwordText?.validate({ s -> s.isValid() }, "Invalid password!")
        if (_emailText?.error != null || _passwordText?.error != null)
            return false
        return true
    }
}
