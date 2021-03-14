package crud.springHibernate.repository;

import crud.springHibernate.model.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    String COUNT_DEPARTMENT = "SELECT COUNT(*) FROM EMPLOYEE";

    @Query(value = COUNT_DEPARTMENT, nativeQuery = true)
    int getCount();
}
