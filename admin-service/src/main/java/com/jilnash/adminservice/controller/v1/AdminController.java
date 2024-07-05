package com.jilnash.adminservice.controller.v1;

import com.jilnash.adminservice.dto.AdminCreateDTO;
import com.jilnash.adminservice.dto.AdminUpdateDTO;
import com.jilnash.adminservice.dto.AppResponse;
import com.jilnash.adminservice.mapper.AdminMapper;
import com.jilnash.adminservice.sevice.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminMapper adminMapper;

    @GetMapping
    public ResponseEntity<?> getAdmins() {
        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Admins fetched successfully",
                        adminService.getAdmins()

                )
        );
    }

    @PutMapping
    public ResponseEntity<?> createAdmin(@Validated @RequestBody AdminCreateDTO adminDTO) {
        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Admins fetched successfully",
                        adminService.saveAdmin(adminMapper.toEntity(adminDTO))
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Admins fetched successfully",
                        adminService.getAdmin(id)
                )
        );
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Long id,
                                         @Validated @RequestBody AdminUpdateDTO adminDTO) {

        //checking if admin with id exists and setting userId
        adminDTO.setUserId(adminService.getAdmin(id).getUserId());
        adminDTO.setId(id);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Admins fetched successfully",
                        adminService.saveAdmin(adminMapper.toEntity(adminDTO))
                )
        );
    }


}
