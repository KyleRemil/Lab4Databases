package model;

public class WorksOn {
    private int essn, pno, hours;

    public WorksOn(int essn, int pno, int hours){
        this.essn = essn;
        this.pno = pno;
        this.hours = hours;
    }

    public int getEssn() {
        return essn;
    }

    public void setEssn(int essn) {
        this.essn = essn;
    }

    public int getPno() {
        return pno;
    }

    public void setPno(int pno) {
        this.pno = pno;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
}
