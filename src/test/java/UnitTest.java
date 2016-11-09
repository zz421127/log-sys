import cn.yy.log.entity.vo.LogIndex;
import cn.yy.log.entity.vo.LogPair;
import cn.yy.log.util.IOUtil;
import cn.yy.log.util.v1.AccurateSearchEngine;
import cn.yy.log.util.v1.IndexBuilder;
import cn.yy.log.util.v1.LogScanner;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: Leo
 * @Blog: http://blog.csdn.net/lc0817
 * @CreateTime: 2016/10/28 14:16
 * @Description:
 */
public class UnitTest {

    @Test
    public void indexBuilderTest() throws IOException {
        long begin = System.nanoTime();
        List<IndexBuilder.KvIndexInfo> kvIndexInfoList = new ArrayList<>();
//        kvIndexInfoList.add(new IndexBuilder.KvIndexInfo("pat_card_no", "\"pat_card_no\"", "\"pat_card_no\":\"", "\""));
//        kvIndexInfoList.add(new IndexBuilder.KvIndexInfo("patName", "<patName>", "<patName>", "</patName>"));
        kvIndexInfoList.add(new IndexBuilder.KvIndexInfo("pat_card_no", "\"pat_card_no\"", "\"pat_card_no\":\"", "\""));
        kvIndexInfoList.add(new IndexBuilder.KvIndexInfo("pat_card_type", "\"pat_card_type\"", "\"pat_card_type\":\"", "\""));
        String fileContent = IOUtil.read("D:\\tmp\\2016\\01\\01\\02\\01\\201601010201.log");
        IndexBuilder indexBuilder = new IndexBuilder(kvIndexInfoList, fileContent);
        indexBuilder.build();
        Map<String, TreeSet<Integer>> normalIndexMap = indexBuilder.getNormalIndexMap();
        Map<String, Map<String, TreeSet<Integer>>> accuratedIndexMap = indexBuilder.getAccuratedIndexMap();
//        System.out.println(JSON.toJSONString(normalIndexMap, true));
//        System.out.println(JSON.toJSONString(specialIndexMap, true));
        LogIndex logIndex = new LogIndex(normalIndexMap, accuratedIndexMap);
        String json = JSON.toJSONString(logIndex, true);
        System.out.println(json);
        IOUtil.write("D:\\tmp\\2016\\01\\01\\02\\01\\201601010201.index", json);
        System.out.println(BigDecimal.valueOf(System.nanoTime() - begin, 9));
    }

    @Test
    public void logScannerTest() throws ParseException {
        String beginDateTime = "2016-01-01 01:01";
        String endDateTime = "2016-02-01 01:03";
        String basePath = "D:\\tmp";
        Map<String, LogPair> logPairMap = LogScanner.scan(beginDateTime, endDateTime, basePath);
        System.out.println(JSON.toJSONString(logPairMap, true));
    }

    @Test
    public void accuratedSearchEngineTest() {
        String beginDateTime = "2016-01-01 01:01";
        String endDateTime = "2016-02-01 01:03";
        String basePath = "D:\\tmp";
        Map<String, LogPair> logPairMap = LogScanner.scan(beginDateTime, endDateTime, basePath);
        List<String> contextList = AccurateSearchEngine.search("pat_card_no", "0000427177", logPairMap);
//        System.out.println(JSON.toJSONString(contextList, true));
        System.out.println(contextList.size());
    }

    @Test
    public void test() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + 3600 * 1000);
        System.out.println(sdf.format(today));
        System.out.println(sdf.format(tomorrow));
    }
}