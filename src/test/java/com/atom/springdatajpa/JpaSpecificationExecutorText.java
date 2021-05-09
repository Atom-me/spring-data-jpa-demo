package com.atom.springdatajpa;

import com.atom.springdatajpa.entity.Customer;
import com.atom.springdatajpa.repository.CustomerRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * @author Atom
 */
@SpringBootTest
public class JpaSpecificationExecutorText {


    @Resource
    private CustomerRepository customerRepository;


    /**
     * 测试生成指定位数的随机数
     */
    @Test
    public void testRandomNextIntDemo() {
        String random = RandomStringUtils.random(4, "0123456789");
        System.out.println(random);
    }

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
        customer.setCustPhone("1888888" + RandomStringUtils.random(4, "0123456789"));
        customerRepository.save(customer);
    }


    /**
     * 测试批量保存
     */
    @Test
    public void testSaveAll() {
        ArrayList<Customer> customers = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> {
            Customer customer = new Customer();
            customer.setCustAddress("china beijing");
            customer.setCustLevel(RandomStringUtils.random(1, "0123456789"));
            customer.setCustName("atom" + UUID.randomUUID());
            customer.setCustSource("jingdong");
            customer.setCustPhone("1888888" + RandomStringUtils.random(4, "0123456789"));
            customers.add(customer);
        });
        customerRepository.saveAll(customers);
    }

    /**
     * 根据条件，查询单个对象
     */
    @Test
    public void testSpecification() {
        /**
         * 匿名内部类
         *
         * 自定义查询条件：
         *  1。 实现Specification接口（提供范型：查询的对象类型）
         *  2。 实现 toPredicate方法（构造查询条件）
         *  3。 需要借助方法参数中的两个参数
         *      root: 获取需要查询的对象的属性，是属性名称，不是字段名称
         *      CriteriaBuilder：构造查询条件的，内部封装了很多的查询条件（模糊匹配，精准匹配）
         *
         *  案例：根据客户名称查询，查询客户名称为 atom 的客户
         *      查询条件：
         *          1。查询方式
         *              通过criteriaBuilder对象构建
         *          2。比较属性名称
         *              从root对象获取
         */

        Optional<Customer> one = customerRepository.findOne(new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //获取比较的属性
                Path<Object> custName = root.get("custName");//是属性名称，不是字段名称
                //构造查询条件，精准匹配使用，equal方法
                Predicate predicate = criteriaBuilder.equal(custName, "atom");//要比较的属性，和比较属性的值
                return predicate;
            }
        });

        System.err.println(one.orElse(null));

    }
}
