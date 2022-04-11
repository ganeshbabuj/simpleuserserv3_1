package com.example.simpleuserserv3_1.service.impl;

import com.example.simpleuserserv3_1.client.ActivationRequest;
import com.example.simpleuserserv3_1.client.ActivationResponse;
import com.example.simpleuserserv3_1.client.AdServClient;
import com.example.simpleuserserv3_1.entity.AddressEntity;
import com.example.simpleuserserv3_1.entity.GroupEntity;
import com.example.simpleuserserv3_1.entity.UserEntity;
import com.example.simpleuserserv3_1.exception.ServiceException;
import com.example.simpleuserserv3_1.repository.GroupRepository;
import com.example.simpleuserserv3_1.repository.UserRepository;
import com.example.simpleuserserv3_1.resource.Group;
import com.example.simpleuserserv3_1.resource.User;
import com.example.simpleuserserv3_1.resource.UserCollection;
import com.example.simpleuserserv3_1.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Scope("singleton") // Not mandatory
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private GroupRepository groupRepository;

    private AdServClient adServClient;

    private ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, GroupRepository groupRepository, AdServClient adServClient, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.adServClient = adServClient;
        this.modelMapper = modelMapper;
    }

    @Override
    //TODO: Encrypt password in production application
    //TODO: Try functional style
    @Transactional
    public User createUser(User user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ServiceException("Username already exists", HttpStatus.BAD_REQUEST);
        }

        User savedUser = null;

        // Save in Repository
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);

        if (!CollectionUtils.isEmpty(userEntity.getAddresses())) {
            for (AddressEntity addressEntity : userEntity.getAddresses()) {
                addressEntity.setUser(userEntity);
            }
        }

        if (!CollectionUtils.isEmpty(user.getGroups())) {
            for (Group group : user.getGroups()) {
                GroupEntity groupEntity = groupRepository.findByName(group.getName());
                if (groupEntity == null) {
                    groupEntity = modelMapper.map(group, GroupEntity.class);
                }
                groupEntity.getUsers().add(userEntity);
                userEntity.getGroups().add(groupEntity);
            }
        }

        UserEntity savedUserEntity = userRepository.save(userEntity);
        savedUser = modelMapper.map(savedUserEntity, User.class);
        System.out.println("Saved User: " + savedUserEntity);

        try {
            ActivationResponse activationResponse = adServClient.activate(new ActivationRequest(savedUser.getId()));
            System.out.println(activationResponse);
            savedUser.setMarketing(activationResponse);
        } catch (Exception e) {

            // ignored intentionally
            e.printStackTrace();
            throw new ServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return savedUser;
    }

    @Override
    // TODO: Implement Pagination
    public UserCollection findUsers(Optional<String> firstName, int pageSize, int page) {

        Pageable pageable = PageRequest.of((page - 1), pageSize, Sort.by("firstName").ascending());
        Page<UserEntity> pagedResult;

        if (firstName.isPresent()) {
            pagedResult = userRepository.findByFirstName(firstName.get(), pageable);
        } else {
            pagedResult = userRepository.findAll(pageable);
        }

        long totalItems = pagedResult.getTotalElements();
        long totalPages = pagedResult.getTotalPages();
        page = pagedResult.getNumber() + 1;
        pageSize = pagedResult.getNumberOfElements();
        List<UserEntity> userEntityList = pagedResult.getContent();
        List<User> userList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(userEntityList)) {
            for (UserEntity userEntity : userEntityList) {
                User user = modelMapper.map(userEntity, User.class);
                userList.add(user);
            }
        }

        UserCollection userCollection = new UserCollection(totalItems, totalPages, page, pageSize, userList);

        return userCollection;
    }

    @Override
    public User getUser(long id) {

        User user = null;

        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);

        if (!optionalUserEntity.isPresent()) {
            throw new ServiceException("User Not Found", HttpStatus.NOT_FOUND);
        }
        user = modelMapper.map(optionalUserEntity.get(), User.class);

        return user;
    }

}
