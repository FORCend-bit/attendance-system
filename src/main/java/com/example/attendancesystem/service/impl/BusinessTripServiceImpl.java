package com.example.attendancesystem.service.impl;

import com.example.attendancesystem.entity.BusinessTripRecord;
import com.example.attendancesystem.entity.Employee;
import com.example.attendancesystem.mapper.BusinessTripRecordMapper;
import com.example.attendancesystem.mapper.EmployeeMapper;
import com.example.attendancesystem.service.BusinessTripService;
import com.example.attendancesystem.vo.RecordVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class BusinessTripServiceImpl implements BusinessTripService {

    private final BusinessTripRecordMapper businessTripRecordMapper;
    private final EmployeeMapper employeeMapper;

    public BusinessTripServiceImpl(BusinessTripRecordMapper businessTripRecordMapper, EmployeeMapper employeeMapper) {
        this.businessTripRecordMapper = businessTripRecordMapper;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public List<RecordVO<BusinessTripRecord>> list(String employeeNo, String name, Long departmentId, LocalDate startDate, LocalDate endDate) {
        List<RecordVO<BusinessTripRecord>> list = businessTripRecordMapper.findAll(clean(employeeNo), clean(name), departmentId, startDate, endDate);
        return list == null ? Collections.emptyList() : list;
    }

    @Override
    public List<BusinessTripRecord> listByEmployeeNo(String employeeNo) {
        Employee employee = employeeMapper.findByEmployeeNo(clean(employeeNo));
        if (employee == null) {
            return Collections.emptyList();
        }
        List<BusinessTripRecord> list = businessTripRecordMapper.findByEmployeeId(employee.getId());
        return list == null ? Collections.emptyList() : list;
    }

    @Override
    public BusinessTripRecord findById(Long id) {
        return id == null ? null : businessTripRecordMapper.findById(id);
    }

    @Override
    public void save(BusinessTripRecord record) {
        businessTripRecordMapper.insert(record);
    }

    @Override
    public void update(BusinessTripRecord record) {
        businessTripRecordMapper.update(record);
    }

    @Override
    public void delete(Long id) {
        if (id != null) {
            businessTripRecordMapper.logicalDelete(id);
        }
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
