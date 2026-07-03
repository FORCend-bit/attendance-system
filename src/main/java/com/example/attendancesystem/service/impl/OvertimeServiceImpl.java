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
        prepare(record);
        overtimeRecordMapper.insert(record);
    }

    @Override
    public void update(OvertimeRecord record) {
        prepare(record);
        overtimeRecordMapper.update(record);
    }

    @Override
    public void delete(Long id) {
        if (id != null) {
            overtimeRecordMapper.logicalDelete(id);
        }
    }

    private void prepare(OvertimeRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("\u52a0\u73ed\u8bb0\u5f55\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (record.getEmployeeId() == null) {
            throw new IllegalArgumentException("\u8bf7\u9009\u62e9\u5458\u5de5");
        }
        if (record.getOvertimeDate() == null) {
            throw new IllegalArgumentException("\u52a0\u73ed\u65e5\u671f\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (record.getStartTime() == null || record.getEndTime() == null) {
            throw new IllegalArgumentException("\u52a0\u73ed\u5f00\u59cb\u548c\u7ed3\u675f\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (record.getEndTime().isBefore(record.getStartTime())) {
            throw new IllegalArgumentException("\u52a0\u73ed\u7ed3\u675f\u65f6\u95f4\u4e0d\u80fd\u65e9\u4e8e\u5f00\u59cb\u65f6\u95f4");
        }
        record.setReason(clean(record.getReason()));
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
