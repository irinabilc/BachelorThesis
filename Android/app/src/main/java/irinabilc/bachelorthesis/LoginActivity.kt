package irinabilc.bachelorthesis

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
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

    private val PREFS_NAME = "user_pref"
    private val PREFS_USERNAME = "username"
    private val PREFS_PASSWORD = "password"
    private var _usernameText: EditText? = null
    private var _passwordText: EditText? = null
    private var _loginButton: Button? = null
    private var _rememberMe: CheckBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<LoginActivityBinding>(this, R.layout.activity_login)
        checkLogin()
        _usernameText = binding.usernameEditText
        _passwordText = binding.passwordEditText
        _loginButton = binding.loginButton
        _rememberMe = binding.rememberMe



        _loginButton?.setOnClickListener {
            login()
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

    override fun onBackPressed() {
    }


    fun checkLogin() {
        val pref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val username = pref.getString(PREFS_USERNAME, null)
        val password = pref.getString(PREFS_PASSWORD, null)

        if (username != null || password != null) {
            startActivity(Intent(this, CameraActivity::class.java))
            finish()
        }

    }

    private fun login() {
        if (!validateFields()) {
            _loginButton!!.isEnabled = true
            return
        }

        _loginButton!!.isEnabled = false

        val service = ServiceGenerator.createService(ApiNetworkInterface::class.java)
        service.confirmUser(User(_usernameText?.text.toString(), _passwordText?.text.toString()))
            .enqueue(object : Callback<Boolean> {
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error!", Toast.LENGTH_SHORT).show()
                    _loginButton!!.isEnabled = true
                }

                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.body()!!) {
                        Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                        if (_rememberMe?.isChecked!!) {
                            rememberMe()
                        }
                        startActivity(Intent(this@LoginActivity, CameraActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid credentials!", Toast.LENGTH_SHORT).show()
                        _loginButton!!.isEnabled = true
                    }
                }
            })
    }

    private fun rememberMe() {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
            .putString(PREFS_USERNAME, _usernameText?.text.toString())
            .putString(PREFS_PASSWORD, _passwordText?.text.toString()).apply()
    }


    private fun validateFields(): Boolean {
        _usernameText?.validate({ s -> s.isValid() }, "Invalid username!")
        _passwordText?.validate({ s -> s.isValid() }, "Invalid password!")
        if (_usernameText?.error != null || _passwordText?.error != null)
            return false
        return true
    }
}
