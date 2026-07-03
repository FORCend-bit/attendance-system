package com.example.attendancesystem.service;

import com.example.attendancesystem.entity.AttendanceRecord;
import com.example.attendancesystem.vo.RecordVO;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    List<RecordVO<AttendanceRecord>> list(String employeeNo, String name, Long departmentId, LocalDate startDate, LocalDate endDate);

    List<AttendanceRecord> listByEmployeeNo(String employeeNo);

    AttendanceRecord findById(Long id);

    void save(AttendanceRecord record);

    void update(AttendanceRecord record);

    void delete(Long id);
}
