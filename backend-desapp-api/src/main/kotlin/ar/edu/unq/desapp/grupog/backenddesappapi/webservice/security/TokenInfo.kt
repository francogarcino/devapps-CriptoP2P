package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.security

class TokenInfo(jwtToken: String) {

    private val jwtToken = jwtToken

    fun getToken(): String{
        return jwtToken
    }
}