import com.opencsv.CSVReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    private static String path = "src/main/resources/FacebookLocaliza.csv";
    private static List<Content> extractContentList() {
        List<Content> contentList = new ArrayList<>();
        try {
            CSVReader csvReader = new CSVReader(new FileReader(path));
            String[] values;
            csvReader.readNext();
            while ((values = csvReader.readNext()) != null) {
                String text = values[12];
                if (StringUtils.isBlank(text)) {
                    continue;
                }
                contentList.add(Content.builder()
                        .comments(NumberUtils.toInt(values[1], 0))
                        .likes(NumberUtils.toInt(values[7], 0))
                        .shares(NumberUtils.toInt(values[11], 0))
                        .text(text)
                        .build()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return contentList;
    }

    private static Map<String, EngagementCounts> getEngagementMap(List<Content> contentList) {
        Map<String, EngagementCounts> engagementMap = new HashMap<>();
        contentList.forEach(c -> {
            String[] text = c.getText().split("\\s+");

            for(int i = 0; i < text.length; i++) {
                text[i] = Normalizer.normalize(text[i], Normalizer.Form.NFKD);
                text[i] = text[i].replaceAll("\\p{M}|[!?.,”]", "").toLowerCase();
                if (engagementMap.containsKey(text[i])) {
                    engagementMap.get(text[i]).setComments(engagementMap.get(text[i]).getComments() + c.getComments());
                    engagementMap.get(text[i]).setLikes(engagementMap.get(text[i]).getLikes() + c.getLikes());
                    engagementMap.get(text[i]).setShares(engagementMap.get(text[i]).getShares() + c.getShares());
                } else {
                    engagementMap.put(
                            text[i],
                            EngagementCounts.builder()
                                    .comments(c.getComments())
                                    .likes(c.getLikes())
                                    .shares(c.getShares())
                                    .build());
                }
            }
        });
        return engagementMap;
    }
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Map<String, EngagementCounts> engagementMap = getEngagementMap(extractContentList());
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        engagementMap.forEach((k, v) -> System.out.println(k + " = " + v));
        System.out.println("A total of " + engagementMap.size() + " words were mapped in " + timeElapsed + " ms");
    }
}
