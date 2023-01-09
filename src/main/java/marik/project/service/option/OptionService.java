package marik.project.service.option;

import marik.project.DTO.TransactionPeriodicity;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class OptionService {

    public final static String TRANSACTION_SHEET_NAME = "transactions";
    public final static int TRANSACTION_SHEET_CELL_TR_TYPE_NUMBER = 3;
    public final static String TR_TYPES_SHEET_NAME = "tr_types";
    public final static int TR_TYPES_SHEET_CELL_TR_ID_NUMBER = 0;
    public final static int TR_TYPES_SHEET_CELL_TR_DESCRIPTION_NUMBER = 1;

    public void execute(String filePath, Workbook excelWorkbook) {   // проверка значения
        if (!filePath.equals("")) {
            executeInExcel(filePath, excelWorkbook);
        } else {
            executeInConsole(excelWorkbook);
        }
    }

    public abstract Integer getOptionNumber();
    protected abstract void executeInConsole(Workbook excelWorkbook);
    protected abstract void executeInExcel(String filePath, Workbook excelWorkbook);

    protected Sheet getTransactionSheet(Workbook excelWorkbook) {
        return excelWorkbook.getSheet(TRANSACTION_SHEET_NAME);
    }
    protected Sheet getTrTypesSheet(Workbook excelWorkbook) {
        return excelWorkbook.getSheet(TR_TYPES_SHEET_NAME);
    }

    protected Map<String, Long> getMapOfTypeAndAmountTransaction(Workbook excelWorkbook) {
        Map<String, Long> mapOfTypeAndAmountTransaction = new HashMap<>();
        Map<Long, Long> mapOfTypeIdAndAmountTransaction = getMapOfTypeIdAndAmountTransaction(excelWorkbook);
        Map<Long, String> mapOfIdAndTypeTransaction = getMapOfIdAndTypeTransaction(excelWorkbook);

        for (Map.Entry<Long, String> entry : mapOfIdAndTypeTransaction.entrySet()) {
            Long trTypeId = entry.getKey();
            String trType = entry.getValue();
            Long transactionAmount = mapOfTypeIdAndAmountTransaction.get(trTypeId);
            mapOfTypeAndAmountTransaction.put(trType, transactionAmount);
        }
        return mapOfTypeAndAmountTransaction;
    }

    protected Map<Long, Long> getMapOfTypeIdAndAmountTransaction(Workbook excelWorkbook) {
        Map<Long, Long> mapOfTypeIdAndAmountTransaction = new HashMap<>();

        Sheet transactionSheet = getTransactionSheet(excelWorkbook);
        int firstRowNum = transactionSheet.getFirstRowNum();
        int lastRowNum = transactionSheet.getLastRowNum();

        for (int rowNum = firstRowNum; rowNum<= lastRowNum; rowNum++) {
            Row row = transactionSheet.getRow(rowNum);
            Long typeTransaction = (long)row.getCell(TRANSACTION_SHEET_CELL_TR_TYPE_NUMBER).getNumericCellValue();

            if (mapOfTypeIdAndAmountTransaction.containsKey(typeTransaction)) {
                Long amount = mapOfTypeIdAndAmountTransaction.get(typeTransaction);
                amount++;
                mapOfTypeIdAndAmountTransaction.put(typeTransaction, amount);
            } else {
                mapOfTypeIdAndAmountTransaction.put(typeTransaction, 1L);
            }
        }

        return mapOfTypeIdAndAmountTransaction;
    }

    protected Map<Long, String> getMapOfIdAndTypeTransaction(Workbook excelWorkbook) {
        Map<Long, String> mapOfIdAndTypeTransaction = new HashMap<>();

        Sheet trTypesSheet = getTrTypesSheet(excelWorkbook);
        int firstRowNum = trTypesSheet.getFirstRowNum();
        int lastRowNum = trTypesSheet.getLastRowNum();

        for (int rowNum = firstRowNum; rowNum<= lastRowNum; rowNum++) {
            Row row = trTypesSheet.getRow(rowNum);
            Long trTypeId = (long)row.getCell(TR_TYPES_SHEET_CELL_TR_ID_NUMBER).getNumericCellValue();
            if (!mapOfIdAndTypeTransaction.containsKey(trTypeId)) {
                String trTypeDescription = row.getCell(TR_TYPES_SHEET_CELL_TR_DESCRIPTION_NUMBER).getStringCellValue();
                mapOfIdAndTypeTransaction.put(trTypeId, trTypeDescription);
            }
        }

        return mapOfIdAndTypeTransaction;
    }

    protected Long getAllAmountTransaction(Map<String, Long> mapOfTrTypeAndAmount) {
        Long allAmount = 0L;
        for (Map.Entry<String, Long> entry : mapOfTrTypeAndAmount.entrySet()) {
            allAmount = allAmount + entry.getValue();
        }
        return allAmount;
    }

    protected List<TransactionPeriodicity> getListDtos(Workbook excelWorkbook) {
        Map<String, Long> mapOfTrTypeAndAmount = getMapOfTypeAndAmountTransaction(excelWorkbook);
        Long allAmount = getAllAmountTransaction(mapOfTrTypeAndAmount);

        List<TransactionPeriodicity> listDtos = new ArrayList<>();
        for (Map.Entry<String, Long> entry : mapOfTrTypeAndAmount.entrySet()) {
            String trType = entry.getKey();
            Long trAmount = entry.getValue();
            Double trPeriodicity =  trAmount.doubleValue()/allAmount.doubleValue();
            TransactionPeriodicity dto = new TransactionPeriodicity(trType, trAmount, trPeriodicity);
            listDtos.add(dto);
        }
        return listDtos;
    }

    protected void saveExcelFile(Workbook newWorkbook, String filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            newWorkbook.write(fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            System.out.println("Ошибка сохранения в Excel файл");
        }
    }
}
