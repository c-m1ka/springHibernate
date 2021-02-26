package crud.springHibernate.service;

import crud.springHibernate.exceptions.CrudException;
import crud.springHibernate.model.Department;
import crud.springHibernate.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;

@Service
public class ValidateService {
    @Autowired
    private EmployeeValidator employeeValidator;

    @Autowired
    private DepartmentValidator departmentValidator;

    public void validateEmployee(Employee employee) {
        DataBinder dataBinder = new DataBinder(employee);
        dataBinder.addValidators(employeeValidator);
        dataBinder.validate();
        if (dataBinder.getBindingResult().hasErrors()){
            throw new CrudException("Fail to add employee, employee name is empty");
        }
    }

    public void validateDepartments(Department department){
        DataBinder dataBinder = new DataBinder(department);
        dataBinder.addValidators(departmentValidator);
        dataBinder.validate();
        if (dataBinder.getBindingResult().hasErrors()){
            throw new CrudException("Fail to add department, department name is empty");
        }
    }

    public void validateName(String name){
        if (name.isEmpty()){
            throw new CrudException("Name is empty, please add non empty name");
        }
    }


}
