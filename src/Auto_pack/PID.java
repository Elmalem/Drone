package Auto_pack;

import java.util.HashMap;

public class PID {

	private double P; // Current distance - target
	private double I; // Sum errors
	private double D; // (error - last error) / delta time

	private double error; // Last error
	private double integralHistory; // Sum integral
	private double MAX; // Max value for integral

	PID(double P, double I, double D , double MAX) {
		this.P = P;
		this.I = I;
		this.D = D;
		this.error = 0;
		this.MAX = MAX;
		this.integralHistory = 0;
	}

	HashMap<Character, Double> getData() {
		HashMap<Character, Double> map = new HashMap<>();
		map.put('P', this.P);
		map.put('I', this.I);
		map.put('D', this.D);
		return map;
	}

	public void setData(double p, double i, double d) {
		this.P = p;
		this.I = i;
		this.D = d;
	}

	public double update(int dt, double error) {
		this.integralHistory = this.I * error * dt;
		double diff = (error - this.error) / dt;
		double realIntegral = this.integralHistory > MAX ? MAX : this.integralHistory;
		realIntegral = realIntegral < -MAX ? -MAX : realIntegral;
		this.error = error;
		return (this.P * error + realIntegral + this.D * diff);
	}
}
