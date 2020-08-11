package com.example.fptufindingmotelv1.service.payment;

import com.example.fptufindingmotelv1.dto.MomoResponseDTO;
import com.example.fptufindingmotelv1.dto.MomoTransactionStatusRequestDTO;
import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.MomoModel;
import com.example.fptufindingmotelv1.model.PaymentModel;
import com.example.fptufindingmotelv1.repository.LandlordRepository;
import com.example.fptufindingmotelv1.repository.PaymentRepository;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    private Environment env;

    @Autowired
    LandlordRepository landlordRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public JSONObject savePayment(MomoResponseDTO momoResponseDTO) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
                CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
                //Get username of landlord
                LandlordModel landlordModel = landlordRepository.findByUsername(userDetails.getUsername());
                if (momoResponseDTO.getErrorCode().equalsIgnoreCase("0")) {

                    jsonObject.put("addAmount", momoResponseDTO.getAmount());
                    //save payment
                    PaymentModel paymentModel = new PaymentModel();
                    paymentModel.setAmount(Float.parseFloat(momoResponseDTO.getAmount()));
                    paymentModel.setLandlordModel(landlordModel);
                    paymentModel.setMomoId(momoResponseDTO.getOrderId());
                    paymentModel.setPaymentMethod("Momo");
                    paymentModel.setPayDate(new Date());
                    paymentRepository.save(paymentModel);
                    //update landlord amount
                    float amount = landlordModel.getAmount();
                    amount += Float.parseFloat(momoResponseDTO.getAmount());
                    landlordModel.setAmount(amount);
                    landlordRepository.save(landlordModel);
                    jsonObject.put("code", "000");
                    jsonObject.put("message", "Nạp tiền thành công");
                    jsonObject.put("landlordAmount", landlordModel.getAmount());
                } else {
                    jsonObject.put("code", "001");
                    jsonObject.put("message", "Nap tien khong thanh cong. - errorCode: " + momoResponseDTO.getErrorCode());
                }

            }
        } catch (Exception e) {
            jsonObject.put("code", "999");
            jsonObject.put("message", "Loi he thong!");
        }
        return jsonObject;
    }

    @Override
    public JSONObject requestMomoPayment(String amount) {
        String partnerCode = env.getProperty("momo.partnerCode");
        String accessKey = env.getProperty("momo.accessKey");
        String requestType = env.getProperty("momo.requestType");
        String requestId = "request_" + createUniqueID();
        String orderId = "order_" + createUniqueID();
        String orderInfo = "Momo pay for user";
        String notifyUrl = env.getProperty("server.hosting.url") + "/payment-momo";
        String returnUrl = env.getProperty("server.hosting.url") + "/payment-momo";
        String extraData = "";
        String rawSign = "partnerCode=" + partnerCode + "&accessKey=" + accessKey
                + "&requestId=" + requestId + "&amount=" + amount + "&orderId=" + orderId
                + "&orderInfo=" + orderInfo + "&returnUrl=" + returnUrl + "&notifyUrl=" + notifyUrl
                + "&extraData=" + extraData;

        String signature = getSignature(rawSign);
        MomoModel momoModel = new MomoModel(partnerCode, accessKey, requestType, requestId, amount, orderId, orderInfo
                , returnUrl, notifyUrl, extraData, signature);
        //call momo request payment api
        final String uri = env.getProperty("momo.pay.url");
        RestTemplate restTemplate = new RestTemplate();
        try {
            String result = restTemplate.postForObject(uri, momoModel ,String.class);
            JSONParser parser = new JSONParser();
            JSONObject momoResponse = (JSONObject) parser.parse(result);
            if (momoResponse.get("errorCode").toString().equals(env.getProperty("momo.errorCode.success"))) {
                JSONObject response = responseMsg("000", momoResponse.get("localMessage").toString(), null);
                response.put("momoPayUrl", momoResponse.get("payUrl"));
                return response;
            } else {
                return responseMsg("999", momoResponse.get("localMessage").toString(), null);
            }
        } catch (Exception e) {
            return responseMsg("998", e.getMessage(), null);
        }
    }

    @Override
    public JSONObject validateAndSaveMomoPayment(MomoResponseDTO momoResponseDTO) {
        MomoTransactionStatusRequestDTO momoTransactionStatusRequestDTO = new MomoTransactionStatusRequestDTO();
        momoTransactionStatusRequestDTO.setPartnerCode(momoResponseDTO.getPartnerCode());
        momoTransactionStatusRequestDTO.setAccessKey(momoResponseDTO.getAccessKey());
        momoTransactionStatusRequestDTO.setRequestId(momoResponseDTO.getRequestId());
        momoTransactionStatusRequestDTO.setOrderId(momoResponseDTO.getOrderId());
        momoTransactionStatusRequestDTO.setRequestType(env.getProperty("momo.transaction.requestType"));
        String rawSign = "partnerCode=" + momoTransactionStatusRequestDTO.getPartnerCode()
                + "&accessKey=" + momoTransactionStatusRequestDTO.getAccessKey()
                + "&requestId=" + momoTransactionStatusRequestDTO.getRequestId()
                + "&orderId=" + momoTransactionStatusRequestDTO.getOrderId()
                + "&requestType=" + momoTransactionStatusRequestDTO.getRequestType();
        momoTransactionStatusRequestDTO.setSignature(getSignature(rawSign));
        //call momo request payment api
        final String uri = env.getProperty("momo.pay.url");
        RestTemplate restTemplate = new RestTemplate();
        try {
            String result = restTemplate.postForObject(uri, momoTransactionStatusRequestDTO ,String.class);
            JSONParser parser = new JSONParser();
            JSONObject momoResponse = (JSONObject) parser.parse(result);
            if (momoResponse.get("errorCode").toString().equals(env.getProperty("momo.errorCode.success"))) {
                JSONObject response = responseMsg("000", momoResponse.get("localMessage").toString(), null);
                response.put("momoPayUrl", momoResponse.get("payUrl"));
                return savePayment(momoResponseDTO);
            } else {
                return responseMsg("999", momoResponse.get("localMessage").toString(), null);
            }
        } catch (Exception e) {
            return responseMsg("998", e.getMessage(), null);
        }
    }

    @Override
    public List<PaymentModel> getPaymentsByLandlord(PaymentDTO paymentDTO) {
        try {
            List<PaymentModel> paymentModels =
                    paymentRepository.getPaymentByLandlord(paymentDTO.getLandlord());
            return paymentModels;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getSignature(String rawSign) {
        String secretKey = env.getProperty("momo.secretKey");
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            sha256_HMAC.init(new SecretKeySpec(secretKey.getBytes(), "HmacSHA256"));
            byte[] result = sha256_HMAC.doFinal(rawSign.getBytes());
            return bytesToHex(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String createUniqueID() {
        return "ffm_" + UUID.randomUUID().toString();
    }

    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }

}
