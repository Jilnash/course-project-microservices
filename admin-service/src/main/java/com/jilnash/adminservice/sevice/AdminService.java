package com.jilnash.adminservice.sevice;


import com.jilnash.adminservice.model.Admin;

import java.util.List;

public interface AdminService {

    List<Admin> getAdmins();

    Admin getAdmin(Long id);

    Admin saveAdmin(Admin admin);
}
