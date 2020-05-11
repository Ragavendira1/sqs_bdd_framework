package sqs.core.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sqs.core.constants.ApplicationTypes;
import sqs.core.constants.Constants;
import sqs.core.constants.PropertyConstants;
import sqs.framework.FrameworkData;

import javax.json.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static sqs.core.constants.Constants.EXCEPTION_ON;
import static sqs.core.utils.Utilities.getCallerMethodName;


/**
 * @author : Srirangan
 * @Description : This class contains HashMap Memory setter and getter
 * @since: 23 May 2016
 */

public class TestDataHandler  extends FrameworkData {
    static List<String> testDataFolderList = new ArrayList<>();
    static Map<String, JsonObject> testObjects = new HashMap<>();
    static String scenarioDataTag = "Payment";
    private static Logger logger = Logger.getLogger(TestDataHandler.class);

    /**
     * @author - Megala
     * @deprecated Sample JSON parser to extract array, object type data
     */
    @Deprecated
    private static void readSimpleJson() {
        String dataFile = ".\\src\\test\\resources\\TestData\\Payment.json";
        JsonReader reader = null;
        JsonObject obj = null;
        JsonArray array;
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        try (InputStream is = new FileInputStream(new File(dataFile))) {
            reader = Json.createReader(is);
            obj = reader.readObject();
            for (Map.Entry entry : obj.entrySet()) {
                Object key = entry.getKey();
                JsonObject jsonObject;
                String entryType = entry.getValue().getClass().toString();
                if (entryType.contains("JsonObjectBuilderImpl$JsonObjectImpl")) {
                    jsonObject = (JsonObject) entry.getValue();
                    testObjects.put(key.toString(), jsonObject);
                } else if (entryType.contains("JsonArrayBuilderImpl$JsonArrayImpl")) {
                    array = obj.getJsonArray(key.toString());
                    jsonObject = array.getJsonObject(0);
                    testObjects.put(key.toString(), jsonObject);
                } else if (entryType.contains("JsonStringImpl")) {
                    objectBuilder.add(key.toString(), entry.getValue().toString());
                }
            }
            JsonObject defultJsonObject = objectBuilder.build();
            testObjects.put("default", defultJsonObject);

        } catch (Exception exception) {
            logger.error("Exception", exception);
            try {
                throw exception;
            } catch (IOException e) {
                logger.error(e);
            }
        } finally {
            if (reader != null) reader.close();
        }
    }

    public static String getData(String key) {
        if(config.getProperty(PropertyConstants.TEST_DATA_FORMAT).equalsIgnoreCase(Constants.JSON_FORMAT)) {
            return getData(getScenarioTestDataTag(), key);
        }else{
            return getDataFromMap(key);
        }
    }

    public static String getApplicationURL(String key){
        String data="";
        for (Map.Entry<String,String> entry : testData.entrySet()){
            if(key.equals(entry.getKey())){
                data=entry.getValue();
                break;
            }
        }
        return data;
    }

    private static String getDataFromMap(String key) {
        String data = "";
        String scenarioId = getCurrentScenarioId();
        String newKey = scenarioId.substring(1) + "_" + key;
        for (Map.Entry<String,String> entry : testData.entrySet()){
            if(newKey.equals(entry.getKey())){
                data=entry.getValue();
                break;
            }
    }
        if(data.equals("")){
            data=key;
        }

        return data;

    }

    private static String getDataFromJsonObject(Map.Entry<String, JsonObject> entry, String key) {
        String data = "";
        for (Map.Entry<String, JsonValue> item : entry.getValue().entrySet()) {
            if (item.getKey().equalsIgnoreCase(key)) {
                data = item.getValue().toString();
                if (data.startsWith("\"") && data.endsWith("\"")) {
                    data = data.substring(3, data.length() - 3);
                }
                break;
            }
        }
        return data;

    }

    private static String getData(String scenarioDataTag, String key) {
        if (key.startsWith("$")) {
            key = key.substring(1);
        }
        String data = "";
        for (Map.Entry<String, JsonObject> entry : testObjects.entrySet()) {
            if (scenarioDataTag.length() != 0) {
                if (entry.getKey().equalsIgnoreCase(scenarioDataTag)) {
                    data = getDataFromJsonObject(entry, key);
                }
            } else {
                data = getDataFromJsonObject(entry, key);
            }
            if (data != "") break;
        }
        if (data == "") {
            data = key;
        }
        return data;
    }


    public static void loadJSONTestData() {
        String dataFile = getTestDataFolderPath();
        loadTestDataFoldersList(dataFile);
        for (String currentDataFile : testDataFolderList) {
            if (FilenameUtils.getExtension(String.valueOf(currentDataFile)).contains("json")) {
                readJson(currentDataFile);
            }
        }
    }

    private static void readJson(String dataFile) {
        Map<String, JsonObject> jsonContent = new HashMap<>();
        JsonReader reader = null;
        JsonObject obj = null;
        JsonArray array;
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        File file = new File(dataFile);
        try (InputStream is = new FileInputStream(file)) {
            reader = Json.createReader(is);
            obj = reader.readObject();
            for (Map.Entry entry : obj.entrySet()) {
                Object key = entry.getKey();
                JsonObject jsonObject;
                String entryType = entry.getValue().getClass().toString();
                if (entryType.contains("JsonObjectBuilderImpl$JsonObjectImpl")) {
                    jsonObject = (JsonObject) entry.getValue();
                    jsonContent.put(key.toString(), jsonObject);
                } else if (entryType.contains("JsonArrayBuilderImpl$JsonArrayImpl")) {
                    array = obj.getJsonArray(key.toString());
                    jsonObject = array.getJsonObject(0);
                    jsonContent.put(key.toString(), jsonObject);
                } else if (entryType.contains("JsonStringImpl")) {
                    objectBuilder.add(key.toString(), entry.getValue().toString());
                }
            }
            JsonObject defultJsonObject = objectBuilder.build();
            jsonContent.put(FilenameUtils.removeExtension(file.getName()), defultJsonObject);
            testObjects.put(FilenameUtils.removeExtension(file.getName()), defultJsonObject);

        } catch (Exception exception) {
            logger.error("Exception", exception);
            try {
                throw exception;
            } catch (IOException e) {
                logger.error(e);
            }
        } finally {
            if (reader != null) reader.close();
        }
    }

    private static void loadTestDataFoldersList(String dataFile) {
        Path folderPath = Paths.get(dataFile);
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath)) {
            for (Path path : directoryStream) {
                if (FilenameUtils.getExtension(String.valueOf(path)).contains("json")) {
                    testDataFolderList.add(path.toString());
                }
            }
        } catch (IOException e) {
            logger.error("Exception on load Test Data ", e);
        }
    }


    public static void mainMethod() {
        loadJSONTestData();
        scenarioDataTag = "";
        getData("$AMyPassword");
        scenarioDataTag = "FundTransfer";
        getData("$APaymentUser");
    }


    public static void loadExcelTestData() {
        testData = getTestDataExcel(config.getProperty(PropertyConstants.REPORT_FILE));
    }


    public static Map<String, String> getTestDataExcel(String fileName) {
        Map<String, String> testData = null;
        XSSFWorkbook workbook = null;
        int sheetIndex;
        if(applicationTypeToExecute.equals(ApplicationTypes.ApplicationType.DESKTOPWEB)){
            sheetIndex = 1;   // DesktopWeb
        }else{
            sheetIndex = 2;   // MobileWeb
        }

        try {
            fileName = FileUtils.getAbsolutePath(fileName);
            try (FileInputStream reportFileInputStream = new FileInputStream(fileName)) {
                workbook = new XSSFWorkbook(reportFileInputStream);
                XSSFSheet sheet;
                sheet = workbook.getSheetAt(sheetIndex);
                testData = loadDataIntoHash(sheet);
                workbook.close();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(0);
            logger.error(EXCEPTION_ON + getCallerMethodName() + " :", exception);
        }
        System.out.println(testData);
        return testData;
    }

    private static HashMap<String, String> loadDataIntoHash(XSSFSheet sheet) {
        String currentCellValue;
        HashMap<String, String> testData = new HashMap<>();

        for (int rowNumber = 1; rowNumber < 3; rowNumber++) {   // 1 --> URL1   , 2 ---> URL2
            Row row = sheet.getRow(rowNumber);
            String testID = "";
            String key = "";
            String testKey;
            String value = "";
            for (int columnNumber = 0; columnNumber < 2; columnNumber++) {
                Cell currentCell = row.getCell(columnNumber);
                currentCellValue = ExcelUtilities.getCellValue(currentCell);
                if (!currentCellValue.isEmpty()) {
                    switch (columnNumber) {
                        case 0:   // TestCase/Scenario ID
                            key = currentCellValue;
                            break;
                        case 1:  // Variable Name
                            value = currentCellValue;
                            break;
                    }
                }
                testKey = "$" + key;
                testData.put(testKey, value);
            }
        }

        for (int rowNumber = 5; rowNumber <= sheet.getLastRowNum(); rowNumber++) {
            Row row = sheet.getRow(rowNumber);
            String testID = "";
            String key = "";
            String testKey;
            String value = "";

            for (int columnNumber = 0; columnNumber < row.getLastCellNum(); columnNumber++) {
                Cell currentCell = row.getCell(columnNumber);
                currentCellValue = ExcelUtilities.getCellValue(currentCell);
                if (!currentCellValue.isEmpty()) {
                    switch (columnNumber) {
                        case 0:   // TestCase/Scenario ID
                            testID = currentCellValue;
                            break;
                        case 3:  // Variable Name
                            key = currentCellValue;
                            break;
                        case 4:  // Password Protected Cells
                            value = ExcelUtilities.getProtectedValues(currentCell);
                            break;
                    }
                }
            }
            testKey = testID + "_$" + key;
            testData.put(testKey, value);
        }
        return testData;
    }

}
