package com.milufamilies.supportapp.repository;

import com.milufamilies.supportapp.model.FamilyDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyDetailsRepository extends JpaRepository<FamilyDetails, Long> {

    FamilyDetails findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
