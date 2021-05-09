package com.atom.springdatajpa;

import com.atom.springdatajpa.entity.Customer;
import com.atom.springdatajpa.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * jqpl查询：
 * <p>
 * jpql: jpa query language jpq 查询语言
 * 特点：语法或关键字和sql语句类似，查询的是类和类中的属性。
 * <p>
 * 需要将JPQL语句配置到接口方法上
 * 1。特有的查询：需要在dao接口上配置方法
 * 2。在新添加的方法上，使用注解的形式配置jpql查询语句
 * 3。注解：@Query
 *
 * @author Atom
 */
@SpringBootTest
public class JpaJpqlTest1 {


    @Resource
    private CustomerRepository customerRepository;

    /**
     * testJqlFind
     */
    @Test
    public void testJpqlFind() {
        Customer atom = customerRepository.findJpql("atom1565f960-a5ad-4bbd-9ab3-ade87f8b4371");
        System.err.println(atom);
    }

    /**
     * testJpqlFind2
     */
    @Test
    public void testJpqlFind2() {
        Customer atom = customerRepository.findByJpql2("atom1565f960-a5ad-4bbd-9ab3-ade87f8b4371", 9l);
        System.err.println(atom);
    }


    /**
     * 测试执行的原生sql，原生sql指可以直接复制sql语句给参数赋值就能运行
     */
    @Test
    public void testQueryByNativeSql() {
        Customer byNativeSql = customerRepository.findByNativeSql("atomf24d4b9d-56da-4032-9fbd-d55671d041c5");
        System.err.println(byNativeSql);
    }

    /**
     * test delete by id
     */
    @Test
    public void testDelete() {
        customerRepository.deleteCustomerById(3l);
    }


}
