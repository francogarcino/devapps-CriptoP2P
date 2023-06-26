package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos

class UserCreateDTO(
    var firstName: String,
    var lastName: String,
    var email: String,
    var address: String,
    var password: String,
    var cvu: String,
    var wallet: String
)