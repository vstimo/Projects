package model;

import java.util.HashMap;
import java.util.Map;
public class Operatii {
    public Operatii(){
    }
    public int findMaxExp(Map<Integer, Integer> p){ //gasim exponentul maxim
        int max=-1, contor=0, k=0;
        while(k<p.size()){
            if(p.get(contor)!=null)
                if(max<contor)
                {k++; max=contor;}
            contor++;
        }
        return max;
    }
    public Map<Integer, Integer> adunare(Map<Integer, Integer> p1, Map<Integer, Integer> p2){
        Map<Integer, Integer> rezultat = new HashMap<>();
        int m1=this.findMaxExp(p1), m2=this.findMaxExp(p2),max;
        if(m1<m2) max=m2;
        else max=m1;
        while(max>-1){
            int c1=0,c2=0;
            if(p1.get(max)!=null)
                c1=p1.get(max);
            if(p2.get(max)!=null)
                c2=p2.get(max);
            if(c1+c2!=0) rezultat.put(max,c1+c2);
            max--;
        }
        return rezultat;
    }
    public Map<Integer, Integer> scadere(Map<Integer, Integer> p1, Map<Integer, Integer> p2){
        Map<Integer, Integer> rezultat = new HashMap<>();
        int m1=this.findMaxExp(p1), m2=this.findMaxExp(p2),max;
        if(m1<m2) max=m2;
        else max=m1;
        while(max>-1){
            int c1=0,c2=0;
            if(p1.get(max)!=null)
                c1=p1.get(max);
            if(p2.get(max)!=null)
                c2=p2.get(max);
            if(c1-c2!=0) rezultat.put(max,c1-c2);
            max--;
        }
        return rezultat;
    }
    public Map<Integer, Integer> derivare(Map<Integer, Integer> p){
        Map<Integer, Integer> rezultat = new HashMap<>();
        int max=this.findMaxExp(p);
        while(max>=1){
            if(p.get(max)!=null) {
                int coeficient = p.get(max) * max;
                rezultat.put(max-1, coeficient);
            }
            max--;
        }
        return rezultat;
    }
    public Map<Integer, Double> integrare(Map<Integer, Integer> p){
        Map<Integer, Double> rezultat = new HashMap<>();
        int max=this.findMaxExp(p);
        while(max>=0){
            if(p.get(max)!=null){
                double coeficient = Math.round((double)p.get(max)/(max+1)*100)/100.0;
                rezultat.put(max+1,coeficient);
            }
            max--;
        }
        return rezultat;
    }
    public Map<Integer, Integer> inmultire(Map<Integer, Integer> p1, Map<Integer, Integer> p2){
        Map<Integer, Integer> rezultat = new HashMap<>();
        int m1=this.findMaxExp(p1);
        while(m1>=0){
            int m2=this.findMaxExp(p2);
            if(p1.get(m1)!=null)
                while(m2>=0){
                    if(p2.get(m2)!=null)
                        if(rezultat.get(m1+m2)==null)
                        rezultat.put(m1+m2,p1.get(m1)*p2.get(m2));
                        else rezultat.put(m1+m2,p1.get(m1)*p2.get(m2)+rezultat.get(m1+m2));
                    m2--;
                }
            m1--;
        }
        return rezultat;
    }
    public Map<Integer, Double> impartire(Map<Integer, Integer> p1, Map<Integer, Integer> p2){
        Map<Integer, Double> rezultat = new HashMap<>();
        int m1=this.findMaxExp(p1), m2=this.findMaxExp(p2),mH,mL;
        Map<Integer, Integer> pH, pL;
        if(m1<m2) {pH=p2; pL=p1; mH=m2; mL=m1;}
        else {pH=p1; pL=p2; mH=m1; mL=m2;}
        while(mH>=mL){
            while(pH.get(mH)==null && mH>=0) mH--;
            if(mH>=0 && mL>=0){
            double c= Math.round((double)pH.get(mH)/pL.get(mL)*100/100);
            int e=mH-mL;
            rezultat.put(e,c);
            //System.out.println("Monom la cat: --Coeficient - exponent " + c + "  " + e);
            int aux=mL;
            Map<Integer, Integer> inmultireAuxiliara = new HashMap<>();
            while(aux>=0){
                if(pL.get(aux)!=null) {
                    int exponent=e+aux, coeficient=(int) Math.ceil(c * pL.get(aux));
                    inmultireAuxiliara.put(exponent, coeficient);
                    //System.out.println("Monom("+aux+") de scazut: ---Coeficient - exponent " + coeficient + "  " + exponent);
                }
                aux--;
            }
            //System.out.println(inmultireAuxiliara);
            pH=this.scadere(pH,inmultireAuxiliara);
            //System.out.println(pH);
            mH--;}
        }
        Polinom p = new Polinom();
        String rest= p.polToString(pH);
        System.out.println("Restul: "+rest);
        return rezultat;
    }
}
