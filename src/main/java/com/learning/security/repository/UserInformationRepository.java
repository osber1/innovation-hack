package com.learning.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.security.model.UserInformation;
import com.learning.security.model.UserInformation.Status;

@Repository
public interface UserInformationRepository extends JpaRepository<UserInformation, Integer> {

    Optional<UserInformation> findByRfid(String rfid);

    boolean existsByRfidAndStatusEquals(String rfid, Status status);
}