package a.hrytsiuk.apiLab.repository;

import a.hrytsiuk.apiLab.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}