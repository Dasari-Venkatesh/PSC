import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class Document {
    private URL url;
    private Map<String, Integer> wordFrequency;

    public Document(String urlString){
        try{
            this.url = new URI(urlString).toURL();
            this.wordFrequency = new HashMap<>();
            fetchTextFromURL();
        } catch(MalformedURLException | URISyntaxException e){
            e.printStackTrace();
        }
    }

    private void fetchTextFromURL(){
        try{
            BufferedReader r = new BufferedReader(new InputStreamReader((url.openStream())));
            String inputLine;
            System.out.println("printing the first line of the text captured"+r.lines);
            while((inputLine = r.readLine()) != null){
                
                String[] words = inputLine.split("\\s+");
                for (String word : words){
                    word = word.toLowerCase();
                    wordFrequency.put(word, wordFrequency.getOrDefault(word, 0)+1);
                }
            }
            r.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    public double calculateCosineSimilarity(Document d){
        double dotProduct = 0;
        double magnitudeA = 0;
        double magnitudeB = 0;
        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()){
            String word = entry.getKey();
            int frequencyA = entry.getValue();
            int frequencyB = d.wordFrequency.getOrDefault(word,0);

            dotProduct += frequencyA * frequencyB;
            magnitudeA += Math.pow(frequencyA,2);
        }

        for(int freq : d.wordFrequency.values()){
            magnitudeB += Math.pow(freq,2);
        }

        magnitudeA = Math.sqrt(magnitudeA);
        magnitudeB = Math.sqrt(magnitudeB);

        if(magnitudeA == 0 || magnitudeB == 0){
            return 0.0;
        } else{
            return dotProduct / (magnitudeA * magnitudeB);
        }
    }
    
    @Override
    public String toString(){
        return "Document URL: " + url.toString();
    }

    public static void main(String[] args) {

        String url1 = "https://en.wikipedia.org/wiki/Shiba_Inu";
        String url2 = "https://en.wikipedia.org/wiki/Analogy";

        Document document1 = new Document(url1);
        Document document2 = new Document(url2);

        System.out.println(document1);
        System.out.println(document2);
        
        double similarity = document1.calculateCosineSimilarity(document2);
        System.out.println("Cosine Similarity: "+ similarity);
    }
    
}