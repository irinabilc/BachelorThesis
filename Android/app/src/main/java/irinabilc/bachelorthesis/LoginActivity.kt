package irinabilc.bachelorthesis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import irinabilc.bachelorthesis.domain.User
import irinabilc.bachelorthesis.networking.ApiNetworkInterface
import irinabilc.bachelorthesis.networking.ServiceGenerator
import irinabilc.bachelorthesis.utils.isValid
import irinabilc.bachelorthesis.utils.validate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    var _usernameText: EditText? = null
    var _passwordText: EditText? = null
    var _loginButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<LoginActivityBinding>(this, R.layout.activity_login)
        _usernameText = binding.usernameEditText
        _passwordText = binding.passwordEditText
        _loginButton = binding.loginButton

        binding.loginButton.setOnClickListener {
            login()
        }

        binding.registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterAccountActivity::class.java))
        }
    }

    private fun login() {
        if (!validateFields()) {
            _loginButton?.isEnabled = false
        }
        //val service = ServiceGenerator.createService(ApiNetworkInterface::class.java)

    }



    private fun validateFields(): Boolean {
        _usernameText?.validate({ s -> s.isValid() }, "Invalid username!")
        _passwordText?.validate({ s -> s.isValid() }, "Invalid password!")
        if (_usernameText?.error != "" || _passwordText?.error != "")
            return false
        return true
    }
}
