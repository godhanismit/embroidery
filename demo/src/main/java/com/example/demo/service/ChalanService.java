package com.example.demo.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.demo.model.Chalan;
import com.example.demo.model.Status;
import com.example.demo.model.User;
import com.example.demo.repository.ChalanRepository;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class ChalanService {

    private final ChalanRepository repo;
    private final MongoTemplate mongoTemplate;

    public ChalanService(ChalanRepository repo, MongoTemplate mongoTemplate) {
        this.repo = repo;
        this.mongoTemplate = mongoTemplate;
    }

    public Chalan create(Chalan chalan) {
        chalan.setStatus(Status.CREATED);
        return repo.save(chalan);
    }

    public Chalan save(Chalan c) {
        return repo.save(c);
    }

    public List<Chalan> findAllByUser(User user) {
        return repo.findByUser(user);
    }

    public List<Chalan> findByStatusAndUser(Status status, User user) {
        return repo.findByStatusAndUser(status, user);
    }

    public Optional<Chalan> findByIdAndUser(String id, User user) {
        return repo.findByIdAndUser(id, user);
    }

    public List<Chalan> filter(Map<String, String> filters, User user) {
        Query q = new Query();
        q.addCriteria(Criteria.where("user").is(user));

        filters.forEach((k, v) -> {
            if (v != null && !v.isEmpty()) {
                q.addCriteria(Criteria.where(k).is(v));
            }
        });

        return mongoTemplate.find(q, Chalan.class);
    }
}
