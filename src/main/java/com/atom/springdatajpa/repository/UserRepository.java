package com.atom.springdatajpa.repository;

import com.atom.springdatajpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Atom
 */
public interface UserRepository extends JpaRepository<User, Integer> {

}
