package crud.springHibernate.controller;

import crud.springHibernate.exceptions.CrudException;
import crud.springHibernate.model.Department;
import crud.springHibernate.model.Employee;
import crud.springHibernate.service.CrudService;
import crud.springHibernate.service.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class MainController {
    @Autowired
    private CrudService crudService;

    @Autowired
    private ValidateService validateService;

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
    public String deleteEmployee(@PathVariable Long employeeId){
        Optional<Employee> employee = crudService.getEmpl(employeeId);
        employee.ifPresent(value -> crudService.deleteEmployee(value));
        return "redirect:/index";
    }

    @GetMapping(value = "/department/{departmentId}/delete")
    public String deleteDepartment(@PathVariable Long departmentId){
        Optional<Department> department = crudService.getDepart(departmentId);
        if (department.isPresent()){
            if (department.get().getEmployees().isEmpty()) {
                crudService.deleteDepartment(department.get());
            } else {
                throw new CrudException("Employees this department non empty, first remove all employees of the department");
            }
        }
        return "redirect:/index";
    }

    @PostMapping(value = "/addEmployee")
    public String addEmployee(@RequestParam("name") String name, @RequestParam("department") Long department, Model model){
        List<Department> departments = crudService.getDepartments();
        model.addAttribute("departments", departments);
        if (department.equals(0L)) {
            throw new CrudException("Fail to add employee departments is empty, please add first department");
        } else {
            departments.stream().filter(x -> Objects.equals(x.getId(), department)).forEach(x-> {
                Employee employee = new Employee(name, x);
                validateService.validateEmployee(employee);
                crudService.addOrUpdateEmployee(employee);
            });
        }
        return "redirect:/index";
    }

    @PostMapping(value = "/addDepartment")
    public String addDepartment(@RequestParam("name") String name) {
        List<Department> departments = crudService.getDepartments();
        Department department = new Department(name);
        validateService.validateDepartments(department);
            if (departments.stream().noneMatch(x -> (Objects.equals(x.getName(), name)))){
                crudService.addOrUpdateDepartment(department);
            } else {
              throw new CrudException("This department name exist, please add new name department");
            }
        return "redirect:/index";
    }

    @GetMapping(value = "/employee/{employeeId}/edit")
    public String getToEditEmployee(@PathVariable Long employeeId, Model model){
        List<Employee> employees = crudService.getEmployees();
        List<Department> departments = crudService.getDepartments();
        Optional<Employee> maybeEmployee = employees.stream().filter(employee -> Objects.equals(employee.getId(), employeeId)).findFirst();
        if (maybeEmployee.isPresent()){
            Employee employee = maybeEmployee.get();
            model.addAttribute("employeeId", employeeId);
            model.addAttribute("employeeName", employee.getName());
            model.addAttribute("departmentName", employee.getDepartment().getName());
            model.addAttribute("departments", departments);
            return "editEmployee";
        } else {
            throw new CrudException("Employee not found");
        }
    }

    @PostMapping(value = "/editEmployee")
    public String editEmployee(@RequestParam("name") String name, @RequestParam("department") Long departmentId, @RequestParam("employeeId") Long employeeId){
        List<Employee> employees = crudService.getEmployees();
        List<Department> departments = crudService.getDepartments();
        employees.stream().filter(employee -> Objects.equals(employee.getId(), employeeId)).forEach(employee ->
        {
            for (Department department : departments) {
                if (Objects.equals(department.getId(), departmentId)) {
                    validateService.validateName(name);
                    employee.setName(name);
                    employee.setDepartment(department);
                    crudService.addOrUpdateEmployee(employee);
                }
            }
        });
        return "redirect:/index";
    }

    @GetMapping(value ="/department/{departmentId}/edit")
    public String getToEditDepartment(@PathVariable Long departmentId, Model model){
        List<Department> departments = crudService.getDepartments();
        Optional<Department> maybeDepartment = departments.stream().filter(department -> department.getId().equals(departmentId)).findFirst();
        if (maybeDepartment.isPresent()){
            Department department = maybeDepartment.get();
            model.addAttribute("departmentId",departmentId);
            model.addAttribute("departmentName", department.getName());
            return "editDepartment";
        } else {
            throw new CrudException("Department not found");
        }
    }

    @PostMapping(value = "/editDepartment")
    public String editDepartment(@RequestParam("name") String name, @RequestParam("departmentId") Long departmentId){
        List<Department> departments = crudService.getDepartments();
        Optional<Department> maybeDepartment = departments.stream().filter(department -> Objects.equals(department.getId(), departmentId)).findFirst();
        if (maybeDepartment.isPresent()){
            Department department = maybeDepartment.get();
                validateService.validateName(name);
                department.setName(name);
                crudService.addOrUpdateDepartment(department);

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

