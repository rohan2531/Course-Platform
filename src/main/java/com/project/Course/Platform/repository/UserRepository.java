package com.project.Course.Platform.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Course.Platform.entity.User;





@Repository 
public interface UserRepository extends JpaRepository<User,Long>  {
    public User findByEmail(String email);
}
