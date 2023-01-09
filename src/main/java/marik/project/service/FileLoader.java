package marik.project.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FileLoader {

    public Workbook loadFile(String filePath) {
        Workbook workbook = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            workbook = new XSSFWorkbook(fis);
        } catch (IOException e) {
            System.out.println("Ошибка получения файла!");
        }
        return workbook;
    }
}
