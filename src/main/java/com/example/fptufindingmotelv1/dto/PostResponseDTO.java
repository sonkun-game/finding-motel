package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.PostModel;
import lombok.Data;
import org.omg.CORBA.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class PostResponseDTO {
    private String dateFormat;

    private String id;
    private String typeId;
    private String typeName;
    private String landlordName;
    private String price;
    private String distance;
    private String square;
    private String roomNumber;
    private String createDate;
    private String description;
    private String expireDate;
    private boolean postVisible;
    private String title;
    private String displayStatus;

    public PostResponseDTO(PostModel postModel) {
        dateFormat = "HH:mm:ss dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        this.id = postModel.getId().toString();
        this.typeId = postModel.getType().getId().toString();
        this.typeName = postModel.getType().getName();
        this.landlordName = postModel.getLandlord().getUsername();
        this.price = postModel.getPrice() + "";
        this.distance = postModel.getDistance() + "";
        this.square = postModel.getSquare() + "";
        this.roomNumber = postModel.getRoomNumber() +"";
        this.createDate = sdf.format(postModel.getCreateDate());
        this.description = postModel.getDescription();
        this.expireDate = sdf.format(postModel.getExpireDate());
        this.postVisible = postModel.isVisible();
        this.title = postModel.getTitle();
        this.displayStatus = this.postVisible ? "Hiện" : "Ẩn";
    }
}
