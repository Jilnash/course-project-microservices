package com.jilnash.hwresponseservice.repo;

import com.jilnash.hwresponseservice.model.HwResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HwResponseRepo extends JpaRepository<HwResponse, Long>, JpaSpecificationExecutor<HwResponse> {

}
