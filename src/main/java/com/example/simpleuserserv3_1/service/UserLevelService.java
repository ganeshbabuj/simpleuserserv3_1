package com.example.simpleuserserv3_1.service;

import com.example.simpleuserserv3_1.resource.*;

import java.util.Optional;

public interface UserLevelService {

    UserLevel createUserLevel(UserLevel userLevel);

    void updateUserLevel(long id, UserLevel userLevel, Optional<Long> delayOpt);
    UserLevel getUserLevel(long id);
    void upgradeUserLevel(LevelUpgradeRequest levelUpgradeRequest, Optional<Long> delayOpt);
    void incrementUserPoints(PointsIncrementRequest pointsIncrementRequest, Optional<Long> delayOpt);

}
