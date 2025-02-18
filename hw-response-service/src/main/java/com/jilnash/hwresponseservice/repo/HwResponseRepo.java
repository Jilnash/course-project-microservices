package com.jilnash.hwresponseservice.repo;

import com.jilnash.hwresponseservice.model.mongo.HwResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HwResponseRepo extends MongoRepository<HwResponse, String> {

    boolean existsByHomeworkId(String homeworkId);
}
