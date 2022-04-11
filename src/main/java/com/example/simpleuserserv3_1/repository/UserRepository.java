package com.example.simpleuserserv3_1.repository;

import com.example.simpleuserserv3_1.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    boolean existsByUsername(String username);

    Page<UserEntity> findByFirstName(String firstName, Pageable pageable);

    Page<UserEntity> findByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);
    UserEntity findTop1ByLastNameOrderByIdDesc(String lastName);

    //@Query("SELECT AVG(u.age) from UserEntity u")
    //Double getAverageAge();



}
