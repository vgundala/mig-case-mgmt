package com.mig.sales.api.model.mapper;

import com.mig.sales.api.model.dto.UserDTO;
import com.mig.sales.api.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * MapStruct mapper for User entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Map User entity to UserDTO.
     */
    @Mapping(target = "fullName", source = ".", qualifiedByName = "getFullName")
    UserDTO toDTO(User user);

    /**
     * Map UserDTO to User entity.
     */
    @Mapping(target = "assignedLeads", ignore = true)
    @Mapping(target = "leadHistory", ignore = true)
    User toEntity(UserDTO userDTO);

    /**
     * Map list of User entities to list of UserDTOs.
     */
    List<UserDTO> toDTOList(List<User> users);

    /**
     * Helper method to get full name.
     */
    @Named("getFullName")
    default String getFullName(User user) {
        return user.getFullName();
    }
}
