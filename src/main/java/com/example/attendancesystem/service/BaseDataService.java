package com.example.attendancesystem.service;

import com.example.attendancesystem.entity.Department;
import com.example.attendancesystem.entity.JobTitle;
import com.example.attendancesystem.entity.Position;

import java.util.List;

public interface BaseDataService {

    List<Department> departments();

    List<Position> positions();

    List<JobTitle> jobTitles();
}
