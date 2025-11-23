package com.milufamilies.supportapp.model;

import com.milufamilies.supportapp.model.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "matches",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"familyId", "volunteerId", "need_id", "offer_id"}
        )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID של המשפחה המשתתפת (חובה תמיד)
    private Long familyId;

    //️ ID של המתנדב המשתתף (חובה תמיד)
    private Long volunteerId;

    //  בקשה — יכולה להיות null אם ההתאמה התחילה מהצעה בלבד
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "need_id", nullable = true)
    private Need need;

    //  הצעה — יכולה להיות null אם ההתאמה התחילה מבקשה בלבד
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id", nullable = true)
    private Offer offer;

    // סטטוס ההתאמה
    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    // אישורים משני הצדדים (ברירת מחדל = false)
    private boolean approvedByFamily = false;
    private boolean approvedByVolunteer = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime closedAt;
}
