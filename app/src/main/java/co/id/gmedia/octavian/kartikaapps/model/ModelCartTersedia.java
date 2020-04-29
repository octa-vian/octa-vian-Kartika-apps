package co.id.gmedia.octavian.kartikaapps.model;

public class ModelCartTersedia {

    private String item1, item2, item3, item4, item5, item6, item7, item8, item9, item10, item11;
    private double jumlah;
    private boolean selected = false;

    public ModelCartTersedia(String item1, String item2){
        this.item1=item1;
        this.item2=item2;
    }

    public ModelCartTersedia(String item1, String item2, String item3){
        this.item1=item1;
        this.item2=item2;
        this.item3=item3;

    }
    public ModelCartTersedia(String item1, String item2, String item3, String item4){
        this.item1=item1;
        this.item2=item2;
        this.item3=item3;
        this.item4=item4;
    }

    public ModelCartTersedia(String item1, String item2, String item3, String item4, String item5){
        this.item1=item1;
        this.item2=item2;
        this.item3=item3;
        this.item4=item4;
        this.item5=item5;
    }

    public ModelCartTersedia(String item1, String item2, String item3, String item4, String item5, String item6, String item7, String item8, String item9, String item10, String item11){
        this.item1=item1;
        this.item2=item2;
        this.item3=item3;
        this.item4=item4;
        this.item5 =item5;
        this.item6=item6;
        this.item7=item7;
        this.item8=item8;
        this.item9=item9;
        this.item10=item10;
        this.item11=item11;
    }

    public ModelCartTersedia(String item1, String item2, String item3, String item4, String item5, String item6, String item7, String item8, String item9, String item10, String item11, boolean selected){
        this.item1=item1;
        this.item2=item2;
        this.item3=item3;
        this.item4=item4;
        this.item5 =item5;
        this.item6=item6;
        this.item7=item7;
        this.item8=item8;
        this.item9=item9;
        this.item10=item10;
        this.item11=item11;
        this.selected=selected;
    }

    public boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }



    public String getItem1() {
        return item1;
    }

    public void setItem1(String item1) {
        this.item1 = item1;
    }

    public String getItem2() {
        return item2;
    }

    public void setItem2(String item2) {
        this.item2 = item2;
    }

    public String getItem3() {
        return item3;
    }

    public void setItem3(String item3) {
        this.item3 = item3;
    }

    public String getItem4() {
        return item4;
    }

    public void setItem4(String item4) {
        this.item4 = item4;
    }

    public String getItem5() {
        return item5;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setItem5(String item5) {
        this.item5 = item5;
    }

    public String getItem6() {
        return item6;
    }

    public void setItem6(String item6) {
        this.item6 = item6;
    }

    public String getItem7() {
        return item7;
    }

    public void setItem7(String item7) {
        this.item7 = item7;
    }

    public String getItem8() {
        return item8;
    }

    public void setItem8(String item8) {
        this.item8 = item8;
    }

    public String getItem9() {
        return item9;
    }

    public void setItem9(String item9) {
        this.item9 = item9;
    }

    public String getItem10() {
        return item10;
    }
    public void setItem11(String item11){
        this.item11 = item11;
    }
    public String getItem11() {
        return item11;
    }

    public void setItem10(String item10) {
        this.item10 = item10;
    }
}
