package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.Chalan;
import com.example.demo.model.Status;
import com.example.demo.model.User;

public interface ChalanRepository extends MongoRepository<Chalan, String> {

    List<Chalan> findByUser(User user);

    List<Chalan> findByStatusAndUser(Status status, User user);

    Optional<Chalan> findByIdAndUser(String id, User user);
}
