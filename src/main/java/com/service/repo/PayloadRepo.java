package com.service.repo;

import org.springframework.data.repository.CrudRepository;

import com.service.model.Payload;

public interface PayloadRepo extends CrudRepository<Payload, Long> {

}
