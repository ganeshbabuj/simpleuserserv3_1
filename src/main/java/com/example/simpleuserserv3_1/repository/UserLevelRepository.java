package com.example.simpleuserserv3_1.repository;

import com.example.simpleuserserv3_1.entity.UserLevelEntity;
import com.example.simpleuserserv3_1.resource.Level;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;


@Repository
public interface UserLevelRepository extends PagingAndSortingRepository<UserLevelEntity, Long> {

    boolean existsByUserId(Long id);

    Optional<UserLevelEntity> findByUserId(Long userId);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
   // @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    @Query("SELECT ul FROM UserLevelEntity ul WHERE ul.id = :id")
    Optional<UserLevelEntity> getUserLevelByIdForUpdate(Long id);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("SELECT ul FROM UserLevelEntity ul WHERE ul.userId = :userId")
    Optional<UserLevelEntity> getUserLevelByUserIdForUpdate(Long userId);

    @Modifying
    @Query("UPDATE UserLevelEntity ul SET ul.level = :newLevel, ul.remark = :remark WHERE ul.userId = :userId")
    int updateLevel(Long userId, Level newLevel, String remark);

    @Modifying
    @Query("UPDATE UserLevelEntity ul SET ul.level = :newLevel, ul.remark = :remark  WHERE ul.userId = :userId AND ul.level = :curLevel")
    int updateLevel(Long userId, Level newLevel, Level curLevel, String remark);

    @Modifying
    @Query("UPDATE UserLevelEntity ul SET ul.points = :points, ul.level = :newLevel, ul.remark = :remark WHERE ul.userId = :userId")
    int updatePointAndLevel(Long userId, Long points, Level newLevel, String remark);

}
