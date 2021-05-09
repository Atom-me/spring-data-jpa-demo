package com.atom.springdatajpa;

import com.atom.springdatajpa.entity.Customer;
import com.atom.springdatajpa.repository.CustomerRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * @author Atom
 */
@SpringBootTest
public class JpaSpecificationExecutorTest5 {


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
     * 添加分页
     * <p>
     * 多条件查询：
     * 案例：
     * 根据客户名模糊匹配查询，分页查询
     * <p>
     * 分页查询：
     * Specification ：查询条件
     * Pageable：分页参数，查询的页码（从0开始），每夜查询的条数
     * findAll(Specification,Pageable) 带有条件的分页
     * findAll(Pageable) 没有条件的分页
     * 返回Page，Spring Data Jpa为我们封装好的pageBean对象，数据列表，共条数
     */
    @Test
    public void testSpecification4() {

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

        Pageable pageable = PageRequest.of(0, 2);
        Page<Customer> all = customerRepository.findAll(specification, pageable);

        all.forEach(System.err::println);

    }
}
