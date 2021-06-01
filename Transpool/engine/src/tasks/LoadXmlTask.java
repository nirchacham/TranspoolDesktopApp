package tasks;

import checks.FileCheck;
import classes.TranspoolSystem;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LoadXmlTask extends Task<Boolean> {
    String filePath;
    TranspoolSystem tsSystem;

    public LoadXmlTask(String filePath, TranspoolSystem tsSystem) {
        this.filePath=filePath;
        this.tsSystem=tsSystem;
    }

    protected Boolean call() throws Exception {
        try {

            FileCheck fileCheck = new FileCheck(filePath);
            updateMessage("Checking file...");
            fileCheck.runAllChecks();
            updateMessage("Loading file...");
            for(int i=0;i<=99;i++)
            {
                Thread.sleep(20);
                updateProgress(i*0.01,1);
            }

            tsSystem.loadDataFromFile(fileCheck.getTransPool());
            updateProgress(1,1);
            updateMessage("Xml file loaded successfully!");
        } catch (Exception e) {
            updateMessage(e.getMessage());
        }

        return Boolean.TRUE;
    }

}
