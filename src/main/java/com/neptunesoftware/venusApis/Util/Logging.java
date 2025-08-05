package com.neptunesoftware.venusApis.Util;


import org.springframework.stereotype.Component;
import ug.ac.mak.java.logger.DailyLogListener;
import ug.ac.mak.java.logger.Log;
import ug.ac.mak.java.logger.Logger;
import ug.ac.mak.java.logger.SimpleLogListener;

import java.io.File;

@Component
public class Logging {
    private static final Log log;

    static {
        new File("Logs\\").mkdirs();
        Logger logger = new Logger();
        logger.addListener(new SimpleLogListener());
        DailyLogListener dailyLogger = new DailyLogListener();
        dailyLogger.setConfiguration("Logs\\events", "gzip");
        logger.addListener(dailyLogger);
        log = new Log(logger, "middleware");
    }
    public void info(Object detail){
        log.info(detail);
    }
    public void debug(Object detail){
        log.debug(detail);
    }
    public void error(Object detail){
        log.error(detail);
    }
}
