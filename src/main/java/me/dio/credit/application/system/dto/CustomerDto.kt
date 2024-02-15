package me.dio.credit.application.system.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Customer
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDto (
    @field:NotEmpty(message = "O nome é obrigatório!") val firstName: String,
    @field:NotEmpty(message = "O sobrenome é obrigatório!") val lastName:String,
    @field:NotEmpty(message = "O CPF é obrigatório!") @Size(min = 11)
    @field:CPF(message = "CPF inválido")
    val cpf: String,

    @field:NotNull(message = "Campo obrigatório") val income: BigDecimal,
    @field:NotEmpty(message = "O email é obrigatório!")
    @Email(message = "Email inválido")
    val email: String,
    @field:NotEmpty(message = "A senha é obrigatória!") val password: String,
    @field:NotEmpty(message = "O CEP é obrigatório!") val zipCode: String,
    @field:NotEmpty(message = "A rua é obrigatória!") val street: String
    ) {

    fun toEntity(): Customer = Customer(
        firstName=this.firstName,
        lastName=this.lastName,
        income=this.income,
        cpf = this.cpf,
        email=this.email,
        password = this.password,
        address = Address(this.zipCode, this.street)
    )

}