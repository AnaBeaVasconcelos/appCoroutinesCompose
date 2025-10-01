package com.example.appcoroutinescompose.ui.form

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FormState(
    val name: String = "",
    val email: String = "",
    val isValid: Boolean = false
)

class FormViewModel : ViewModel() {
    private val _formState = MutableStateFlow(FormState())
    val formState = _formState.asStateFlow()

    fun updateName(new: String) {
        _formState.value = _formState.value.copy(
            name = new,
            isValid = validate(new, _formState.value.email)
        )
    }

    fun updateEmail(new: String) {
        _formState.value = _formState.value.copy(
            email = new,
            isValid = validate(_formState.value.name, new)
        )
    }

    private fun validate(name: String, email: String): Boolean {
        return name.isNotBlank() && email.contains("@")
    }
}