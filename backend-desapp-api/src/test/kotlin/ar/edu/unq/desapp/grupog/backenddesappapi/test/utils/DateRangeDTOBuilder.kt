package ar.edu.unq.desapp.grupog.backenddesappapi.test.utils

import ar.edu.unq.desapp.grupog.backenddesappapi.webservice.dtos.DateRangeDTO
import java.time.LocalDateTime

class DateRangeDTOBuilder {
    private var initialDate: LocalDateTime = LocalDateTime.of(2023, 1, 1,0,0,0)
    private var finalDate: LocalDateTime = LocalDateTime.now()

    fun build() = DateRangeDTO(
        initialDate.dayOfMonth,
        initialDate.monthValue,
        initialDate.year,

        finalDate.dayOfMonth,
        finalDate.monthValue,
        finalDate.year
        )
}