package com.milufamilies.supportapp.repository;

import com.milufamilies.supportapp.model.Match;
import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.model.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    long countByStatus(MatchStatus status);

    List<Match> findByFamilyIdAndStatus(Long familyId, MatchStatus status);

    List<Match> findByFamilyIdAndVolunteerId(Long familyId, Long volunteerId);

    Collection<Match> findByFamilyIdAndOffer_Id(Long familyId, Long offerId);

    Collection<Match> findByNeed_IdAndVolunteerId(Long needId, Long volunteerId);

    int countByFamilyIdAndStatus(Long familyId, MatchStatus matchStatus);

    List<Match> findByVolunteerIdAndStatus(Long volunteerId, MatchStatus matchStatus);
}
