package com.exmple.mapper;

import com.exmple.entity.User;
import com.exmple.model.RegisterResponse;
import com.exmple.model.UpdateResponse;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-03T14:50:17+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.7 (Amazon.com Inc.)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public RegisterResponse toRegisterResponse(User user) {
        if ( user == null ) {
            return null;
        }

        RegisterResponse registerResponse = new RegisterResponse();

        registerResponse.setEmail( user.getEmail() );
        registerResponse.setUsername( user.getUsername() );

        return registerResponse;
    }

    @Override
    public UpdateResponse toUpdateResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UpdateResponse updateResponse = new UpdateResponse();

        updateResponse.setName( user.getName() );
        updateResponse.setEmail( user.getEmail() );

        return updateResponse;
    }
}
