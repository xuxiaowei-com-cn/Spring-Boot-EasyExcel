package cn.com.xuxiaowei.util;

import com.alibaba.excel.analysis.v03.XlsSaxAnalyser;
import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * xls csv 工具类
 *
 * @author xuxiaowei
 * @see XlsSaxAnalyser
 * @see <a href="http://svn.apache.org/repos/asf/poi/trunk/src/examples/src/org/apache/poi/hssf/eventusermodel/examples/XLS2CSVmra.java">XLS2CSVmra</a>
 * @since 0.0.1
 */
public class XlsCsvUtils implements HSSFListener {

    /**
     * 是否需要显示 sheet 名
     */
    private boolean sheetName = true;

    /**
     * 是否在开始需要一个空行
     */
    private boolean blankline = true;

    private int minColumns;
    private POIFSFileSystem fs;
    private PrintStream output;

    private int lastRowNumber;
    private int lastColumnNumber;

    /**
     * 我们应该输出公式还是公式的值？
     */
    private boolean outputFormulaValues = true;

    /**
     * 用于解析公式
     */
    private EventWorkbookBuilder.SheetRecordCollectingListener workbookBuildingListener;
    private HSSFWorkbook stubWorkbook;

    /**
     * 用于处理带有字符串结果的公式
     */
    private SSTRecord sstRecord;
    private FormatTrackingHSSFListener formatListener;

    /**
     * 所以我们知道我们在哪张纸上
     */
    private int sheetIndex = -1;
    private BoundSheetRecord[] orderedBsrs;
    private List<BoundSheetRecord> boundSheetRecords = new ArrayList<>();

    /**
     * 用于处理带有字符串结果的公式
     */
    private int nextRow;
    private int nextColumn;
    private boolean outputNextStringRecord;

    /**
     * 创建一个新的 XLS --> CSV转换器
     *
     * @param fs         要处理的 POIFSFileSystem
     * @param output     将CSV输出到的 PrintStream
     * @param minColumns 要输出的最小列数，或-1表示没有最小值
     */
    public XlsCsvUtils(POIFSFileSystem fs, PrintStream output, int minColumns) {
        this.fs = fs;
        this.output = output;
        this.minColumns = minColumns;
    }

    /**
     * 创建一个新的 XLS --> CSV转换器
     *
     * @param filename   要处理的文件
     * @param minColumns 要输出的最小列数，或 -1 表示没有最小值
     */
    public XlsCsvUtils(String filename, int minColumns) throws IOException {
        this(new POIFSFileSystem(new FileInputStream(filename)), System.out, minColumns);
    }

    /**
     * 开始将 XLS 文件处理为 CSV
     */
    public void process() throws IOException {
        MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
        formatListener = new FormatTrackingHSSFListener(listener);

        HSSFEventFactory factory = new HSSFEventFactory();
        HSSFRequest request = new HSSFRequest();

        if (outputFormulaValues) {
            request.addListenerForAllRecords(formatListener);
        } else {
            workbookBuildingListener = new EventWorkbookBuilder.SheetRecordCollectingListener(formatListener);
            request.addListenerForAllRecords(workbookBuildingListener);
        }

        factory.processWorkbookEvents(request, fs);
    }

    /**
     * HSSFListener 的主要方法，处理事件，并在处理文件时输出CSV。
     */
    @Override
    public void processRecord(Record record) {
        int thisRow = -1;
        int thisColumn = -1;
        String thisStr = null;

        switch (record.getSid()) {
            case BoundSheetRecord.sid:
                boundSheetRecords.add((BoundSheetRecord) record);
                break;
            case BOFRecord.sid:
                BOFRecord br = (BOFRecord) record;
                if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
                    // 根据需要创建子工作簿
                    if (workbookBuildingListener != null && stubWorkbook == null) {
                        stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
                    }

                    // 输出工作表名称Works，方法是按BSR的BOFRecords的位置对BSR进行排序，然后知道我们以字节偏移顺序处理BOFRecords
                    sheetIndex++;
                    if (orderedBsrs == null) {
                        orderedBsrs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
                    }
                    if (blankline) {
                        output.println();
                    }
                    if (sheetName) {
                        output.println(
                                orderedBsrs[sheetIndex].getSheetname() +
                                        " [" + (sheetIndex + 1) + "]:"
                        );
                    }
                }
                break;

            case SSTRecord.sid:
                sstRecord = (SSTRecord) record;
                break;

            case BlankRecord.sid:
                BlankRecord brec = (BlankRecord) record;

                thisRow = brec.getRow();
                thisColumn = brec.getColumn();
                thisStr = "";
                break;
            case BoolErrRecord.sid:
                BoolErrRecord berec = (BoolErrRecord) record;

                thisRow = berec.getRow();
                thisColumn = berec.getColumn();
                thisStr = "";
                break;

            case FormulaRecord.sid:
                FormulaRecord frec = (FormulaRecord) record;

                thisRow = frec.getRow();
                thisColumn = frec.getColumn();

                if (outputFormulaValues) {
                    if (Double.isNaN(frec.getValue())) {
                        // 公式结果是一个字符串
                        // 这存储在下一条记录中
                        outputNextStringRecord = true;
                        nextRow = frec.getRow();
                        nextColumn = frec.getColumn();
                    } else {
                        thisStr = formatListener.formatNumberDateCell(frec);
                    }
                } else {
                    thisStr = '"' +
                            HSSFFormulaParser.toFormulaString(stubWorkbook, frec.getParsedExpression()) + '"';
                }
                break;
            case StringRecord.sid:
                if (outputNextStringRecord) {
                    // 公式字符串
                    StringRecord srec = (StringRecord) record;
                    thisStr = srec.getString();
                    thisRow = nextRow;
                    thisColumn = nextColumn;
                    outputNextStringRecord = false;
                }
                break;

            case LabelRecord.sid:
                LabelRecord lrec = (LabelRecord) record;

                thisRow = lrec.getRow();
                thisColumn = lrec.getColumn();
                thisStr = '"' + lrec.getValue() + '"';
                break;
            case LabelSSTRecord.sid:
                LabelSSTRecord lsrec = (LabelSSTRecord) record;

                thisRow = lsrec.getRow();
                thisColumn = lsrec.getColumn();
                if (sstRecord == null) {
                    thisStr = '"' + "(没有SST记录，无法识别字符串)" + '"';
                } else {
                    thisStr = '"' + sstRecord.getString(lsrec.getSSTIndex()).toString() + '"';
                }
                break;
            case NoteRecord.sid:
                NoteRecord nrec = (NoteRecord) record;

                thisRow = nrec.getRow();
                thisColumn = nrec.getColumn();
                thisStr = '"' + "(TODO)" + '"';
                break;
            case NumberRecord.sid:
                NumberRecord numrec = (NumberRecord) record;

                thisRow = numrec.getRow();
                thisColumn = numrec.getColumn();

                // Format
                thisStr = formatListener.formatNumberDateCell(numrec);
                break;
            case RKRecord.sid:
                RKRecord rkrec = (RKRecord) record;

                thisRow = rkrec.getRow();
                thisColumn = rkrec.getColumn();
                thisStr = '"' + "(TODO)" + '"';
                break;
            default:
                break;
        }

        // Handle new row
        if (thisRow != -1 && thisRow != lastRowNumber) {
            lastColumnNumber = -1;
        }

        // 处理缺失列
        if (record instanceof MissingCellDummyRecord) {
            MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
            thisRow = mc.getRow();
            thisColumn = mc.getColumn();
            thisStr = "";
        }

        // 如果我们有需要打印的内容，请这样做
        if (thisStr != null) {
            if (thisColumn > 0) {
                output.print(',');
            }
            output.print(thisStr);
        }

        // 更新列数和行数
        if (thisRow > -1) {
            lastRowNumber = thisRow;
        }
        if (thisColumn > -1) {
            lastColumnNumber = thisColumn;
        }

        // 处理行尾
        if (record instanceof LastCellOfRowDummyRecord) {
            // 如果需要，请打印出所有缺少的逗号
            if (minColumns > 0) {
                // 列从0开始
                if (lastColumnNumber == -1) {
                    lastColumnNumber = 0;
                }
                for (int i = lastColumnNumber; i < (minColumns); i++) {
                    output.print(',');
                }
            }

            // 我们进入了新的行
            lastColumnNumber = -1;

            // 结束行
            output.println();
        }
    }

    public boolean isOutputFormulaValues() {
        return outputFormulaValues;
    }

    public void setOutputFormulaValues(boolean outputFormulaValues) {
        this.outputFormulaValues = outputFormulaValues;
    }

    public boolean isSheetName() {
        return sheetName;
    }

    public void setSheetName(boolean sheetName) {
        this.sheetName = sheetName;
    }

    public boolean isBlankline() {
        return blankline;
    }

    public void setBlankline(boolean blankline) {
        this.blankline = blankline;
    }

}
