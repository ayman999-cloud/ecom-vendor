package com.aymane.ecom.multivendor.model;

import com.aymane.ecom.multivendor.domain.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import static com.aymane.ecom.multivendor.domain.UserRole.CUSTOMER;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Table(name = "u_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String email;

    private String fullName;

    private String mobile;

    private UserRole role = CUSTOMER;

    @OneToMany
    private Set<Address> addresses = new HashSet<>();

    @ManyToMany
    @JsonIgnore
    private Set<Coupon> usedCoupons = new HashSet<>();
}
