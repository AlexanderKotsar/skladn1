package xyz.kots.service;

import lombok.extern.log4j.Log4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.kots.domain.TireEntity;
import xyz.kots.repository.StorageRepository;
import xyz.kots.repository.TireRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kots on 23.01.2018.
 */
@Log4j
@Service
public class TireService {

    private static final String FILES_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "_ExcelFiles";

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private TireRepository tireRepository;

    public void saveFile(MultipartFile excelFile) throws IOException {
        try {
            storageRepository.save(FILES_FOLDER, excelFile.getOriginalFilename(), excelFile);
        } catch (IOException e) {
            log.error("Error saving file", e);
        }

        saveTiresIntoDB(getTiresInfo(FILES_FOLDER + File.separator + excelFile.getOriginalFilename()));
    }

    private void saveTiresIntoDB(List<TireEntity> tireEntities){

        for (TireEntity tireEntity : tireEntities) {
            tireRepository.save(tireEntity);
        }
    }

    private List<TireEntity> getTiresInfo(String path) throws IOException {
        InputStream dbbsFile = new FileInputStream(path);
        List<TireEntity> tiresInfo = new ArrayList<TireEntity>();
        File addressDB = new File(path);
        POIFSFileSystem fileSystem = new POIFSFileSystem(dbbsFile); // Open the document

        HSSFWorkbook workBook = new HSSFWorkbook(fileSystem); // Get a workbook
        HSSFSheet sheet = workBook.getSheetAt(0); // Check only the first page

        Iterator<Row> rows = sheet.rowIterator();

        // Skip the table name
        if (rows.hasNext()) {
            rows.next();
        }
        // Skip the "header" of the table
        if (rows.hasNext()) {
            rows.next();
        }

        // We go through all the rows until the document is finished
        while (rows.hasNext()) {
            HSSFRow row = (HSSFRow) rows.next();

            //Get cells from a row by column number
            HSSFCell tireTypeCell = row.getCell(0); //tireType
            HSSFCell tireSeasonCell = row.getCell(1); //season
            HSSFCell tireNameCell = row.getCell(2); //tireName
            HSSFCell tireBalanceCell = row.getCell(3); //tireBalance
            HSSFCell tirePriceCell = row.getCell(4); //tirePrice
            HSSFCell countryCell = row.getCell(5); //country
            HSSFCell yearCell = row.getCell(6); //year

            // If there is no data in the first column, then we do not create a record
            if (tireTypeCell != null) {
                TireEntity tireEntity = new TireEntity();
                tireEntity.setTireType(tireTypeCell.getStringCellValue()); //We get the string value from the cell

                if (tireSeasonCell != null && tireSeasonCell.getCellTypeEnum() == CellType.STRING) {
                    tireEntity.setSeason(tireSeasonCell.getStringCellValue());
                }

                if (tireNameCell != null && tireNameCell.getCellTypeEnum() == CellType.STRING) {

                    String pattern = "(?<width>\\d{3})/(?<height>\\d{2})\\sR(?<diameter>\\d{2})(?<reinforcedR>C|С)" +
                            "?\\s(?<brandName>\\w*)\\s(?<modelName1>\\w*)\\s(?<modelName2>\\w*)?\\s?(?<modelName3>\\w*)" +
                            "?\\s?(?<loadIndex>\\d{2})(?<speedIndex>\\w)\\s?(?<reinforced>\\w{2})?\\s?(?<spikeNonShip>шип)" +
                            "?\\s?(?<extrasOptions>\\w{2})?";
                    Pattern p = Pattern.compile(pattern);
                    Matcher m = p.matcher(tireNameCell.getStringCellValue());

                    if (m.lookingAt()) {
                        String modelName = "";

                        tireEntity.setWidth(m.matches() ? Integer.parseInt(m.group("width")) : null);
                        tireEntity.setHeight(m.matches() ? Integer.parseInt(m.group("height")) : null);
                        tireEntity.setDiameter(m.matches() ? Integer.parseInt(m.group("diameter")) : null);
                        tireEntity.setReinforced(m.group("reinforcedR") != null ? m.group("reinforcedR") : m.group("reinforced") != null ? m.group("reinforced") : null);
                        tireEntity.setBrandName(m.matches() ? m.group("brandName") : null);

                        if (m.group("modelName1")!= null){
                            modelName = modelName + m.group("modelName1");
                        }
                        if (m.group("modelName2")!= null){
                            modelName = modelName + " " + m.group("modelName2");
                        }
                        if (m.group("modelName3")!= null){
                            modelName = modelName + " " + m.group("modelName3");
                        }
                        tireEntity.setModelName(modelName);

                        tireEntity.setLoadIndex(m.matches() ? Integer.parseInt(m.group("loadIndex")) : null);
                        tireEntity.setSpeedIndex(m.matches() ? m.group("speedIndex") : null);
                        tireEntity.setSpikeNonShip(m.matches() ? m.group("spikeNonShip") : null);
                        tireEntity.setExtrasOptions(m.matches() ? m.group("extrasOptions") : null);
                    }
                }
                tireEntity.setCountry(countryCell != null && countryCell.getCellTypeEnum() == CellType.STRING ? countryCell.getStringCellValue() : null);
                tireEntity.setBalance(tireBalanceCell != null && tireBalanceCell.getCellTypeEnum() == CellType.NUMERIC ? (int) tireBalanceCell.getNumericCellValue() : 0);
                tireEntity.setPrice(tirePriceCell != null && tirePriceCell.getCellTypeEnum() == CellType.NUMERIC ? (int) tirePriceCell.getNumericCellValue(): 0);
                tireEntity.setYear(yearCell != null && yearCell.getCellTypeEnum() == CellType.NUMERIC ? (int) yearCell.getNumericCellValue(): 0);
                tiresInfo.add(tireEntity); // Add an entry to the list
            }
        }
        return tiresInfo;
    }

    public Iterable<TireEntity> getTires() {
        return tireRepository.findAll();
    }
}
