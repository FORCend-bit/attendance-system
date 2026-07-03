package com.example.attendancesystem.service;

import com.example.attendancesystem.entity.BusinessTripRecord;
import com.example.attendancesystem.vo.RecordVO;

import java.time.LocalDate;
import java.util.List;

public interface BusinessTripService {

    List<RecordVO<BusinessTripRecord>> list(String employeeNo, String name, Long departmentId, LocalDate startDate, LocalDate endDate);

    List<BusinessTripRecord> listByEmployeeNo(String employeeNo);

    BusinessTripRecord findById(Long id);

    void save(BusinessTripRecord record);

    void update(BusinessTripRecord record);

    void delete(Long id);
}
