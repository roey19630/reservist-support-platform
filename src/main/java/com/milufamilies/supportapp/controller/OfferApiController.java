package com.milufamilies.supportapp.controller;

import com.milufamilies.supportapp.model.Offer;
import com.milufamilies.supportapp.model.User;
import com.milufamilies.supportapp.service.OfferService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
public class OfferApiController {

    private final OfferService offerService;

    @PostMapping
    public Offer createOffer(@RequestBody Offer offer, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        offer.setUser(user);
        return offerService.createOffer(offer);
    }

    @GetMapping("/my")
    public List<Offer> getMyOffers(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        return offerService.getOffersByUser(user);
    }

    @GetMapping("/all")
    public List<Offer> getAllOffers() {
        return offerService.getAllOffers();
    }
}
