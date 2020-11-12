package edu.makarov.customer.service;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.Card;
import edu.makarov.customer.models.Customer;
import edu.makarov.customer.repository.AccountRepository;
import edu.makarov.customer.repository.CustomerRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceImplTest {

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    private Customer customer;

    @Before
    public void initModels() {
        customer = new Customer();
        customer.setFullName("Иванов");
        customer.setDocumentNumber("12345");
    }

    @Test
    public void findAllTest() {
        List<Customer> customers = customerService.findAll();

        Mockito.verify(customerRepository, Mockito.times(1)).findAll();
        Assert.assertNotNull(customers);
    }

    @Test
    public void findByIdTest() {
        Mockito.doReturn(Optional.of(customer)).when(customerRepository).findById(3L);

        Customer customerFromDb = customerService.findById(3);

        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Assert.assertNotNull(customerFromDb);
        Assert.assertEquals(customer.getFullName(), customerFromDb.getFullName());
    }

    @Test
    public void findByIdFailTest() {
        Mockito.doReturn(Optional.empty()).when(customerRepository).findById(3L);

        Customer customerFromDb = customerService.findById(3);

        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Assert.assertNull(customerFromDb);
    }

    @Test
    public void createCustomerTest() {
        Mockito.doReturn(customer).when(customerRepository).save(customer);

        Customer customerFromDb = customerService.create(customer);

        Mockito.verify(customerRepository, Mockito.times(1)).save(Mockito.any());
        Assert.assertNotNull(customerFromDb);
        Assert.assertEquals(customer.getFullName(), customerFromDb.getFullName());
    }

    @Test
    public void updateCustomerTest() {
        customer.setId(10);
        Mockito.doReturn(Optional.of(customer)).when(customerRepository).findById(10L);
        Mockito.doReturn(customer).when(customerRepository).save(customer);

        Customer customerFromDb = customerService.update(10, customer);

        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(customerRepository, Mockito.times(1)).save(Mockito.any());
        Assert.assertEquals(customer.getId(), customerFromDb.getId());
    }

    @Test
    public void updateCustomerFailTest() {
        Mockito.doReturn(Optional.empty()).when(customerRepository).findById(10L);
        Mockito.doReturn(customer).when(customerRepository).save(customer);

        Customer customerFromDb = customerService.update(10, customer);

        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(customerRepository, Mockito.times(0)).save(Mockito.any());
        Assert.assertNull(customerFromDb);
    }

    @Test
    public void deleteCustomerTest() {
        Mockito.doReturn(Optional.of(customer)).when(customerRepository).findById(10L);

        boolean result = customerService.delete(10);

        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(customerRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
        Assert.assertTrue(result);
    }

    @Test
    public void deleteCustomerFailTest() {
        Mockito.doReturn(Optional.empty()).when(customerRepository).findById(10L);

        boolean result = customerService.delete(10);

        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(customerRepository, Mockito.times(0)).deleteById(Mockito.anyLong());
        Assert.assertFalse(result);
    }
}