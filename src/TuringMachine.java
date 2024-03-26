import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TuringMachine {

    private class Rule {

        private String currentState;
        private String currentValue;
        private String nextAction;
        private String nextState;

        private Rule(String currentState, String currentValue, String nextAction, String nextState) {
            this.currentState = currentState;
            this.currentValue = currentValue;
            this.nextAction = nextAction;
            this.nextState = nextState;
        }

        public String getCurrentState() {
            return currentState;
        }

        public String getCurrentValue() {
            return currentValue;
        }

        public String getNextAction() {
            return nextAction;
        }

        public String getNextState() {
            return nextState;
        }
    }

    HashMap<String, ArrayList<Rule>> rules = new HashMap();

    private void loadRules(String filename) throws IOException {
        FileReader file = new FileReader(filename);
        BufferedReader reader = new BufferedReader(file);
        String line = reader.readLine();
        while (line != null) {
            String[] splitLine = line.split(",");
            String key = (splitLine[0]);
            Rule rule = new Rule(key,splitLine[1], splitLine[2], splitLine[3]);
            if (!rules.containsKey(key)) {
                rules.put(key, new ArrayList<>(Arrays.asList(rule)));
            } else {
                rules.get(key).add(rule);
            }
            line = reader.readLine();
        }
    }

    public void run(String inputText, String fileName) throws IOException {
        loadRules(fileName);
        System.out.println(runTuring(inputText, "q1"));
    }

    public String runTuring(String inputText, String startState) {
        int head = 0;
        String currentState = startState;
        char[] inputTape = inputText.toCharArray();

        StringBuilder resultTapeSB = new StringBuilder("");
        resultTapeSB.append(currentState + new String(inputTape));

        while (head < inputTape.length) {
            if (rules.containsKey(currentState)) {
                ArrayList<Rule> instructions = rules.get(currentState);
                Rule rule = isRuleAvailable(String.valueOf(inputTape[head]), instructions);
                if (rule == null) {
                    break;
                }
                if (rule.getCurrentValue().equals(String.valueOf(inputTape[head]))) {
                    switch (rule.getNextAction()) {
                        case "B": {
                            inputTape[head] = 'B';
                            currentState = rule.getNextState();
                            resultTapeSB.append(" -> " + addChar(addChar(new String(inputTape), currentState.charAt(0), head), currentState.charAt(1), head + 1));
                            break;
                        }
                        case "R": {
                            head++;
                            currentState = rule.getNextState();
                            resultTapeSB.append(" -> " + addChar(addChar(new String(inputTape), currentState.charAt(0), head), currentState.charAt(1), head + 1));
                            break;
                        }
                        case "L": {
                            head--;
                            currentState = rule.getNextState();
                            resultTapeSB.append(" -> " + addChar(addChar(new String(inputTape), currentState.charAt(0), head), currentState.charAt(1), head + 1));
                        }
                        default:
                            break;
                    }
                }
            } else {
                break;
            }
        }
        return resultTapeSB.toString();
    }

    private Rule isRuleAvailable(String currentValue, ArrayList<Rule> rules) {
        for (Rule rule : rules) {
            if (rule.getCurrentValue().equals(currentValue)) {
                return rule;
            }
        }
        return null;
    }

    private String addChar(String str, char ch, int position) {
        int len = str.length();
        char[] updatedArr = new char[len + 1];
        str.getChars(0, position, updatedArr, 0);
        updatedArr[position] = ch;
        str.getChars(position, len, updatedArr, position + 1);
        return new String(updatedArr);
    }
}
