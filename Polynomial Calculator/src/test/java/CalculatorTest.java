import model.Operatii;
import model.Polinom;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class CalculatorTest {
    @Test
    public void adunare() {
        Operatii op = new Operatii();
        Polinom p = new Polinom();
        Map<Integer, Integer> polinom1=new HashMap<>(); //2x^4+3x^3-x^2-3x-1
        polinom1.put(4,2);
        polinom1.put(3,3);
        polinom1.put(2,-1);
        polinom1.put(1,-3);
        polinom1.put(0,-1);
        Map<Integer, Integer> polinom2=new HashMap<>(); //2x^2+3x+1
        polinom2.put(2,2);
        polinom2.put(1,3);
        polinom2.put(0,1);
        Map<Integer, Integer> adunare = op.adunare(polinom1, polinom2);
        assertEquals("2x^4 + 3x^3 + x^2", p.polToString(adunare));
    }
    @Test
    public void scadere(){
        Operatii op = new Operatii();
        Polinom p = new Polinom();
        Map<Integer, Integer> polinom1=new HashMap<>(); //2x^4+3x^3-x^2-3x-1
        polinom1.put(4,2);
        polinom1.put(3,3);
        polinom1.put(2,-1);
        polinom1.put(1,-3);
        polinom1.put(0,-1);
        Map<Integer, Integer> polinom2=new HashMap<>(); //2x^2+3x+1
        polinom2.put(2,2);
        polinom2.put(1,3);
        polinom2.put(0,1);
        Map<Integer, Integer> scadere=op.scadere(polinom1,polinom2);
        assertEquals("2x^4 + 3x^3 - 3x^2 - 6x - 2",p.polToString(scadere));
    }
    @Test
    public void derivare(){
        Operatii op = new Operatii();
        Polinom p = new Polinom();
        Map<Integer, Integer> polinom1=new HashMap<>(); //2x^4+3x^3-x^2-3x-1
        polinom1.put(4,2);
        polinom1.put(3,3);
        polinom1.put(2,-1);
        polinom1.put(1,-3);
        polinom1.put(0,-1);
        Map<Integer, Integer> derivare=op.derivare(polinom1);
        assertEquals("8x^3 + 9x^2 - 2x - 3",p.polToString(derivare));
    }
    @Test
    public void integrare(){
        Operatii op = new Operatii();
        Polinom p = new Polinom();
        Map<Integer, Integer> polinom1=new HashMap<>(); //DIFERIIIIT  - > 4x^3+3x^2+2x+1
        polinom1.put(3,4);
        polinom1.put(2,3);
        polinom1.put(1,2);
        polinom1.put(0,1);
        Map<Integer, Double> integrare=op.integrare(polinom1);
        assertEquals("x^4 + x^3 + x^2 + x",p.polToStringD(integrare));
    }
    @Test
    public void inmultire(){
        Operatii op = new Operatii();
        Polinom p = new Polinom();
        Map<Integer, Integer> polinom1=new HashMap<>(); //2x^2+3x+1
        polinom1.put(2,2);
        polinom1.put(1,3);
        polinom1.put(0,1);
        Map<Integer, Integer> polinom2=new HashMap<>(); //x^2-1
        polinom2.put(2,1);
        polinom2.put(0,-1);
        Map<Integer, Integer> inmultire=op.inmultire(polinom1,polinom2);
        assertEquals("2x^4 + 3x^3 - x^2 - 3x - 1",p.polToString(inmultire));
    }
    @Test
    public void impartire(){
        Operatii op = new Operatii();
        Polinom p = new Polinom();
        Map<Integer, Integer> polinom1=new HashMap<>(); //2x^4+3x^3-x^2-3x-1
        polinom1.put(4,2);
        polinom1.put(3,3);
        polinom1.put(2,-1);
        polinom1.put(1,-3);
        polinom1.put(0,-1);
        Map<Integer, Integer> polinom2=new HashMap<>(); //2x^2+3x+1
        polinom2.put(2,2);
        polinom2.put(1,3);
        polinom2.put(0,1);
        Map<Integer, Double> impartire=op.impartire(polinom1,polinom2);
        assertEquals("x^2 - 1.0",p.polToStringD(impartire));
    }
}