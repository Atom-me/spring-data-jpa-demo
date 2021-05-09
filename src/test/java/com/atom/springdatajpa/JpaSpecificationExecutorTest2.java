package com.atom.springdatajpa;

import com.atom.springdatajpa.entity.Customer;
import com.atom.springdatajpa.repository.CustomerRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Resource;
import javax.persistence.criteria.*;

/**
 * @author Atom
 */
@SpringBootTest
public class JpaSpecificationExecutorTest2 {


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
     * 使用Specification动态查询，根据条件(组合多条件查询)，查询多个个对象，数据库中明确只有一个对象，使用findOne,如果有多个对象则使用findAll
     * <p>
     * 多条件查询：
     * 案例：
     * 根据客户名和客户所属行业查询
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

        Page<Customer> all = customerRepository.findAll(new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //获取比较的属性
                Path<Object> custName = root.get("custName");//是属性名称，不是字段名称
                //构造查询条件，精准匹配使用，equal方法
                Predicate predicate = criteriaBuilder.equal(custName, "atom");//要比较的属性，和比较属性的值
                return predicate;
            }
        }, PageRequest.of(0, 10));

        System.err.println(all.toList());

    }
}
