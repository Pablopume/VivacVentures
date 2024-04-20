package com.example.vivacventures.data.modelo.mappers;

import com.example.vivacventures.domain.modelo.User;
import com.example.vivacventures.data.modelo.UserEntity;
import com.example.vivacventures.domain.modelo.dto.UserRegisterDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    UserEntity toUserEntity(User user);
    User toUser(UserEntity userEntity);

    UserEntity toUserEntity(UserRegisterDTO userRegisterDTO);

}
