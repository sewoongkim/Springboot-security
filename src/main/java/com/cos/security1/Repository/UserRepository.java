package com.cos.security1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.model.User;

// CRUD 함수를 JpeWPOSITORY가 들고 있음.
// @Repoitory라는 어노테이션이 없어도 IoC되요. JpaRepository를 상속했기 때문에...
public interface UserRepository extends JpaRepository<User, Integer>{

}
