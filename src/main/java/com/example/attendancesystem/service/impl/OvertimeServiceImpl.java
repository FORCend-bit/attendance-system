package com.example.attendancesystem.service.impl;

import com.example.attendancesystem.entity.Employee;
import com.example.attendancesystem.entity.OvertimeRecord;
import com.example.attendancesystem.mapper.EmployeeMapper;
import com.example.attendancesystem.mapper.OvertimeRecordMapper;
import com.example.attendancesystem.service.OvertimeService;
import com.example.attendancesystem.vo.RecordVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class OvertimeServiceImpl implements OvertimeService {

    private final OvertimeRecordMapper overtimeRecordMapper;
    private final EmployeeMapper employeeMapper;

    public OvertimeServiceImpl(OvertimeRecordMapper overtimeRecordMapper, EmployeeMapper employeeMapper) {
        this.overtimeRecordMapper = overtimeRecordMapper;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public List<RecordVO<OvertimeRecord>> list(String employeeNo, String name, Long departmentId, LocalDate startDate, LocalDate endDate) {
        List<RecordVO<OvertimeRecord>> list = overtimeRecordMapper.findAll(clean(employeeNo), clean(name), departmentId, startDate, endDate);
        return list == null ? Collections.emptyList() : list;
    }

    @Override
    public List<OvertimeRecord> listByEmployeeNo(String employeeNo) {
        Employee employee = employeeMapper.findByEmployeeNo(clean(employeeNo));
        if (employee == null) {
            return Collections.emptyList();
        }
        List<OvertimeRecord> list = overtimeRecordMapper.findByEmployeeId(employee.getId());
        return list == null ? Collections.emptyList() : list;
    }

    @Override
    public OvertimeRecord findById(Long id) {
        return id == null ? null : overtimeRecordMapper.findById(id);
    }

    @Override
    public void save(OvertimeRecord record) {
        overtimeRecordMapper.insert(record);
    }

    @Override
    public void update(OvertimeRecord record) {
        overtimeRecordMapper.update(record);
    }

    @Override
    public void delete(Long id) {
        if (id != null) {
            overtimeRecordMapper.logicalDelete(id);
        }
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
