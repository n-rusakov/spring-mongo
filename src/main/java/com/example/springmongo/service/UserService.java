package com.example.springmongo.service;

import com.example.springmongo.entity.User;
import com.example.springmongo.repository.UserRepository;
import com.example.springmongo.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Mono<User> insert(User user){
        user.setId(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    public Mono<User> update(User user) {
        return findById(user.getId())
                .flatMap(existed -> {
                    BeanUtils.copyNonNullProperties(user, existed);
                    return userRepository.save(existed);
                });
    }


    public Mono<Void> deleteById(String id) {
        return userRepository.deleteById(id);
    }
}
