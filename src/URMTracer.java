import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class URMTracer {

    private int[] register;
    private int pointer;
    private ArrayList<String[]> code;
    private ArrayList<String> output;
    private File source;

    public URMTracer(File source) {
        this.register = new int[10];
        this.pointer = 1;
        this.code = new ArrayList<String[]>();
        this.output = new ArrayList<String>();
        this.source = source;
    }

    public void loadValues() throws FileNotFoundException {
        Scanner sc = new Scanner(source);
        this.code.add(null);

        for (int i = 0; i < 10; i++) {
            this.register[i] = sc.nextInt();
        }
        sc.nextLine();

        //load lines of code to array
        while (sc.hasNextLine()) {
            this.code.add(sc.nextLine().split("\\s"));
        }
        this.code.add(null);
    }

    public void interpret() throws IOException {
        this.printRegisters();

        for (; this.pointer != this.code.size() + 1; this.pointer++) {

            try {
                String[] lineCode = this.code.get(this.pointer);

                int index = Integer.parseInt(lineCode[1]);

                switch (lineCode[0]) {
                    case "S":
                        this.register[index]++;
                        break;
                    case "Z":
                        this.register[index] = 0;
                        break;
                    case "C":
                        int index1 = Integer.parseInt(lineCode[2]);
                        this.register[index1] = this.register[index];
                        break;
                    case "J":
                        if (this.register[Integer.parseInt(lineCode[1])]
                                == this.register[Integer.parseInt(lineCode[2])]) {
                            pointer = Integer.parseInt(lineCode[3]) - 1;
                        }   break;
                }
                this.printRegisters();
            } catch (NumberFormatException |
                    NullPointerException exception) {
                //end of interpretation
                this.writeRegisters();
                break;
            }

        }

    }

    private void printRegisters() {
        String s = new String();
        for (int i = 0; i < this.register.length - 1; i++) {
            System.out.print(this.register[i] + " ");
            s += this.register[i] + " ";
        }
        System.out.println(this.register[9]);
        s += this.register[9] + "\n";
        this.output.add(s);
    }

    private void writeRegisters() throws IOException {
        String fileName = source.getName().split("[.]")[0] + ".out";
        FileWriter fw;
        fw = new FileWriter(fileName);

        for (String s : this.output)
            fw.append(s);

        fw.close();
    }

    public void clear() {
        this.code.clear();
        this.pointer = 1;
        this.register = new int[10];
        this.source = null;
    }

    public static void main(String[] args) throws IOException {
        try {
            for (String arg : args) {
                URMTracer urm = new URMTracer(new File(arg));
                urm.loadValues();
                urm.interpret();
                urm.clear();
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found!");
        }
    }
}