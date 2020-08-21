package com.example.fptufindingmotelv1.service.manageaccount;

import com.example.fptufindingmotelv1.dto.LoginResponseDTO;
import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.LandlordRepository;
import com.example.fptufindingmotelv1.repository.RentalRequestRepository;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ViewAccountInformationServiceImpl implements ViewAccountInformationService {

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    LandlordRepository landlordRepository;

    @Autowired
    RentalRequestRepository rentalRequestRepository;

    @Override
    public LoginResponseDTO getUserInformation(UserModel userModel, LoginResponseDTO responseDTO) {
        try {
            if(userModel.getRole().getId() == 1){
                userModel = renterRepository.getRenterByUsername(userModel.getUsername());
            }else if(userModel.getRole().getId() == 2){
                userModel = landlordRepository.getLandlordByUsername(userModel.getUsername());
            }
            UserDTO userDTO = new UserDTO(userModel);
            if(userModel instanceof LandlordModel){
                int countRequest = rentalRequestRepository.getRequestNumber(userModel.getUsername(), 7L);
                userDTO.setRequestNumber(countRequest);
            }
            responseDTO.setUserDTO(userDTO);
            return responseDTO;
        }catch (Exception e){
            e.printStackTrace();
            responseDTO.setMsgCode("999");
            responseDTO.setMessage("Lỗi hệ thống!");
            return responseDTO;
        }
    }
}
