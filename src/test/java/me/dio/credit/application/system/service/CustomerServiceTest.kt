package me.dio.credit.application.system.service

import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.exception.BusinessException
import me.dio.credit.application.system.repository.CustomerRepository
import me.dio.credit.application.system.service.impl.CustomerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

@ActiveProfiles("test")
class CustomerServiceTest{
    @Mock lateinit var customerRepository: CustomerRepository
    @InjectMocks lateinit var customerService: CustomerService

    @BeforeEach
    fun initMocks(){
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `testSaveCustomer`() {
        val customer: Customer = buildCustomer();
       `when`(customerRepository.save(customer)).thenReturn(customer)

        val customerSaved = customerService.save(customer);

        Assertions.assertThat(customerSaved).isNotNull
        Assertions.assertThat(customerSaved).isSameAs(customer)

        verify(customerRepository, times(1)).save(customer)
    }

    @Test
    fun `testFindById`(){
        val id = Random.nextLong();
        val customer :  Customer =  buildCustomer(id = id);
        `when`(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer))

        val customerFound: Customer = customerService.findById(id);

        Assertions.assertThat(customerFound).isExactlyInstanceOf(Customer::class.java)
        Assertions.assertThat(customerFound).isNotNull
        Assertions.assertThat(customerFound).isSameAs(customer)

        verify(customerRepository, times(1)).findById(id)
    }


    @Test
    fun `testFindByIdWhenNotFound`(){
        val id = Random.nextLong();

        `when`(customerRepository.findById(anyLong())).thenReturn(Optional.empty())

        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { customerService.findById(id) }
            .withMessage("Id $id not found")
    }

    @Test
    fun `testDeleteCustomer`(){
        val id = Random.nextLong();
        val customer :  Customer =  buildCustomer(id = id);
        `when`(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        customerService.delete(id);

        verify(customerRepository, times(1)).findById(id)
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
}