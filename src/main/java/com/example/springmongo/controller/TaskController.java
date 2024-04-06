package com.example.springmongo.controller;

import com.example.springmongo.dto.TaskResponse;
import com.example.springmongo.dto.TaskUpsertRequest;
import com.example.springmongo.mapper.TaskMapper;
import com.example.springmongo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;


    @GetMapping
    public Flux<TaskResponse> getAll(){
        return taskService
                .findAll()
                .map(taskMapper::taskToTaskResponse);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> getById(@PathVariable String id) {
        return taskService
                .findById(id)
                .map(taskMapper::taskToTaskResponse)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<TaskResponse>> create(@RequestBody TaskUpsertRequest request) {
        return taskService
                .insert(taskMapper.taskUpsertRequestToTask(request))
                .map(taskMapper::taskToTaskResponse)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> update(@PathVariable String id,
                                                     @RequestBody TaskUpsertRequest request) {

        return taskService
                .update(taskMapper.taskUpsertRequestToTask(id, request))
                .map(taskMapper::taskToTaskResponse)
                .map(dto-> ResponseEntity.status(HttpStatus.CREATED).body(dto))
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return taskService
                .deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @PostMapping("/add-observer/{id}")
    public Mono<ResponseEntity<TaskResponse>> addObserver(@PathVariable String id,
                                                          @RequestParam String observerId) {
        return taskService
                .addObserver(id, observerId)
                .map(taskMapper::taskToTaskResponse)
                .map(ResponseEntity::ok);
    }



}
