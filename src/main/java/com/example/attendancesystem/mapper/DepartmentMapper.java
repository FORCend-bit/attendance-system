package com.example.attendancesystem.mapper;

import com.example.attendancesystem.entity.Department;

import java.util.List;

public interface DepartmentMapper {

    List<Department> findAll();

    List<Department> selectAll();
}
