package me.dio.credit.application.system.service

import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.exception.BusinessException
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.service.impl.CreditService
import me.dio.credit.application.system.service.impl.CustomerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.math.BigDecimal
import java.util.UUID
import kotlin.random.Random

class CreditServiceTest {
    @InjectMocks lateinit var creditService: CreditService
    @Mock lateinit var creditRepository: CreditRepository
    @Mock lateinit var customerService: CustomerService

    @BeforeEach
    fun initMocks(){
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testSaveCredit() {
        val customer: Customer = buildCustomer();
        val credit:Credit = buildCredit();

        `when`(customerService.findById(anyLong())).thenReturn(customer)
        `when`(creditRepository.save(credit)).thenReturn(credit);

        val creditSaved: Credit = creditService.save(credit);

        Assertions.assertThat(creditSaved).isNotNull
        Assertions.assertThat(creditSaved).isSameAs(credit)

        verify(customerService, times(1)).findById(anyLong())
        verify(creditRepository, times(1)).save(credit)
    }

    @Test
    fun `findAllByCustomer`(){
        val customerId: Long = Random.nextLong()
        val listOfCredits: List<Credit> =  mutableListOf(buildCredit())

        `when`(creditRepository.findAllByCustomer(anyLong())).thenReturn(listOfCredits)

        val actual: List<Credit> = creditService.findAllByCustomer(customerId);

        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual.isEmpty()).isFalse()

        verify(creditRepository, times(1)).findAllByCustomer(customerId)
    }

    @Test
    fun `testFindByCreditCode`(){
        val creditCode: UUID = UUID.randomUUID();
        val customerId: Long = Random.nextLong()
        val customer: Customer = buildCustomer(id= customerId)
        val credit:Credit = buildCredit(customer = customer);

        `when`(creditRepository.findByCreditCode(creditCode)).thenReturn(credit)

        val creditFound: Credit = creditService.findByCreditCode(creditCode, customerId)

        Assertions.assertThat(creditFound).isNotNull
        Assertions.assertThat(creditFound).isSameAs(credit)
    }


    @Test
    fun `testFindByCreditCodeWhenCreditCodeNotFound`(){
        val creditCode: UUID = UUID.randomUUID()
        val customerId: Long = Random.nextLong()

        `when`(creditRepository.findByCreditCode(creditCode)).thenReturn(null)

        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.findByCreditCode(creditCode, customerId) }
            .withMessage("Creditcode $creditCode not found")
    }

    @Test
    fun `testFindByCreditCodeWhenCustomerIdIsDifferent`(){
        val creditCode: UUID = UUID.randomUUID()
        val customerId: Long = Random.nextLong()
        val customer: Customer = buildCustomer()
        val credit:Credit = buildCredit(customer = customer)

        `when`(creditRepository.findByCreditCode(creditCode)).thenReturn(credit)

        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.findByCreditCode(creditCode, customerId) }
            .withMessage("Contact Admin")
    }


    private fun buildCustomer(
        firstName: String = "Maria",
        lastName: String = "Silva",
        cpf: String = "56254524074",
        email: String = "maria@email.com",
        password: String = "12345",
        zipCode: String = "01140000",
        street: String = "Rua Zero",
        income: BigDecimal = BigDecimal.valueOf(1500),
        id: Long = 1L
    ) = Customer(
        firstName= firstName,
        lastName= lastName,
        cpf= cpf,
        email = email,
        password = password,
        address = Address (
            zipCode= zipCode,
            street= street
        ),
        income = income,
        id = id
    )

    private fun buildCredit(
            customer: Customer = buildCustomer()
    ) = Credit(
        customer = customer
    )
}