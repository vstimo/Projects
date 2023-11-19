package model;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Polinom {
    public Map<Integer, Integer> polinom;
    public Polinom(String p) {
        polinom = new HashMap<>();
        /* ---CE FACE REGEX-UL---
        * " [-+]?\\d+ " -> indica coeficientul care poate fi format din unul sau mai multe cifre(+) si care poate fi -/+
        * " x " -> este efectiv variabila x din polinom
        * " \\^(\\d+) " -> indica aparitia operatorului pentru exponent urmat de unul sau mai multe cifre la exponent
        * Operatorul " | " separa posibilitatile de aparitie, vezi mai jos
                   ------>   Monoame de forma:          -/+ ax^b      |   -/+ ax   |  -/+ a      -x^a         -/+  x^a       -x    x */
        Pattern pattern = Pattern.compile("([-+]?\\d+)x\\^(\\d+)|([-+]?\\d+)x|([-+]?\\d+)|-x\\^(\\d+)|[-+]?x\\^(\\d+)|(-x)|([+]?x)");
        Matcher matcher = pattern.matcher(p);
        while(matcher.find()){ //atat timp cat gasim un monom care sa se potriveasca in pattern-ul nostru
            String coeficient=null, exponent=null;
            if(matcher.group(1)!=null && matcher.group(2)!=null){
                coeficient=matcher.group(1); exponent=matcher.group(2);
            }
            else if(matcher.group(3) != null){
                coeficient = matcher.group(3); exponent = "1";
            }
            else if(matcher.group(4) !=null){
                coeficient = matcher.group(4); exponent = "0";
            }
            else if(matcher.group(5)!=null){
                coeficient="-1"; exponent= matcher.group(5);
            }
            else if(matcher.group(6)!=null){
                coeficient="1"; exponent= matcher.group(6);
            }
            else if(matcher.group(7) != null){
                coeficient="-1"; exponent="1";
            }
           else if(matcher.group(8)!=null){
                    coeficient="1"; exponent="1";
                }
            polinom.put(Integer.parseInt(exponent),Integer.parseInt(coeficient));
        }
    }

    public Polinom(){
        polinom = new HashMap<>();
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
    public int findMaxExpD(Map<Integer, Double> p){ //gasim exponentul maxim pentru polinoame cu coeficienti reali
        int max=-1, contor=0, k=0;
        while(k<p.size()){
            if(p.get(contor)!=null)
                if(max<contor)
                {k++; max=contor;}
            contor++;
        }
        return max;
    }
    public String polToString(Map<Integer,Integer> p){
        String rezultat = "";
        int max=this.findMaxExp(p);
        while(max>=0){
            if(p.get(max)!=null){
                int c=p.get(max);
                    if(rezultat.length() > 0){ //if pentru primul monom din polinomul rezultat
                        if(c>0) rezultat=rezultat + " + ";
                        else {rezultat=rezultat + " - "; c=-c;}
                    }
                    else if(c<0) {rezultat=rezultat + " - "; c=-c;}

                    if(max==0) rezultat = rezultat + c;
                    else if (max==1) {
                        if (c != 1) rezultat = rezultat + c + "x";
                        else rezultat = rezultat + "x";
                    }
                    else {
                        if(c!=1) rezultat = rezultat + c +"x^"+max;
                        else rezultat = rezultat + "x^"+max;
                    }
            }
            max--;
        }
        return rezultat;
    }
    public String polToStringD(Map<Integer,Double> p){ //pentru polinaome cu coeficienti reali
        String rezultat = "";
        int max=this.findMaxExpD(p);
        while(max>=0){
            if(p.get(max)!=null){
                double c=p.get(max);
                if(rezultat.length() > 0){ //if pentru primul monom din polinomul rezultat
                    if(c>0) rezultat=rezultat + " + ";
                    else {rezultat=rezultat + " - "; c=-c;}
                }
                else if(c<0) {rezultat=rezultat + " - "; c=-c;}

                if(max==0) rezultat = rezultat + c;
                else if (max==1) {
                    if (c != 1) rezultat = rezultat + c + "x";
                    else rezultat = rezultat + "x";
                }
                else {
                    if(c!=1) rezultat = rezultat + c +"x^"+max;
                    else rezultat = rezultat + "x^"+max;
                }
            }
            max--;
        }
        return rezultat;
    }
    public Map<Integer, Integer> getPolinom() {
        return polinom;
    }
}