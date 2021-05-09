package com.atom.springdatajpa.repository;

import com.atom.springdatajpa.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * @author Atom
 */
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {


    /**
     * 案例：
     * 根据客户名称查询客户
     * 使用jpql的形式查询,查询的是实体类对象，和类对象中的属性的值
     * <p>
     * JPQL书写规则
     * <p>
     * JPA的查询语言,类似于sql
     * ①里面不能出现表名,列名,只能出现java的类名,属性名，区分大小写
     * ②出现的sql关键字是一样的意思,不区分大小写
     * ③如果要写select 不能写select *  要写select 别名
     * <p>
     * jpql:  from Customer where custName = ?
     * <p>
     * 配置jpql语句，使用 @Query 注解
     *
     * @param custName
     * @return
     */
    @Query(value = "from Customer where custName = ?1")
    Customer findJpql(String custName);


    /**
     * 根据客户名称和客户ID查询客户信息
     *
     * <p>
     * * JPQL书写规则
     * * <p>
     * * JPA的查询语言,类似于sql
     * * ①里面不能出现表名,列名,只能出现java的类名,属性名，区分大小写
     * * ②出现的sql关键字是一样的意思,不区分大小写
     * * ③如果要写select 不能写select *  要写select 别名
     * * 4：使用索引（1，2，3，4。。。）指定占位符参数的位置，
     *
     * @param name
     * @param id
     * @return
     */
    @Query(value = "select c from Customer c where c.custName = ?1 and c.custId = ?2")
    Customer findByJpql2(String name, Long id);


    /**
     * 注意:
     * 对于自定义sql的删改方法,在方法上还要添加@Transactional/@Modifying注解,如下所示:
     *
     * @param id
     */
    @Modifying
    @Query(value = "delete from Customer where custId=?1")
    void deleteCustomerById(Long id);


    /**
     * 在@Query中传入了一个属性nativeQuery,
     *
     * @Query有nativeQuery=true，表示可执行的原生sql，原生sql指可以直接复制sql语句给参数赋值就能运行
     * @Query无nativeQuery=true， 表示不是原生sql，查询语句中的表名则是对应的项目中实体类的类名
     */
    @Query(value = "select * from tbl_customer where cust_name = ?", nativeQuery = true)
    Customer findByNativeSql(String name);


}
