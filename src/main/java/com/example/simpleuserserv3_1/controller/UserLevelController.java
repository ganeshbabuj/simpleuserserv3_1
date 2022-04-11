package com.example.simpleuserserv3_1.controller;

import com.example.simpleuserserv3_1.resource.PointsIncrementRequest;
import com.example.simpleuserserv3_1.resource.UserLevel;
import com.example.simpleuserserv3_1.resource.LevelUpgradeRequest;
import com.example.simpleuserserv3_1.service.UserLevelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/v1/account")
public class UserLevelController {

    private UserLevelService userLevelService;

    public UserLevelController(UserLevelService userLevelService) {
        this.userLevelService = userLevelService;
    }

    @PostMapping("/user-levels")
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserLevel create(@RequestBody UserLevel userLevel) {
        return userLevelService.createUserLevel(userLevel);
    }

    @PutMapping("/user-levels/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Long id, @RequestBody UserLevel userLevel, @RequestParam(name="delay", required = false) Optional<Long> delayOpt) {
        userLevelService.updateUserLevel(id, userLevel, delayOpt);
    }

    @GetMapping("/user-levels/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public UserLevel read(@PathVariable("id") Long id) {
        return userLevelService.getUserLevel(id);
    }

    @PostMapping("/upgrade-level")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void upgradeUserLevel(@RequestBody LevelUpgradeRequest levelUpgradeRequest, @RequestParam(name="delay", required = false) Optional<Long> delayOpt) {
        log.debug("LevelUpgradeRequest: {}", levelUpgradeRequest);
        userLevelService.upgradeUserLevel(levelUpgradeRequest, delayOpt);
    }

    @PostMapping("/increment-points")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void incrementUserPoints(@RequestBody PointsIncrementRequest pointsIncrementRequest, @RequestParam(name="delay", required = false) Optional<Long> delayOpt) {
        log.debug("PointsIncrementRequest: {}", pointsIncrementRequest);
        userLevelService.incrementUserPoints(pointsIncrementRequest, delayOpt);
    }


}


