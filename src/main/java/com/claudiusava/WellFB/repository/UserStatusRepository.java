package com.claudiusava.WellFB.repository;

import com.claudiusava.WellFB.model.UserStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserStatusRepository extends CrudRepository<UserStatus, Integer> {

    Optional<UserStatus> findFirstByUsernameOrderByTimestampDesc(String username);

}
