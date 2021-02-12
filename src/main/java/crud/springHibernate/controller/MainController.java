package crud.springHibernate.controller;

import crud.springHibernate.model.Department;
import crud.springHibernate.model.Employee;
import crud.springHibernate.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping(value = "/employee/{employeeId}/delete")
    public String deleteEmployee(@PathVariable Long employeeId){
        Employee employee = crudService.getEmpl(employeeId).get();
        crudService.deleteEmployee(employee);
        return "redirect:/index";
    }

    @GetMapping(value = "/department/{departmentId}/delete")
    public String deleteDepartment(@PathVariable Long departmentId){
        Department department = crudService.getDepart(departmentId).get();
        if (department.getEmployees().isEmpty()) {
            crudService.deleteDepartment(department);
            return "redirect:/index";
        } else {
            return "redirect:/index";
        }
    }

    @PostMapping(value = "/addUpdateEmployee")
    public String addOrUpdateEmployee(@RequestParam("id") Long id,
                              @RequestParam("name") String name,
                              @RequestParam("department") Long department){
        Optional<Department> maybeDepart = crudService.getDepart(department);
        Optional<Employee> maybeEmpl = crudService.getEmpl(id);
        if (maybeDepart.isPresent() && maybeEmpl.isPresent()) {
            Department depart = maybeDepart.get();
            Employee employee = maybeEmpl.get();
            employee.setDepartment(depart);
            employee.setName(name);
            crudService.addOrUpdateEmployee(employee);
        } else if(maybeDepart.isPresent() && !maybeEmpl.isPresent()) {
            Department depart = maybeDepart.get();
            Employee newEmployee = new Employee(id, name, depart);
            crudService.addOrUpdateEmployee(newEmployee);
        }
        return "redirect:/index";
    }

    @PostMapping(value = "/addUpdateDepartment")
    public String addOrUpdateDepartment(@RequestParam("id") Long id, @RequestParam("name") String name) {
        Optional<Department> maybeDep = crudService.getDepart(id);
        if(maybeDep.isPresent()){
            Department department = maybeDep.get();
            department.setName(name);
            crudService.addOrUpdateDepartment(department);
        } else {
            Department department = new Department(name);
            crudService.addOrUpdateDepartment(department);
        }
        return "redirect:/index";
    }
}

