package ar.edu.unq.desapp.grupog.backenddesappapi.service.impl

import java.io.Serializable
import java.time.LocalDateTime

class PriceWithTime(var dateTime: LocalDateTime, var price: String) : Serializable
