package test;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookScrabbleHandler implements ClientHandler {
    InputStream inFromClient;
    OutputStream outToClient;
    DictionaryManager dictionaryManager;
    public BookScrabbleHandler() {
        inFromClient = null;
        outToClient = null;
        dictionaryManager = DictionaryManager.get();
    }

    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        this.inFromClient = inFromClient;
        this.outToClient = outToClient;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inFromClient));
            String inputLine = reader.readLine();
            char searchMethod = inputLine.charAt(0);

            Pattern fileNamesPattern = Pattern.compile("(s\\d+\\.txt)");
            Matcher fileNamesMatcher = fileNamesPattern.matcher(inputLine);
            ArrayList<String> files = new ArrayList<>();
            while (fileNamesMatcher.find()) {
                files.add(fileNamesMatcher.group());
            }

            Pattern wordToSearchPattern = Pattern.compile("[^,]+$");
            Matcher wordToSearchMatcher = wordToSearchPattern.matcher(inputLine);
            if (wordToSearchMatcher.find()) {
                String wordToSearch = wordToSearchMatcher.group();
                files.add(wordToSearch);
            }

            String[] args = files.toArray(new String[0]);

            boolean flag = false;
            if (searchMethod == 'Q') {
                flag = dictionaryManager.query(args);
            } else if (searchMethod == 'C') {
                flag = dictionaryManager.challenge(args);
            }

            String response = flag ? "true\n" : "false\n";
            outToClient.write(response.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            if (inFromClient != null) {
                inFromClient.close();
            }
            if (outToClient != null) {
                outToClient.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
