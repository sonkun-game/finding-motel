package com.example.fptufindingmotelv1.controller.admin.managepaymentpackage;

import com.example.fptufindingmotelv1.dto.PaymentPackageDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.repository.PaymentPackageRepository;
import com.example.fptufindingmotelv1.service.admin.managepaymentpackage.ViewListPaymentPackageService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ViewListPaymentPackageController {
    @Autowired
    ViewListPaymentPackageService viewListPaymentPackageService;

    @Autowired
    Environment env;

    @Autowired
    PaymentPackageRepository paymentPackageRepository;

    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }

    public JSONObject paginationModel(Page page) {
        JSONObject msg = new JSONObject();
        msg.put("totalPages", page.getTotalPages());
        msg.put("sizePage", page.getSize());
        msg.put("currentPage", page.getNumber());
        msg.put("totalItems", page.getTotalElements());
        msg.put("hasNext", page.hasNext());
        msg.put("hasPrevious", page.hasPrevious());
        return msg;
    }

    @ResponseBody
    @RequestMapping(value = "/api-get-all-payment-package")
    public JSONObject getAllPaymentPacket(@RequestParam Optional<Integer> currentPage) {
        try {
            Integer pageSize = new Integer(env.getProperty("ffm.pagination.pageSize"));
            Pageable pageable = PageRequest.of(currentPage.orElse(0), pageSize);
            Page<PaymentPackageModel> allPaymentPackage = paymentPackageRepository.getAllPaymentPackage(pageable);
            List<PaymentPackageDTO> paymentPackageDTOS = new ArrayList<>();
            for (PaymentPackageModel paymentPackage :
                    allPaymentPackage.getContent()) {
                paymentPackageDTOS.add(new PaymentPackageDTO(paymentPackage));
            }
            JSONObject response = responseMsg("000", "Success!", paymentPackageDTOS);
            response.put("pagination", paginationModel(allPaymentPackage));
            return response;
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống!", null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/api-get-list-payment-package")
    public JSONObject getListPaymentPackage() {
        try {
            List<PaymentPackageModel> paymentPackageModels = viewListPaymentPackageService.getListPaymentPackage(null);
            List<PaymentPackageDTO> response = new ArrayList<>();
            for (PaymentPackageModel paymentPackage :
                    paymentPackageModels) {
                response.add(new PaymentPackageDTO(paymentPackage));
            }
            return paymentPackageModels != null
                    ? responseMsg("000", "Success!", response)
                    : responseMsg("999", "Lỗi hệ thống", null);
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
