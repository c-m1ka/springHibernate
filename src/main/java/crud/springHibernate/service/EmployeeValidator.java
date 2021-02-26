package crud.springHibernate.service;

import crud.springHibernate.model.Employee;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class EmployeeValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Employee.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Employee employee = (Employee) o;
        if (employee.getName().isEmpty()){
            errors.rejectValue("name", "Name employee is empty, add non empty name");
        }
    }
}
