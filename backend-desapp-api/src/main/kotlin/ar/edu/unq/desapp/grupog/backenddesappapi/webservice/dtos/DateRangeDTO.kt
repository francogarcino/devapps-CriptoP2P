package ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos

import java.time.LocalDateTime

class DateRangeDTO (
    val initDay : Int,
    val initMonth : Int,
    val initYear : Int,
    val endDay : Int,
    val endMonth : Int,
    val endYear : Int
)