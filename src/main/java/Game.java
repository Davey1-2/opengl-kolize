

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Game {


    private static ArrayList<Square> squares = new ArrayList<Square>();



    public static void init(long window) throws IOException {

        Shaders.initShaders();

        BufferedReader br = new BufferedReader(new FileReader("lvl1.txt"));
        ArrayList<String> arr = new ArrayList<>();

        int countVertical = 0;
        String line;

        Shaders.initShaders();

        while ((line = br.readLine()) != null) {
            arr.add(line);

            countVertical++;
        }
        br.close();


        for (int y = 0; y < countVertical; y++) {
            line = arr.get(y);
            String [] splitted = arr.get(y).split(";");
            for (int x = 0; x < line.length(); x++) {
                    squares.add(new Square(Float.parseFloat(splitted[0]),Float.parseFloat(splitted[1]),Float.parseFloat(splitted[2])));
            }
        }
    }

    public static Square movingSquare = new Square(0f, 0f, 0.25f);


    public static void render(long window) {
        boolean check = false;

        for (Square square : squares){
            square.draw();
        }


        for (Square square : squares) {
            if (isInSquare(movingSquare, square)) {
                check = true;

            }
        }
        movingSquare.update(window);
        movingSquare.draw();

        if (!check) {
            movingSquare.green();
        }
        if (check){
            movingSquare.red();
        }


    }


    public static boolean isInSquare(Square movingSquare, Square notMoving) {
        return (movingSquare.getX() + movingSquare.getWidth() > notMoving.getX() &&
                movingSquare.getX() < notMoving.getX() + notMoving.getWidth() &&

                movingSquare.getY() + movingSquare.getWidth() / 2 + movingSquare.getWidth() > notMoving.getY() &&
                movingSquare.getY() + movingSquare.getWidth() / 2 < notMoving.getY() + notMoving.getWidth());
    }
}