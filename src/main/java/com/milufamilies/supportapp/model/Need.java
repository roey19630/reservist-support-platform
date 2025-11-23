package com.milufamilies.supportapp.model;

import com.milufamilies.supportapp.model.enums.HelpType;
import com.milufamilies.supportapp.model.enums.TimeSlot;
import com.milufamilies.supportapp.model.enums.DaysAvailable;
import com.milufamilies.supportapp.model.enums.NeedStatus;
import com.milufamilies.supportapp.model.enums.Region;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "needs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Need {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private HelpType helpType;

    @Enumerated(EnumType.STRING)
    private NeedStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<DaysAvailable> daysAvailable;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<TimeSlot> timeSlots;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Region> regions;

    @Column(length = 500)
    @Size(max = 500)
    @NotBlank
    private String description;

    private String preferredResponseTime;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}
