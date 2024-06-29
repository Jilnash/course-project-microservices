package com.jilnash.user.repo;

import com.jilnash.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Boolean existsUserByLogin(String login);

    Boolean existsUserByEmail(String Email);

    Boolean existsUserByPhone(String login);

    List<User> findAllByEmailLikeAndLoginLike(String email, String login);
}
