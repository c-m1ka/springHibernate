package crud.springHibernate.controller;

import crud.springHibernate.exceptions.CrudException;
import crud.springHibernate.model.Department;
import crud.springHibernate.model.Employee;
import crud.springHibernate.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@Validated
public class MainController {
    @Autowired
    private CrudService crudService;

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        List<Employee> employees = crudService.getEmployees();
        List<Department> departments = crudService.getDepartments();
        model.addAttribute("employees", employees);
        model.addAttribute("departments", departments);
        return "/index";
    }

    @GetMapping(value = "/statistics")
    public String statistics(Model model) {
        int employees = crudService.getCountEmployee();
        int departments = crudService.getCountDepartments();
        model.addAttribute("employees", employees);
        model.addAttribute("departments", departments);
        return "/statistics";
    }

    @GetMapping(value = "/employee/{employeeId}/delete")
    public String deleteEmployee(@PathVariable Long employeeId) {
        Optional<Employee> employee = crudService.getEmpl(employeeId);
        employee.ifPresent(value -> crudService.deleteEmployee(value));
        return "redirect:/index";
    }

    @GetMapping(value = "/department/{departmentId}/delete")
    public String deleteDepartment(@PathVariable Long departmentId) {
        Optional<Department> department = crudService.getDepart(departmentId);
        if (department.isPresent()) {
            if (department.get().getEmployees().isEmpty()) {
                crudService.deleteDepartment(department.get());
            } else {
                throw new CrudException("Employees this department non empty, first remove all employees of the department");
            }
        }
        return "redirect:/index";
    }

    @PostMapping(value = "/addEmployee")
    public String addEmployee(@RequestParam("name") String name,
                              @RequestParam("department") Long department,
                              Model model) {
        Optional<Department> maybeDepart = crudService.getDepart(department);
        if (maybeDepart.isPresent()){
            model.addAttribute("department", department);
            Employee employee = new Employee(name, maybeDepart.get());
            crudService.addOrUpdateEmployee(employee);

        } else {
            throw new CrudException("Department is empty, add first record to add employee");
        }
        return "redirect:/index";
    }

    @PostMapping(value = "/addDepartment")
    public String addDepartment(@RequestParam("name") String name) {
        Department department = new Department(name);
        crudService.addDepartment(department, name);
        return "redirect:/index";
    }

    @GetMapping(value = "/employee/{employeeId}/edit")
    public String getToEditEmployee(@PathVariable Long employeeId, Model model) {
        Optional<Employee> maybeEmployee = crudService.getEmpl(employeeId);
        if (maybeEmployee.isPresent()) {
            List<Department> departments = crudService.getDepartments(); // необходимо работать с данными департамента
            model.addAttribute("employeeId", employeeId);
            model.addAttribute("employeeName", maybeEmployee.get().getName());
            model.addAttribute("departmentName", maybeEmployee.get().getDepartment().getName());
            model.addAttribute("departments", departments);

            return "/editEmployee";
        } else {
            throw new CrudException("Employee not found");
        }
    }

    @PostMapping(value = "/editEmployee")
    public String editEmployee(@RequestParam("name") String name,
                               @RequestParam("department") Long departmentId,
                               @RequestParam("employeeId") Long employeeId) {
        Optional<Employee> maybeEmployee = crudService.getEmpl(employeeId);
        if (maybeEmployee.isPresent()){
            Optional<Department> maybeDepartment = crudService.getDepart(departmentId);
            maybeEmployee.get().setName(name);
            maybeEmployee.get().setDepartment(maybeDepartment.get());
            Employee employee = maybeEmployee.get();
            crudService.addOrUpdateEmployee(employee);
        }
        return "redirect:/index";
    }

    @GetMapping(value = "/department/{departmentId}/edit")
    public String getToEditDepartment(@PathVariable Long departmentId, Model model) {
        Optional<Department> maybeDepartment = crudService.getDepart(departmentId);
        if (maybeDepartment.isPresent()) {
            Department department = maybeDepartment.get();
            model.addAttribute("departmentId", departmentId);
            model.addAttribute("departmentName", department.getName());
            return "/editDepartment";
        } else {
            throw new CrudException("Department not found");
        }
    }

    @PostMapping(value = "/editDepartment")
    public String editDepartment(@RequestParam("name") String name,
                                 @RequestParam("departmentId") Long departmentId) {
        Optional<Department> maybeDepartment = crudService.getDepart(departmentId);
        if (maybeDepartment.isPresent()) {
          Department department = maybeDepartment.get();
            department.setName(name);
            crudService.updateDepartment(department);
        }
        return "redirect:/index";
    }

    @ExceptionHandler(CrudException.class)
    public ModelAndView handleCustomException(CrudException ex) {
        ModelAndView modelAndView = new ModelAndView("errorPage");
        modelAndView.addObject("error", ex.getErrMessage());
        return modelAndView;
    }
    // При добавлении нового сотрудника или департамента валидация проходит верно, но при обновлении сотрудника или департаметна валится с ошибкой TransactionSystemException работает для методов
    // addEmployee, addDepartment
    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView handleValidateException(ConstraintViolationException ex){
        ModelAndView modelAndView = new ModelAndView("errorPage");
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation constraintViolation: constraintViolations){
            message.append(constraintViolation.getMessage());
        }
        modelAndView.addObject("error", message);
        return modelAndView;
    }
    // По всей видимости ConstraintViolationException оборачивается в TransactionSystemException, по этой причине добавил метод обработчик для этого исключения, каст в ConstraintViolationException
    // пример взял с этого ресурса https://coderoad.ru/51446480/Spring-boot-2-0-2-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D1%83%D1%8F-%D0%B4%D0%B0%D0%BD%D0%BD%D1%8B%D0%B5-Spring-%D0%BA%D0%B0%D0%BA-%D0%BF%D0%BE%D0%BB%D1%83%D1%87%D0%B8%D1%82%D1%8C-%D1%81%D0%BE%D0%BE%D0%B1%D1%89%D0%B5%D0%BD%D0%B8%D0%B5-%D0%BE%D1%82-%D0%BF%D1%80%D0%BE%D0%B2%D0%B5%D1%80%D0%BA%D0%B8
    // обрабочтичк для editEmployee, editDepartment
    @ExceptionHandler(value = { TransactionSystemException.class })
    public ModelAndView handleValidateExceptionFromTransaction(TransactionSystemException ex) {
        ModelAndView modelAndView = new ModelAndView("errorPage");
        Throwable cause = ex.getRootCause();
        if (cause instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) cause).getConstraintViolations();
            StringBuilder message = new StringBuilder();
            for (ConstraintViolation constraintViolation: constraintViolations){
                message.append(constraintViolation.getMessage());
            }
            modelAndView.addObject("error", message);
        }
        return modelAndView;
    }

}

