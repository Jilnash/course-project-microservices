package com.jilnash.hwresponseservice.repo;

import com.jilnash.hwresponseservice.model.mongo.HwResponse;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HwResponseRepo extends MongoRepository<HwResponse, String> {

    boolean existsByHomeworkId(String homeworkId);

    List<HwResponse> find(Query query, Class<HwResponse> hwResponseClass);
}
