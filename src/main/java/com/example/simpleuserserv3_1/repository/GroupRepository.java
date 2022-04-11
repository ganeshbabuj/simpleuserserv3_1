package com.example.simpleuserserv3_1.repository;

import com.example.simpleuserserv3_1.entity.GroupEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GroupRepository extends CrudRepository<GroupEntity, Long> {
    GroupEntity findByName(String name);
}
