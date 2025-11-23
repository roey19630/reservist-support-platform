package com.milufamilies.supportapp.model;

import com.milufamilies.supportapp.model.enums.SoldierRelation;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String soldierName;

    @Enumerated(EnumType.STRING)
    private SoldierRelation soldierRelation;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
