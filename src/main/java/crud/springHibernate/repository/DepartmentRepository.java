package crud.springHibernate.repository;

import crud.springHibernate.model.Department;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Long> {
    String COUNT_DEPARTMENT = "SELECT COUNT(*) FROM DEPARTMENT";

    Department findByName(String name);

    @Query(value = COUNT_DEPARTMENT, nativeQuery = true)
    int getCount();
}
