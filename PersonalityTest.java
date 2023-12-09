/*
Nadav Horowitz CS 210 2/28/22

This program accepts file input of personality test answer data and processes and prints the results to an output file.


*/
import java.util.*;
import java.io.*;
public class PersonalityTest {
    public static final String[] OPTIONS = {"EI", "SN", "TF", "JP"}; //Array of Strings (Class constant) for the possible results of the personality test
    public static final int[] MAX_VALUES = {10,20,20,20}; //Integer array (Class constant) for the number of questions for each personality category
    public static final int DIMENSIONS = 4; //Class constant for the number of categories tested
    public static final int QUESTIONS = 7; //Class constant for the number of questions in each question cycle
    public static final int GROUPS = 10; //Class constant for the number of question cycles
 
    
    //Main method, throws FileNotFound exception if input file is not found.
    //Prompts user to provide input from console for input file name, recieves name using constructed console Scanner
    //Constructs File object of name "inputted name" + -out.txt, constructs PrintStream object to print to that file
    //Constructs Scanner object to read from input file
    //repeatedly calls processPerson method, providing Scanner input to read input file and PrintStream output to write output file as parameters,
    //until input file contains no more data
    public static void main(String[] args)
               throws FileNotFoundException{
        System.out.print("Please enter the name of an input text file, do not include the file extension:");
        Scanner console = new Scanner(System.in);
        String inputFileName = console.nextLine();
        PrintStream output = new PrintStream(new File(inputFileName+"-out.txt"));
        inputFileName = inputFileName + ".txt";
        Scanner input = new Scanner(new File(inputFileName));
        while(input.hasNextLine()){
            processPerson(input,output);
        }
    }


    //processPerson method, has Scanner input object and PrintStream output object as parameters
    //Takes the name of the person tested as String name using Scanner object
    //Takes the String of the persons answers as String rawData, also using Scanner object
    //Calls computeTally, passing the String of raw data as parameter
    //Recieves integer array containing a count of all B answers from computeTally
    //Prints the name of the person tested to output file, prints a summary of their number of B answers, and A answers for each category
    //calls computePercentage, passing integer array containing a count of all B answers
    //Recieves double array containing the percentage of B answers for each category
    //Prints percentage information for each tested category to output file
    //Uses percent values in double array "percentages" to determine the result for each personality category
    //Prints the correct result to output file
    public static void processPerson(Scanner input, PrintStream output) {
        String name = input.nextLine();
        String rawData = input.nextLine();
        int[] bAnswers = computeTally(rawData);
        output.println(name+":");
        for(int i =0; i<MAX_VALUES.length;i++) {
            int bs = bAnswers[i];
            int as = MAX_VALUES[i] - bs;
            output.print(as + "A-" + bs + "B ");
        }
        output.println();
        
        double[] percentages = computePercentage(bAnswers);
        output.print("[");
        for(int i =0; i<DIMENSIONS-1; i++){
            double percent = percentages[i]*100;
            output.printf("%.0f%%, ", percent);
        }
        double percent = percentages[DIMENSIONS-1]*100;
        output.printf("%.0f%%] = ", percent);
        
        for(int i = 0; i<DIMENSIONS; i++){
            String result = OPTIONS[i];
            if(percentages[i]>0.5){
                output.print(result.substring(1,2));
            }
            else{
                output.print(result.substring(0,1));
            }
        }
        output.println();
    }


    //computeTally method recieves the String of the persons answers from processPerson method
    //Creates integer array "counts" of size class constant DIMENSIONS (the number of categories tested)
    //Uses a for loop to traverse the answer String beginning to end, processing one character at a time
    //Tallys the number of B answers, updating corresponding value in integer array "counts"
    //returns completed tally aray "counts"
    public static int[] computeTally(String rawData){
        int[] counts = new int[DIMENSIONS];
        for(int i =1; i<=QUESTIONS*GROUPS; i++){
            String letterAnswer = rawData.substring(i-1,i);
            if(letterAnswer.equals("B")){
                if(i%QUESTIONS==1){
                    counts[0]++;
                }
                if(i%QUESTIONS==2||i%QUESTIONS==3){
                    counts[1]++;
                }
                if(i%QUESTIONS==4||i%QUESTIONS==5){
                    counts[2]++;
                }
                if(i%QUESTIONS==6||i%QUESTIONS==0){
                    counts[3]++;
                }
            }
        }
        return counts;
    }


    //Method computePercentage recieves integer array of count of B answers for each personality category as parameter
    //Constructs double array of size class constant DIMENSIONS
    //Calculates the percentage of B answers for each category using the passed integer array of B answers and the class constant integer array MAX_VALUES
    //Stores calculated percent values in double array "percentages" and returns it to calling function 
    public static double[] computePercentage(int[] bAnswers){
        double[] percentages = new double[DIMENSIONS];
        for(int i =0; i<DIMENSIONS; i++){
            double bAnswerDouble = bAnswers[i];
            percentages[i]=bAnswerDouble/MAX_VALUES[i];
        }
        return percentages;
    }
    
}