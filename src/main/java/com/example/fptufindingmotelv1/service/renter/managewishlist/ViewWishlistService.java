package com.example.fptufindingmotelv1.service.renter.managewishlist;

import net.minidev.json.JSONObject;
import org.springframework.data.domain.Pageable;

public interface ViewWishlistService {
    JSONObject getWishlist(Pageable pageable);
}
