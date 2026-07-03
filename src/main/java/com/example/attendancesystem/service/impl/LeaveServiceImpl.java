package com.example.attendancesystem.service.impl;

import com.example.attendancesystem.entity.Employee;
import com.example.attendancesystem.entity.LeaveRecord;
import com.example.attendancesystem.mapper.EmployeeMapper;
import com.example.attendancesystem.mapper.LeaveRecordMapper;
import com.example.attendancesystem.service.LeaveService;
import com.example.attendancesystem.vo.RecordVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRecordMapper leaveRecordMapper;
    private final EmployeeMapper employeeMapper;

    public LeaveServiceImpl(LeaveRecordMapper leaveRecordMapper, EmployeeMapper employeeMapper) {
        this.leaveRecordMapper = leaveRecordMapper;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public List<RecordVO<LeaveRecord>> list(String employeeNo, String name, Long departmentId, LocalDate startDate, LocalDate endDate) {
        List<RecordVO<LeaveRecord>> list = leaveRecordMapper.findAll(clean(employeeNo), clean(name), departmentId, startDate, endDate);
        return list == null ? Collections.emptyList() : list;
    }

    @Override
    public List<LeaveRecord> listByEmployeeNo(String employeeNo) {
        Employee employee = employeeMapper.findByEmployeeNo(clean(employeeNo));
        if (employee == null) {
            return Collections.emptyList();
        }
        List<LeaveRecord> list = leaveRecordMapper.findByEmployeeId(employee.getId());
        return list == null ? Collections.emptyList() : list;
    }

    @Override
    public LeaveRecord findById(Long id) {
        return id == null ? null : leaveRecordMapper.findById(id);
    }

    @Override
    public void save(LeaveRecord record) {
        prepare(record);
        leaveRecordMapper.insert(record);
    }

    @Override
    public void update(LeaveRecord record) {
        prepare(record);
        leaveRecordMapper.update(record);
    }

    @Override
    public void delete(Long id) {
        if (id != null) {
            leaveRecordMapper.logicalDelete(id);
        }
    }

    private void prepare(LeaveRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("\u8bf7\u5047\u8bb0\u5f55\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (record.getEmployeeId() == null) {
            throw new IllegalArgumentException("\u8bf7\u9009\u62e9\u5458\u5de5");
        }
        if (clean(record.getLeaveType()) == null) {
            throw new IllegalArgumentException("\u8bf7\u5047\u7c7b\u578b\u4e0d\u80fd\u4e3a\u7a7a");
        }
        record.setLeaveType(clean(record.getLeaveType()));
        record.setReason(clean(record.getReason()));
        if (record.getStartTime() == null || record.getEndTime() == null) {
            throw new IllegalArgumentException("\u8bf7\u5047\u5f00\u59cb\u548c\u7ed3\u675f\u65f6\u95f4\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (record.getEndTime().isBefore(record.getStartTime())) {
            throw new IllegalArgumentException("\u8bf7\u5047\u7ed3\u675f\u65f6\u95f4\u4e0d\u80fd\u65e9\u4e8e\u5f00\u59cb\u65f6\u95f4");
        }
        if (record.getStatus() == null || record.getStatus().isBlank()) {
            record.setStatus("\u5df2\u8bb0\u5f55");
        }
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
