package Auto_pack;

/*
 * A simple class of the drone battery,  starts at 100% and goes down
 */
public class Battery {

	private double stamina;
	
	Battery(){
		this.stamina = 100;
	}

	public double getStamina() {
		return this.stamina;
	}

	public void setStamina() {
		if(GameVariabales.toogleAI)
			this.stamina -= 0.5;
	}
	
	public String toString() {
		return "" + this.stamina;
	}
}
