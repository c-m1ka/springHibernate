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
import java.util.Objects;
import java.util.Optional;

@Controller
public class MainController {
    @Autowired
    private CrudService crudService;

    private List<Department> departments;
    private List<Employee> employees;

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        employees = crudService.getEmployees();
        departments = crudService.getDepartments();
        model.addAttribute("employees", employees);
        model.addAttribute("departments", departments);
        return "index";
    }

    @GetMapping(value = "statistics")
    public String statistics(Model model) {
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
                throw new CrudException("Employees this deprtment non empty, first remove all employees of the department");
            }
        }
        return "redirect:/index";
    }

    @PostMapping(value = "/addEmployee")
    public String addEmployee(@RequestParam("name") String name, @RequestParam("department") Long department, Model model){
        model.addAttribute("departments", departments);
        if (departments.isEmpty()) {
            throw new CrudException("To save an employee, you need a department, add a department first");
        } else {
            departments.stream().filter(x -> Objects.equals(x.getId(), department)).forEach(x-> {
                if (name.isEmpty()) {
                    throw new CrudException("Fail to add empty name employee");
                } else {
                    crudService.addOrUpdateEmployee(new Employee(name, x));
                }
            });
        }
        return "redirect:/index";
    }

    @PostMapping(value = "/addDepartment")
    public String addDepartment(@RequestParam("name") String name) {
        if(!name.isEmpty()){
            Department department = new Department(name);
            crudService.addOrUpdateDepartment(department);
        } else {
            throw new CrudException("Fail to add empty name department, please add name non empty");
        }

        return "redirect:/index";
    }

    @GetMapping(value = "/employee/{employeeId}/edit")
    public String getToEditEmployee(@PathVariable Long employeeId, Model model){
        Optional<Employee> maybeEmployee = employees.stream().filter(employee -> Objects.equals(employee.getId(), employeeId)).findFirst();
        if (maybeEmployee.isPresent()){
            Employee employee = maybeEmployee.get();
            model.addAttribute("employeeId", employeeId);
            model.addAttribute("employeeName", employee.getName());
            model.addAttribute("employeeDepartmentId", employee.getDepartment().getId());
            model.addAttribute("departments", departments);
            return "editEmployee";
        } else {
            throw new CrudException("Employee not found");
        }
    }

    @PostMapping(value = "/editEmployee")
    public String editEmployee(@RequestParam("name") String name, @RequestParam("department") Long departmentId, @RequestParam("employeeId") Long employeeId){
        employees.stream().filter(employee -> Objects.equals(employee.getId(), employeeId)).forEach(employee -> {
            departments.stream().filter(department -> Objects.equals(department.getId(), departmentId)).forEach(department -> {
                System.out.println(department);
                if (!name.isEmpty()){
                    employee.setName(name);
                } else {
                    employee.setName(employee.getName());
                }
                employee.setDepartment(department);
                crudService.addOrUpdateEmployee(employee);
            });
        });
        return "redirect:/index";
    }

    @GetMapping(value ="/department/{departmentId}/edit")
    public String getToEditDepartment(@PathVariable Long departmentId, Model model){
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
        Optional<Department> maybeDepartment = departments.stream().filter(department -> Objects.equals(department.getId(), departmentId)).findFirst();
        if (maybeDepartment.isPresent()){
            Department department = maybeDepartment.get();
            if(!name.isEmpty()){
                department.setName(name);
                crudService.addOrUpdateDepartment(department);
            } else {
                throw new CrudException("Name department is empty, please add non empty record");
            }
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

