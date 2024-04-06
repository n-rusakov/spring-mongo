package com.example.springmongo.mapper;

import com.example.springmongo.dto.TaskResponse;
import com.example.springmongo.dto.TaskUpsertRequest;
import com.example.springmongo.entity.Task;
import com.example.springmongo.entity.User;
import com.example.springmongo.repository.TaskRepository;
import com.example.springmongo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = UserMapper.class)
@RequiredArgsConstructor
public abstract class TaskMapper {

    public abstract Task taskUpsertRequestToTask(TaskUpsertRequest request);

    @Mapping(source = "taskId", target = "id")
    public abstract Task taskUpsertRequestToTask(String taskId, TaskUpsertRequest request);

    public abstract TaskResponse taskToTaskResponse(Task task);


}
