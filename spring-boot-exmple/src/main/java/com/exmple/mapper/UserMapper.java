package com.exmple.mapper;

import com.exmple.entity.User;
import com.exmple.model.RegisterResponse;
import com.exmple.model.UpdateRequest;
import com.exmple.model.UpdateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    RegisterResponse toRegisterResponse(User user);
    UpdateResponse toUpdateResponse(User user);

}

