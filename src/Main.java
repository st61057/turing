import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TuringMachine turingMachine = new TuringMachine();
        turingMachine.run("111B11", "src//addRules.txt");
    }
}
