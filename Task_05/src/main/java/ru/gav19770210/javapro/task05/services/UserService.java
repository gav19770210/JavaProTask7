package ru.gav19770210.javapro.task05.services;

import ru.gav19770210.javapro.task05.entities.UserEntity;

import java.util.List;

public interface UserService {
    List<UserEntity> getAllUsers();

    UserEntity getUserById(Long id);

    UserEntity getUserByName(String name);

    UserEntity createUser(UserEntity userEntity);

    UserEntity updateUser(UserEntity userEntity);

    void deleteUserById(Long id);
}
