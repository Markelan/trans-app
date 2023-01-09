package marik.project.service.option;

import marik.project.OptionNum;
import marik.project.DTO.TransactionPeriodicity;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionService1 extends OptionService {

    public final static Integer OPTION_NUMBER = OptionNum.FIRST_OPTION.getOptionNumber();
    @Override
    public Integer getOptionNumber() {
        return OPTION_NUMBER;
    }
    @Override
    protected void executeInExcel(String filePath, Workbook excelWorkbook) {
        List<TransactionPeriodicity> listDtos = getListDtos(excelWorkbook);
        Workbook newWorkbook = new XSSFWorkbook();
        Sheet newSheet = newWorkbook.createSheet();

        int rowNum = 0;
        for (TransactionPeriodicity dto : listDtos) {
            Row row = newSheet.createRow(rowNum);
            row.createCell(0).setCellValue(dto.getTrType());
            row.createCell(1).setCellValue(dto.getTrAmount());
            row.createCell(2).setCellValue(dto.getTrPeriodicity());
            rowNum++;
        }
        saveExcelFile(newWorkbook, filePath);
    }

    @Override
    protected void executeInConsole(Workbook excelWorkbook) {
        List<TransactionPeriodicity> listDtos = getListDtos(excelWorkbook);
        String s = "Количество и частота встречаемости транзакций по их типу:";

        for (TransactionPeriodicity dto : listDtos) {
            s = s + "\n" + dto.getTrType() + " " + dto.getTrAmount() + " " + dto.getTrPeriodicity();
        }

        System.out.println(s);
    }
}
