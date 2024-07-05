package com.jilnash.adminservice.mapper;

import com.jilnash.adminservice.dto.AdminCreateDTO;
import com.jilnash.adminservice.dto.AdminUpdateDTO;
import com.jilnash.adminservice.model.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

    public Admin toEntity(AdminUpdateDTO adminUpdateDTO) {

        return Admin.builder()
                .id(adminUpdateDTO.getId())
                .name(adminUpdateDTO.getName())
                .surname(adminUpdateDTO.getSurname())
                .build();
    }

    public Admin toEntity(AdminCreateDTO adminCreateDTO) {

        return Admin.builder()
                .userId(adminCreateDTO.getUserId())
                .name(adminCreateDTO.getName())
                .surname(adminCreateDTO.getSurname())
                .build();
    }
}
