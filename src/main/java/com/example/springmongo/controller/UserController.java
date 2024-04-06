package com.example.springmongo.controller;

import com.example.springmongo.dto.UserResponse;
import com.example.springmongo.dto.UserUpsertRequest;
import com.example.springmongo.mapper.UserMapper;
import com.example.springmongo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping
    public Flux<UserResponse> getAll() {
        return userService.findAll().map(userMapper::UserToUserResponse);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> getById(@PathVariable String id) {
        return userService
                .findById(id)
                .map(userMapper::UserToUserResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public Mono<ResponseEntity<UserResponse>> create(@RequestBody UserUpsertRequest request) {
        return userService
                .insert(userMapper.userUpsertRequestToUser(request))
                .map(userMapper::UserToUserResponse)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> update(@PathVariable String id,
                                                     @RequestBody UserUpsertRequest request) {
        return userService
                .update(userMapper.userUpsertRequestToUser(id, request))
                .map(userMapper::UserToUserResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return userService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }

}
