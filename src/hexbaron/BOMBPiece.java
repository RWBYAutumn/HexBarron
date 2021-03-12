
package hexbaron;

public class BOMBPiece extends Piece {
     int movesLeft = 5;
        boolean isPrimed = false;
         
        
    public BOMBPiece(boolean player1) {
        super(player1);
        vPValue = 5;
        fuelCostOfMove = 2;
        pieceType = "N";
        
    }
    
}
