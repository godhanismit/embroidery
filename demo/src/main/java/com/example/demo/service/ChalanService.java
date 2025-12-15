package com.example.demo.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.demo.model.Chalan;
import com.example.demo.model.Status;
import com.example.demo.repository.ChalanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class ChalanService {
    private final ChalanRepository repo;
    @Autowired
    private MongoTemplate mongoTemplate;


    public ChalanService(ChalanRepository repo) {
        this.repo = repo;
    }

    public Chalan create(Chalan chalan) {
        chalan.setStatus(Status.CREATED);
        return repo.save(chalan);
    }

    public Optional<Chalan> findById(String id) {
        return repo.findById(id);
    }

    public List<Chalan> findAll() {
        return repo.findAll();
    }

    public List<Chalan> findByStatus(Status status) {
        return repo.findByStatus(status);
    }

    public Chalan save(Chalan c) {
        return repo.save(c);
    }

    public void deleteById(String id) {
        repo.deleteById(id);
    }

    public List<Chalan> filter(Map<String, String> filters) {
        Query query = new Query();

        filters.forEach((key, value) -> {
            if (value != null && !value.isEmpty()) {
                query.addCriteria(Criteria.where(key).is(value));
            }
        });

        return mongoTemplate.find(query, Chalan.class);
    }

}
