package com.example.fptufindingmotelv1.controller.renter.managerentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.PaymentModel;
import com.example.fptufindingmotelv1.service.renter.managerentalrequest.ViewListOwnRentalRequestService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class ViewListOwnRentalRequestController {

    @Autowired
    ViewListOwnRentalRequestService viewListOwnRentalRequestService;

    @Autowired
    Environment env;

    @ResponseBody
    @RequestMapping(value = "/search-rental-request")
    public JSONObject searchRentalRequest(@RequestBody RentalRequestDTO rentalRequestDTO, @RequestParam Optional<Integer> currentPage) {
        try {
            Integer pageSize = new Integer(env.getProperty("ffm.pagination.pageSize"));
            Pageable pageable = PageRequest.of(currentPage.orElse(0), pageSize);
            return viewListOwnRentalRequestService.searchRentalRequest(rentalRequestDTO, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
