package com.example.attendancesystem.mapper;

import com.example.attendancesystem.entity.BusinessTripRecord;
import com.example.attendancesystem.vo.RecordVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface BusinessTripRecordMapper {

    List<RecordVO<BusinessTripRecord>> findAll(@Param("employeeNo") String employeeNo,
                                               @Param("name") String name,
                                               @Param("departmentId") Long departmentId,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);

    List<BusinessTripRecord> findByEmployeeId(@Param("employeeId") Long employeeId);

    BusinessTripRecord findById(@Param("id") Long id);

    int insert(BusinessTripRecord record);

    int update(BusinessTripRecord record);

    int logicalDelete(@Param("id") Long id);
}
