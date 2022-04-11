package com.example.simpleuserserv3_1.service.impl;

import com.example.simpleuserserv3_1.entity.UserLevelEntity;
import com.example.simpleuserserv3_1.exception.ServiceException;
import com.example.simpleuserserv3_1.repository.UserLevelRepository;
import com.example.simpleuserserv3_1.resource.*;
import com.example.simpleuserserv3_1.service.UserLevelService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserLevelServiceImpl implements UserLevelService {

    private UserLevelRepository userLevelRepository;
    private ModelMapper modelMapper;

    public UserLevelServiceImpl(UserLevelRepository userLevelRepository, ModelMapper modelMapper) {
        this.userLevelRepository = userLevelRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserLevel createUserLevel(UserLevel userLevel) {
        if (userLevelRepository.existsByUserId(userLevel.getUserId())) {
            throw new ServiceException("UserId already exists", HttpStatus.BAD_REQUEST);
        }
        UserLevelEntity userLevelEntity = modelMapper.map(userLevel, UserLevelEntity.class);
        UserLevelEntity savedUserLevelEntity = userLevelRepository.save(userLevelEntity);
        return modelMapper.map(savedUserLevelEntity, UserLevel.class);
    }

    @Override
    public UserLevel getUserLevel(long id) {

        Optional<UserLevelEntity> optionalUserLevelEntity = userLevelRepository.findById(id);
        if (!optionalUserLevelEntity.isPresent()) {
            throw new ServiceException("User Level Not Found", HttpStatus.NOT_FOUND);
        }
        return modelMapper.map(optionalUserLevelEntity.get(), UserLevel.class);
    }


    @Override
    public void upgradeUserLevel(LevelUpgradeRequest levelUpgradeRequest, Optional<Long> delayOpt) {

        Optional<UserLevelEntity> optionalUserLevelEntity = userLevelRepository.findByUserId(levelUpgradeRequest.getUserId());
        if (!optionalUserLevelEntity.isPresent()) {
            throw new ServiceException("User Level Not Found", HttpStatus.NOT_FOUND);
        }
        UserLevelEntity userLevelEntity = optionalUserLevelEntity.get();
        log.debug("{} | Existing userLevelEntity: {}", levelUpgradeRequest.getRemark(), userLevelEntity);

        // Delay for demo optimistic Locking @Version
        if (delayOpt.isPresent()) {
            try {
                log.debug("{} | Sleeping for {}", levelUpgradeRequest.getRemark(), delayOpt.get() + " ms");
                Thread.sleep(delayOpt.get());
            } catch (InterruptedException ie) {
                // ignore
            }
        }

        Level curLevel = userLevelEntity.getLevel();
        Level newLevel = (curLevel.ordinal() + 1 < Level.values().length) ? Level.values()[curLevel.ordinal() + 1] : curLevel;

        int updatedCount = userLevelRepository.updateLevel(levelUpgradeRequest.getUserId(), newLevel, levelUpgradeRequest.getRemark());

        /*
        int updatedCount = userLevelRepository.updateLevel(levelUpgradeRequest.getUserId(), newLevel, curLevel, levelUpgradeRequest.getRemark());
        log.info("Updated Count: {}", updatedCount);
        if(updatedCount <= 0) {
            throw new ServiceException("Stale or Inconsistent data sent in Request", HttpStatus.CONFLICT);
        }
         */

    }

    @Override
    public void incrementUserPoints(PointsIncrementRequest pointsIncrementRequest, Optional<Long> delayOpt) {

        Optional<UserLevelEntity> optionalUserLevelEntity = userLevelRepository.getUserLevelByUserIdForUpdate(pointsIncrementRequest.getUserId());
        if (!optionalUserLevelEntity.isPresent()) {
            throw new ServiceException("User Level Not Found", HttpStatus.NOT_FOUND);
        }
        UserLevelEntity userLevelEntity = optionalUserLevelEntity.get();
        log.debug("{} | Existing userLevelEntity: {}", pointsIncrementRequest.getRemark(), userLevelEntity);

        // Delay for demo optimistic Locking @Version
        if (delayOpt.isPresent()) {
            try {
                log.debug("{} | Sleeping for {}", pointsIncrementRequest.getRemark(), delayOpt.get() + " ms");
                Thread.sleep(delayOpt.get());
            } catch (InterruptedException ie) {
                // ignore
            }
        }

        long totalPoints = userLevelEntity.getPoints() + pointsIncrementRequest.getPoints();
        int levelOrdinal = (int) totalPoints / 100;
        Level level = (levelOrdinal < Level.values().length) ? Level.values()[levelOrdinal] : userLevelEntity.getLevel();

        userLevelRepository.updatePointAndLevel(pointsIncrementRequest.getUserId(), totalPoints, level, pointsIncrementRequest.getRemark());
        
    }


    @Override
    public void updateUserLevel(long id, UserLevel userLevel, Optional<Long> delayOpt) {

        Optional<UserLevelEntity> optionalUserLevelEntity = userLevelRepository.getUserLevelByIdForUpdate(id);
        if (!optionalUserLevelEntity.isPresent()) {
            throw new ServiceException("User Level Not Found", HttpStatus.NOT_FOUND);
        }

        UserLevelEntity userLevelEntity = optionalUserLevelEntity.get();
        log.debug("{} | Existing userLevelEntity: {}", userLevel.getRemark(), userLevelEntity);

        // Delay for demo pessimistic & optimistic Locking @Version
        if (delayOpt.isPresent()) {
            try {
                log.debug("{} | Sleeping for {}", userLevel.getRemark(), delayOpt.get() + " ms");
                Thread.sleep(delayOpt.get());
            } catch (InterruptedException ie) {
                // ignore
            }
        }

        modelMapper.map(userLevel, userLevelEntity);
        UserLevelEntity updatedUserLevelEntity = userLevelRepository.save(userLevelEntity);
        log.debug("{} | Updated userLevelEntity: {}", userLevel.getRemark(), updatedUserLevelEntity);

    }



}
