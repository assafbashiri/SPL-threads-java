package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Ewok {
	int serialNumber;
	boolean available;
	//private Ewoks ewoks = Ewoks.getInstance();
	
  public Ewok(int serialNumber){
      this.serialNumber=serialNumber;
      available=true;
  }
    /**
     * Acquires an Ewok
     */
    public void acquire() {//הופכת מאמת לשקר
        if(this.available!=true)
            throw new IllegalArgumentException("before call acquire act the Ewok need to be available ");
        available=false;
		
    }

    /**
     * release an Ewok
     */
    public void release() {//הפוכך משקר לאמת
        if(this.available!=false)
            throw new IllegalArgumentException("before call acquire act the Ewok need to be not available ");
        available=true;
    }
    public int getSerialNumber(){
        return serialNumber;
    }
    public boolean getAvailable(){
        return available;
    }
}
