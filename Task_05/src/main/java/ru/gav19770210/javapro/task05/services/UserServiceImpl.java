package ru.gav19770210.javapro.task05.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gav19770210.javapro.task05.entities.UserEntity;
import ru.gav19770210.javapro.task05.repositories.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {
    public static final String msgUserNotFoundById = "Не найден пользователь с id = %d";
    public static final String msgUserNotFoundByName = "Не найден пользователь с name = %s";
    public static final String msgUserExistByName = "Уже существует пользователь с name = %s";
    private final UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<UserEntity> getAllUsers() {
        var userEntities = new ArrayList<UserEntity>();
        userRepo.findAll().forEach(userEntities::add);
        return userEntities;
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format(msgUserNotFoundById, id)));
    }

    @Override
    public UserEntity getUserByName(String name) {
        return userRepo.findByName(name)
                .orElseThrow(() -> new NoSuchElementException(String.format(msgUserNotFoundByName, name)));
    }

    @Override
    @Transactional
    public UserEntity createUser(UserEntity userEntity) {
        if (userRepo.findByName(userEntity.getName()).isPresent()) {
            throw new IllegalArgumentException(String.format(msgUserExistByName, userEntity.getName()));
        }
        return userRepo.save(userEntity);
    }

    @Override
    @Transactional
    public UserEntity updateUser(UserEntity userEntity) {
        var userById = userRepo.findById(userEntity.getId())
                .orElseThrow(() -> new NoSuchElementException(String.format(msgUserNotFoundById, userEntity.getId())));

        var userByName = userRepo.findByName(userEntity.getName()).orElse(null);
        if (userByName != null && !userByName.getId().equals(userEntity.getId())) {
            throw new IllegalArgumentException(String.format(msgUserExistByName, userEntity.getName()));
        }
        if (!userById.equals(userEntity)) {
            return userRepo.save(userEntity);
        }
        return userById;
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        if (userRepo.findById(id).isEmpty()) {
            throw new NoSuchElementException(String.format(msgUserNotFoundById, id));
        }
        userRepo.deleteById(id);
    }
}
