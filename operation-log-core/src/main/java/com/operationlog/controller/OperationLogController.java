package com.operationlog.controller;

import com.operationlog.manager.OperationLogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operationLog")
public class OperationLogController {

    @Autowired
    private OperationLogManager operationLogManager;

    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public void changeStatus(@RequestParam boolean status) {
        operationLogManager.changeStatus(status);
    }
}
