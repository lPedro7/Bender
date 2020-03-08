import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Classe Bender, realitza la creació del mapa i el recorregut del robot dins el mateix
class Bender {

    // Cream diferents listes per als diferents elements que podem tenir dintre del mapa
    // El mapa está format per caselles, i una casella pot servir per una cosa distinta.
    // Tenim caselles que poden ser teletransportadors, iteradors, el robot, o el punt final
    private List<Casilla> casillas = new ArrayList<>();
    private List<Casilla> teleport = new ArrayList<>();
    private List<Casilla> iterator = new ArrayList<>();

    // Definim l'ordre de prioritats de moviment del robot
    private char[] pri1 = new char[]{'S', 'E', 'N', 'W'};
    private char[] pri2 = new char[]{'N', 'W', 'S', 'E'};
    private char[] currentPri = pri1;
    private Casilla robot;
    private Casilla meta;
    private String result = "";


    // Constructor: ens passen el mapa en forma d'String
    Bender(String mapa) {

        //Emprarem la llista que tenim creada per la creació del mapa,
        //Cada casella tindrá una posició x, y, a més d'un índex que serà la seva posició a la llista
        int x = 0;
        int y = 0;

        //Recorrem la longitut del mapa, anirem agafant el caràcter corresponent a cada una de les posicions
        // i afegint-lo a la llista, si ens trobam un salt de línia , haurem de incrementar 1 a y, i tornar 0 a x.
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


    }

    // Navegar fins a l'objectiu («$»).
// El valor retornat pel mètode consisteix en una cadena de
// caràcters on cada lletra pot tenir
// els valors «S», «N», «W» o «E»,
// segons la posició del robot a cada moment.
    String run() {
        // Cream dues variables, notMove fa referència al nombre de vegades que duim sense fer cap moviment
        int notMove;
        int posPrio;


        notMove = 0;

        posPrio = 0;

        // Hem de recòrrer l'array de prioritats que tinguem assignat
        for (; posPrio < currentPri.length; ) {


            //Segons la posició que toqui farem el seu moviment corresponent
            //Tenim en compte la posició del moviment a l'array, ja que si canviam la prioritat, haurem de anar canviant
            // posicions
            switch (currentPri[posPrio]) {

                // Tenim 4 possibles casos, 'N' ( North ) , 'S' ( South ), 'E' ( East ), 'W' ( West )
                // En cas de que quan intentem moure cap a aquesta direcció, ens retorni un false, hem de
                // veure si agafam la següent posició o tornam a començar del principi.
                // Si ens movem resetetjam la variable que indica els "torns" que duim sense moure

                case 'N':


                    if (!canMove('N')) {
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
                        // Si ens movem, haurem de situar el robot a la següent casella a la llista i a la casella actual
                        // posam una nova casella buida, també cambiam les variables X o Y depenent d'on ens moguem
                        // Afegim el moviment al resultat
                        notMove = 0;
                        int index = robot.index;
                        casillas.set(index, new Casilla(index, robot.x, robot.y, ' '));
                        index = Objects.requireNonNull(getCasilla(robot.x, robot.y - 1)).index;
                        casillas.set(index, robot);
                        robot.y--;
                        robot.index = index;
                        this.result += 'N';

                        //Comprovam si la casella actual forma part de la llista d'iterators o de teletransportadors
                        if (isIterator()) {

                            iterator();
                            if (currentPri == pri1) {
                                posPrio = 2;
                            } else {
                                posPrio = 0;
                            }
                        }
                        if (isTeleport()) {

                            teleport();
                        }
                    }
                    break;
                case 'S':

                    if (!canMove('S')) {
                        if (currentPri == pri1) {
                            if (notMove == 0) {
                                posPrio++;
                                notMove++;
                            } else posPrio = 0;
                        } else {
                            if (notMove == 2) {
                                posPrio++;
                                notMove++;
                            } else {
                                posPrio = 0;
                            }

                        }

                    } else {

                        int index = robot.index;

                        casillas.set(index, new Casilla(index, robot.x, robot.y, ' '));
                        index = Objects.requireNonNull(getCasilla(robot.x, robot.y + 1)).index;
                        casillas.set(index, robot);
                        robot.y++;
                        robot.index = index;
                        this.result += 'S';

                        if (isIterator()) {

                            iterator();
                            if (currentPri == pri1) {
                                posPrio = 0;
                            } else {
                                posPrio = 2;
                            }
                        }
                        if (isTeleport()) {

                            teleport();
                        }
                        notMove = 0;
                    }
                    break;
                case 'E':

                    if (!canMove('E')) {
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

                        int index = robot.index;

                        casillas.set(index, new Casilla(index, robot.x, robot.y, ' '));
                        index++;
                        casillas.set(index, robot);
                        robot.x++;
                        robot.index = index;
                        this.result += 'E';

                        if (isIterator()) {

                            iterator();
                            notMove = 0;
                            if (currentPri == pri1) {
                                posPrio = 1;
                            } else {
                                posPrio = 3;
                            }
                        }
                        if (isTeleport()) {

                            teleport();
                        }
                    }
                    break;
                case 'W':

                    if (!canMove('W')) {
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

                        int index = robot.index;

                        casillas.set(index, new Casilla(index, robot.x, robot.y, ' '));
                        index--;
                        casillas.set(index, robot);
                        robot.x--;
                        robot.index = index;
                        this.result += 'W';

                        if (isIterator()) {

                            iterator();
                            if (currentPri == pri1) {
                                posPrio = 3;
                            } else {
                                posPrio = 1;
                            }
                        }
                        if (isTeleport()) {

                            teleport();
                        }
                    }
                    break;
            }

            // Una vegada fet el moviment, comprovam si som a un teletransportador i cridam a la funció teleport


            //A mesura que feim un moviment, anem dibuixant el mapa per a veure el recorregut
            //     dibuixaMapa(casillas);

            // Una vegada que la posició del robot sigui el mateix que el de la meta, retornarem el resultat
            if (robot.equals(meta)) return result;
            // Declaram que un mapa es impossible quan duim més de 100 moviments
            if (result.length() > this.casillas.size()) return null;
        }


        return "Imposible";


    }

    // Funció que realitza el moviment del robot, retorna un boolean
    private boolean canMove(char orientation) {

        switch (orientation) {

            case 'N':
                //Si a la posició a la que ens volem moure ens trobam una casella amb caràcter '#', retornarem un
                // false, ja que hem trobat una paret, si no, retornarem true
                if (getCasilla(robot.x, robot.y - 1).car == '#') {
                    return false;
                } else {

                    return true;

                }
            case 'E':
                if (getCasilla(robot.x + 1, robot.y).car == '#') {

                    return false;
                } else {

                    return true;

                }
            case 'S':
                if (getCasilla(robot.x, robot.y + 1).car == '#') {

                    return false;
                } else {

                    return true;

                }

            case 'W':
                if (getCasilla(robot.x - 1, robot.y).car == '#') {
                    return false;
                } else {

                    return true;

                }
        }
        return false;
    }

    //Aquesta funció te l'objectiu de retornar l'objecte casella segons les coordenades que li passem
    private Casilla getCasilla(int x, int y) {
        for (int i = 0; i < casillas.size(); i++) {
            if (casillas.get(i).x == x) {
                if (casillas.get(i).y == y) {
                    return casillas.get(i);
                }
            }
        }
        return null;
    }

    //Comprova que la casella ons ens situam es un teletransportador
    private boolean isTeleport() {

        for (int i = 0; i < teleport.size(); i++) {

            if (teleport.get(i).equals(robot)) {
                return true;
            }

        }
        return false;
    }

    //Realitza la teletransportació del robot des de un teletransportador fins altre més próxim
    private void teleport() {

        // Si només hi ha 2 teletransportadors al mapa, aniran d'un al altre
        if (teleport.size() == 2) {
            for (int i = 0; i < teleport.size(); i++) {

                if (!teleport.get(i).equals(robot)) {

                    casillas.set(robot.index, new Casilla(robot.index, robot.x, robot.y, 'T'));
                    casillas.set(teleport.get(i).index, robot);
                    robot.x = teleport.get(i).x;
                    robot.y = teleport.get(i).y;
                    robot.index = teleport.get(i).index;
                    break;
                }

            }

        } else {

            int minim = 999999999;
            int iTp = 0;
            int xTp = 0;
            int yTp = 0;

            int posRobot = robot.x + robot.y;

            boolean mismaDistancia = false;
            List<Casilla> teleports = new ArrayList<>();
            for (Casilla casilla : teleport) {
                if (!casilla.equals(robot)) {

                    int posTp = casilla.x + casilla.y;

                    int distancia;

                    if (posRobot > posTp) {
                        distancia = posRobot - posTp;
                    } else distancia = posTp - posRobot;

                    if (distancia < minim) {
                        teleports = new ArrayList<>();
                        minim = distancia;
                        xTp = casilla.x;
                        yTp = casilla.y;
                        iTp = casilla.index;
                        mismaDistancia = false;
                        teleports.add(casilla);
                    } else if (distancia == minim) {
                        mismaDistancia = true;
                        teleports.add(casilla);
                    }
                }
            }

            if (mismaDistancia) {
                for (Casilla casilla : teleports) {

                    if (casilla.y == robot.y) {
                        xTp = casilla.x;
                        yTp = casilla.y;
                        break;
                    }

                }
            }

            casillas.set(robot.index, new Casilla(robot.index, robot.x, robot.y, 'T'));
            casillas.set(iTp, robot);
            robot.x = xTp;
            robot.y = yTp;
            robot.index = iTp;

        }

    }

    // Comprovam si la casella on s'ha situat el robot és un iterator
    private boolean isIterator() {

        for (Casilla casilla : iterator) {

            if (casilla.index == robot.index) return true;
        }
        return false;

    }

    //Realitza el canvi de prioritats de moviments
    private void iterator() {
        if (currentPri == pri1) {
            currentPri = pri2;
        } else currentPri = pri1;
    }

}

//Classe que conté la informació de les caselles
class Casilla {

    // index : posició que correspon a la llista de caselles
    // x / y : coordenades al mapa
    // car : caràcter que té la casella
    int index;
    int x;
    int y;
    char car;

    //Constructor de la classe
    Casilla(int index, int x, int y, char c) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.car = c;
    }

    // Métode equals per denominar que dues caselles son a la mateixa posicio
    boolean equals(Casilla c) {
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
