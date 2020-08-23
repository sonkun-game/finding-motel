package com.example.fptufindingmotelv1.service.admin.manageuser;

import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.ReportModel;
import com.example.fptufindingmotelv1.model.StatusModel;
import com.example.fptufindingmotelv1.repository.LandlordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Service
public class BanUnbanLandlordServiceImpl implements BanUnbanLandlordService{
    @Autowired
    LandlordRepository landlordRepository;

    @Override
    public LandlordModel banLandlord(String username) {
        try {
            LandlordModel landlord = landlordRepository.findByUsername(username);
            if (landlord == null) {
                return null;
            } else {
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                dateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Ho_Chi_Minh")));

                String currentDate = dateFormat.format(new Date());
                Date date = dateFormat.parse(currentDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, 14);

                landlord.setUnBanDate(calendar.getTime());
                for (PostModel post :
                        landlord.getPosts()) {
                    post.setVisible(false);
                    for (ReportModel report :
                            post.getReports()) {
                        if (report.getStatusReport().getId() == 3) {
                            StatusModel statusModel = new StatusModel(5L);
                            report.setStatusReport(statusModel);
                        } else if (report.getStatusReport().getId() == 4) {
                            StatusModel statusModel = new StatusModel(6L);
                            report.setStatusReport(statusModel);
                        }
                    }
                }
                return landlordRepository.save(landlord);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public LandlordModel unbanLandlord(String username) {
        try {
            LandlordModel landlord = landlordRepository.findByUsername(username);
            if (landlord == null) {
                return null;
            } else {
                landlord.setUnBanDate(null);
                for (PostModel post :
                        landlord.getPosts()) {
                    post.setVisible(true);
                }
                return landlordRepository.save(landlord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
