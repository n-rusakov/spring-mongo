package com.example.springmongo.mapper;

import com.example.springmongo.dto.UserResponse;
import com.example.springmongo.dto.UserUpsertRequest;
import com.example.springmongo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User userUpsertRequestToUser(UserUpsertRequest userUpsertRequest);

    @Mapping(source = "userId", target = "id")
    User userUpsertRequestToUser(String userId, UserUpsertRequest userUpsertRequest);

    UserResponse UserToUserResponse(User user);

}
