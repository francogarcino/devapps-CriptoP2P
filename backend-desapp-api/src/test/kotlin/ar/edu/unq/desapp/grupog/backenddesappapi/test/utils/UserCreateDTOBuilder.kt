package ar.edu.unq.desapp.grupog.backenddesappapi.test.utils

import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.UserCreateDTO

class UserCreateDTOBuilder {
    private var firstName = "no firstname"
    private var lastName = "no lastname"
    private var email = "defaultemail@gmail.com"
    private var address = "default 1234"
    private var password = "defaultPassword0"
    private var cvu = "0011223344556677889900"
    private var wallet = "10000000"

    fun build() = UserCreateDTO(firstName, lastName, email, address, password, cvu, wallet)

    fun withName(newName: String) = this.apply { firstName = newName }
    fun withLastname(newLastname: String) = this.apply { lastName = newLastname }
    fun withEmail(newEmail: String) = this.apply { email = newEmail }
    fun withAddress(newAddress: String) = this.apply { address = newAddress }
    fun withPassword(newPassword: String) = this.apply { password = newPassword }
    fun withCVU(newCVU: String) = this.apply { cvu = newCVU }
    fun withWallet(newWallet: String) = this.apply { wallet = newWallet }
}