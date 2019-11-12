package general;

public enum Coin {
    PENNY(1), NICKLE(5), DIME(10), QUARTER(25), HALF_DOLLAR(50), GOLDEN_DOLLAR(100);
   
    private int denomination;
   
    private Coin(int denomination){
        this.denomination = denomination;
    }
   
    public int getDenomination(){
        return denomination;
    }
}
