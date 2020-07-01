package com.example.fptufindingmotelv1.controller.landlord;

import com.example.fptufindingmotelv1.dto.ImageDTO;
import com.example.fptufindingmotelv1.dto.PaymentPackageDTO;
import com.example.fptufindingmotelv1.dto.TypePostDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.model.TypeModel;
import com.example.fptufindingmotelv1.service.landlord.ManagePostService;
import net.minidev.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Controller
public class ManagePostController {

    @Autowired
    private ManagePostService managePostService;

    @ResponseBody
    @PostMapping("/api-get-init-new-post")
    public JSONObject getInitNewPost(){
        JSONObject response = new JSONObject();
        List<PaymentPackageModel> paymentPackages = managePostService.getListPaymentPackage();
        if(paymentPackages != null){
            List<PaymentPackageDTO> paymentPackageDTOS = new ArrayList<>();
            for (PaymentPackageModel paymentPackage:
                    paymentPackages) {
                paymentPackageDTOS.add(new PaymentPackageDTO(paymentPackage));
            }
            response.put("listPaymentPackage", paymentPackageDTOS);
        }
        List<TypeModel> typePosts = managePostService.getListTypePost();
        if(typePosts != null){
            List<TypePostDTO> typePostDTOS = new ArrayList<>();
            for (TypeModel typeModel:
                    typePosts) {
                typePostDTOS.add(new TypePostDTO(typeModel));
            }
            response.put("listTypePost", typePostDTOS);
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/upload")
        public ResponseEntity uploadToLocalFileSystem(@RequestParam("file") MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path path = Paths.get("src/main/resources/static/assets/img/rooms/");
        try {
            Files.copy(file.getInputStream(), path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/assets/img/rooms/")
                    .path(fileName)
                    .toUriString();
            return ResponseEntity.ok(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @ResponseBody
    @PostMapping("/api-upload-image")
    public ResponseEntity uploadFile(@RequestBody ImageDTO imageDTO) {
        String imgBase64 = imageDTO.getImgBase64();
        StringBuffer fileName = new StringBuffer();
        fileName.append(UUID.randomUUID().toString().replaceAll("-", ""));
        if (StringUtils.isEmpty(imgBase64)) {
            return null;
        } else if (imgBase64.indexOf("data:image/png;") != -1) {
            imgBase64 = imgBase64.replace("data:image/png;base64,", "");
            fileName.append(".png");
        } else if (imgBase64.indexOf("data:image/jpeg;") != -1) {
            imgBase64 = imgBase64.replace("data:image/jpeg;base64,", "");
            fileName.append(".jpeg");
        } else {
            return null;
        }
        File file = new File("src/main/resources/static/assets/img/rooms/", fileName.toString());
        byte[] fileBytes = Base64.getDecoder().decode(imgBase64);
        try {
            FileUtils.writeByteArrayToFile(file, fileBytes);
            return ResponseEntity.ok("Thành công");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @ResponseBody
    @PostMapping("/multi-upload")
    public ResponseEntity multiUpload(@RequestParam("files") MultipartFile[] files) {
        List<Object> fileDownloadUrls = new ArrayList<>();
        Arrays.asList(files)
                .stream()
                .forEach(file -> fileDownloadUrls.add(uploadToLocalFileSystem(file).getBody()));
        return ResponseEntity.ok(fileDownloadUrls);
    }
}
