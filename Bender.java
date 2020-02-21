import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

class Bender {

    String[][] mapa;
    Player p;
    int[] posSalida;
    String resultado = "";
    LinkedList<Integer[]> teleport = new LinkedList<>();

    // Constructor: ens passen el mapa en forma d'String
    public Bender(String mapa) {

        String[] columnas = mapa.split("\n");

        this.mapa = new String[columnas.length][];
        for (int i = 0; i < columnas.length; i++) {
            this.mapa[i] = columnas[i].split("");
        }

        for (int i = 0; i < this.mapa.length; i++) {
            for (int j = 0; j < this.mapa[0].length; j++) {
                if (this.mapa[i][j].equals("X")) {
                    this.p = new Player(new int[]{i, j});
                }
                if (this.mapa[i][j].equals("$")) {
                    this.posSalida = new int[]{i, j};
                }
                if (this.mapa[i][j].equals("T")) {
                    teleport.add(new Integer[]{i, j});
                    System.out.println("There's a teleport !" + i + " " + j);
                }
            }
        }

    }

    // Navegar fins a l'objectiu («$»).
// El valor retornat pel mètode consisteix en una cadena de
// caràcters on cada lletra pot tenir
// els valors «S», «N», «W» o «E»,
// segons la posició del robot a cada moment.
    public String run() {

        while (true) {

            while (p.moveS(this.mapa) != null) {
                this.resultado += p.lastMove;

                dibuixaMapa(this.mapa);

                if (isTeleport(p.currentPos)) {
                    this.mapa = teletransportar(this.mapa);
                }
                if (Arrays.equals(p.currentPos, posSalida)) {
                    return resultado;
                }
            }
            while (p.moveE(this.mapa) != null) {
                this.resultado += p.lastMove;

                dibuixaMapa(this.mapa);

                if (isTeleport(p.currentPos)) {
                    this.mapa = teletransportar(this.mapa);
                }
                if (Arrays.equals(p.currentPos, posSalida)) {
                    return resultado;
                }
            }

            if (p.moveS(this.mapa) != null) {

                this.resultado += p.lastMove;

                dibuixaMapa(this.mapa);

                if (isTeleport(p.currentPos)) {
                   this.mapa = teletransportar(this.mapa);
                }
                if (Arrays.equals(p.currentPos, posSalida)) {
                    return resultado;
                }
                continue;
            }

            while (p.moveN(this.mapa) != null) {
                this.resultado += p.lastMove;

                dibuixaMapa(this.mapa);

                if (isTeleport(p.currentPos)) {
                    this.mapa = teletransportar(this.mapa);
                }
                if (Arrays.equals(p.currentPos, posSalida)) {
                    return resultado;
                }
            }

            if (p.moveE(this.mapa) != null) {
                this.resultado += p.lastMove;

                dibuixaMapa(this.mapa);

                if (isTeleport(p.currentPos)) {
                    this.mapa = teletransportar(this.mapa);
                }
                if (Arrays.equals(p.currentPos, posSalida)) {
                    return resultado;
                }
                continue;
            }

            if (p.moveS(this.mapa) != null) {

                this.resultado += p.lastMove;

                if (isTeleport(p.currentPos)) {
                    this.mapa = teletransportar(this.mapa);
                }
                if (Arrays.equals(p.currentPos, posSalida)) {
                    return resultado;
                }
                continue;
            }


            while (p.moveW(this.mapa) != null) {
                this.resultado += p.lastMove;


                if (isTeleport(p.currentPos)) {
                    this.mapa = teletransportar(this.mapa);
                }
                if (Arrays.equals(p.currentPos, posSalida)) {
                    return resultado;
                }
            }


        }


    }

    void dibuixaMapa(String[][] mapa) {

        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[0].length; j++) {
                System.out.print(mapa[i][j]);
            }
            System.out.println();
        }

    }

    String[][] teletransportar(String[][] mapa) {

        System.out.println("teletransportamos !!");

        LinkedList<Integer> distancia = new LinkedList<>();

        int distPlayer = p.currentPos[0] + p.currentPos[1];

        for (int i = 0; i < teleport.size(); i++) {
            distancia.add(teleport.get(i)[0] + teleport.get(i)[1]);
        }

        return mapa;
    }

    boolean isTeleport(int[] coor) {

        if (teleport.size() == 0) return false;

        for (int i = 0; i < teleport.size(); i++) {

            int x = teleport.get(i)[0];
            int y = teleport.get(i)[1];

            int[] tele = new int[]{x, y};

            if (Arrays.equals(tele, coor)) return true;

        }

        return false;
    }

}

class Player {

    int nMovs = 0;

    int[] currentPos;

    char lastMove = '0';

    char[] priority1 = new char[]{'S', 'E', 'N', 'W'};

    char[] priority2 = new char[]{'N', 'W', 'S', 'E'};

    char[] currentPriority = priority1;

    Player(int[] currentPos) {
        this.currentPos = currentPos;
        System.out.println("Current position is " + currentPos[0] + " " + currentPos[1]);
    }

    String[][] moveS(String[][] mapa) {

        System.out.println("Moving to South");

        if (mapa[currentPos[0] + 1][currentPos[1]].equals("#")) {
            return null;
        } else {
            mapa[currentPos[0]][currentPos[1]] = " ";
            mapa[currentPos[0] + 1][currentPos[1]] = "X";
            currentPos[0] = currentPos[0] + 1;
            lastMove = 'S';
            nMovs++;
        }
        return mapa;
    }


    String[][] moveN(String[][] mapa) {

        System.out.println("Moving to Nort");

        if (mapa[currentPos[0] - 1][currentPos[1]].equals("#")) {
            return null;
        } else {
            mapa[currentPos[0]][currentPos[1]] = " ";
            mapa[currentPos[0] - 1][currentPos[1]] = "X";
            currentPos[0] = currentPos[0] - 1;
            lastMove = 'N';
            nMovs++;
        }
        return mapa;
    }


    String[][] moveE(String[][] mapa) {

        System.out.println("Moving to East");

        if (mapa[currentPos[0]][currentPos[1] + 1].equals("#")) {
            return null;
        } else {
            mapa[currentPos[0]][currentPos[1]] = " ";
            mapa[currentPos[0]][currentPos[1] + 1] = "X";
            currentPos[1] = currentPos[1] + 1;
            lastMove = 'E';
            nMovs++;
        }

        return mapa;
    }


    String[][] moveW(String[][] mapa) {

        System.out.println("Moving to Weast");

        if (mapa[currentPos[0]][currentPos[1] - 1].equals("#")) {
            return null;
        } else {
            mapa[currentPos[0]][currentPos[1]] = " ";
            mapa[currentPos[0]][currentPos[1] - 1] = "X";
            currentPos[1] = currentPos[1] - 1;
            lastMove = 'W';
            nMovs++;
        }
        return mapa;
    }


}


