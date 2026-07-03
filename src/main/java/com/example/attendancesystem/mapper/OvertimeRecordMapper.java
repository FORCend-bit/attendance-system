package com.example.attendancesystem.mapper;

import com.example.attendancesystem.entity.OvertimeRecord;
import com.example.attendancesystem.vo.RecordVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface OvertimeRecordMapper {

    List<RecordVO<OvertimeRecord>> findAll(@Param("employeeNo") String employeeNo,
                                           @Param("name") String name,
                                           @Param("departmentId") Long departmentId,
                                           @Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);

    List<OvertimeRecord> findByEmployeeId(@Param("employeeId") Long employeeId);

    OvertimeRecord findById(@Param("id") Long id);

    int insert(OvertimeRecord record);

    int update(OvertimeRecord record);

    int logicalDelete(@Param("id") Long id);
}
