package Util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class SecurityQuestions {

    public static final Map<Integer, String> questionsMap = new LinkedHashMap<>();
    static
    {
        questionsMap.clear();
        questionsMap.put(1, "What is your favorite movie ?");
        questionsMap.put(2, "What is your birth year?");
        questionsMap.put(3, "What are the last 4 digits of your telephone number?");
        questionsMap.put(4, "What is your favorite hobby?");
    }

    public static String getQuestionByIndex(int index) {

        return  questionsMap.get(index);
    }

    public static  int getRandomSecurityQuestionIndex() {
         int min = 1;
         int max = 5;
         int randomQuestionIndex = new Random().nextInt(max - min) + 1;
         return randomQuestionIndex;
    }

}
