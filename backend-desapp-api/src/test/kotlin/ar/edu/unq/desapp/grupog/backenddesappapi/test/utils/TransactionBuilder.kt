package ar.edu.unq.desapp.grupog.backenddesappapi.test.utils

import ar.edu.unq.desapp.grupog.backenddesappapi.model.Intention
import ar.edu.unq.desapp.grupog.backenddesappapi.model.Transaction
import ar.edu.unq.desapp.grupog.backenddesappapi.model.User

class TransactionBuilder {

    private var anIntention: Intention = IntentionBuilder().build()
    private var anUser: User = UserBuilder().build()
    private var aPrice : Double = 1.0

    fun withIntention(newIntention: Intention) {
        this.apply { anIntention = newIntention }
    }

    fun withUser(newUser: User) {
        this.apply { anUser = newUser }
    }

    fun withPrice(newPrice: Double) {
        this.apply { aPrice = newPrice }
    }

    fun build() : Transaction {
        return Transaction(anIntention, anUser, aPrice)
    }
}