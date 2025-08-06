package com.aymane.ecom.multivendor.model;

import com.aymane.ecom.multivendor.domain.AccountStatus;
import com.aymane.ecom.multivendor.domain.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sellerName;

    private String mobile;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Embedded
    private BusinessDetails businessDetails = new BusinessDetails();

    @Embedded
    private BankDetails bankDetails = new BankDetails();

    @OneToOne(cascade = CascadeType.ALL)
    private Address pickupAddress = new Address();

    private UserRole role;

    private boolean isMailVerified = false;

    private AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;
}
