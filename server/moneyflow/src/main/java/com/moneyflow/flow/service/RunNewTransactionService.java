package com.moneyflow.flow.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RunNewTransactionService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void run(Runnable runnable) {
        runnable.run();
    }

}
