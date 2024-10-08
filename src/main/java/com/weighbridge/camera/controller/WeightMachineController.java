package com.weighbridge.camera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weighbridge.admin.entities.UserMaster;
import com.weighbridge.admin.exceptions.ResourceNotFoundException;
import com.weighbridge.admin.repsitories.UserMasterRepository;

@RestController
@RequestMapping("/api/weight")
public class WeightMachineController
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserMasterRepository userMasterRepository;

    @GetMapping("/latest-weight")
    public ResponseEntity<String> getLatestWeight(@RequestParam String userId) {
        System.out.println("-----------");
        if(userId==null){
            throw new ResourceNotFoundException("User id is null: "+userId);
        }
        UserMaster user = userMasterRepository.findByUserId(userId);
        if(user==null){
            throw new ResourceNotFoundException("User Id is not found: "+userId);
        }
        String company = user.getCompany().getCompanyId();
        String site = user.getSite().getSiteId();
        String latestWeight = fetchLatestWeightFromDatabase(company, site);

        if (latestWeight != null ) {
            return ResponseEntity.ok(latestWeight);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    private String fetchLatestWeightFromDatabase(String company, String site) {
        System.out.println(company+"----"+site);
        try {
            String sql = "SELECT weight FROM meter_data WHERE company = ? AND site = ? ORDER BY timestamp DESC LIMIT 1";
            return jdbcTemplate.queryForObject(sql, new Object[]{company, site}, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
