package crud.springHibernate.service;

import crud.springHibernate.model.Department;
import crud.springHibernate.model.Employee;
import crud.springHibernate.repository.DepartmentRepository;
import crud.springHibernate.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CrudService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getDepartments(){
        return (List<Department>) departmentRepository.findAll();
    }

    public Optional<Department> getDepart(Long id){
        return departmentRepository.findById(id);
    }

    public void addOrUpdateDepartment(Department department){
        departmentRepository.save(department);
    }

    public void deleteDepartment(Department department){
        departmentRepository.delete(department);
    }


    public List<Employee> getEmployees(){
        return (List<Employee>) employeeRepository.findAll();
    }

    public Optional<Employee> getEmpl(Long id){
        return employeeRepository.findById(id);
    }

    public void addOrUpdateEmployee(Employee employee){
        employeeRepository.save(employee);
    }

    public void deleteEmployee(Employee employee){
        employeeRepository.delete(employee);
    }



}
