package spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.entity.Operation;
import spring.repository.OperationRepository;
import spring.service.OperationService;

import java.util.List;

@Service
@Transactional
public class OperationServiceImpl implements OperationService {

    private OperationRepository operationRepository;

    @Autowired
    public OperationServiceImpl(OperationRepository transactionRepository) {
        this.operationRepository = transactionRepository;
    }

    @Override
    public List<Operation> getStatement(Long account_id) {
        return operationRepository.findAllByAccountId(account_id);
    }

    @Override
    public Operation save(Operation operation) {
        return operationRepository.save(operation);
    }

}



