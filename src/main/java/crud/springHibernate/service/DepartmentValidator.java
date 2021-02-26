package crud.springHibernate.service;

import crud.springHibernate.model.Department;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class DepartmentValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Department.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Department department = (Department) o;
        if (department.getName().isEmpty()){
            errors.reject("name", "Departments name is empty");
        }
    }
}
