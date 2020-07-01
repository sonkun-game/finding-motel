package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.TypeModel;
import lombok.Data;

@Data
public class TypePostDTO {
    private long id;
    private String name;

    public TypePostDTO(TypeModel typeModel) {
        this.id = typeModel.getId();
        this.name = typeModel.getName();
    }
}
