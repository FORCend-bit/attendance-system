package com.example.attendancesystem.service;

import com.example.attendancesystem.entity.OvertimeRecord;
import com.example.attendancesystem.vo.RecordVO;

import java.time.LocalDate;
import java.util.List;

public interface OvertimeService {

    List<RecordVO<OvertimeRecord>> list(String employeeNo, String name, Long departmentId, LocalDate startDate, LocalDate endDate);

    List<OvertimeRecord> listByEmployeeNo(String employeeNo);

    OvertimeRecord findById(Long id);

    void save(OvertimeRecord record);

    void update(OvertimeRecord record);

    void delete(Long id);
}
