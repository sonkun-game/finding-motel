package com.example.fptufindingmotelv1.service.common.viewinstruction;

import com.example.fptufindingmotelv1.model.InstructionModel;
import com.example.fptufindingmotelv1.repository.InstructionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewInstructionServiceImpl implements ViewInstructionService {

    @Autowired
    InstructionRepository instructionRepository;

    @Override
    public List<InstructionModel> getListInstruction() {
        try {
            return instructionRepository.getAllInstruction();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
