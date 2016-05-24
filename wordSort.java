package huawei;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SortFile {

	private static final Logger LOGGER = Logger.getLogger(SortFile.class.getName());

	public enum SortType {
		WORDASC, // 升序
		WORDDSC, // 降序
		WORDCOUNT; // 按出现次数排序
	}

	/**
	 * 对输入文件中的单词进行排序
	 * 
	 * @param srcPathName
	 *            源数据文件路径
	 * @param dstPathName
	 *            输出文件路径
	 * @param type
	 *            排序方式
	 * @return 操作是否成功
	 * */
	public boolean sortWords(String srcPathName, String dstPathName, SortType type) {
		if (null == srcPathName || "".equals(srcPathName) || "".equals(dstPathName)
				|| null == dstPathName) {
			return false;
		}
		if (!type.equals(SortType.WORDASC) && !type.equals(SortType.WORDDSC)
				&& !type.equals(SortType.WORDCOUNT)) {
			return false;
		}
		File file = new File(srcPathName);
		if (!file.exists()) {
			return false;
		}
		Map<String, Integer> map = new HashMap<String, Integer>();

		Pattern pattern = Pattern.compile("[ .,:;?!\"]");
		Pattern illegalPattern = Pattern.compile("[^a-zA-Z]");
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				String[] words = pattern.split(next);
				for (String word : words) {
					Matcher matcher = illegalPattern.matcher(word);
					if (matcher.find()) {
						return false;
					}
					if (map.containsKey(word)) {
						map.put(word, map.get(word) + 1);
					} else {
						map.put(word, 1);
					}
				}
			}
		} catch (FileNotFoundException e1) {
			LOGGER.log(Level.INFO, "file not exist");
		}

		List<Entry<String, Integer>> wordCount = new LinkedList<Entry<String, Integer>>(
				map.entrySet());
		switch (type) {
		case WORDASC:
			Collections.sort(wordCount, new Comparator<Entry<String, Integer>>() {

				@Override
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
					return o1.getKey().compareTo(o2.getKey());
				}
			});
			break;
		case WORDDSC:
			Collections.sort(wordCount, new Comparator<Entry<String, Integer>>() {

				@Override
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
					return o2.getKey().compareTo(o1.getKey());
				}
			});
			break;
		case WORDCOUNT:
			Collections.sort(wordCount, new Comparator<Entry<String, Integer>>() {

				@Override
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
					if (o1.getValue().compareTo(o2.getValue()) == 0) {
						return o1.getKey().compareTo(o2.getKey());
					} else {
						return o1.getValue().compareTo(o2.getValue());
					}
				}
			});
			break;
		default:
			break;
		}
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new File(dstPathName));
			for (Entry<String, Integer> entry : wordCount) {
				writer.println(entry.getKey() + ":" + entry.getValue());
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.INFO, "file not exist");

		} finally {
			writer.close();
		}
		return true;
	}

}
