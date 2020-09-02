package com.example.fptufindingmotelv1.service.admin.manageuser;

import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.ReportModel;
import com.example.fptufindingmotelv1.model.StatusModel;
import com.example.fptufindingmotelv1.repository.LandlordRepository;
import com.example.fptufindingmotelv1.repository.PostRepository;
import com.example.fptufindingmotelv1.repository.ReportRepository;
import com.example.fptufindingmotelv1.untils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Service
public class BanUnbanLandlordModel implements BanUnbanLandlordService{
    @Autowired
    LandlordRepository landlordRepository;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    PostRepository postRepository;

    @Override
    public boolean banLandlord(String username) {
        try {
            if (username == null && username.isEmpty()) {
                return false;
            } else {
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                dateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Ho_Chi_Minh")));

                String currentDate = dateFormat.format(new Date());
                Date date = dateFormat.parse(currentDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, Constant.BAN_USER_DATE_NUMBER);

                landlordRepository.updateUnBanDate(calendar.getTime(), username);
                postRepository.updateVisiblePostByLandlord(false, username);
                reportRepository.updateStatusReportByLandlord(username,
                        Constant.STATUS_REPORT_PROCESSED_USER, Constant.STATUS_REPORT_PROCESSED_ALL);

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean unbanLandlord(String username) {
        try {
            if (username == null && username.isEmpty()) {
                return false;
            } else {
                landlordRepository.updateUnBanDate(null, username);
                postRepository.updateVisiblePostByLandlord(true, username);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
