package ru.ellaid.app.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import ru.ellaid.app.R
import dagger.hilt.android.AndroidEntryPoint
import ru.ellaid.app.databinding.ActivityRegistrationBinding
import ru.ellaid.app.ui.viewmodels.RegisteredUser
import ru.ellaid.app.ui.viewmodels.RegistrationErrorOccurred
import ru.ellaid.app.ui.viewmodels.RegistrationViewModel

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    private val registrationViewModel: RegistrationViewModel by viewModels()
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val passwordConfirm = binding.passwordConfirm
        val register = binding.register
        val loading = binding.loading

        register.setBackgroundColor(resources.getColor(R.color.inactiveBlue))

        registrationViewModel.registrationResult.observe(this@RegistrationActivity, Observer {
            val registrationResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (registrationResult.error != null) {
                provideRegistrationError(registrationResult.error)
            } else if (registrationResult.success != null) {
                provideRegistrationSuccess(registrationResult.success)
                setResult(RESULT_OK)
                finish()
            }
        })

        registrationViewModel.registrationFormState.observe(this@RegistrationActivity, Observer {
            val registrationState = it ?: return@Observer

            register.isEnabled = registrationState.isDataValid
            if (register.isEnabled) {
                register.setBackgroundColor(resources.getColor(R.color.colorAccent))
            } else {
                register.setBackgroundColor(resources.getColor(R.color.inactiveBlue))
            }
        })

        register.setOnClickListener {
            closeKeyboard()
            loading.visibility = View.VISIBLE
            registrationViewModel.register(username.text.toString(), password.text.toString())
        }

        password.doAfterTextChanged {
            registrationViewModel.registrationDataChanged(
                username.text.toString(),
                password.text.toString(),
                passwordConfirm.text.toString()
            )
        }

        passwordConfirm.doAfterTextChanged {
            registrationViewModel.registrationDataChanged(
                username.text.toString(),
                password.text.toString(),
                passwordConfirm.text.toString()
            )
        }
    }

    private fun provideRegistrationError(error: RegistrationErrorOccurred) {
        Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
    }

    private fun provideRegistrationSuccess(success: RegisteredUser) {
        Toast.makeText(applicationContext, success.message, Toast.LENGTH_SHORT).show()
    }

    private fun closeKeyboard() {
        val view = this@RegistrationActivity.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}