package com.example.attendancesystem.service;

import com.example.attendancesystem.entity.LeaveRecord;
import com.example.attendancesystem.vo.RecordVO;

import java.time.LocalDate;
import java.util.List;

public interface LeaveService {

    List<RecordVO<LeaveRecord>> list(String employeeNo, String name, Long departmentId, LocalDate startDate, LocalDate endDate);

    List<LeaveRecord> listByEmployeeNo(String employeeNo);

    LeaveRecord findById(Long id);

    void save(LeaveRecord record);

    void update(LeaveRecord record);

    void delete(Long id);
}
