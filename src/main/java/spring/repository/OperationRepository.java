package spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.entity.Operation;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findAllByAccountId(Long account_id);
}
