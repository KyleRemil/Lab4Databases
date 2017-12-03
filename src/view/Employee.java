package model;

public class Employee {

    public String fname, lname;
    public int supper_ssn, dno, ssn;

    public Employee(String fname, String lname, int supper_ssn, int dno, int ssn){
        this.fname = fname;
        this.lname = lname;
        this.supper_ssn = supper_ssn;
        this.dno = dno;
        this.ssn = ssn;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getSupper_ssn() {
        return supper_ssn;
    }

    public void setSupper_ssn(int supper_ssn) {
        this.supper_ssn = supper_ssn;
    }

    public int getDno() {
        return dno;
    }

    public void setDno(int dno) {
        this.dno = dno;
    }

    public int getSsn() {
        return ssn;
    }

    public void setSsn(int ssn) {
        this.ssn = ssn;
    }
}
