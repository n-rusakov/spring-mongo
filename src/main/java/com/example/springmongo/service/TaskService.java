package com.example.springmongo.service;

import com.example.springmongo.dto.TaskUpsertRequest;
import com.example.springmongo.entity.Task;
import com.example.springmongo.entity.User;
import com.example.springmongo.repository.TaskRepository;
import com.example.springmongo.repository.UserRepository;
import com.example.springmongo.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;


    public Flux<Task> findAll() {
        return taskRepository.findAll().flatMap(this::loadUserData);
    }

    public Mono<Task> findById(String id) {
        return taskRepository.findById(id).flatMap(this::loadUserData);
    }

    public Mono<Task> loadUserData(Task task) {

        Mono<User> author = userRepository.findById(task.getAuthorId());
        Mono<User> assignee = userRepository.findById(task.getAssigneeId());

        Mono<Set<User>> observers = Flux
                .fromIterable(task.getObserverIds())
                .flatMap(userRepository::findById)
                .collect(Collectors.toSet())
                .defaultIfEmpty(new HashSet<>());

        return Mono
                .zip(Mono.just(task), author, assignee, observers)
                .map((tuple4) -> {
                    Task taskZip = tuple4.getT1();
                    User authorZip = tuple4.getT2();
                    User assigneeZip = tuple4.getT3();
                    Set<User> observersZip = tuple4.getT4();

                    taskZip.setAuthor(authorZip);
                    taskZip.setAssignee(assigneeZip);
                    taskZip.setObservers(observersZip);

                    return taskZip;
                });

    }

    public Mono<Task> insert(Task task) {
        task.setId(UUID.randomUUID().toString());
        task.setCreatedAt(Instant.now());
        return loadUserData(task).flatMap(taskRepository::save);
    }

    public Mono<Task> update(Task task) {
        return taskRepository
                .findById(task.getId())
                .map(existed -> {
                    BeanUtils.copyNonNullProperties(task, existed);
                    existed.setUpdatedAt(Instant.now());

                    return existed;
                })
                .flatMap(this::loadUserData)
                .flatMap(taskRepository::save);
    }

    public Mono<Task> addObserver(String taskId, String observerId) {
        return taskRepository
                .findById(taskId)
                .map(task -> {
                    task.getObserverIds().add(observerId);
                    return task;
                })
                .flatMap(this::loadUserData)
                .flatMap(taskRepository::save);
    }

    public Mono<Void> deleteById(String id) {
        return taskRepository.deleteById(id);
    }


}
