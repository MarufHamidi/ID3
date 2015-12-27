
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        System.out.println("Number of iterations : ");
        Scanner scn = new Scanner(System.in);
        int iterations = scn.nextInt();
        Data.reset(iterations);
        Data.readData("data.csv");
        Data.log("Total number of data : " + Data.totalData);
        Data.log("|||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("Total number of data : " + Data.totalData);
        int i = 1;
        Learning learn;
        Node root;
        while (i <= Data.totalIterations) {
            System.out.println(i);
            Data.log("Iteration #" + i + " ");
            Data.log("");
            Data.partitionData();
            Data.log("Train : " + Data.trainDataList.size() + "  " + (Data.trainDataList.size() * 1.0 / Data.totalData));
            Data.log("Test : " + Data.testDataList.size() + "  " + (Data.testDataList.size() * 1.0 / Data.totalData));

            learn = new Learning(Data.trainDataList, 9);
            root = learn.id3(Data.trainDataList, 10, learn.attributeIndexSet);
            Data.tree("~~~~~~~~~~~~~~~~~~~~~~~~");
            Data.tree("~~~~~~~ Tree #" + i + "# ~~~~~~~");
            Data.tree("~~~~~~~~~~~~~~~~~~~~~~~~");
            Data.tree("");
            Data.printTree(root);
            Data.log(root.getDecisionAttribute());
            Data.log("|||||||||||||||||||||||||||||||||||||||||||");

            Data.output("~~~~~~~~~~~~~~~~~~~~~~~~");
            Data.output("~~~~~~~ Test #" + i + "# ~~~~~~~");
            Data.output("~~~~~~~~~~~~~~~~~~~~~~~~");
            Data.output("Serial\tIdentifier\tPrediction");
            learn.decide(root, Data.testDataList);
            learn.outputDecisions();
            Data.output("");
            Data.output("Summary %"+i+" ");
            Data.output("");
            learn.calculateTestPermance();
            i++;
        }

        Data.calculateAvgPerformance();
        Data.output("");
        Data.output("Final average performance measure *#% ");
        Data.output("");
        Data.output("Accuracy\t:\t" + Data.avgAccuracy);
        Data.output("Precision\t:\t" + Data.avgPrecision);
        Data.output("Recall\t\t:\t" + Data.avgRecall);
        Data.output("F-measure\t:\t" + Data.avgFMeasure);
        Data.output("G-mean\t\t:\t" + Data.avgGMean);
        Data.output("");

        System.out.println("");
        System.out.println("Final average performance measure over " + Data.totalIterations + " iterations *#% ");
        System.out.println("");
        System.out.println("Accuracy\t:\t" + Data.avgAccuracy);
        System.out.println("Precision\t:\t" + Data.avgPrecision);
        System.out.println("Recall\t\t:\t" + Data.avgRecall);
        System.out.println("F-measure\t:\t" + Data.avgFMeasure);
        System.out.println("G-mean\t\t:\t" + Data.avgGMean);
    }
}
