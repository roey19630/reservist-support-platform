package com.milufamilies.supportapp.repository;

import com.milufamilies.supportapp.model.Need;
import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.model.enums.HelpType;
import com.milufamilies.supportapp.model.enums.NeedStatus;
import com.milufamilies.supportapp.model.enums.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NeedRepository extends JpaRepository<Need, Long> {
    List<Need> findByStatus(NeedStatus needStatus);
    List<Need> findByUser(User user);

    Optional<Need> findTopByUserAndStatusOrderByCreatedAtDesc(User family, NeedStatus needStatus);
    long countByHelpType(HelpType helpType);
    @Query("SELECT COUNT(n) FROM Need n WHERE :region MEMBER OF n.regions")
    long countByRegion(@Param("region") Region region);

}
