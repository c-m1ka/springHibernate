package crud.springHibernate;

import crud.springHibernate.model.Department;
import crud.springHibernate.model.Employee;
import crud.springHibernate.service.CrudService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class SpringHibernateApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context =  SpringApplication.run(SpringHibernateApplication.class, args);
		// данные для старта
      	CrudService crudService = context.getBean(CrudService.class);
		Department department = new Department("department name 1");
		Employee employee = new Employee(1L,"Bob", department);
		Employee employee1 = new Employee(2L,"Sam", department);
		Employee employee2 = new Employee(3L,"Glan", department);
		List<Employee> employeeList = Arrays.asList(employee, employee1, employee2);
		department.setEmployees(employeeList);
		crudService.addOrUpdateDepartment(department);
	}
}



