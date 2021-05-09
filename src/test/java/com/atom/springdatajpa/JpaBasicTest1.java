package com.atom.springdatajpa;

import com.atom.springdatajpa.entity.Customer;
import com.atom.springdatajpa.repository.CustomerRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Atom
 */
@SpringBootTest
public class JpaBasicTest1 {


    @Resource
    private CustomerRepository customerRepository;


    /**
     * 测试单个保存
     */
    @Test
    public void testSave() {
        Customer customer = new Customer();
        customer.setCustAddress("china beijing");
        customer.setCustLevel(RandomStringUtils.random(1, "0123456789"));
        customer.setCustName("atom");
        customer.setCustSource("jingdong");
        customer.setCustIndustry("教育");
        customer.setCustPhone("1888888" + RandomStringUtils.random(4, "0123456789"));
        customerRepository.save(customer);
    }


    /**
     * 测试查询所有，findAll()
     */
    @Test
    public void testFindAll() {
        List<Customer> all = customerRepository.findAll();
        System.err.println(all);
    }

    /**
     * 测试统计查询，count，统计总条数
     */
    @Test
    public void testCount() {
        long count = customerRepository.count();
        System.err.println(count);
    }


    /**
     * 测试根据ID查询是否存在
     */
    @Test
    public void testExistsById() {
        boolean exists = customerRepository.existsById(42l);
        System.err.println(exists);
    }

    /**
     * 测试 getOne()方法, getOne()方法所在的方法必须添加事务注解：@Transactional
     * import org.springframework.transaction.annotation.Transactional;
     * import javax.transaction.Transactional;
     * <p>
     * findOne()： 立即加载
     * getOne():    return this.em.getReference(this.getDomainClass(), id); 延迟加载
     * getOne()方法返回的是Customer的动态代理对象，什么时候用什么时候查询。在这里就是，
     * Customer one = customerRepository.getOne(4l); 这句代码不会触发sql查询
     * System.err.println(one); 这句代码才会触发sql查询，即什么时候用，什么时候加载（延迟加载）
     */
    @Test
    @Transactional
    public void testGetOne() {
        Customer one = customerRepository.getOne(4l);
        System.err.println(one);
    }

}
