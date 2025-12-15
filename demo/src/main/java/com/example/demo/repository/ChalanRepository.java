package com.example.demo.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.model.Chalan;
import com.example.demo.model.Status;

public interface ChalanRepository extends MongoRepository<Chalan, String> {
    List<Chalan> findByStatus(Status status);
}
