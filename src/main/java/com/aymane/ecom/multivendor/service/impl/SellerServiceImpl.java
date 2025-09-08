package com.aymane.ecom.multivendor.service.impl;

import com.aymane.ecom.multivendor.config.JwtProvider;
import com.aymane.ecom.multivendor.domain.AccountStatus;
import com.aymane.ecom.multivendor.domain.UserRole;
import com.aymane.ecom.multivendor.exception.SellerException;
import com.aymane.ecom.multivendor.model.Address;
import com.aymane.ecom.multivendor.model.Seller;
import com.aymane.ecom.multivendor.repository.AddressRepository;
import com.aymane.ecom.multivendor.repository.SellerRepository;
import com.aymane.ecom.multivendor.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    @Override
    public Seller getSellerProfile(final String jwt) throws SellerException {
        final String sellerEmail = this.jwtProvider.getEmailFromToken(jwt);
        if (Strings.isEmpty(sellerEmail)) {
            throw new SellerException("Token not valid");
        }
        return this.getSellerByEmail(sellerEmail);
    }

    @Override
    public Seller createSeller(final Seller seller) throws Exception {
        final Seller sellerExist = this.sellerRepository.findByEmail(seller.getEmail());
        if (Objects.nonNull(sellerExist)) {
            throw new Exception("Seller already exist, use different email");
        }
        final Address savedAddress = addressRepository.save(seller.getPickupAddress());
        return this.sellerRepository.save(Seller
                .builder()
                .email(seller.getEmail())
                .password(passwordEncoder.encode(seller.getPassword()))
                .sellerName(seller.getSellerName())
                .pickupAddress(savedAddress)
                .gstin(seller.getGstin())
                .role(UserRole.SELLER)
                .mobile(seller.getMobile())
                .bankDetails(seller.getBankDetails())
                .businessDetails(seller.getBusinessDetails())
                .accountStatus(AccountStatus.PENDING_VERIFICATION)
                .build());
    }

    @Override
    public Seller getSellerById(Long id) throws SellerException {
        return this.sellerRepository.findById(id)
                .orElseThrow(() -> new SellerException("Seller not found with Id: " + id));
    }

    @Override
    public Seller getSellerByEmail(String email) throws SellerException {
        final Seller seller = this.sellerRepository.findByEmail(email);
        if (Objects.isNull(seller)) {
            throw new SellerException("User not found ...");
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
        return this.sellerRepository.findByAccountStatus(status);
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {
        final Seller sellerToUpdate = this.getSellerById(id);

        if (seller.getSellerName() != null) {
            sellerToUpdate.setSellerName(seller.getSellerName());
        }

        if (seller.getMobile() != null) {
            sellerToUpdate.setMobile(seller.getMobile());
        }

        if (seller.getEmail() != null) {
            sellerToUpdate.setEmail(seller.getEmail());
        }

        if (seller.getBusinessDetails() != null &&
                seller.getBusinessDetails().getBusinessName() != null) {
            sellerToUpdate.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
        }

        if (seller.getBankDetails() != null
                && seller.getBankDetails().getAccountHolderName() != null
                && seller.getBankDetails().getIfscCode() != null
                && seller.getBankDetails().getAccountNumber() != null) {
            sellerToUpdate.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
            sellerToUpdate.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());
            sellerToUpdate.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
        }

        if (seller.getPickupAddress() != null
                && seller.getPickupAddress().getAddress() != null
                && seller.getPickupAddress().getMobile() != null
                && seller.getPickupAddress().getCity() != null
                && seller.getPickupAddress().getState() != null) {
            sellerToUpdate.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
            sellerToUpdate.getPickupAddress().setCity(seller.getPickupAddress().getCity());
            sellerToUpdate.getPickupAddress().setState(seller.getPickupAddress().getState());
            sellerToUpdate.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
        }

        if (seller.getGstin() != null) {
            sellerToUpdate.setGstin(seller.getGstin());
        }

        return sellerRepository.save(sellerToUpdate);
    }

    @Override
    public void deleteSeller(Long id) throws Exception {
        sellerRepository.delete(this.getSellerById(id));
    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {
        Seller sellerToVerify = this.getSellerByEmail(email);
        sellerToVerify.setMailVerified(true);
        return this.sellerRepository.save(sellerToVerify);
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception {
        Seller sellerToUpdate = this.getSellerById(sellerId);
        sellerToUpdate.setAccountStatus(status);
        return this.sellerRepository.save(sellerToUpdate);
    }
}
