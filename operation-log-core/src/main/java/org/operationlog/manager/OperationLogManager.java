package org.operationlog.manager;

import org.operationlog.aspect.OperationLogAspect;
import org.operationlog.context.SpringApplicationContext;
import org.operationlog.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OperationLogManager {

    @Autowired
    private SpringApplicationContext springApplicationContext;

    public void changeStatus(boolean status) {
        OperationLogAspect bean = springApplicationContext.getBean(OperationLogAspect.class);
        if (Objects.isNull(bean)) {
            throw new BusinessException(10, "OperationLogAspect is not exists or configured to disable");
        }
        if (status) {
            bean.enable();
        } else {
            bean.disable();
        }
    }
}
