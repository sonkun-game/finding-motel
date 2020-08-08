package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.ImageModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.ReportModel;
import com.example.fptufindingmotelv1.untils.Constant;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Data
public class PostResponseDTO {
    private String id;
    private String typeId;
    private String typeName;
    private String landlordName;
    private double price;
    private double distance;
    private double square;
    private int roomNumber;
    private String createDate;
    private String description;
    private String expireDate;
    private boolean postVisible;
    private boolean banned;
    private String title;
    private String displayStatus;
    private List<RoomDTO> listRoom;
    private List<String> listImage;
    private int reportNumber;
    private boolean banAvailable;
    private String address;
    private String mapLocation;

    public PostResponseDTO(PostModel postModel) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
        this.id = postModel.getId();
        this.typeId = postModel.getType().getId().toString();
        this.typeName = postModel.getType().getName();
        this.landlordName = postModel.getLandlord().getUsername();
        this.price = postModel.getPrice();
        this.distance = postModel.getDistance();
        this.square = postModel.getSquare();
        this.roomNumber = postModel.getRoomNumber();
        this.createDate = sdf.format(postModel.getCreateDate());
        this.description = postModel.getDescription();
        this.expireDate = sdf.format(postModel.getExpireDate());
        this.postVisible = postModel.isVisible();
        this.banned = postModel.isBanned();
        this.title = postModel.getTitle();
        this.address = postModel.getAddress();
        this.mapLocation = postModel.getMapLocation();
        this.displayStatus = this.postVisible ? "Hiển thị" : "Không hiển thị";
        this.displayStatus = this.banned ? "Bị khóa" : this.displayStatus;
        this.listRoom = new ArrayList<>();
        for (int i = 0; i < postModel.getRooms().size(); i++) {
            listRoom.add(new RoomDTO(i + 1, postModel.getRooms().get(i)));
        }
        this.listImage = new ArrayList<>();
        for (ImageModel image:
             postModel.getImages()) {
            String imageUrl = "data:image/"+ image.getFileType()+";base64,"
                    + Base64.getEncoder().encodeToString(image.getFileContent());
            listImage.add(imageUrl);
        }
        this.reportNumber = 0;
        for (ReportModel report:
             postModel.getReports()) {
            if(report.getStatusReport().getId() == 3 || report.getStatusReport().getId() == 5){
                this.reportNumber ++;
            }
        }
    }
}
