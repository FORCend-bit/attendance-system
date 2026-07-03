package com.example.attendancesystem.service.impl;

import com.example.attendancesystem.entity.AttendanceRecord;
import com.example.attendancesystem.entity.Employee;
import com.example.attendancesystem.mapper.AttendanceRecordMapper;
import com.example.attendancesystem.mapper.EmployeeMapper;
import com.example.attendancesystem.service.AttendanceService;
import com.example.attendancesystem.vo.RecordVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private static final LocalTime WORK_START = LocalTime.of(9, 0);
    private static final LocalTime WORK_END = LocalTime.of(18, 0);

    private final AttendanceRecordMapper attendanceRecordMapper;
    private final EmployeeMapper employeeMapper;

    public AttendanceServiceImpl(AttendanceRecordMapper attendanceRecordMapper, EmployeeMapper employeeMapper) {
        this.attendanceRecordMapper = attendanceRecordMapper;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public List<RecordVO<AttendanceRecord>> list(String employeeNo, String name, Long departmentId, LocalDate startDate, LocalDate endDate) {
        List<RecordVO<AttendanceRecord>> list = attendanceRecordMapper.findAll(clean(employeeNo), clean(name), departmentId, startDate, endDate);
        return list == null ? Collections.emptyList() : list;
    }

    @Override
    public List<AttendanceRecord> listByEmployeeNo(String employeeNo) {
        Employee employee = employeeMapper.findByEmployeeNo(clean(employeeNo));
        if (employee == null) {
            return Collections.emptyList();
        }
        List<AttendanceRecord> list = attendanceRecordMapper.findByEmployeeId(employee.getId());
        return list == null ? Collections.emptyList() : list;
    }

    @Override
    public AttendanceRecord findById(Long id) {
        return id == null ? null : attendanceRecordMapper.findById(id);
    }

    @Override
    public void save(AttendanceRecord record) {
        prepare(record);
        attendanceRecordMapper.insert(record);
    }

    @Override
    public void update(AttendanceRecord record) {
        prepare(record);
        attendanceRecordMapper.update(record);
    }

    @Override
    public void delete(Long id) {
        if (id != null) {
            attendanceRecordMapper.logicalDelete(id);
        }
    }

    private void prepare(AttendanceRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("出勤记录不能为空");
        }
        if (record.getAttendanceDate() == null) {
            record.setAttendanceDate(LocalDate.now());
        }
        if (record.getCheckInTime() == null && record.getCheckOutTime() == null) {
            record.setStatus("缺勤");
        } else if (record.getCheckInTime() != null && record.getCheckInTime().toLocalTime().isAfter(WORK_START)) {
            record.setStatus("迟到");
        } else if (record.getCheckOutTime() != null && record.getCheckOutTime().toLocalTime().isBefore(WORK_END)) {
            record.setStatus("早退");
        } else {
            record.setStatus("正常");
        }
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
