package com.atom.springdatajpa;

import com.atom.springdatajpa.entity.Customer;
import com.atom.springdatajpa.repository.CustomerRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * @author Atom
 */
@SpringBootTest
public class JpaSpecificationExecutorTest4 {


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
     * 添加排序
     * <p>
     * 多条件查询：
     * 案例：
     * 根据客户名模糊匹配查询
     */
    @Test
    public void testSpecification4() {
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
         *  案例：根据客户名模糊匹配查询
         *      查询条件：
         *          1。比较属性名称
         *               从root对象获取
         *               客户名称
         *               所属行业
         *
         *          2。查询方式
         *              通过criteriaBuilder对象构建
         *              criteriaBuilder 组合多个查询条件
         */
        Specification<Customer> specification = new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //获取比较的属性
//                Path<String> custName = root.get("custName");//是属性名称，不是字段名称,使用like 模糊匹配，需要指定Path<X> 的具体类型，或者path.as(String.class)
                Path<Object> custName = root.get("custName");//是属性名称，不是字段名称,使用like 模糊匹配，需要指定Path<X> 的具体类型，或者path.as(String.class)

                //构造查询条件，模糊匹配使用，like方法
//                Predicate predicate = criteriaBuilder.like(custName, "atom");//要比较的属性，和比较属性的值
                Predicate predicate = criteriaBuilder.like(custName.as(String.class), "atom%");//要比较的属性，和比较属性的值
                return predicate;
            }
        };


        //添加排序,按照custId倒序排序
        Sort sort = Sort.by(Sort.Direction.DESC, "custId");
        List<Customer> all = customerRepository.findAll(specification, sort);

        all.forEach(System.err::println);

    }
}
