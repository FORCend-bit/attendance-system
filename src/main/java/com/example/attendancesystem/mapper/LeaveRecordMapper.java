package com.example.attendancesystem.mapper;

import com.example.attendancesystem.entity.LeaveRecord;
import com.example.attendancesystem.vo.RecordVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRecordMapper {

    List<RecordVO<LeaveRecord>> findAll(@Param("employeeNo") String employeeNo,
                                        @Param("name") String name,
                                        @Param("departmentId") Long departmentId,
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);

    List<LeaveRecord> findByEmployeeId(@Param("employeeId") Long employeeId);

    LeaveRecord findById(@Param("id") Long id);

    int insert(LeaveRecord record);

    int update(LeaveRecord record);

    int logicalDelete(@Param("id") Long id);
}
