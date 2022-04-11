package com.example.simpleuserserv3_1;

import com.example.simpleuserserv3_1.entity.UserEntity;
import com.example.simpleuserserv3_1.entity.UserLevelEntity;
import com.example.simpleuserserv3_1.resource.User;
import com.example.simpleuserserv3_1.resource.UserLevel;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class Simpleuserserv3_1Application {

    public static void main(String[] args) {
        SpringApplication.run(Simpleuserserv3_1Application.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(User.class, UserEntity.class).addMappings(mapper -> {
            mapper.skip(UserEntity::setGroups);
        });
        modelMapper.typeMap(UserLevel.class, UserLevelEntity.class).addMappings(mapper -> {
            mapper.skip(UserLevelEntity::setId);
            mapper.skip(UserLevelEntity::setVersion);
        });
        return modelMapper;
    }


}
