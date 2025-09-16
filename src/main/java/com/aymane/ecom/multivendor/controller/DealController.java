package com.aymane.ecom.multivendor.controller;

import com.aymane.ecom.multivendor.controller.response.ApiResponse;
import com.aymane.ecom.multivendor.model.Deal;
import com.aymane.ecom.multivendor.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/admin/deals")
@RestController
public class DealController {
    private final DealService dealService;

    @PostMapping
    public ResponseEntity<Deal> createDeal(
            @RequestBody Deal deal
    ) {
        final Deal dealCreated = this.dealService.createDeal(deal);

        return new ResponseEntity<>(dealCreated, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Deal> updateDeal(
            @PathVariable Long id,
            @RequestBody Deal deal
    ) throws Exception {
        final Deal dealUpdate = this.dealService.updateDeal(deal, id);
        return ResponseEntity.ok(dealUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDeal(
            @PathVariable Long id
    ) throws Exception {
        this.dealService.deleteDeal(id);
        final ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Deal deleted");
        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }
}
