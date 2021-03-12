package crud.springHibernate.controller;

import crud.springHibernate.exceptions.CrudException;
import crud.springHibernate.model.Department;
import crud.springHibernate.model.Employee;
import crud.springHibernate.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    @Autowired
    private CrudService crudService;

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        List<Employee> employees = crudService.getEmployees();
        List<Department> departments = crudService.getDepartments();
        model.addAttribute("employees", employees);
        model.addAttribute("departments", departments);
        return "index";
    }

    @GetMapping(value = "/statistics")
    public String statistics(Model model) {
        List<Employee> employees = crudService.getEmployees();
        List<Department> departments = crudService.getDepartments();
        model.addAttribute("employees", employees.size());
        model.addAttribute("departments", departments.size());
        return "statistics";
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
    public String addEmployee(@RequestParam("name") String name, @RequestParam("department") Long department, Model model) {
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
            List<Department> departments = crudService.getDepartments();
            model.addAttribute("employeeId", employeeId);
            model.addAttribute("employeeName", maybeEmployee.get().getName());
            model.addAttribute("departmentName", maybeEmployee.get().getDepartment().getName());
            model.addAttribute("departments", departments);
            return "editEmployee";
        } else {
            throw new CrudException("Employee not found");
        }
    }

    @PostMapping(value = "/editEmployee")
    public String editEmployee(@RequestParam("name") String name, @RequestParam("department") Long departmentId, @RequestParam("employeeId") Long employeeId) {
        Optional<Employee> maybeEmployee = crudService.getEmpl(employeeId);
        if (maybeEmployee.isPresent()){
            Optional<Department> maybeDepartment = crudService.getDepart(departmentId);
            maybeEmployee.get().setName(name);
            maybeEmployee.get().setDepartment(maybeDepartment.get());
            crudService.addOrUpdateEmployee(maybeEmployee.get());
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
            return "editDepartment";
        } else {
            throw new CrudException("Department not found");
        }
    }

    @PostMapping(value = "/editDepartment")
    public String editDepartment(@RequestParam("name") String name, @RequestParam("departmentId") Long departmentId) {
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

}

