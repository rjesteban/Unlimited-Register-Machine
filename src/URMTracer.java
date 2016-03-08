import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * URM Tracer
 * I hereby solemnly swear that I did code this by myself.
 */
/**
 *
 * @author 2013-50109 Esteban, Arnold Joseph Caesar P.
 * @version 1.0
 * @since 2016-3-6
 */
public class URMTracer {

    private int[] register; //the values of the register from input file
    private int pointer;
    private ArrayList<String[]> code; //contains the code from input file
    private ArrayList<String> output; // contains the output upon execution of code
    private File source; //input file

    public URMTracer(File source) {
        this.register = new int[10];
        this.pointer = 1; //code execution starts at line 1
        this.code = new ArrayList<String[]>();
        this.output = new ArrayList<String>();
        this.source = source;
    }
    
    /**
     * loadValues stores all values to the attributes of the class
     * codes are split into string arrays, as delimited by spaces
     * add null code to signify end of execution
     * @throws FileNotFoundException scanner's exception if input is not given
     */
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
    
    /**
     * interpret the code by printing the values of the register whenever a
     * line of code is executed
     * @throws IOException
     */
    public void interpret() throws IOException {
        this.storeRegistersToOutput();

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
                this.storeRegistersToOutput();
            } catch (NumberFormatException |
                    NullPointerException exception) {
                //end of interpretation
                this.writeRegisters();
                break;
            }

        }

    }
    
    /**
     * printRegisters prints the current value of the registers, 
     * separated by space
     * 
     */
    private void printRegisters() {
        String s = new String();
        for (int i = 0; i < this.register.length - 1; i++) {
            System.out.print(this.register[i] + " ");
            s += this.register[i] + " ";
        }
        System.out.println(this.register[9]);
        s += this.register[9] + "\n";
    }

    /**
     * storeRegistersToOutput stores the current value of the registers, 
     * separated by space, to the output array list.
     * 
     */
    private void storeRegistersToOutput() {
        String s = new String();
        for (int i = 0; i < this.register.length - 1; i++) {
            s += this.register[i] + " ";
        }
        s += this.register[9] + "\n";
        this.output.add(s);
    }
    
    
    /**
     * writeRegisters writes the output to the file upon every line execution
     * output is the value of the register upon line execution
     * @throws IOException
     */
    private void writeRegisters() throws IOException {
        String fileName = source.getName().split("[.]")[0] + ".out";
        FileWriter fw;
        fw = new FileWriter(fileName);

        for (String s : this.output)
            fw.append(s);

        fw.close();
    }

        
    /**
     * clear clears code, removes source, sets pointer back to start and
     * register all to zero
     */

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