package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos

data class UserStatsDTO(
    var firstName: String,
    var lastName: String,
    var completedTrx: Int,
    var reputation: Int
)
