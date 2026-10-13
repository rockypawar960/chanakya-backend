package com.chanakya.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(name = "class_or_year")
    private String classOrYear;

    @Column(name = "stream")
    private String stream;

    @Column(name = "interests")
    private String interests;

    @Column(name = "strengths")
    private String strengths;

    @Column(name = "challenges")
    private String challenges;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

   // ---------- NEW: Personal ----------

   @Column(name = "profile_picture_url")

   private String profilePictureUrl;



   @Column(name = "bio", length = 500)

   private String bio;



   @Column(name = "date_of_birth")

   private LocalDate dateOfBirth;



   @Column(name = "gender")

   private String gender;



   @Column(name = "phone_number")

   private String phoneNumber;



   // ---------- NEW: Location ----------

   @Column(name = "city")

   private String city;



   @Column(name = "state")

   private String state;



   @Column(name = "country")

   private String country;



   // ---------- NEW: Academic ----------

   @Column(name = "school_or_college_name")

   private String schoolOrCollegeName;



   @Column(name = "board")

   private String board;



   @Column(name = "target_exam")

   private String targetExam;



   @Column(name = "career_goal")

   private String careerGoal;



   // ---------- NEW: Social/Extra ----------

   @Column(name = "languages_known")

   private String languagesKnown;



   // ---------- NEW: Completion tracking ----------

   @Column(name = "profile_completion_percentage")

   @Builder.Default

   private Integer profileCompletionPercentage = 0;



   @Column(name = "is_profile_complete")

   @Builder.Default

   private Boolean isProfileComplete = false;



   // ---------- Roles / timestamps / status (unchanged) ----------

   @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)

   @JoinTable(

           name = "user_roles",

                       joinColumns = @JoinColumn(name = "user_id"),

                       inverseJoinColumns = @JoinColumn(name = "role_id")

   )

//   private Set<Role> roles = new HashSet<>();
//
//
//
//   @Column(name = "created_at", nullable = false, updatable = false)
//
//   private LocalDateTime createdAt;
//
//
//
//   @Column(name = "updated_at")
//
//   private LocalDateTime updatedAt;
//
//
//
//   @Column(name = "last_login")
//
//   private LocalDateTime lastLogin;
//
//
//
//   @Column(name = "is_active", nullable = false)
//
//   private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
