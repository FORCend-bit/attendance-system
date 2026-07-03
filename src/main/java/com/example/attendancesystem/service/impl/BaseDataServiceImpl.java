package com.example.attendancesystem.service.impl;

import com.example.attendancesystem.entity.Department;
import com.example.attendancesystem.entity.JobTitle;
import com.example.attendancesystem.entity.Position;
import com.example.attendancesystem.mapper.DepartmentMapper;
import com.example.attendancesystem.mapper.JobTitleMapper;
import com.example.attendancesystem.mapper.PositionMapper;
import com.example.attendancesystem.service.BaseDataService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BaseDataServiceImpl implements BaseDataService {

    private final DepartmentMapper departmentMapper;
    private final PositionMapper positionMapper;
    private final JobTitleMapper jobTitleMapper;

    public BaseDataServiceImpl(DepartmentMapper departmentMapper, PositionMapper positionMapper, JobTitleMapper jobTitleMapper) {
        this.departmentMapper = departmentMapper;
        this.positionMapper = positionMapper;
        this.jobTitleMapper = jobTitleMapper;
    }

    @Override
    public List<Department> departments() {
        List<Department> list = departmentMapper.findAll();
        return list == null ? Collections.emptyList() : list;
    }

    @Override
    public List<Position> positions() {
        List<Position> list = positionMapper.findAll();
        return list == null ? Collections.emptyList() : list;
    }

    @Override
    public List<JobTitle> jobTitles() {
        List<JobTitle> list = jobTitleMapper.findAll();
        return list == null ? Collections.emptyList() : list;
    }
}
