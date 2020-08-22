package com.example.fptufindingmotelv1.controller.common;

import com.example.fptufindingmotelv1.model.InstructionModel;
import com.example.fptufindingmotelv1.service.common.viewinstruction.ViewInstructionService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ViewInstructionController {

    @Autowired
    ViewInstructionService viewInstructionService;

    @GetMapping("/huong-dan")
    public String viewInstruction() {
        return "instruction";
    }

    @ResponseBody
    @GetMapping("/api-get-list-instruction")
    public JSONObject getListInstruction(){
        List<InstructionModel> instructions = viewInstructionService.getListInstruction();
        return instructions != null ?
                Constant.responseMsg("000", "Success", instructions) :
                Constant.responseMsg("999", "Lỗi hệ thống!", null);
    }
}
