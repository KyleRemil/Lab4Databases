package model;

public class Department {

    private String Dname, Dlocation;
    private int Dnumber, Mgr_ssn;

    public Department(String dname, String dlocation, int dnumber, int mgr_ssn) {
        this.Dname = dname;
        this.Dlocation = dlocation;
        this.Dnumber = dnumber;
        this.Mgr_ssn = mgr_ssn;
    }
    public String getDname() {
        return Dname;
    }

    public void setDname(String dname) {
        Dname = dname;
    }

    public String getDlocation() {
        return Dlocation;
    }

    public void setDlocation(String dlocation) {
        Dlocation = dlocation;
    }

    public int getDnumber() {
        return Dnumber;
    }

    public void setDnumber(int dnumber) {
        Dnumber = dnumber;
    }

    public int getMgr_ssn() {
        return Mgr_ssn;
    }

    public void setMgr_ssn(int mgr_ssn) {
        Mgr_ssn = mgr_ssn;
    }

}
