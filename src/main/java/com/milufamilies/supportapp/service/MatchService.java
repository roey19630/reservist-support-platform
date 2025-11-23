package com.milufamilies.supportapp.service;

import com.milufamilies.supportapp.model.Match;
import com.milufamilies.supportapp.model.Need;
import com.milufamilies.supportapp.model.Offer;
import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.model.enums.*;
import com.milufamilies.supportapp.repository.MatchRepository;
import com.milufamilies.supportapp.repository.NeedRepository;
import com.milufamilies.supportapp.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final NeedRepository needRepository;
    private final OfferRepository offerRepository;
    private final MatchRepository matchRepository;
    private final ChatThreadService chatThreadService;

    // 🟢 Matching para מתנדבים: מציאת בקשות שמתאימות להצעות
    public List<Need> findMatchingNeedsForVolunteer(User volunteer) {
        List<Offer> offers = offerRepository.findByUser(volunteer);
        List<Need> needs = needRepository.findByStatus(NeedStatus.PENDING);

        List<Need> result = new ArrayList<>();
        for (Need need : needs) {
            for (Offer offer : offers) {
                if (
                        need.getHelpType() == offer.getHelpType() &&
                                need.getRegions().stream().anyMatch(offer.getRegions()::contains) &&
                                need.getDaysAvailable().stream().anyMatch(offer.getDaysAvailable()::contains) &&
                                need.getTimeSlots().stream().anyMatch(offer.getTimeSlots()::contains)
                ) {
                    result.add(need);
                    break;
                }
            }
        }
        return result;
    }

    // 🟡 Matching כללי עם פילטרים
    public List<Need> filterNeeds(NeedStatus status,
                                  HelpType helpType,
                                  Region region,
                                  DaysAvailable day,
                                  TimeSlot time) {

        return needRepository.findAll().stream()
                .filter(n -> status == null || n.getStatus() == status)
                .filter(n -> helpType == null || n.getHelpType() == helpType)
                .filter(n -> region == null || n.getRegions().contains(region))
                .filter(n -> day == null || n.getDaysAvailable().contains(day))
                .filter(n -> time == null || n.getTimeSlots().contains(time))
                .toList();
    }


    public List<Need> findAllNeedsByStatus(NeedStatus needStatus) {
        return needRepository.findByStatus(needStatus);
    }

    public List<Offer> findMatchingOffersForFamily(User family) {
        List<Need> needs = needRepository.findByUser(family);
        List<Offer> allOffers = offerRepository.findAll();

        List<Offer> matching = new ArrayList<>();

        for (Offer offer : allOffers) {
            for (Need need : needs) {
                if (
                        offer.getHelpType() == need.getHelpType() &&
                                offer.getRegions().stream().anyMatch(need.getRegions()::contains) &&
                                offer.getDaysAvailable().stream().anyMatch(need.getDaysAvailable()::contains) &&
                                offer.getTimeSlots().stream().anyMatch(need.getTimeSlots()::contains)
                ) {
                    matching.add(offer);
                    break;
                }
            }
        }
        return matching;
    }

    public List<Offer> filterOffers(HelpType helpType, Region region, DaysAvailable day, TimeSlot time) {
        return offerRepository.findAll().stream()
                .filter(o -> helpType == null || o.getHelpType() == helpType)
                .filter(o -> region == null || o.getRegions().contains(region))
                .filter(o -> day == null || o.getDaysAvailable().contains(day))
                .filter(o -> time == null || o.getTimeSlots().contains(time))
                //.filter(o -> o.getStatus() == OfferStatus.AVAILABLE)
                .toList();
    }


    public void createMatchRequest(Long needId, User volunteer) {
        Need need = needRepository.findById(needId)
                .orElseThrow(() -> new IllegalArgumentException("Need not found"));

        Offer matchedOffer = offerRepository.findByUser(volunteer).stream()
                .filter(offer -> isMatching(need, offer))
                .findFirst()
                .orElse(null);


        Match match = Match.builder()
                .volunteerId(volunteer.getId())
                .familyId(need.getUser().getId())
                .need(need)
                .offer(matchedOffer)
                .status(MatchStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();


        if (matchAlreadyExists(need.getUser().getId(), volunteer.getId(), need, matchedOffer)) {
            System.out.println("⚠️ התאמה כזאת כבר קיימת - לא נוצרת כפילות");
            return;
        }

        matchRepository.save(match);
        System.out.println("📩 נשלחה בקשת קשר למשפחה ID: " + need.getUser().getId());
        chatThreadService.getOrCreateChat(need.getUser(), volunteer);

    }

    public void createMatchFromOffer(Long offerId, User family) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found"));

        Need matchedNeed = needRepository.findByUser(family).stream()
                .filter(need -> isMatching(need, offer))
                .findFirst()
                .orElse(null);


        Match match = Match.builder()
                .volunteerId(offer.getUser().getId())
                .familyId(family.getId())
                .offer(offer)
                .need(matchedNeed)
                .status(MatchStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        if (matchAlreadyExists(family.getId(), offer.getUser().getId(), matchedNeed,offer)) {
            System.out.println("⚠️ התאמה כזאת כבר קיימת - לא נוצרת כפילות");
            return;
        }

        matchRepository.save(match);
        System.out.println("📩 נשלחה בקשת קשר למתנדב ID: " + offer.getUser().getId());
        chatThreadService.getOrCreateChat(family, offer.getUser());

    }

    private boolean isMatching(Need need, Offer offer) {
        if (need == null || offer == null) return false;

        return need.getHelpType() == offer.getHelpType()
                && need.getRegions().stream().anyMatch(offer.getRegions()::contains)
                && need.getDaysAvailable().stream().anyMatch(offer.getDaysAvailable()::contains)
                && need.getTimeSlots().stream().anyMatch(offer.getTimeSlots()::contains);
    }



    public List<Offer> findAllOffers() {
        return offerRepository.findAll();
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public List<Match> getPendingMatchesForFamily(User family) {
        return matchRepository.findByFamilyIdAndStatus(family.getId(), MatchStatus.PENDING);
    }

    public void updateMatchStatus(Long matchId, MatchStatus newStatus) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));
        match.setStatus(newStatus);
        match.setClosedAt(LocalDateTime.now());
        matchRepository.save(match);
    }

    public boolean matchAlreadyExists(Long familyId, Long volunteerId, Need need, Offer offer) {
        List<Match> matches = matchRepository.findByFamilyIdAndVolunteerId(familyId, volunteerId);

        for (Match m : matches) {
            boolean sameNeed =
                    (m.getNeed() == null && need == null) ||
                            (m.getNeed() != null && need != null && m.getNeed().getId().equals(need.getId()));

            boolean sameOffer =
                    (m.getOffer() == null && offer == null) ||
                            (m.getOffer() != null && offer != null && m.getOffer().getId().equals(offer.getId()));

            if (sameNeed && sameOffer) {
                return true;
            }
        }

        return false;
    }

    public Match findExistingMatch(Long familyId, Long offerId) {
        return matchRepository.findByFamilyIdAndOffer_Id(familyId, offerId)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public Match findExistingMatchByNeedAndVolunteer(Long needId, Long volunteerId) {
        return matchRepository
                .findByNeed_IdAndVolunteerId(needId, volunteerId)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public int countPendingMatchesForFamily(Long familyId) {
        return matchRepository.countByFamilyIdAndStatus(familyId, MatchStatus.PENDING);

    }

    public List<Match> getActiveMatchesForVolunteer(Long volunteerId) {
        return matchRepository.findByVolunteerIdAndStatus(volunteerId, MatchStatus.ACTIVE);
    }

    public int countActiveMatchesForVolunteer(Long volunteerId) {
        return getActiveMatchesForVolunteer(volunteerId).size();
    }

}
