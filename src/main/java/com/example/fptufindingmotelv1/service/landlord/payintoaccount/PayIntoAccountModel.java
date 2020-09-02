package com.example.fptufindingmotelv1.service.landlord.payintoaccount;

import com.example.fptufindingmotelv1.dto.MomoResponseDTO;
import com.example.fptufindingmotelv1.dto.MomoTransactionStatusRequestDTO;
import com.example.fptufindingmotelv1.dto.VnpayRequestDTO;
import com.example.fptufindingmotelv1.dto.VnpayResponseDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.MomoModel;
import com.example.fptufindingmotelv1.model.PaymentModel;
import com.example.fptufindingmotelv1.repository.LandlordRepository;
import com.example.fptufindingmotelv1.repository.PaymentPostRepository;
import com.example.fptufindingmotelv1.repository.PaymentRepository;
import com.example.fptufindingmotelv1.untils.Constant;
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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class PayIntoAccountModel implements PayIntoAccountService {

    @Autowired
    private Environment env;

    @Autowired
    LandlordRepository landlordRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PaymentPostRepository paymentPostRepository;

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
                    paymentModel.setPaymentTransaction(momoResponseDTO.getOrderId());
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
                    jsonObject.put("message", "Nạp tiền không thành công - errorCode: " + momoResponseDTO.getErrorCode());
                }

            }
        } catch (Exception e) {
            jsonObject.put("code", "999");
            jsonObject.put("message", "Lỗi hệ thống");
        }
        return jsonObject;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public JSONObject savePaymentVnpay(VnpayResponseDTO vnpayResponseDTO) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
                CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
                //Get username of landlord
                LandlordModel landlordModel = landlordRepository.findByUsername(userDetails.getUsername());
                if (vnpayResponseDTO.getVnp_ResponseCode().equalsIgnoreCase("00")) {

                    jsonObject.put("addAmount", vnpayResponseDTO.getVnp_Amount());
                    //save payment
                    PaymentModel paymentModel = new PaymentModel();
                    paymentModel.setAmount(vnpayResponseDTO.getVnp_Amount()/100);
                    paymentModel.setLandlordModel(landlordModel);
                    paymentModel.setPaymentTransaction(vnpayResponseDTO.getVnp_TxnRef());
                    paymentModel.setPaymentMethod("VnPay");
                    paymentModel.setPayDate(new Date());
                    paymentRepository.save(paymentModel);
                    //update landlord amount
                    float amount = landlordModel.getAmount();
                    amount += vnpayResponseDTO.getVnp_Amount()/100;
                    landlordModel.setAmount(amount);
                    landlordRepository.save(landlordModel);
                    jsonObject.put("code", "000");
                    jsonObject.put("message", "Nạp tiền thành công");
                    jsonObject.put("landlordAmount", landlordModel.getAmount());
                } else {
                    jsonObject.put("code", "001");
                    jsonObject.put("message", "Nạp tiền không thành công - errorCode: " + vnpayResponseDTO.getVnp_ResponseCode());
                }

            }
        } catch (Exception e) {
            jsonObject.put("code", "999");
            jsonObject.put("message", "Lỗi hệ thống");
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
            String result = restTemplate.postForObject(uri, momoModel, String.class);
            JSONParser parser = new JSONParser();
            JSONObject momoResponse = (JSONObject) parser.parse(result);
            if (momoResponse.get("errorCode").toString().equals(env.getProperty("momo.errorCode.success"))) {
                JSONObject response = Constant.responseMsg("000", momoResponse.get("localMessage").toString(), null);
                response.put("momoPayUrl", momoResponse.get("payUrl"));
                return response;
            } else {
                return Constant.responseMsg("999", momoResponse.get("localMessage").toString(), null);
            }
        } catch (Exception e) {
            return Constant.responseMsg("998", e.getMessage(), null);
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
            String result = restTemplate.postForObject(uri, momoTransactionStatusRequestDTO, String.class);
            JSONParser parser = new JSONParser();
            JSONObject momoResponse = (JSONObject) parser.parse(result);
            if (momoResponse.get("errorCode").toString().equals(env.getProperty("momo.errorCode.success"))) {
                JSONObject response = Constant.responseMsg("000", momoResponse.get("localMessage").toString(), null);
                response.put("momoPayUrl", momoResponse.get("payUrl"));
                return savePayment(momoResponseDTO);
            } else {
                return Constant.responseMsg("999", momoResponse.get("localMessage").toString(), null);
            }
        } catch (Exception e) {
            return Constant.responseMsg("998", e.getMessage(), null);
        }
    }

    @Override
    public JSONObject validateAndSaveVnpayPayment(VnpayResponseDTO vnpayResponseDTO) {
        try {
            Map<String, String> vnp_Params = new HashMap<>();
            for (Field field : vnpayResponseDTO.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                String fieldValue = field.get(vnpayResponseDTO).toString();
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    vnp_Params.put(fieldName, fieldValue);
                }
            }
            if (vnp_Params.containsKey("vnp_SecureHashType")) {
                vnp_Params.remove("vnp_SecureHashType");
            }
            if (vnp_Params.containsKey("vnp_SecureHash")) {
                vnp_Params.remove("vnp_SecureHash");
            }

            String signValue = hashAllFields(vnp_Params);
            if (signValue.equals(vnpayResponseDTO.getVnp_SecureHash())) {
                //Kiem tra chu ky OK
                /* Kiem tra trang thai don hang trong DB: checkOrderStatus,
                - Neu trang thai don hang OK, tien hanh cap nhat vao DB, tra lai cho VNPAY RspCode=00
                - Neu trang thai don hang (da cap nhat roi) => khong cap nhat vao DB, tra lai cho VNPAY RspCode=02
                */
                if ("00".equals(vnpayResponseDTO.getVnp_ResponseCode())) {
                    //Xu ly thanh toan thanh cong
                    // out.print("GD Thanh cong");
                    return savePaymentVnpay(vnpayResponseDTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constant.responseMsg("995", "Thanh toán không thành công", null);
    }

    @Override
    public JSONObject requestVnpayPayment(VnpayRequestDTO vnpayRequestDTO) {
        try {
            String vnp_Version = "2.0.0";
            String vnp_Command = "pay";
            String vnp_OrderInfo = vnpayRequestDTO.getVnpayContent();
            String orderType = "250000";
            String vnp_TxnRef = "vnpay_" + createUniqueID();
            String vnp_IpAddr = InetAddress.getLocalHost().getHostAddress();

            String vnp_TmnCode = "FG6VK38F";

            String vnp_TransactionNo = vnp_TxnRef;
            String vnp_hashSecret = "AQTAENKJJBTBDEAVHUWGCOFNTAVOGEQW";

            Long amount = vnpayRequestDTO.getVnpayAmount() * 100;
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");

            String bank_code = vnpayRequestDTO.getVnpayBank();
            if (bank_code != null && bank_code.isEmpty()) {
                vnp_Params.put("vnp_BankCode", bank_code);
            }
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
            vnp_Params.put("vnp_OrderType", orderType);

            String locate = "vi";
            if (locate != null && !locate.isEmpty()) {
                vnp_Params.put("vnp_Locale", locate);
            } else {
                vnp_Params.put("vnp_Locale", "vn");
            }
            vnp_Params.put("vnp_ReturnUrl", env.getProperty("server.hosting.url") + "/api-check-sum-vnpay-payment");
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            Date dt = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(dt);
            String vnp_CreateDate = dateString;
            String vnp_TransDate = vnp_CreateDate;
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            //Build data to hash and querystring
            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(fieldValue);
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = Sha256(vnp_hashSecret + hashData.toString());
            String vnp_PayUrl = "http://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
            //System.out.println("HashData=" + hashData.toString());
            queryUrl += "&vnp_SecureHashType=SHA256&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = vnp_PayUrl + "?" + queryUrl;
            JSONObject response = Constant.responseMsg("000", "Thành công", null);
            response.put("vnpayUrl", paymentUrl);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.responseMsg("999", "Lỗi hệ thống", null);
        }
    }

    public static String Sha256(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(message.getBytes("UTF-8"));

            // converting byte array to Hexadecimal String
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }

            digest = sb.toString();

        } catch (UnsupportedEncodingException ex) {
            digest = "";
            // Logger.getLogger(StringReplace.class.getName()).log(Level.SEVERE,
            // null, ex);
        } catch (NoSuchAlgorithmException ex) {
            // Logger.getLogger(StringReplace.class.getName()).log(Level.SEVERE,
            // null, ex);
            digest = "";
        }
        return digest;
    }

    //Util for VNPAY
    public static String hashAllFields(Map fields) throws UnsupportedEncodingException {
        // create a list and sort it
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
        // create a buffer for the md5 input and add the secure secret first
        StringBuilder sb = new StringBuilder();
        String vnp_hashSecret = "AQTAENKJJBTBDEAVHUWGCOFNTAVOGEQW";
        sb.append(vnp_hashSecret);
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                sb.append(fieldName);
                sb.append("=");
                sb.append(URLDecoder.decode(fieldValue, "UTF-8"));
            }
            if (itr.hasNext()) {
                sb.append("&");
            }
        }
        return Sha256(sb.toString());
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

}
