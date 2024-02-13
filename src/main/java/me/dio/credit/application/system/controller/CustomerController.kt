package me.dio.credit.application.system.controller

import me.dio.credit.application.system.dto.CustomerDto
import me.dio.credit.application.system.dto.CustomerUpdateDto
import me.dio.credit.application.system.dto.CustomerView
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.service.impl.CustomerService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerService
){

    @PostMapping
    fun saveCustomer(@RequestBody customerDto: CustomerDto): String{
        val savedCustomer = this.customerService.save(customerDto.toEntity());
        return "Customer ${savedCustomer.email} saved!"
    }

    @GetMapping("/{id}")
    fun findCustomerById(@PathVariable id: Long): CustomerView {
        return CustomerView(customerService.findById(id));
    }

    @DeleteMapping("/{id}")
    fun deleteCustomerById(@PathVariable id: Long){
        customerService.delete(id);
    }

    @PatchMapping
    fun updateCustomer(@RequestParam(value = "customerId") id: Long,
                       @RequestBody customerUpdateDto: CustomerUpdateDto
    ): CustomerView {
        val customer: Customer = this.customerService.findById(id)
        val toEntity = customerUpdateDto.toEntity(customer)
        val entitySaved = this.customerService.save(toEntity);
        return CustomerView(entitySaved)
    }
}