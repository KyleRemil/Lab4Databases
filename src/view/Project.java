package model;

public class Project {
    private String pname, plocation;
    private int pnumber, dnum;

    public Project(String pName, int pNumber, String pLocation, int dNum){
        this.pname = pName;
        this.pnumber = pNumber;
        this.plocation = pLocation;
        this.dnum =dNum;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPlocation() {
        return plocation;
    }

    public void setPlocation(String plocation) {
        this.plocation = plocation;
    }

    public int getPnumber() {
        return pnumber;
    }

    public void setPnumber(int pnumber) {
        this.pnumber = pnumber;
    }

    public int getDnum() {
        return dnum;
    }

    public void setDnum(int dnum) {
        this.dnum = dnum;
    }
}
