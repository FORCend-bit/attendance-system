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
            throw new IllegalArgumentException("\u51fa\u52e4\u8bb0\u5f55\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (record.getEmployeeId() == null) {
            throw new IllegalArgumentException("\u8bf7\u9009\u62e9\u5458\u5de5");
        }
        if (record.getAttendanceDate() == null) {
            throw new IllegalArgumentException("\u51fa\u52e4\u65e5\u671f\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (record.getCheckInTime() != null && record.getCheckOutTime() != null
                && record.getCheckOutTime().isBefore(record.getCheckInTime())) {
            throw new IllegalArgumentException("\u4e0b\u73ed\u6253\u5361\u65f6\u95f4\u4e0d\u80fd\u65e9\u4e8e\u4e0a\u73ed\u6253\u5361\u65f6\u95f4");
        }
        if (record.getCheckInTime() == null && record.getCheckOutTime() == null) {
            record.setStatus("\u7f3a\u52e4");
        } else if (record.getCheckInTime() != null && record.getCheckInTime().toLocalTime().isAfter(WORK_START)) {
            record.setStatus("\u8fdf\u5230");
        } else if (record.getCheckOutTime() != null && record.getCheckOutTime().toLocalTime().isBefore(WORK_END)) {
            record.setStatus("\u65e9\u9000");
        } else {
            record.setStatus("\u6b63\u5e38");
        }
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
