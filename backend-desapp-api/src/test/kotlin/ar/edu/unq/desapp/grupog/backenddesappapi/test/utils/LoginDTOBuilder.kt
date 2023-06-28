package ar.edu.unq.desapp.grupog.backenddesappapi.test.utils

import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.LoginDTO

class LoginDTOBuilder {
    private var email = "defaultemail2@gmail.com"
    private var password = "defaultPassword0"

    fun build() = LoginDTO(email, password)

    fun withEmail(newEmail: String) = this.apply { email = newEmail }
    fun withPassword(newPassword: String) = this.apply { password = newPassword }
}