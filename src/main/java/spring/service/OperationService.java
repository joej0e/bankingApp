package spring.service;

import spring.entity.Operation;

import java.util.List;

public interface OperationService {

    List<Operation> getStatement(Long account_id);

    Operation save(Operation operation);
}
