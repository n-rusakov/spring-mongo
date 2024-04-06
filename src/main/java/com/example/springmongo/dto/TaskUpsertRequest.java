package com.example.springmongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpsertRequest {

    private String name;

    private String description;

    private String status;

    private String authorId;

    private String assigneeId;

    private Set<String> observerIds;

}
