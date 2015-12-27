
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Learning {

    public List<PatientData> trainData;
    public Set<Integer> attributeIndexSet;
    public Set<Integer> possibleValues;
    public Map<Integer, Integer> testDecisions;
    public int totalAttributes;

    public Learning(List<PatientData> td, int n) {
        this.trainData = new ArrayList<>(td);
        this.totalAttributes = n;
        this.attributeIndexSet = new HashSet<>(totalAttributes);
        this.possibleValues = new HashSet<>(totalAttributes);
        this.testDecisions = new HashMap<>();
        // generating attibute index set 1 - 9
        for (int i = 1; i <= totalAttributes; i++) {
            attributeIndexSet.add(i);
        }
        // generating all possible value set (1 - 10)
        for (int i = 1; i <= 10; i++) {
            possibleValues.add(i);
        }
    }

    // attribute indexing starts from 1 (not 0)
    public Node id3(List<PatientData> examples, int targetAttribute, Set<Integer> attributes) {
        Data.log("");
        Data.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Data.log("");
        Node node = new Node();

        if (areAllPositive(examples)) {
            node.setLabel(1);
            Data.log("All +");
            return node;
        }
        if (areAllNegative(examples)) {
            node.setLabel(0);
            Data.log("All -");
            return node;
        }

        if (attributes.isEmpty()) {
            node.setLabel(getMostCommonValue(examples));
            Data.log("Attribute set empty");
            return node;
        }

        int bestClassifierAttibute = chooseBestClassifier(examples, attributes);
        node.setDecisionAttribute(bestClassifierAttibute);
        Data.log("$$$Best classifier is attribute " + bestClassifierAttibute);

        node.initBranching(possibleValues);
        // for each possible value separate the example set
        Iterator itr = examples.iterator();
        PatientData tempPatData;
        int tempAttrVal;
        Iterator tempItr;
        int tempPosVal;
        while (itr.hasNext()) {
            tempPatData = (PatientData) itr.next();
            tempAttrVal = tempPatData.getAttributeValue(bestClassifierAttibute);
            tempItr = possibleValues.iterator();
            while (tempItr.hasNext()) {
                tempPosVal = (int) tempItr.next();
                if (tempAttrVal == tempPosVal) {
                    node.branchMap.get(tempPosVal).examples.add(tempPatData);
                    break;
                }
            }
        }

        // logging the count of values of best classifier attribute
        // for each possible values compute the subtree or return a leaf node
        itr = possibleValues.iterator();
        List<PatientData> tempExample;
        Set<Integer> tempAttributes;
        while (itr.hasNext()) {
            tempPosVal = (int) itr.next();
            tempExample = node.branchMap.get(tempPosVal).examples;
            Data.log("Total #of " + tempPosVal + " @" + bestClassifierAttibute + " : " + tempExample.size());
            // example(vi) is empty
            if (tempExample.isEmpty()) {
                node.branchMap.get(tempPosVal).setLabel(getMostCommonValue(examples));
            } else {
                tempAttributes = new HashSet<>(attributes);
                tempAttributes.remove(bestClassifierAttibute);
                Data.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>");
                Data.log(attributes.toString());
                node.branchMap.put(tempPosVal, id3(tempExample, targetAttribute, tempAttributes));
            }
        }

        return node;
    }

    public boolean areAllPositive(List<PatientData> examples) {
        Iterator itr = examples.iterator();
        PatientData temp;

        while (itr.hasNext()) {
            temp = (PatientData) itr.next();
            if (temp.getTumorClass() == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean areAllNegative(List<PatientData> examples) {
        Iterator itr = examples.iterator();
        PatientData temp;

        while (itr.hasNext()) {
            temp = (PatientData) itr.next();
            if (temp.getTumorClass() == 1) {
                return false;
            }
        }
        return true;
    }

    public int getMostCommonValue(List<PatientData> examples) {
        Iterator itr = examples.iterator();
        PatientData temp;

        int totalP = 0;
        int totalN = 0;
        while (itr.hasNext()) {
            temp = (PatientData) itr.next();
            if (temp.getTumorClass() == 1) {
                totalP++;
            } else {
                totalN++;
            }
        }
        if (Math.max(totalP, totalN) == totalP) {
            return 1;
        } else {
            return 0;
        }
    }

    public int chooseBestClassifier(List<PatientData> examples, Set<Integer> attributes) {
        double currentEntropy = getEntropy(examples);
        Data.log("Current entropy : " + currentEntropy);
        Iterator itr = attributes.iterator();
        PatientData temp;
        int tempAttr;

        Map<Integer, Double> infoGains = new HashMap<>();
        double maxIG = -999;
        double tempIG;
        int bestClassifier = -1;
        while (itr.hasNext()) {
            tempAttr = (int) itr.next();
            tempIG = currentEntropy - getExpectedEntropy(examples, tempAttr);
            infoGains.put(tempAttr, tempIG);
            Data.log("Information gain for attribute " + tempAttr + " : " + tempIG);
            if (maxIG < tempIG) {
                maxIG = tempIG;
                bestClassifier = tempAttr;
            }
        }

        Data.log("");
        itr = attributes.iterator();
        while (itr.hasNext()) {
            tempAttr = (int) itr.next();
            Data.log("Information gain for attribute " + tempAttr + " : " + infoGains.get(tempAttr));
        }

//        Data.log("***Best classifier is attribute " + bestClassifier + " with info gain : " + maxIG);
        return bestClassifier;
    }

    public double getEntropy(List<PatientData> examples) {
        if (examples.isEmpty()) {
            return 0;
        }
        Iterator itr = examples.iterator();
        PatientData temp;

        int totalP = 0;
        int totalN = 0;
        while (itr.hasNext()) {
            temp = (PatientData) itr.next();
            if (temp.getTumorClass() == 1) {
                totalP++;
            } else {
                totalN++;
            }
        }

        double probP = totalP * 1.0 / examples.size();
        double probN = totalN * 1.0 / examples.size();

        double hP = 0;
        double hN = 0;
        if (probP != 0) {
            hP = -probP * log2(probP);
        }
        if (probN != 0) {
            hN = -probN * log2(probN);
        }

        Data.log("totalP : " + totalP);
        Data.log("totalN : " + totalN);
        Data.log("probP : " + probP);
        Data.log("probN : " + probN);
        Data.log("Train Entropy : " + (hP + hN));
        return hP + hN;
    }

    public double getExpectedEntropy(List<PatientData> examples, int tempAttr) {
        Iterator itr;
        PatientData temp;
        double expectedEntropy = 0;

        Map<Integer, List> exampleDataByValue = new HashMap<>();

        itr = possibleValues.iterator();
        int tempPosAttr;
        while (itr.hasNext()) {
            tempPosAttr = (int) itr.next();
            exampleDataByValue.put(tempPosAttr, new ArrayList<>());
        }

        itr = examples.iterator();
        Iterator itr2;
        while (itr.hasNext()) {
            temp = (PatientData) itr.next();
            itr2 = possibleValues.iterator();
            while (itr2.hasNext()) {
                tempPosAttr = (int) itr2.next();
                if (temp.getAttributeValue(tempAttr) == tempPosAttr) {
                    exampleDataByValue.get(tempPosAttr).add(temp);
                }
            }
        }

        Data.log("++++++++++++++++++++++++");
        Data.log("For attribute : " + tempAttr);

        itr = possibleValues.iterator();
        double wE;
        List<PatientData> tempList;
        while (itr.hasNext()) {
            tempPosAttr = (int) itr.next();
            tempList = exampleDataByValue.get(tempPosAttr);
            Data.log("Total : " + tempList.size());
            wE = tempList.size() * 1.0 * getEntropy(tempList) / examples.size();
            expectedEntropy = expectedEntropy + wE;
            Data.log("Weighted Entropy for value " + tempPosAttr + " : " + wE);
        }
        Data.log("Expected entropy for attribute " + tempAttr + " : " + expectedEntropy);
        return expectedEntropy;
    }

    public double log2(double x) {
        return Math.log10(x) / Math.log10(2);
    }

    public void decide(Node root, List<PatientData> testDataList) {
        Iterator itr = testDataList.iterator();
        PatientData tempData;
        while (itr.hasNext()) {
            tempData = (PatientData) itr.next();
            testDecisions.put(tempData.getIdentifier(), root.decideClass(tempData));
        }
    }

    public void outputDecisions() {
        Iterator itr = testDecisions.keySet().iterator();
        int tK;
        int i = 1;
        while (itr.hasNext()) {
            tK = (int) itr.next();
            Data.output(i + ".\t\t\t" + tK + "\t\t~\t\t" + testDecisions.get(tK));
            i++;
        }
    }

    public void calculateTestPermance() {
        int TP = 0;
        int FP = 0;
        int TN = 0;
        int FN = 0;
        Iterator itr = Data.testDataList.iterator();
        PatientData pD;
        int prediction;
        int actual;
        while (itr.hasNext()) {
            pD = (PatientData) itr.next();
            actual = pD.getTumorClass();
            prediction = testDecisions.get(pD.getIdentifier());
            if (actual == 1 && prediction == 1) {
                TP++;
            } else if (actual == 0 && prediction == 1) {
                FP++;
            } else if (actual == 1 && prediction == 0) {
                FN++;
            } else if (actual == 0 && prediction == 0) {
                TN++;
            }
        }

        double accuracy = ((TP + TN) * 1.0 * 100) / Data.testDataList.size();
        double precision = (TP * 1.0) / (TP + FP);
        double recall = (TP * 1.0) / (TP + FN);
        double fMeasure = (2 * recall * precision * 1.0) / (recall + precision);
        double gMean = Math.sqrt((TP * TN * 1.0) / ((TP + FN) * (FP + TN)));

        Data.accuracyList.add(accuracy);
        Data.precisionList.add(precision);
        Data.recallList.add(recall);
        Data.fMeasureList.add(fMeasure);
        Data.gMeanList.add(gMean);

        Data.output("Total test data: "+Data.testDataList.size());
        Data.output("TP\t:\t"+TP);
        Data.output("FN\t:\t"+FN);
        Data.output("FP\t:\t"+FP);
        Data.output("TN\t:\t"+TN);
        Data.output("");
        Data.output("Accuracy\t:\t" + accuracy);
        Data.output("Precision\t:\t" + precision);
        Data.output("Recall\t\t:\t" + recall);
        Data.output("F-measure\t:\t" + fMeasure);
        Data.output("G-mean\t\t:\t" + gMean);
        Data.output("");
    }
}
