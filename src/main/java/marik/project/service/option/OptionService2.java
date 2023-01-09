package marik.project.service.option;

import marik.project.OptionNum;
import marik.project.DTO.TransactionPeriodicity;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionService2 extends OptionService {

    public final static Integer OPTION_NUMBER = OptionNum.SECOND_OPTION.getOptionNumber();

    @Override
    public Integer getOptionNumber() {
        return OPTION_NUMBER;
    }

    @Override
    protected void executeInExcel(String filePath, Workbook excelWorkbook) {
        List<TransactionPeriodicity> listDtos = getSortedList(getListDtos(excelWorkbook));
        Workbook newWorkbook = new XSSFWorkbook();
        Sheet newSheet = newWorkbook.createSheet();

        int rowNum = 0;
        if (listDtos.size() < 5) {
            for (TransactionPeriodicity dto : listDtos) {
                Row row = newSheet.createRow(rowNum);
                row.createCell(0).setCellValue(dto.getTrType());
                rowNum++;
            }
            saveExcelFile(newWorkbook, filePath);
        }
        else {
            for (TransactionPeriodicity dto : listDtos) {
                if (rowNum == 4) {
                    saveExcelFile(newWorkbook, filePath);
                    return;
                }
                Row row = newSheet.createRow(rowNum);
                row.createCell(0).setCellValue(dto.getTrType());
                rowNum++;
            }
        }
    }

    @Override
    protected void executeInConsole(Workbook excelWorkbook) {
        List<TransactionPeriodicity> listDtos = getSortedList(getListDtos(excelWorkbook));
        String s = "5 самых часто встречающихся транзакций:";

        if (listDtos.size() < 5) {
            for (int i = 0; i < listDtos.size(); i++) {
                s = s + "\n" + listDtos.get(i).getTrType() + " " + listDtos.get(i).getTrPeriodicity();
            }

        } else {
            for (int i = 0; i < 5; i++) {
                s = s + "\n" + listDtos.get(i).getTrType() + " " + listDtos.get(i).getTrPeriodicity();
            }
        }
        System.out.println(s);
    }

    private List<TransactionPeriodicity> getSortedList(List<TransactionPeriodicity> listDtos) {
        return listDtos.stream()
                .sorted(new TransactionPeriodicityDtoComparator())
                .collect(Collectors.toList());
    }

    private class TransactionPeriodicityDtoComparator implements Comparator<TransactionPeriodicity> {
        public int compare(TransactionPeriodicity a, TransactionPeriodicity b){
            return a.getTrPeriodicity().compareTo(b.getTrPeriodicity());
        }
    }
}
