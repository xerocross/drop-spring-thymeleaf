package com.adamfgcross.springherokudemo.querying;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HashTagSearch {


    public List<String> getHashTags(String inputText) {
        List<String> results = new ArrayList<>();
        String regexp = "(?<=[\\s>]|^)#(\\w*[A-Za-z_]+\\w*)\\b(?!;)";
        Pattern pattern = Pattern.compile(regexp);
        Matcher m = pattern.matcher(inputText);
        while (m.find()) {
            String s = m.group(1);
            results.add(s);
        }
        return results;
    }
}
