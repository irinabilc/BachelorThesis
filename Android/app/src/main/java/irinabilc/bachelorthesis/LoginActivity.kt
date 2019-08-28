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
import irinabilc.bachelorthesis.camera_feature.CameraActivity
import irinabilc.bachelorthesis.domain.User
import irinabilc.bachelorthesis.networking.ApiNetworkInterface
import irinabilc.bachelorthesis.networking.ServiceGenerator
import irinabilc.bachelorthesis.utils.isValid
import irinabilc.bachelorthesis.utils.validate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            //login()
            loginUser()
        }

        binding.registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterAccountActivity::class.java))
        }


//        var wifiMan = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//        var wifiInf = wifiMan.connectionInfo
//        var ipAddress = wifiInf.ipAddress
//        var ip = String.format(
//            "%d.%d.%d.%d",
//            ipAddress and 0xff, ipAddress shr 8 and 0xff, ipAddress shr 16 and 0xff, ipAddress shr 24 and 0xff
//        )

    }

    private fun loginUser() {
        if (validateFields()) {
            _progressBar!!.visibility = View.VISIBLE

            mAuth!!.signInWithEmailAndPassword(_emailText!!.text.toString(), _passwordText!!.text.toString())
                .addOnCompleteListener { task ->
                    _progressBar!!.visibility = View.GONE

                    if (task.isSuccessful){
                        Log.d(TAG, "signInWithEMailAndPassword:success")
                        updateUI()
                    }
                    else{
                        _progressBar!!.visibility = View.GONE
                        Log.e(TAG,"signInWithEMailAndPassword:failure", task.exception)
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



//    fun checkLogin() {
//        val pref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//        val status = pref.getString(USER_STATUS_PREFS, null)
//
//        if (status != null) {
//            startActivity(Intent(this, CameraActivity::class.java))
//            finish()
//        }
//
//    }

    private fun login() {
        if (!validateFields()) {
            _loginButton!!.isEnabled = true
            return
        }

        _loginButton!!.isEnabled = false

        val service = ServiceGenerator.createService(ApiNetworkInterface::class.java)
        service.confirmUser(User(_emailText?.text.toString(), _passwordText?.text.toString()))
            .enqueue(object : Callback<Boolean> {
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error!", Toast.LENGTH_SHORT).show()
                    _loginButton!!.isEnabled = true
                }

                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.body()!!) {
                        Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, CameraActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid credentials!", Toast.LENGTH_SHORT).show()
                        _loginButton!!.isEnabled = true
                    }
                }
            })
    }

//    private fun rememberMe() {
//        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
//            .putString(PREFS_USERNAME, _emailText?.text.toString())
//            .putString(PREFS_PASSWORD, _passwordText?.text.toString()).apply()
//    }


    private fun validateFields(): Boolean {
        _emailText?.validate({ s -> s.isValid() }, "Invalid username!")
        _passwordText?.validate({ s -> s.isValid() }, "Invalid password!")
        if (_emailText?.error != null || _passwordText?.error != null)
            return false
        return true
    }
}
