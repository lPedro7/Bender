import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class Bender {

    LinkedList<Casilla> casillas = new LinkedList<>();
    LinkedList<Casilla> teleport = new LinkedList<>();
    LinkedList<Casilla> iterator = new LinkedList<>();

    LinkedList<Character> prioridad1 = new LinkedList<>();
    LinkedList<Character> prioridad2= new LinkedList<>();
    LinkedList<Character> prioridadActual= prioridad1;


    char[] pri1 = new char[]{'S', 'E', 'N', 'W'};
    char[] pri2 = new char[]{'N', 'W', 'S', 'E'};
    char[] currentPri = pri1;
    Casilla robot;
    Casilla meta;
    String resultado = "";


    // Constructor: ens passen el mapa en forma d'String
    public Bender(String mapa) {

        prioridad1.add('S');
        prioridad1.add('E');
        prioridad1.add('N');
        prioridad1.add('W');
        prioridad2.add('N');
        prioridad2.add('W');
        prioridad2.add('S');
        prioridad2.add('E');

        int x = 0;
        int y = 0;
        for (int i = 0; i < mapa.length(); i++) {
            Casilla c = new Casilla(i, x, y, mapa.charAt(i));

            if (mapa.charAt(i) == 'X') robot = c;
            if (mapa.charAt(i) == '$') meta = c;
            if (mapa.charAt(i) == 'T') teleport.add(c);
            if (mapa.charAt(i) == 'I') iterator.add(c);
            x++;

            if (mapa.charAt(i) == '\n') {
                y++;
                x = 0;
            }

            casillas.add(c);
        }

        dibuixaMapa(casillas);

    }

    // Navegar fins a l'objectiu («$»).
// El valor retornat pel mètode consisteix en una cadena de
// caràcters on cada lletra pot tenir
// els valors «S», «N», «W» o «E»,
// segons la posició del robot a cada moment.
    public String run() {
        int notMove;
        int posPrio;
        System.out.println("Position robot : x:" + this.robot.x + " y:" + this.robot.y);


        while(true) {
        notMove = 0;

        posPrio = 0;

            for (; posPrio < currentPri.length; ) {


                switch (currentPri[posPrio]) {
                    case 'N':
                        System.out.println("Move to Nort");
                        if (!move('N')) {

                            if (currentPri == pri1) {
                                if (notMove == 2) {
                                    posPrio++;
                                    notMove++;
                                } else posPrio = 0;
                            } else {
                                posPrio++;
                                notMove++;

                            }
                        } else {
                            notMove = 0;
                            if (isIterator()) {
                                System.out.println("Encima de un Iterator");
                                iterator();
                                if (currentPri == pri1){
                                    posPrio = 2;
                                }else {
                                    posPrio = 0;
                                }
                            }
                        }
                        break;
                    case 'S':
                        System.out.println("Move to South");
                        if (!move('S')) {
                            if (currentPri == pri1) {
                                if (notMove == 0) {
                                    posPrio++;
                                    notMove++;
                                } else posPrio = 0;
                            } else {
                                if (notMove == 2) {
                                    posPrio++;
                                    notMove++;
                                }else {
                                    posPrio = 0;
                                }

                            }

                        } else {
                            if (isIterator()) {
                                System.out.println("Encima de un Iterator");
                                iterator();
                                if (currentPri == pri1){
                                    posPrio = 0;
                                }else {
                                    posPrio = 2;
                                }
                            }
                            notMove = 0;
                        }
                        break;
                    case 'E':
                        System.out.println("Move to East");
                        if (!move('E')) {
                            if (currentPri == pri1) {
                                if (notMove == 1) {
                                    posPrio++;
                                    notMove++;
                                } else posPrio = 0;
                            } else {
                                if (notMove == 4) {
                                    posPrio++;
                                    notMove++;
                                }

                            }
                        } else {
                            notMove = 0;
                            if (isIterator()) {
                                System.out.println("Encima de un Iterator");
                                iterator();
                                notMove = 0;
                                if (currentPri == pri1){
                                    posPrio = 1;
                                }else {
                                    posPrio = 3;
                                }
                            }
                        }
                        break;
                    case 'W':
                        System.out.println("Move to Weast");
                        if (!move('W')) {
                            if (currentPri != pri1) {
                                if (notMove == 1) {
                                    posPrio++;
                                    notMove++;
                                } else {
                                    posPrio = 0;
                                }
                            } else posPrio = 0;


                        } else {
                            notMove = 0;
                            if (isIterator()) {
                                System.out.println("Encima de un Iterator");
                                iterator();
                                if (currentPri == pri1){
                                    posPrio = 3;
                                }else {
                                    posPrio = 1;
                                }
                            }
                        }
                        break;
                }

                if (isTeleport()) {
                    System.out.println("Encima de un teleport");
                    teleport();
                }

                dibuixaMapa(casillas);

                if (robot.x == meta.x && robot.y == meta.y) return resultado;
                if (resultado.length()>100)return null;
            }



        return "Imposible";
    }

}

    void dibuixaMapa(LinkedList<Casilla> c) {

        for (int i = 0; i < c.size(); i++) {
            System.out.print(c.get(i).car);
        }

    }


    boolean move(char orientation) {
        int index = robot.index;


        switch (orientation) {

            case 'N':
                if (getCasilla(robot.x, robot.y - 1).car == '#') {
                    System.out.println("Hay una pared, no puede seguir");
                    return false;
                } else {
                    System.out.println("Nos moveremos al norte");
                    casillas.set(index, new Casilla(index, robot.x, robot.y, ' '));
                    index = getIndex(robot.x, (robot.y - 1));
                    casillas.set(index, robot);
                    robot.y--;
                    robot.index = index;
                    this.resultado += 'N';
                    return true;
                }
            case 'E':
                if (getCasilla(robot.x + 1, robot.y).car == '#') {
                    System.out.println("Hay una pared, no puede seguir");
                    return false;
                } else {
                    System.out.println("Nos moveremos al este");
                    casillas.set(index, new Casilla(index, robot.x, robot.y, ' '));
                    index = getIndex(robot.x + 1, robot.y);
                    casillas.set(index, robot);
                    robot.x++;
                    robot.index = index;
                    this.resultado += 'E';
                    return true;
                }
            case 'S':
                if (getCasilla(robot.x, robot.y + 1).car == '#') {
                    System.out.println("Hay una pared, no puede seguir");
                    return false;
                } else {
                    System.out.println("Nos moveremos al sur");
                    casillas.set(index, new Casilla(index, robot.x, robot.y, ' '));
                    index = getIndex(robot.x, robot.y + 1);
                    casillas.set(index, robot);
                    robot.y++;
                    robot.index = index;
                    this.resultado += 'S';
                    return true;
                }

            case 'W':
                if (getCasilla(robot.x - 1, robot.y).car == '#') {
                    System.out.println("Hay una pared, no puede seguir");
                    return false;
                } else {
                    System.out.println("Nos moveremos al oeste ");
                    casillas.set(index, new Casilla(index, robot.x, robot.y, ' '));
                    index = getIndex(robot.x - 1, robot.y);
                    casillas.set(index, robot);
                    robot.x--;
                    robot.index = index;
                    this.resultado += 'W';
                    return true;
                }
        }
        return false;
    }


    Casilla getCasilla(int x, int y) {
        for (int i = 0; i < casillas.size(); i++) {
            if (casillas.get(i).x == x) {
                if (casillas.get(i).y == y) {
                    return casillas.get(i);
                }
            }
        }
        return null;
    }

    int getIndex(int x, int y) {

        for (int i = 0; i < casillas.size(); i++) {
            if ((casillas.get(i).x == x) && (casillas.get(i).y == y)) {

                return casillas.get(i).index;

            }
        }
        return 0;
    }

    boolean isTeleport() {

        for (int i = 0; i < teleport.size(); i++) {
            if (teleport.get(i).index == robot.index) {
                return true;
            }
        }
        return false;
    }

    void teleport() {

        if (teleport.size() == 2) {
            for (int i = 0; i < teleport.size(); i++) {

                if (!teleport.get(i).equals(robot)) {

                    casillas.set(robot.index, new Casilla(robot.index, robot.x, robot.y, ' '));
                    casillas.set(teleport.get(i).index, robot);
                    robot.x = teleport.get(i).x;
                    robot.y = teleport.get(i).y;
                    robot.index = teleport.get(i).index;
                    break;
                }

            }

        }

    }

    boolean isIterator() {

        for (int i = 0; i < iterator.size(); i++) {

            if (iterator.get(i).index == robot.index) return true;
        }
        return false;

    }

    void iterator() {
        if (currentPri == pri1) {
            currentPri = pri2;
        } else currentPri = pri1;
    }

}

class Casilla {

    int index;
    int x;
    int y;
    char car;

    Casilla(int index, int x, int y, char c) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.car = c;
    }

    Casilla(int x, int y, char c) {
        this.x = x;
        this.y = y;
        this.car = c;
    }

    void setCar(char car) {
        this.car = car;
    }

    char getCar(int x, int y) {
        return car;
    }

    void setIndex(int x, int y, int index) {
        this.index = index;
    }

    public boolean equals(Casilla c) {
        if (c.index == this.index) {
            if (c.x == this.x) {
                if (c.y == this.y) {
                    return true;
                }
            }
        }
        return false;
    }
}
