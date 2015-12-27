
public class PatientData {

    public int identifier;

    // clumpThickness
    public int attr1;
    // cellSizeUniformity
    public int attr2;
    // cellShapeUniformity
    public int attr3;
    // marginalAdhesion
    public int attr4;
    // singleEpithelialCellSize
    public int attr5;
    // bareNuclei
    public int attr6;
    // blandChromatin
    public int attr7;
    // normalNucleoli
    public int attr8;
    // mitoses
    public int attr9;

    // benign 0 (not cancerous) -
    // malignant 1 (cancerous)  + 
    public int tumorClass;

    public PatientData(int identifier, int attr1, int attr2, int attr3, int attr4, int attr5, int attr6, int attr7, int attr8, int attr9, int tumorClass) {
        this.identifier = identifier;
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.attr3 = attr3;
        this.attr4 = attr4;
        this.attr5 = attr5;
        this.attr6 = attr6;
        this.attr7 = attr7;
        this.attr8 = attr8;
        this.attr9 = attr9;
        this.tumorClass = tumorClass;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public int getAttr1() {
        return attr1;
    }

    public int getAttr2() {
        return attr2;
    }

    public int getAttr3() {
        return attr3;
    }

    public int getAttr4() {
        return attr4;
    }

    public int getAttr5() {
        return attr5;
    }

    public int getAttr6() {
        return attr6;
    }

    public int getAttr7() {
        return attr7;
    }

    public int getAttr8() {
        return attr8;
    }

    public int getAttr9() {
        return attr9;
    }

    public int getTumorClass() {
        return tumorClass;
    }

    // attribute indexing starts from 1 (not 0)
    public int getAttributeValue(int i) {
        switch (i) {
            case 1:
                return getAttr1();
            case 2:
                return getAttr2();
            case 3:
                return getAttr3();
            case 4:
                return getAttr4();
            case 5:
                return getAttr5();
            case 6:
                return getAttr6();
            case 7:
                return getAttr7();
            case 8:
                return getAttr8();
            case 9:
                return getAttr9();
            default:
                return -1;
        }
    }
}
