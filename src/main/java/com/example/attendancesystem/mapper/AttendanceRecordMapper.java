package com.example.attendancesystem.mapper;

import com.example.attendancesystem.entity.AttendanceRecord;
import com.example.attendancesystem.vo.RecordVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRecordMapper {

    List<RecordVO<AttendanceRecord>> findAll(@Param("employeeNo") String employeeNo,
                                             @Param("name") String name,
                                             @Param("departmentId") Long departmentId,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

    List<AttendanceRecord> findByEmployeeId(@Param("employeeId") Long employeeId);

    AttendanceRecord findById(@Param("id") Long id);

    int countLateInMonth(@Param("yearMonth") String yearMonth);

    int insert(AttendanceRecord record);

    int update(AttendanceRecord record);

    int logicalDelete(@Param("id") Long id);
}
