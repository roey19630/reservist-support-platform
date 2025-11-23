package com.milufamilies.supportapp.repository;

import com.milufamilies.supportapp.model.Offer;
import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.model.enums.Region;
import com.milufamilies.supportapp.model.enums.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import com.milufamilies.supportapp.model.enums.HelpType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByUser(User user);
    @Query("SELECT COUNT(o) FROM Offer o WHERE :slot MEMBER OF o.timeSlots")
    long countByTimeSlot(@Param("slot") TimeSlot slot);

}
