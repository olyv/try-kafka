package com.olyv.persistance;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightStatusRepository extends MongoRepository<FlightStatus, String> {

    Optional<FlightStatus> findFirstByOrderByCreatedDesc();

    Optional<FlightStatus> findByCreatedBy(String createdBy);
}
