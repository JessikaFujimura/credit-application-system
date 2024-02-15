package me.dio.credit.application.system.service.impl

import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.exception.BusinessException
import me.dio.credit.application.system.repository.CustomerRepository
import me.dio.credit.application.system.service.ICustomerService
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository: CustomerRepository
):ICustomerService {
    override fun save(customer: Customer): Customer =
        this.customerRepository.save(customer);

    override fun findById(customerId: Long): Customer =
        this.customerRepository.findById(customerId).orElseThrow{
            throw BusinessException("Id $customerId not found" )
        }

    override fun delete(customerId: Long) {
        val customer : Customer = this.findById(customerId);
        this.customerRepository.delete(customer);
    }
}