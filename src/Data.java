
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Data {

    public static int totalData;
    public static int totalIterations;
    public static ArrayList<PatientData> patientDataList;
    public static ArrayList<PatientData> trainDataList;
    public static ArrayList<PatientData> testDataList;

    public static BufferedWriter outputFile;
    public static BufferedWriter logFile;
    public static BufferedWriter treesFile;

    public static ArrayList<Double> accuracyList;
    public static ArrayList<Double> precisionList;
    public static ArrayList<Double> recallList;
    public static ArrayList<Double> fMeasureList;
    public static ArrayList<Double> gMeanList;

    public static double avgAccuracy;
    public static double avgPrecision;
    public static double avgRecall;
    public static double avgFMeasure;
    public static double avgGMean;

    public static void reset(int i) {
        Data.totalIterations = i;
        Data.totalData = 0;
        Data.patientDataList = new ArrayList<>();
        Data.trainDataList = new ArrayList<>();
        Data.testDataList = new ArrayList<>();
        try {
            Data.outputFile = new BufferedWriter(new FileWriter(new File("output.txt")));
            Data.logFile = new BufferedWriter(new FileWriter(new File("log.txt")));
            Data.treesFile = new BufferedWriter(new FileWriter(new File("trees.txt")));
        } catch (Exception ex) {
            System.out.println("rwerwe");
        }

        Data.accuracyList = new ArrayList<>();
        Data.precisionList = new ArrayList<>();
        Data.recallList = new ArrayList<>();
        Data.fMeasureList = new ArrayList<>();
        Data.gMeanList = new ArrayList<>();

        Data.avgAccuracy = -1;
        Data.avgPrecision = -1;
        Data.avgRecall = -1;
        Data.avgFMeasure = -1;
        Data.avgGMean = -1;
    }

    public static void readData(String fileName) {
        Data.reset(Data.totalIterations);
        try {
            String dataLine;
            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            while (true) {
                dataLine = br.readLine();
                if (dataLine == null) {
                    break;
                }
                Data.totalData++;
                Data.patientDataList.add(Data.extractData(totalData, dataLine));
            }
            br.close();
        } catch (Exception ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static PatientData extractData(int id, String dataLine) {
        String[] lineParts = dataLine.split(",");
        return new PatientData(id,
                Integer.parseInt(lineParts[0]),
                Integer.parseInt(lineParts[1]),
                Integer.parseInt(lineParts[2]),
                Integer.parseInt(lineParts[3]),
                Integer.parseInt(lineParts[4]),
                Integer.parseInt(lineParts[5]),
                Integer.parseInt(lineParts[6]),
                Integer.parseInt(lineParts[7]),
                Integer.parseInt(lineParts[8]),
                Integer.parseInt(lineParts[9])
        );
    }

    public static void partitionData() {
        Data.trainDataList = new ArrayList<>();
        Data.testDataList = new ArrayList<>();
        Iterator<PatientData> itr = patientDataList.iterator();
        Random rand = new Random();
        double d;
        while (itr.hasNext()) {
            d = rand.nextDouble();
            if (d < 0.8) {
                trainDataList.add(itr.next());
            } else {
                testDataList.add(itr.next());
            }
        }
    }

    public static void output(Object obj) {
        String nl = System.getProperty("line.separator");
        try {
            outputFile.write(obj + nl);
            outputFile.flush();
        } catch (Exception ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void log(Object obj) {
        String nl = System.getProperty("line.separator");
        try {
            logFile.write(obj + nl);
            logFile.flush();
        } catch (Exception ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void tree(Object obj) {
        String nl = System.getProperty("line.separator");
        try {
            treesFile.write(obj + nl);
            treesFile.flush();
        } catch (Exception ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String printTree(Node node) {
        int indent = 0;
        StringBuilder sb = new StringBuilder();
        printSubTree(node, indent, sb);
        Data.tree(sb.toString());
        return sb.toString();
    }

    private static void printSubTree(Node node, int indent, StringBuilder sb) {
        if (node == null) {
            return;
        }
        sb.append(getIndentString(indent));
        sb.append("+-------");
        if (node.getDecisionAttribute() == 0) {
            if (node.getLabel() == 1) {
                sb.append("Leaf:" + node.getLabel() + "(+)");
            }
            if (node.getLabel() == 0) {
                sb.append("Leaf:" + node.getLabel() + "(-)");
            }
        } else {
            sb.append("Node ~ DA:" + node.getDecisionAttribute());
        }

        sb.append("\n");

        Node tempNode;
        int tempPosVal;
        Iterator itr;
        if (node.getBranchMap() != null) {
            itr = node.getBranchMap().keySet().iterator();
        } else {
            return;
        }
        while (itr.hasNext()) {
            tempPosVal = (int) itr.next();
            tempNode = node.getBranchMap().get(tempPosVal);
            printSubTree(tempNode, indent + 1, sb);
        }
    }

    private static String getIndentString(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("|     ");
        }
        return sb.toString();
    }

    public static void calculateAvgPerformance() {
        Iterator itr = Data.accuracyList.iterator();
        double sum = 0.0;
        while (itr.hasNext()) {
            sum = sum + (Double) itr.next();
        }
        Data.avgAccuracy = (sum * 1.0) / Data.totalIterations;

        itr = Data.precisionList.iterator();
        sum = 0.0;
        while (itr.hasNext()) {
            sum = sum + (Double) itr.next();
        }
        Data.avgPrecision = (sum * 1.0) / Data.totalIterations;

        itr = Data.recallList.iterator();
        sum = 0.0;
        while (itr.hasNext()) {
            sum = sum + (Double) itr.next();
        }
        Data.avgRecall = (sum * 1.0) / Data.totalIterations;

        itr = Data.fMeasureList.iterator();
        sum = 0.0;
        while (itr.hasNext()) {
            sum = sum + (Double) itr.next();
        }
        Data.avgFMeasure = (sum * 1.0) / Data.totalIterations;

        itr = Data.gMeanList.iterator();
        sum = 0.0;
        while (itr.hasNext()) {
            sum = sum + (Double) itr.next();
        }
        Data.avgGMean = (sum * 1.0) / Data.totalIterations;
    }
}
