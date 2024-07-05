package com.jilnash.adminservice.sevice;

import com.jilnash.adminservice.model.Admin;
import com.jilnash.adminservice.repo.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepo adminRepo;


    @Override
    public List<Admin> getAdmins() {
        return adminRepo.findAll();
    }

    @Override
    public Admin getAdmin(Long id) {
        return adminRepo
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Admin not found with id: " + id));
    }

    @Override
    public Admin saveAdmin(Admin admin) {
        return adminRepo.save(admin);
    }
}
