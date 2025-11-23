    package com.milufamilies.supportapp.model;

    import com.milufamilies.supportapp.model.enums.Role;
    import jakarta.persistence.*;
    import jakarta.validation.constraints.NotBlank;
    import lombok.*;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.util.Collection;
    import java.util.List;

    @Entity
    @Table(name = "users")
    @Data
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class User implements UserDetails{

        @Column(name = "profile_image")
        private String profileImagePath;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private String idNumber;

        private String fullName;

        @Column(unique = true, nullable = false)
        private String email;

        private String password;

        @NotBlank
        private String phone;

        @Enumerated(EnumType.STRING)
        private Role role;


        @Column(nullable = false)
        private boolean approved;

        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Offer> offers;

        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Need> needs;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of();
        }

        @Override
        public String getUsername() {
            return email;
        }
        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private FamilyDetails familyDetails;

    }
