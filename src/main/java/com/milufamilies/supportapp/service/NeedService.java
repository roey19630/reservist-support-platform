package com.milufamilies.supportapp.service;

import com.milufamilies.supportapp.model.Need;
import com.milufamilies.supportapp.model.Offer;
import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.model.enums.*;
import com.milufamilies.supportapp.repository.NeedRepository;
import com.milufamilies.supportapp.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NeedService {

    private final NeedRepository needRepository;
    private final OfferRepository offerRepository;

    public void saveNeed(Need need) {
        needRepository.save(need);
    }

    public List<Need> getNeedsByUser(User user) {
        return needRepository.findByUser(user);
    }

    public List<Need> findNeedsMatchingUserOffers(User volunteer) {
        List<Offer> offers = offerRepository.findByUser(volunteer);
        List<Need> allNeeds = needRepository.findByStatus(NeedStatus.PENDING);

        List<Need> matching = new ArrayList<>();

        for (Need need : allNeeds) {
            for (Offer offer : offers) {
                if (
                        need.getHelpType() == offer.getHelpType() &&
                                need.getRegions() == offer.getRegions() &&
                                need.getDaysAvailable() == offer.getDaysAvailable() &&
                                need.getTimeSlots() == offer.getTimeSlots()
                ) {
                    matching.add(need);
                    break; // no need to check other offers if one matched
                }
            }
        }

        return matching;
    }

    public List<Need> filterNeeds(HelpType helpType, Region region, DaysAvailable day, TimeSlot time) {
        return needRepository.findAll().stream()
                .filter(n -> helpType == null || n.getHelpType() == helpType)
                .filter(n -> region == null || n.getRegions().contains(region))
                .filter(n -> day == null || n.getDaysAvailable().contains(day))
                .filter(n -> time == null || n.getTimeSlots().contains(time))
                .filter(n -> n.getStatus() == NeedStatus.PENDING)
                .toList();
    }

    public List<Need> filterForAdmin(String helpTypeStr, String statusStr, String keyword) {
        return needRepository.findAll().stream()
                .filter(n -> helpTypeStr == null || helpTypeStr.isBlank() || n.getHelpType().name().equalsIgnoreCase(helpTypeStr))
                .filter(n -> statusStr == null || statusStr.isBlank() || n.getStatus().name().equalsIgnoreCase(statusStr))
                .filter(n -> keyword == null || keyword.isBlank() ||
                        n.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                        (n.getUser() != null && n.getUser().getFullName().toLowerCase().contains(keyword.toLowerCase()))
                )
                .toList();
    }

}
