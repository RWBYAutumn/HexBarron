/*
 * Skeleton Program code for the AQA A Level Paper 1 Summer 2021 examination.
 * this code should be used in conjunction with the Preliminary Material
 * written by the AQA Programmer Team
 * developed in the NetBeans IDE 8.1 environment
 */
package hexbaron;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class HexBaron {

    public HexBaron() {
        boolean fileLoaded;
        Player player1 = new Player();
        Player player2 = new Player();
        HexGrid grid;
        String choice = "";
        while (!choice.equals("Q")) {
            displayMainMenu();
            choice = Console.readLine();
            if (choice.equals("1")) {
                grid = setUpDefaultGame(player1, player2);
                playGame(player1, player2, grid);
            } else if (choice.equals("2")) {
                Object[] returnObjects = loadGame(player1, player2);
                fileLoaded = (boolean) returnObjects[1];
                if (fileLoaded) {
                    grid = (HexGrid) returnObjects[0];
                    playGame(player1, player2, grid);
                }
            }
        }
    }

    Object[] loadGame(Player player1, Player player2) {
        Console.write("Enter the name of the file to load: ");
        String fileName = Console.readLine();
        List<String> items;
        String lineFromFile;
        HexGrid grid;
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            lineFromFile = in.readLine();
            items = Arrays.asList(lineFromFile.split(","));

            //Task 11 - added the chances variable 
            player1.setUpPlayer(items.get(0), Integer.parseInt(items.get(1)), Integer.parseInt(items.get(2)), Integer.parseInt(items.get(3)), Integer.parseInt(items.get(4)), Integer.parseInt(items.get(5)));
            lineFromFile = in.readLine();
            items = Arrays.asList(lineFromFile.split(","));
            player2.setUpPlayer(items.get(0), Integer.parseInt(items.get(1)), Integer.parseInt(items.get(2)), Integer.parseInt(items.get(3)), Integer.parseInt(items.get(4)), Integer.parseInt(items.get(5)));

            // 11 11 11
            int gridSize = Integer.parseInt(in.readLine());
            grid = new HexGrid(gridSize);
            List<String> t = Arrays.asList(in.readLine().split(","));
            grid.setUpGridTerrain(t);
            lineFromFile = in.readLine();
            while (lineFromFile != null) {
                items = Arrays.asList(lineFromFile.split(","));
                if (items.get(0).equals("1")) {
                    grid.addPiece(true, items.get(1), Integer.parseInt(items.get(2)));
                } else {
                    grid.addPiece(false, items.get(1), Integer.parseInt(items.get(2)));
                }
                lineFromFile = in.readLine();
            }
        } catch (Exception e) {
            Console.writeLine("File not loaded");
            return new Object[]{null, false};
        }
        return new Object[]{grid, true};
    }

    HexGrid setUpDefaultGame(Player player1, Player player2) {
        List<String> t = Arrays.asList(new String[]{" ", "#", "#", " ", "~", "~", " ", " ", " ", "~", " ", "#", "#", " ", " ", " ",
            " ", " ", "#", "#", "#", "#", "~", "~", "~", "~", "~", " ", "#", " ", "#", " "});
        int gridSize = 8;
        HexGrid grid = new HexGrid(gridSize);
        System.out.println("Enter playerOne name ?");
        String name1 = Console.readLine();  //This changes to allow the user to enter two names
        System.out.println("Enter playerTwo name ?");
        String name2 = Console.readLine();

        player1.setUpPlayer(name1, 0, 10, 10, 5, 3);
        player2.setUpPlayer(name2, 0, 10, 10, 5, 3);
        grid.setUpGridTerrain(t);

        grid.addPiece(true, "Baron", 0);
        grid.addPiece(true, "Serf", 8);
        grid.addPiece(false, "Baron", 31);
        grid.addPiece(false, "Serf", 23);

        return grid;
    }

    boolean checkMoveCommandFormat(List<String> items) {
        int result;
        if (items.size() == 3) {
            for (int count = 1; count <= 2; count++) {
                try {
                    result = Integer.parseInt(items.get(count));
                } catch (Exception e) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    boolean checkStandardCommandFormat(List<String> items) {
        int result;
        if (items.size() == 2 || items.size() == 3) {
            try {
                result = Integer.parseInt(items.get(1));
                
            } 
            catch (Exception e) {
                
                try{
                result = Integer.parseInt(items.get(2));
                return true;
                }
                catch(Exception d){   
                return false;
                }
  
            }
            return true;
        }
        return false;
    }

    boolean checkUpgradeCommandFormat(List<String> items) {
        int result;
        if (items.size() == 3) {
            if (!items.get(1).toUpperCase().equals("LESS") && !items.get(1).toUpperCase().equals("PBDS")) {
                return false;
            }
            try {
                result = Integer.parseInt(items.get(2));
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }

    boolean checkDigCommandFormat(List<String> items) {

        return true;
    }

    boolean checkSawCommandFormat(List<String> items) {

        return true;
    }

    //Task 8 create a downgrade command to serif
    boolean checkDowngradeCommandFormat(List<String> items) {

        if (items.size() == 2) {

            try {

                int result = Integer.parseInt(items.get(1));
                return true;

            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    //888888888888888888888888888888888888888
    //Task 10
    boolean checkSalvageCommandFormat(List<String> items) {

        if (items.size() == 2) {

            try {

                int result = Integer.parseInt(items.get(1));
                return true;

            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }
    //100000000000000000000000000000000000000000000000000000000

    boolean checkCommandIsValid(List<String> items) {
        if (items.size() > 0) {
            switch (items.get(0)) {
                case "move":

                    return checkMoveCommandFormat(items);
                case "dig":

                    return checkDigCommandFormat(items);
                case "saw":

                    return checkSawCommandFormat(items);
                case "spawn":

                    return checkStandardCommandFormat(items);
                case "upgrade":

                    return checkUpgradeCommandFormat(items);

                case "downgrade":                                    //Task 8
                    return checkDowngradeCommandFormat(items);

                case "salvage":                                     //task 10
                    return checkSalvageCommandFormat(items);
            }
        }
        return false;
    }

    public String strip(String command) {
        int index;
        for (index = 0; index < command.length(); index++) {
            if (command.charAt(index) != ' ') {
                break;
            }
        }
        command = command.substring(index);

        for (index = command.length() - 1; index >= 0; index--) {
            if (command.charAt(index) != ' ') {
                break;
            }
        }
        return command.substring(0, index + 1);
    }

    void playGame(Player player1, Player player2, HexGrid grid) {
        boolean gameOver = false;
        boolean player1Turn = true;
        boolean validCommand;
        List<String> commands = new ArrayList<>();
        Console.writeLine("Player One current state - " + player1.getStateString());
        Console.writeLine("Player Two current state - " + player2.getStateString());
        do {
            Console.writeLine(grid.getGridAsString(player1Turn));
            if (player1Turn) {
                Console.writeLine(player1.getName() + " state your three commands, pressing enter after each one.");
            } else {
                Console.writeLine(player2.getName() + " state your three commands, pressing enter after each one.");
            }
            for (int count = 1; count <= 3; count++) {
                Console.write("Enter command number " + count + ": ");

                //Task 5 Creating an index grid
                String stringCommand = Console.readLine();
                strip(stringCommand);
                if (stringCommand.equals("hexes")) {
                    System.out.println(grid.getGridAsIndicies());
                    count--;
                } else {
                    commands.add(strip(stringCommand.toLowerCase()));
                }

                //-----------------------------------------------------------
            }

            //Task 6 checking if all teh commands are move commands and adding +1 fuel to the user if they are
            boolean addFuel = true;

            for (int i = 0; i < 3; i++) {
                if (!commands.get(i).contains("move")) {
                    addFuel = false;
                }
            }

            if (addFuel == true) {
                if (player1Turn == true) {
                    player1.updateFuel(1);
                } else {
                    player2.updateFuel(1);
                }

            }

            //---------------------------------------------------------
            for (int count = 0; count < 3; count++) {
                List<String> items = Arrays.asList(commands.get(count).split(" "));
                validCommand = checkCommandIsValid(items);

                OUTER:
                if (!validCommand) {
                    Console.writeLine("Command " + (count + 1) + ": Is an invalid command");

                    //task 11 11 11 11 11 11 11 11 11 11 11 11 11 11 11 11 11 11 11 11 11 11 11 
                    if (player1Turn && player1.Chances > 0) {
                        Console.write("Reenter command number " + (1 + count) + ": ");

                        String stringCommand = Console.readLine();
                        strip(stringCommand);

                        if (stringCommand.equals("hexes")) {
                            System.out.println(grid.getGridAsIndicies());

                        } else {
                            commands.remove(count);
                            commands.add(count, stringCommand);

                            player1.removeChance();

                            Console.write("you have " + player1.Chances + " chances left");
                            System.out.println("");
                        }
                        count--;

                    } else if (!player1Turn && player2.Chances > 0) {
                        Console.write("Reenter command number " + (1 + count) + ": ");

                        String stringCommand = Console.readLine();
                        strip(stringCommand);

                        if (stringCommand.equals("hexes")) {
                            System.out.println(grid.getGridAsIndicies());

                        } else {
                            commands.remove(count);
                            commands.add(count, stringCommand);

                            player2.removeChance();

                            Console.write("you have " + player2.Chances + " chances left");
                            System.out.println("");
                        }

                        count--;
                    }

                    //11 11 11 11 11 11 11 11 11 11 11 11 11 11 11 11 11 11 11 11 11 11 11 
                } else {
                    int fuelChange = 0;
                    int lumberChange = 0;
                    int supplyChange = 0;
                    String summaryOfResult;
                    Object[] returnObjects;
                    if (player1Turn) {
                        returnObjects = grid.executeCommand(items, fuelChange, lumberChange, supplyChange,
                                player1.getFuel(), player1.getLumber(),
                                player1.getPiecesInSupply());
                        summaryOfResult = returnObjects[0].toString();
                        fuelChange = (int) returnObjects[1];
                        lumberChange = (int) returnObjects[2];
                        supplyChange = (int) returnObjects[3];
                        player1.updateLumber(lumberChange);
                        player1.updateFuel(fuelChange);
                        if (supplyChange == 1) {
                            player1.removeTileFromSupply();
                        }

                        //task 10
                        if (supplyChange == -1) {
                            player1.AddPiecesInSupply(1);
                        }

                        //task 11
                        if (!summaryOfResult.equals("Command executed") && player1.Chances > 0) {

                            Console.writeLine(summaryOfResult);

                            Console.write("Reenter command number " + (1 + count) + ": ");

                            String stringCommand = Console.readLine();
                            strip(stringCommand);

                            if (stringCommand.equals("hexes")) {
                                System.out.println(grid.getGridAsIndicies());

                            } else {
                                commands.remove(count);
                                commands.add(count, stringCommand);

                                player1.removeChance();

                                Console.write("you have " + player1.Chances + " chances left");
                                System.out.println("");
                            }

                            count--;
                            break OUTER;
                        }

                    } else {
                        returnObjects = grid.executeCommand(items, fuelChange, lumberChange, supplyChange,
                                player2.getFuel(), player2.getLumber(),
                                player2.getPiecesInSupply());
                        summaryOfResult = returnObjects[0].toString();
                        fuelChange = (int) returnObjects[1];
                        lumberChange = (int) returnObjects[2];
                        supplyChange = (int) returnObjects[3];
                        player2.updateLumber(lumberChange);
                        player2.updateFuel(fuelChange);
                        if (supplyChange == 1) {
                            player2.removeTileFromSupply();
                        }

                        //task 10
                        if (supplyChange == -1) {
                            player2.AddPiecesInSupply(1);
                        }

                        //task 11
                        if (!summaryOfResult.equals("Command executed") && player2.Chances > 0) {

                            Console.writeLine(summaryOfResult);

                            Console.write("Reenter command number " + (1 + count) + ": ");

                            String stringCommand = Console.readLine();
                            strip(stringCommand);
                            
                            
                        if (stringCommand.equals("hexes")) {
                            System.out.println(grid.getGridAsIndicies());

                        } else {
                            commands.remove(count);
                            commands.add(count, stringCommand);

                            player2.removeChance();

                            Console.write("you have " + player2.Chances + " chances left");
                            System.out.println("");
                        }

                            count--;

                            break OUTER;
                        }
                    }
                    Console.writeLine(summaryOfResult);

                }
            }

            //---------------------------------------------------------
            //Task 7 -  checking if 3 saw or dig commands are made 
            int valid = 0;
            for (String c : commands) {

                List<String> items = Arrays.asList(c.split(" "));
                validCommand = checkCommandIsValid(items);

                if (validCommand) {
                    valid++;
                }

                if (valid == 3) {

                    boolean consecutive = false;

                    if (commands.get(0).equals(commands.get(1)) && commands.get(0).equals(commands.get(2))) {
                        consecutive = true;
                    }

                    if (consecutive) {

                        if (player1Turn == true && commands.get(0).contains("dig")) {
                            player1.updateFuel(2);
                            grid.makeField(items);
                        } else if (player1Turn == false && commands.get(0).contains("dig")) {
                            player2.updateFuel(2);
                            grid.makeField(items);
                        } else if (player1Turn == true && commands.get(0).contains("saw")) {
                            player1.updateLumber(2);
                            grid.makeField(items);
                        } else if (player1Turn == false && commands.get(0).contains("saw")) {
                            player2.updateLumber(2);
                            grid.makeField(items);
                        }

                    }
                }

            }
//77777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777

            commands.clear();
            player1Turn = !player1Turn;
            int player1VPsGained = 0;
            int player2VPsGained = 0;
            Object[] returnObjects;
            if (gameOver) {
                returnObjects = grid.destroyPiecesAndCountVPs(player1VPsGained, player2VPsGained, player1, player2);
            } else {
                returnObjects = grid.destroyPiecesAndCountVPs(player1VPsGained, player2VPsGained, player1, player2);
                gameOver = (boolean) returnObjects[0];

            }
            player1VPsGained = (int) returnObjects[1];
            player2VPsGained = (int) returnObjects[2];
            player1.addToVPs(player1VPsGained);
            player2.addToVPs(player2VPsGained);
            Console.writeLine("Player One current state - " + player1.getStateString());
            Console.writeLine("Player Two current state - " + player2.getStateString());
            Console.write("Press Enter to continue...");
            Console.readLine();
        } while (!gameOver || !player1Turn);
        Console.writeLine(grid.getGridAsString(player1Turn));
        displayEndMessages(player1, player2);
    }

    void displayEndMessages(Player player1, Player player2) {
        Console.writeLine();
        Console.writeLine(player1.getName() + " final state: " + player1.getStateString());
        Console.writeLine();
        Console.writeLine(player2.getName() + " final state: " + player2.getStateString());
        Console.writeLine();
        if (player1.getVPs() > player2.getVPs()) {
            Console.writeLine(player1.getName() + " is the winner!");
        } else {
            Console.writeLine(player2.getName() + " is the winner!");
        }
    }

    void displayMainMenu() {
        Console.writeLine("1. Default game");
        Console.writeLine("2. Load game");
        Console.writeLine("Q. Quit");
        Console.writeLine();
        Console.write("Enter your choice: ");
    }

    public static void main(String[] args) {
        new HexBaron();
    }
}
