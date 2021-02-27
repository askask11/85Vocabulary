/*
 * Author: Jianqing Gao
 * Date: Oct 15, 2020
 * Description: This document is created for translating excels.
 */
package com.vocab85.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This class is for translating Excels with to the work bank given.
 *
 * @author Jianqing Gao
 */
public class ExcelTranslator
{

    private ArrayList<Vocabulary> wordBank;
    private File excelFile;

    public ExcelTranslator(ArrayList<Vocabulary> wordBank, File excelFile)
    {
        this.wordBank = wordBank;
        this.excelFile = excelFile;
    }

    public ExcelTranslator()
    {
        wordBank = null;
        excelFile = null;
    }

    public ArrayList<Vocabulary> getWordBank()
    {
        return wordBank;
    }

    public void setWordBank(ArrayList<Vocabulary> wordBank)
    {
        this.wordBank = wordBank;
    }

    public File getExcelFile()
    {
        return excelFile;
    }

    public void setExcelFile(File excelFile)
    {
        this.excelFile = excelFile;
    }

    public Object[][] readFromExcel(boolean translate) throws IOException
    {

//creating workbook instance that refers to .xls outFile  
        // HSSFWorkbook wb = new HSSFWorkbook(url.openStream());
        XSSFWorkbook www = new XSSFWorkbook(new FileInputStream(excelFile));
//creating a Sheet object to retrieve the object  
        //HSSFSheet sheet = wb.getSheetAt(0);
        XSSFSheet sheet = www.getSheetAt(0);
//evaluating cell type   
        FormulaEvaluator formulaEvaluator = www.getCreationHelper().createFormulaEvaluator();
//        ArrayList<HashMap<Integer, String>> mapList = new ArrayList<>();
//        HashMap<Integer, String> map; //= new HashMap<>();
        Object[][] data = new Object[18][12];

        //counter counts the column the pointer is in
        int counter = 0;//,counter=0;
        //String vocab;
        int rotate = 0;//dotate counts the row
        //each time when one row counts up to 6, switch to next row
        int noneCount = 0;
        String word;
        for (Row row : sheet)     //iteration over row using for each loop  
        {
            for (Cell cell : row)    //iteration over cell using for each loop  
            {
                switch (formulaEvaluator.evaluateInCell(cell).getCellType())
                {

                    case NUMERIC:
                        //System.err.print("n");
                        // System.out.print(cell.getNumericCellValue() + "  ");
                        cell.getNumericCellValue();
                        //System.out.println("Rotate=" + rotate + ";counter=" + counter);
                        data[rotate][counter] = (int) cell.getNumericCellValue();

                        //System.out.println("Numeric");
                        //System.out.println(cell.getNumericCellValue());
                        break;
                    case STRING:
                        //System.out.println("-Rotate=" + rotate + ";counter=" + counter);
                        word = cell.getStringCellValue().trim();
                        if (translate)
                        {
                            word = getTranslate(word);
                        }
                        data[rotate][counter + 1] = word;
                        //System.out.println("String");
                        if (counter == 10)
                        {
                            rotate++;
                            counter = 0;
                        } else
                        {
                            counter += 2;
                        }

                        break;
                    default:
                        //System.out.println(formulaEvaluator.evaluateInCell(cell).getCellType());
                        //when it meets empty cells
                        if (noneCount == 0)
                        {
                            //key
                            data[rotate][counter] = "";
                            noneCount++;
                        } else
                        {
                            //value
                            data[rotate][counter + 1] = "";
                            if (counter == 10)
                            {
                                rotate++;
                                counter = 0;
                            } else
                            {
                                counter += 2;
                            }
                            noneCount = 0;
                        }
                        break;
                }
            }

        }
        www.close();
        return data;
    }

    /**
     * Read outFile from the class and write it out to output stream.
     *
     * @param skip index of vocabs that will be highlighted.
     * @return File translated
     * @throws IOException
     */
    public File translateExcel(int... skip) throws IOException
    {
        try (FileInputStream in = new FileInputStream(excelFile))
        {
            XSSFWorkbook workbook = new XSSFWorkbook(in);
//creating a Sheet object to retrieve the object
//HSSFSheet sheet = wb.getSheetAt(0);
            XSSFSheet sheet = workbook.getSheetAt(0);
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            Iterator<Row> objIterator = sheet.rowIterator();
            DataFormatter objDefaultFormat = new DataFormatter();
            int counter = 0;
//go through all the rows
            while (objIterator.hasNext())
            {
                Row row = objIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                //go through each cells in a row.

                while (cellIterator.hasNext())
                {

                    Cell cellValue = cellIterator.next();
                    formulaEvaluator.evaluate(cellValue);// This will evaluate the cell, And any type of cell will return string value
                    String cellValueStr = objDefaultFormat.formatCellValue(cellValue, formulaEvaluator);
                    //System.out.println(cellValueStr);
                    if (!isNum(cellValueStr) & cellValueStr != null && !cellValueStr.isEmpty())
                    {
                        counter++;
                        //System.out.println(counter);

                        String translatedValue = getTranslate(cellValueStr.replace(" ", "").replace(".", "").trim().toLowerCase());
                        //System.out.println(Arrays.toString(skip));
                        if (skip.length > 0)
                        {
                            if (containNum(skip, counter))
                            {
                                //System.out.println("counter, true" + counter);
                                CellStyle style = workbook.createCellStyle();
                                style.setFillBackgroundColor(IndexedColors.RED.getIndex());
                                style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                                cellValue.setCellStyle(style);
                                //CellUtil.setCellStyleProperty(cellValue, CellUtil.FILL_BACKGROUND_COLOR, "#ff6666");
                                cellValue.setCellValue("(d)" + translatedValue);
                            } else
                            {
                                //System.out.println("Counter" + counter);
                                cellValue.setCellValue(translatedValue);
                            }
                        } else
                        {
                            cellValue.setCellValue(translatedValue);
                        }
                    }

                }
            }
            File outFile = File.createTempFile("translatedVocab_", ".xlsx");
            outFile.deleteOnExit();
            OutputStream outputStream = new FileOutputStream(outFile);
            workbook.write(outputStream);
            outputStream.flush();
            workbook.close();
            return outFile;
        }
    }

    public boolean containNum(int[] sq, int num)
    {
        for (int i = 0; i < sq.length; i++)
        {
            int j = sq[i];
            if (j == num)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * This will go through the vocab bank with translation. If a translation is
     * found, the translated word will be returned. Otherwise, the original word
     * will be returned.
     *
     * @param vocab
     * @return
     */
    public String getTranslate(String vocab)
    {
        //don't translate a serial number
        if (vocab == null || vocab.isEmpty() || isNum(vocab))
        {
            return vocab;
        }

        for (Vocabulary vocabObj : wordBank)
        {
            if (vocabObj.getTerm().equals(vocab))
            {
                return vocabObj.getTranslate();
            }
        }

        //still cannot find an appropriate translation, try to match one
        String psbTranslation = vocab;
        for (Vocabulary vocabulary : wordBank)
        {
            String term = vocabulary.getTerm();

            if (term.contains(vocab))
            {
                int matches = term.length();
                if (term.compareTo(vocab) < matches)
                {

                    matches = term.compareTo(vocab);
                    System.out.println("match=" + matches);
                    psbTranslation = vocabulary.getTranslate();
                }
            }
        }
        return psbTranslation;
    }

    public boolean isNum(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e)
        {
            return false;
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException
    {
        //obtaining input bytes from a outFile
//        URL url = new URL("https://server.1000classes.com/bmcserver/attachment/2020-10/d5b60eac1ad54d33a4866a12c69e9a92.xlsx");

//        TestJFrame frame = new TestJFrame(data);
//        frame.setVisible(true);
//
//        frame.getViewAnswerButton().setVisible(false);
//        frame.getjLabel1().setText("Answer of your BMC test");
        // new CheatingJFrame().setVisible(true);
        // System.out.println("miaoaoao".compareTo("miao"));
    }

}
