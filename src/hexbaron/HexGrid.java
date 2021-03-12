/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hexbaron;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ir191258
 */
public class HexGrid {

    protected List<Tile> tiles = new ArrayList<>();
    protected List<Piece> pieces = new ArrayList<>();
    protected int size;
    protected boolean player1Turn;

    public HexGrid(int n) {
        size = n;
        setUpTiles();
        setUpNeighbours();
        player1Turn = true;
    }

    public void setUpGridTerrain(List<String> listOfTerrain) {
        for (int count = 0; count < listOfTerrain.size(); count++) {
            tiles.get(count).setTerrain(listOfTerrain.get(count));
        }
    }

    public void addPiece(boolean belongsToplayer1, String typeOfPiece, int location) {
        Piece newPiece;
        switch (typeOfPiece) {
            case "Baron":
                newPiece = new BaronPiece(belongsToplayer1);
                break;
            case "LESS":
                newPiece = new LESSPiece(belongsToplayer1);
                break;
            case "PBDS":
                newPiece = new PBDSPiece(belongsToplayer1);
                break;
            default:
                newPiece = new Piece(belongsToplayer1);
                break;
        }
        pieces.add(newPiece);
        tiles.get(location).setPiece(newPiece);
    }

    public Object[] executeCommand(List<String> items, int fuelChange, int lumberChange,
            int supplyChange, int fuelAvailable, int lumberAvailable,
            int piecesInSupply) {
        int lumberCost;
        switch (items.get(0)) {
            case "move":
                int fuelCost = executeMoveCommand(items, fuelAvailable);
                if (fuelCost < 0) {
                    return new Object[]{"That move can't be done", fuelChange, lumberChange, supplyChange};
                }
                fuelChange = -fuelCost;
                break;
            case "saw":

            case "dig":
                Object[] returnObjects = executeCommandInTile(items, fuelChange, lumberChange);
                boolean execute = (boolean) returnObjects[0];
                fuelChange = (int) returnObjects[1];
                lumberChange = (int) returnObjects[2];
                if (!execute) {
                    return new Object[]{"Couldn't do that", fuelChange, lumberChange, supplyChange};
                }
                break;
            case "spawn":
                lumberCost = executeSpawnCommand(items, lumberAvailable, piecesInSupply);
                if (lumberCost < 0) {
                    return new Object[]{"Spawning did not occur", fuelChange, lumberChange, supplyChange};
                }
                lumberChange = -lumberCost;
                supplyChange = 1;
                break;
            case "upgrade":
                lumberCost = executeUpgradeCommand(items, lumberAvailable);
                if (lumberCost < 0) {
                    return new Object[]{"Upgrade not possible", fuelChange, lumberChange, supplyChange};
                }
                lumberChange = -lumberCost;
                break;
           //Task 8 
            case "downgrade":
                lumberCost = executeDowngradeCommand(items, lumberAvailable);
                if (lumberCost < 0) {
                    return new Object[]{"Downgrade not possible", fuelChange, lumberChange, supplyChange};
                }
                 lumberChange = lumberCost;
                 break;
                //88888888888888888888888888888888
                 
            //Task 9 
            case "salvage": 
                lumberCost = executeSalavageCommand(items);
                if (lumberCost < 0) {
                    return new Object[]{"Salvage not possible", fuelChange, lumberChange, supplyChange};
                }
                   supplyChange = -1;
                 lumberChange = lumberCost;
                 break;
                
              
                
                //999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999
                 
                 
                 
                 
                 
                 
                 
        }
        return new Object[]{"Command executed", fuelChange, lumberChange, supplyChange};
    }

    private boolean checkTileIndexIsValid(int tileToCheck) {
        return tileToCheck >= 0 && tileToCheck < tiles.size();
    }

    private boolean checkPieceAndTileAreValid(int tileToUse) {
        if (checkTileIndexIsValid(tileToUse)) {
            Piece thePiece = tiles.get(tileToUse).getPieceInTile();
            if (thePiece != null) {
                if (thePiece.getBelongsToplayer1() == player1Turn) {
                    return true;
                }
            }
        }
        return false;
    }
    
    //Task 9 
    
    private int executeSalavageCommand(List<String> items){
        
     int tileToUse = 0;
        try{
        tileToUse = Integer.parseInt(items.get(1));             //changed to fix 
        
        }catch(Exception e){
        return -1;
        }
        
        if (!checkPieceAndTileAreValid(tileToUse) 
                || tiles.get(tileToUse).getPieceInTile().pieceType.equals("B") 
              ) {
            return -1;
        }else{
         Piece thePiece = tiles.get(tileToUse).getPieceInTile();
            
         if (thePiece.getPieceType().toUpperCase().equals("B") || thePiece.belongsToplayer1 != player1Turn) {
                return -1;
            }
         
            
         
         
         
            thePiece.destroyPiece();
            pieces.remove(thePiece);
            tiles.get(tileToUse).setPiece(null);
            
             return 5;
        }
        
    }

    
    
    
    
    
    
    
    
    //Task 8 - making new downgrade command 
    
    private int executeDowngradeCommand(List<String> items, int lumberAvailable){
    
          int tileToUse = 0;
        try{
        tileToUse = Integer.parseInt(items.get(1));             //changed to fix 
        
        }catch(Exception e){
        return -1;
        }
        
        if (!checkPieceAndTileAreValid(tileToUse) || lumberAvailable < 1 
                || tiles.get(tileToUse).getPieceInTile().pieceType.equals("S") 
              ) {
            return -1;
        }else{
         Piece thePiece = tiles.get(tileToUse).getPieceInTile();
            if (!thePiece.getPieceType().toUpperCase().equals("P") 
                    && !thePiece.getPieceType().toUpperCase().equals("L")) {
                return -1;
            }
            thePiece.destroyPiece();
            thePiece = new Piece(player1Turn);
            pieces.add(thePiece);
            tiles.get(tileToUse).setPiece(thePiece);
             return 1;
        }
        
    }
    
    //88888888888888888888888888888888888888888888888888
    
    
    
    
    

    private Object[] executeCommandInTile(List<String> items, int fuel, int lumber) {
       
        int tileToUse = 0; 
        try{
         tileToUse = Integer.parseInt(items.get(1));
        
        }catch(Exception e){
        
         return new Object[]{false, fuel, lumber};
    }
      
        if (checkPieceAndTileAreValid(tileToUse) == false) {
            return new Object[]{false, fuel, lumber};
        }
        Piece thePiece = tiles.get(tileToUse).getPieceInTile();
        if (thePiece.hasMethod(items.get(0))) {
            String methodToCall = items.get(0);
            Class t = thePiece.getClass();
            try {
                Method method = t.getMethod(methodToCall, String.class);
                Object parameters = tiles.get(tileToUse).getTerrain();
                if (items.get(0).equals("saw")) {
                    lumber += (int) method.invoke(thePiece, parameters);
                } else if (items.get(0).equals("dig")) {
                    fuel += (int) method.invoke(thePiece, parameters);
//                    if (Math.abs(fuel) > 2) {
//                        tiles.get(tileToUse).setTerrain(" ");
//                    }
                }
                return new Object[]{true, fuel, lumber};
            } catch (Exception ex) {
                Console.writeLine(ex.getMessage());
            }
        }
        return new Object[]{false, fuel, lumber};
    }

    private int executeMoveCommand(List<String> items, int fuelAvailable) {
        int startID = Integer.parseInt(items.get(1));
        int endID = Integer.parseInt(items.get(2));
        if (!checkPieceAndTileAreValid(startID) || !checkTileIndexIsValid(endID)) {
            return -1;
        }
        Piece thePiece = tiles.get(startID).getPieceInTile();
        if (tiles.get(endID).getPieceInTile() != null) {
            return -1;
        }
        int distance = tiles.get(startID).getDistanceToTileT(tiles.get(endID));
        int fuelCost = thePiece.checkMoveIsValid(distance, tiles.get(startID).getTerrain(), tiles.get(endID).getTerrain());
        if (fuelCost == -1 || fuelAvailable < fuelCost) {
            return -1;
        }
        movePiece(endID, startID);
        return fuelCost;
    }

    private int executeSpawnCommand(List<String> items, int lumberAvailable, int piecesInSupply) {
        
        int tileToUse;
       int playerPieces = 0;
       
        if(items.size() == 3){
            tileToUse = Integer.parseInt(items.get(2));
            

        if (piecesInSupply < 1 || lumberAvailable < 10 || !checkTileIndexIsValid(tileToUse)) {
            return -1;
        }
        Piece thePiece = tiles.get(tileToUse).getPieceInTile();
        if (thePiece != null) {
            return -1;
        }
        boolean ownBaronIsNeighbour = false;
        List<Tile> listOfNeighbours = new ArrayList<>(tiles.get(tileToUse).getNeighbours());
        for (Tile n : listOfNeighbours) {
            thePiece = n.getPieceInTile();
            if (thePiece != null) {
                if (player1Turn && thePiece.getPieceType().equals("B") || !player1Turn && thePiece.getPieceType().equals("b")) {
                    ownBaronIsNeighbour = true;
                    break;
                }
            }
        }

        if (!ownBaronIsNeighbour) {
            return -1;
        }

        //Task 4 making sure the player can only have 6 pieces on the board at a time
        if (!ownBaronIsNeighbour) {
            return -1;
        }
        for (Piece p : pieces) {
            if (p.getBelongsToplayer1() == player1Turn) {
                playerPieces += 1;
            }
        }
        if (playerPieces >= 6) {
            Console.writeLine("Spawn attempted to exceed max pieces.");
            return -1;
        }

        Piece newPiece = new BOMBPiece(player1Turn);
        pieces.add(newPiece);

        tiles.get(tileToUse).setPiece(newPiece);
        return 10;
        
            
       }
        else{
        
        tileToUse = Integer.parseInt(items.get(1));
        if (piecesInSupply < 1 || lumberAvailable < 3 || !checkTileIndexIsValid(tileToUse)) {
            return -1;
        }
        Piece thePiece = tiles.get(tileToUse).getPieceInTile();
        if (thePiece != null) {
            return -1;
        }
        boolean ownBaronIsNeighbour = false;
        List<Tile> listOfNeighbours = new ArrayList<>(tiles.get(tileToUse).getNeighbours());
        for (Tile n : listOfNeighbours) {
            thePiece = n.getPieceInTile();
            if (thePiece != null) {
                if (player1Turn && thePiece.getPieceType().equals("B") || !player1Turn && thePiece.getPieceType().equals("b")) {
                    ownBaronIsNeighbour = true;
                    break;
                }
            }
        }

        if (!ownBaronIsNeighbour) {
            return -1;
        }

        //Task 4 making sure the player can only have 6 pieces on the board at a time
        if (!ownBaronIsNeighbour) {
            return -1;
        }
        for (Piece p : pieces) {
            if (p.getBelongsToplayer1() == player1Turn) {
                playerPieces += 1;
            }
        }
        if (playerPieces >= 6) {
            Console.writeLine("Spawn attempted to exceed max pieces.");
            return -1;
        }

        Piece newPiece = new Piece(player1Turn);
        pieces.add(newPiece);

        tiles.get(tileToUse).setPiece(newPiece);
        
        return 3;
        }
         
    }

    private int executeUpgradeCommand(List<String> items, int lumberAvailable) {
       
        int tileToUse = 0;
        try{
        tileToUse = Integer.parseInt(items.get(2));             //changed to fix 
        
        }catch(Exception e){
        return -1;
        }
        
        if (!checkPieceAndTileAreValid(tileToUse) || lumberAvailable < 5 || !(items.get(1).equals("pbds") || items.get(1).equals("less"))) {
            return -1;
        } else {
            Piece thePiece = tiles.get(tileToUse).getPieceInTile();
            if (!thePiece.getPieceType().toUpperCase().equals("S")) {
                return -1;
            }
            thePiece.destroyPiece();
            if (items.get(1).equals("pbds")) {
                thePiece = new PBDSPiece(player1Turn);
            } else {
                thePiece = new LESSPiece(player1Turn);
            }
            pieces.add(thePiece);
            tiles.get(tileToUse).setPiece(thePiece);
            return 5;
        }
    }
    
    
    
    
    
    
    
    

    private void setUpTiles() {
        int evenStartY = 0;
        int evenStartZ = 0;
        int oddStartZ = 0;
        int oddStartY = -1;
        int x, y, z;
        for (int count = 1; count <= size / 2; count++) {
            y = evenStartY;
            z = evenStartZ;
            for (x = 0; x < size - 1; x += 2) {
                Tile tempTile = new Tile(x, y, z);
                tiles.add(tempTile);
                y -= 1;
                z -= 1;
            }
            evenStartZ += 1;
            evenStartY -= 1;
            y = oddStartY;
            z = oddStartZ;
            for (x = 1; x < size; x += 2) {
                Tile tempTile = new Tile(x, y, z);
                tiles.add(tempTile);
                y -= 1;
                z -= 1;
            }
            oddStartZ += 1;
            oddStartY -= 1;
        }
    }

    private void setUpNeighbours() {
        for (Tile fromTile : tiles) {
            for (Tile toTile : tiles) {
                if (fromTile.getDistanceToTileT(toTile) == 1) {
                    fromTile.addToNeighbours(toTile);
                }
            }
        }
    }

    public Object[] destroyPiecesAndCountVPs(int player1VPs, int player2VPs, Player player1, Player player2) {

        boolean baronDestroyed = false;
        List<Tile> listOfTilesContainingDestroyedPieces = new ArrayList<>();

        for (Tile t : tiles) {
            if (t.getPieceInTile() != null) {
                List<Tile> listOfNeighbours = new ArrayList<>(t.getNeighbours());
                int noOfConnections = 0;
                
                for (Tile n : listOfNeighbours) {
                    if (n.getPieceInTile() != null && !n.pieceInTile.belongsToplayer1 && t.pieceInTile.belongsToplayer1) {
                        noOfConnections++;
                    } else if (n.getPieceInTile() != null && n.pieceInTile.belongsToplayer1 && !t.pieceInTile.belongsToplayer1) {
                        noOfConnections++;
                    }
                }

                Piece thePiece = t.getPieceInTile();
                if (noOfConnections >= thePiece.getConnectionsNeededToDestroy()) {

                    thePiece.destroyPiece();

                    if (thePiece.getPieceType().toUpperCase().equals("B")) {
                        baronDestroyed = true;
                    }
                    listOfTilesContainingDestroyedPieces.add(t);
                    if (thePiece.getBelongsToplayer1()) {
                        player1.AddPiecesInSupply(1);
                        player2VPs += thePiece.getVPs();
                    } else {
                        player2.AddPiecesInSupply(1);
                        player1VPs += thePiece.getVPs();
                    }
                }
            }
        }
        for (Tile t : listOfTilesContainingDestroyedPieces) {
            t.setPiece(null);
        }

        return new Object[]{baronDestroyed, player1VPs, player2VPs};
    }

    public String getGridAsString(boolean P1Turn) {

        int listPositionOfTile = 0;
        player1Turn = P1Turn;
        Object[] returnObjects = createEvenLine(true, listPositionOfTile);
        String gridAsString = createTopLine() + returnObjects[0].toString();
        listPositionOfTile = (int) returnObjects[1];
        listPositionOfTile += 1;
        returnObjects = createOddLine(listPositionOfTile);
        gridAsString += returnObjects[0].toString();
        listPositionOfTile = (int) returnObjects[1];
        for (int count = 1; count < size - 1; count += 2) {
            listPositionOfTile += 1;
            returnObjects = createEvenLine(false, listPositionOfTile);
            gridAsString += returnObjects[0].toString();
            listPositionOfTile = (int) returnObjects[1];
            listPositionOfTile += 1;
            returnObjects = createOddLine(listPositionOfTile);
            gridAsString += returnObjects[0].toString();
            listPositionOfTile = (int) returnObjects[1];
        }
        return gridAsString + createBottomLine();
    }

    private void movePiece(int newIndex, int oldIndex) {
        tiles.get(newIndex).setPiece(tiles.get(oldIndex).getPieceInTile());
        tiles.get(oldIndex).setPiece(null);
    }

    public String getPieceTypeInTile(int id) {
        Piece thePiece = tiles.get(id).getPieceInTile();
        if (thePiece == null) {
            return " ";
        } else {
            return thePiece.getPieceType();
        }
    }

    private String createBottomLine() {
        String line = "   ";
        for (int count = 1; count <= size / 2; count++) {
            line += " \\__/ ";
        }
        return line + "\n";
    }

    private String createTopLine() {
        String line = "\n  ";
        for (int count = 1; count <= size / 2; count++) {
            line += "__    ";
        }
        return line + "\n";
    }

    private Object[] createOddLine(int listPositionOfTile) {
        String line = "";
        for (int count = 1; count <= size / 2; count++) {
            if (count > 1 && count < size / 2) {
                line += getPieceTypeInTile(listPositionOfTile) + "\\__/";
                listPositionOfTile += 1;
                line += tiles.get(listPositionOfTile).getTerrain();
            } else if (count == 1) {
                line += " \\__/" + tiles.get(listPositionOfTile).getTerrain();
            }
        }
        line += getPieceTypeInTile(listPositionOfTile) + "\\__/";
        listPositionOfTile += 1;
        if (listPositionOfTile < tiles.size()) {
            line += tiles.get(listPositionOfTile).getTerrain() + getPieceTypeInTile(listPositionOfTile) + "\\\n";
        } else {
            line += "\\\n";
        }
        return new Object[]{line, listPositionOfTile};
    }

    private Object[] createEvenLine(boolean firstEvenLine, int listPositionOfTile) {
        String line = " /" + tiles.get(listPositionOfTile).getTerrain();
        for (int count = 1; count < size / 2; count++) {
            line += getPieceTypeInTile(listPositionOfTile);
            listPositionOfTile += 1;
            line += "\\__/" + tiles.get(listPositionOfTile).getTerrain();
        }
        if (firstEvenLine) {
            line += getPieceTypeInTile(listPositionOfTile) + "\\__\n";
        } else {
            line += getPieceTypeInTile(listPositionOfTile) + "\\__/\n";
        }
        return new Object[]{line, listPositionOfTile};

    }

    // Task 7 - Make a field method
    
    
    public void makeField(List<String> items ){
     int tileToUse = Integer.parseInt(items.get(1));
     tiles.get(tileToUse).setTerrain(" ");
     
    }
    
    //777777777777777777777777777777777777777777777777777777777777
    
    
    
    
    
    // Task 5 make an index grid
    
    //---------------------------------------------------------------------------------------------
    int gridIndex = 0;

    public String getGridAsIndicies() {

        int listPositionOfTile = 0;
        Object[] returnObjects = createEvenLineHex(true, listPositionOfTile);
        String gridAsString = createTopLine() + returnObjects[0].toString();
        listPositionOfTile = (int) returnObjects[1];
        listPositionOfTile += 1;
        returnObjects = createOddLineHex(listPositionOfTile);
        gridAsString += returnObjects[0].toString();
        listPositionOfTile = (int) returnObjects[1];

        for (int count = 1; count < size - 1; count += 2) {
            listPositionOfTile += 1;
            returnObjects = createEvenLineHex(false, listPositionOfTile);
            gridAsString += returnObjects[0].toString();
            listPositionOfTile = (int) returnObjects[1];
            listPositionOfTile++;
            returnObjects = createOddLineHex(listPositionOfTile);
            gridAsString += returnObjects[0].toString();
            listPositionOfTile = (int) returnObjects[1];
        }
        gridIndex = 0;
        return gridAsString + createBottomLine();

    }

    private Object[] createEvenLineHex(boolean firstEvenLine, int listPositionOfTile) {
        String line = " /" + gridIndex;
        gridIndex++;
        for (int count = 1; count < size / 2; count++) {

            String add = "";
            if (!gridIndexTooBig()) {
                add = " ";
            }

            line += add;

            listPositionOfTile += 1;
            line += "\\__/" + gridIndex;
            gridIndex++;

        }
        if (firstEvenLine) {

            String add = "";
            if (!gridIndexTooBig()) {
                add = " ";
            }

            line += add + "\\__\n";
        } else {

            String add = "";
            if (!gridIndexTooBig()) {
                add = " ";
            }

            line += add + "\\__/\n";
        }
        return new Object[]{line, listPositionOfTile};
    }

    private Object[] createOddLineHex(int listPositionOfTile) {
        String line = "";
        for (int count = 1; count <= size / 2; count++) {
            if (count > 1 && count < size / 2) {

                String add = "";
                if (!gridIndexTooBig()) {
                    add = " ";
                }

                line += add + "\\__/";
                listPositionOfTile += 1;
                line += gridIndex;gridIndex++;
                
            } else if (count == 1) {
                line += " \\__/" + gridIndex;
                gridIndex++;

            }
        }

        String add = "";
        if (!gridIndexTooBig()) {
            add = " ";
        }

        line += add + "\\__/";

        listPositionOfTile += 1;
        if (listPositionOfTile < tiles.size()) {

            add = "";
            if (!gridIndexTooBig()) {
                add = " ";
            }
            line += gridIndex + add + "\\\n";
            gridIndex++;

        } else {
            line += "\\\n";
        }
        return new Object[]{line, listPositionOfTile};
    }

    private boolean gridIndexTooBig() {

        if (gridIndex > 10) {
            return true;
        }
        return false;
    }

}
