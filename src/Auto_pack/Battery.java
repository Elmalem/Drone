package Auto_pack;

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
		if(this.stamina < 0.5) {
			Utils.gameOverMessage();
		}
	}
	
	public String toString() {
		return "" + this.stamina;
	}
}
