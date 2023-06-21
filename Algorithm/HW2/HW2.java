//21912117 김병철
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HW2 {
	public static HashMap<String, List<Integer>> fileIO(String fileName) throws IOException {
		String line, docuName;
		BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
		BufferedReader stopReader = new BufferedReader(new FileReader("stopwords.txt"));

		HashMap<String, List<Integer>> document = new HashMap<String, List<Integer>>();
		List<String> stopWords = new ArrayList<String>(750);

		while ((line = stopReader.readLine()) != null) {
			stopWords.add(line);
		}
		stopReader.close();

		while ((line = bufferedReader.readLine()) != null) {
			List<Integer> wordList = new LinkedList<Integer>();
			docuName = line;
			line = bufferedReader.readLine();
			line = line.toLowerCase();
			String token[] = line.split("[,.?!:\"\\s]+");
			for (String word : token) {
				if (!stopWords.contains(word))
					wordList.add(word.hashCode());
			}
			document.put(docuName, wordList);
		}
		bufferedReader.close();

		return document;
	}

	public static HashMap<String, HashMap<Integer, Double>> termFrequency(HashMap<String, List<Integer>> document) {
		HashMap<String, HashMap<Integer, Double>> tiVector = new HashMap<String, HashMap<Integer, Double>>();
		HashMap<Integer, Double> tf;

		for (String docuName : document.keySet()) {
			List<Integer> wordList = document.get(docuName);
			tf = new HashMap<Integer, Double>();

			for (int word : wordList) {
				if (tf.containsKey(word)) {
					double frequency = tf.get(word);
					tf.put(word, frequency + 1);
				} else {
					tf.put(word, (double) 1);
				}
			}
			for (int word : tf.keySet()) {
				tf.put(word, tf.get(word) / wordList.size());
			}
			tiVector.put(docuName, tf);
		}

		return tiVector;
	}

	public static void inverseDocumentFrequency(HashMap<String, HashMap<Integer, Double>> tiVector) {
		HashMap<Integer, Double> make = new HashMap<Integer, Double>();
		int docuNum;
		double idf;

		docuNum = tiVector.size();
		for (HashMap<Integer, Double> tf : tiVector.values()) {
			for (int word : tf.keySet()) {
				if (make.containsKey(word)) {
					double count = make.get(word);
					make.put(word, count + 1);
				} else
					make.put(word, (double) 1);
			}
		}

		for (HashMap<Integer, Double> tf : tiVector.values())
			for (int word : tf.keySet()) {
				idf = Math.log(docuNum / make.get(word));
				tf.put(word, tf.get(word) * idf);
			}
	}

	public static HashMap<String, Double> similarity(HashMap<String, HashMap<Integer, Double>> tiVector,
			String target) {
		HashMap<String, Double> similar = new HashMap<String, Double>();
		HashMap<Integer, Double> targetMap;
		double value;
		double targetSum = 0, otherSum = 0;

		targetMap = tiVector.get(target);
		targetSum = sum(targetMap);

		for (String docuNum : tiVector.keySet()) {
			HashMap<Integer, Double> otherMap = tiVector.get(docuNum);
			value = 0;
			if (otherMap == targetMap)
				continue;

			otherSum = sum(otherMap);
			for (int word : otherMap.keySet()) {
				if (targetMap.containsKey(word)) {
					value += (targetMap.get(word) * otherMap.get(word));
				}
			}
			value /= (targetSum * otherSum);
			if (Double.isNaN(value))
				value = 0;
			similar.put(docuNum, value);
		}

		return similar;
	}

	public static double sum(HashMap<Integer, Double> target) {
		double targetSum = 0;
		for (int key : target.keySet()) {
			double value = target.get(key);
			targetSum += (value * value);
		}
		return Math.sqrt(targetSum);
	}

	public static void prtVector(HashMap<String, HashMap<Integer, Double>> tiVector, String target) {
		ArrayList<MapSave> targetMap = new ArrayList<MapSave>(tiVector.get(target).size());
		for (Map.Entry<Integer, Double> entry : tiVector.get(target).entrySet())
			targetMap.add(new MapSave(entry.getKey(), entry.getValue()));
		Collections.sort(targetMap, new hashSort());
		if (tiVector.containsKey(target)) {
			System.out.println("결과 1. \"" + target + "\"의 TF-IDF 벡터");
			System.out.print("[ ");
			for (MapSave s : targetMap) {
				System.out.printf("(%s, %.3f) ", s.key, s.value);
			}
		} else {
			System.out.println("Document not found: " + target);
		}
		System.out.println("]");
		System.out.println();
	}

	public static void prtSimilar(HashMap<String, Double> similar, int k, String target) {
		List<Map.Entry<String, Double>> entries = new ArrayList<>(similar.entrySet());
		entries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
		System.out.println("결과 2. \"" + target + "\"과(와) 유사한 " + k + "개의 문서");
		int count = 0;
		for (Map.Entry<String, Double> value : entries) {
			if (count >= k)
				break;
			count++;
			System.out.printf("%d. %s (유사도=%.5f)\n", count, value.getKey(), value.getValue());
		}
		System.out.println();
	}

	public static void main(String[] args) throws IOException {
		System.out.print("파일 이름, k, 문서 제목: ");
		Scanner in = new Scanner(System.in);
		String fileName = in.next();
		String j = in.next();
		String titleName = in.nextLine().trim();
		System.out.println();

		int k = Integer.parseInt(j);
		HashMap<String, List<Integer>> document = fileIO(fileName);
		HashMap<String, HashMap<Integer, Double>> tiVector = termFrequency(document);
		inverseDocumentFrequency(tiVector);
		HashMap<String, Double> similar = similarity(tiVector, titleName);
		prtVector(tiVector, titleName);
		prtSimilar(similar, k, titleName);
	}
}

class hashSort implements Comparator<MapSave> {
	public int compare(MapSave a, MapSave b) {
		return Integer.compare(a.key, b.key);
	}
}

class MapSave {
	int key;
	double value;

	public MapSave(int key, double value) {
		this.key = key;
		this.value = value;
	}
}