package com.milufamilies.supportapp.service;

import com.milufamilies.supportapp.model.enums.HelpType;
import com.milufamilies.supportapp.model.Offer;
import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.model.enums.Region;
import com.milufamilies.supportapp.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;

    public Offer createOffer(Offer offer) {
        offer.setCreatedAt(LocalDateTime.now());
        return offerRepository.save(offer);
    }

    public List<Offer> getOffersByUser(User user) {
        return offerRepository.findByUser(user);
    }

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public List<Offer> filterForAdmin(String helpTypeStr, String statusStr, String keyword) {
        return offerRepository.findAll().stream()
                .filter(o -> helpTypeStr == null || helpTypeStr.isBlank() || o.getHelpType().name().equalsIgnoreCase(helpTypeStr))
                .filter(o -> statusStr == null || statusStr.isBlank() || o.getStatus().name().equalsIgnoreCase(statusStr))
                .filter(o -> keyword == null || keyword.isBlank() ||
                        o.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                        (o.getUser() != null && o.getUser().getFullName().toLowerCase().contains(keyword.toLowerCase()))
                )
                .toList();
    }

}
