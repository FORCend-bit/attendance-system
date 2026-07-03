package com.example.attendancesystem.mapper;

import com.example.attendancesystem.entity.Employee;
import com.example.attendancesystem.vo.EmployeeDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeMapper {

    Employee findByEmployeeNo(@Param("employeeNo") String employeeNo);

    Employee selectByEmployeeNo(@Param("employeeNo") String employeeNo);

    Employee findById(@Param("id") Long id);

    EmployeeDetailVO findDetailById(@Param("id") Long id);

    EmployeeDetailVO findDetailByEmployeeNo(@Param("employeeNo") String employeeNo);

    List<EmployeeDetailVO> findPage(@Param("employeeNo") String employeeNo,
                                    @Param("name") String name,
                                    @Param("departmentId") Long departmentId,
                                    @Param("offset") int offset,
                                    @Param("size") int size);

    int count(@Param("employeeNo") String employeeNo,
              @Param("name") String name,
              @Param("departmentId") Long departmentId);

    int insert(Employee employee);

    int update(Employee employee);

    int updateContact(Employee employee);

    int resign(@Param("id") Long id);
}
