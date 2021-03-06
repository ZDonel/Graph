package SPath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Scanner;

/*
 * Graph programs driver file, console interface
 * edited depending on purpose
 * */
public class App {

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        FileWriter myFile = null;
        String fn = "";
        File file = null;
        Scanner read = null;
        double time1;
        FileInputStream fin;
        LinkedList<String> GAL;
        System.out.println("Would you like to find SP for graph?\n" + "1. Calculate SSSP\n" + "2. Quit");
        int choice = kb.nextInt();
        while (choice != 2) {
            if (choice == 1) {
                System.out.print("Please enter filename: ");
                fn = kb.next();// "" + kb.nextLine();
                file = new File(fn);
                System.out.println(file.getAbsolutePath());
                while (!file.exists()) {
                    System.out.print("Please enter VALID filename: ");
                    fn = kb.next();
                    file = new File(fn);
                }
                fin = new FileInputStream(file);
                read = new Scanner(fin);
                GAL = new LinkedList<String>();
                while (read.hasNext()) {
                    GAL.add(read.nextLine().trim());
                }
                time1 = System.nanoTime();
                Graph graph = new Graph(GAL, false);
                time1 = System.nanoTime() - time1;
                fin.close();
                read.close();
                // time1 = System.nanoTime();
                int[][] sPaths = graph.FWPath();
                // time1 = System.nanoTime() - time1;
                myFile = new FileWriter("results\\resultmed.dat");
                if (sPaths != null) {// if proper paths are found, outprint
                    myFile.write(graph.toString());
                    // for (int i = 0; i < sPaths.length; i++) {
                    // myFile.write(i + "\t\t{");
                    // for (int j = 0; j < sPaths.length; j++) {
                    // if(sPaths[i][j] < 1000000000){
                    // myFile.write(" " + sPaths[i][j] + " ");
                    // } else{
                    // myFile.write(" INF ");}
                    // }
                    // myFile.write("}\n");
                }
                myFile.write("total time to calculate: " + time1 / 1000000000.0 + "secs");
            } else {
                myFile.write("Negative weight cycle found(BF/FW)\nNegative edge found(Dij)");
            }
            myFile.close();
            System.out.println("Would you like to find SP in a different graph?\n" + "1. Calculate SSSP\n" + "2. Quit");
            choice = kb.nextInt();
        }
        kb.close();
    }


}

