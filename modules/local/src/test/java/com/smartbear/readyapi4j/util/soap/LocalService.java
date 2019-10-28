package com.smartbear.readyapi4j.util.soap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public class LocalService {
    private static Logger logger = LoggerFactory.getLogger(LocalService.class);

    public LocalService() {
        logger.debug("Starting localservice!");
    }

    @WebMethod
    public int importantCalculation(@WebParam(name = "a") int a, @WebParam(name = "b") int b) {
        return a + b;
    }
}