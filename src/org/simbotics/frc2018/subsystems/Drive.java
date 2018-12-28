package org.simbotics.frc2018.subsystems;

import org.simbotics.frc2018.io.RobotOutput;

public class Drive extends Subsystem {

	private static Drive instance;
	private RobotOutput robotOut;

	private double leftOut;
	private double rightOut;
	private boolean outputControl;

	public static Drive getInstance() {
		if (instance == null) {
			instance = new Drive();
		}
		return instance;
	}

	private Drive() {
		this.robotOut = RobotOutput.getInstance();
	}

	@Override
	public void firstCycle() {
		// TODO Auto-generated method stub

	}

	public void setOutput(double y, double turn) {
		this.leftOut = y + turn;
		this.rightOut = y - turn;
		this.outputControl = true;
	}

	public void setVelocity(double y, double turn) {
		this.leftOut = y + turn;
		this.rightOut = y - turn;
		this.outputControl = false;
	}

	public void setRampRate(double rate) {
		this.robotOut.setDriveRampRate(rate, 40);
	}

	@Override
	public void calculate() {
		// TODO Auto-generated method stub
		if (this.outputControl) {
			this.robotOut.setDriveLeft(this.leftOut);
			this.robotOut.setDriveRight(this.rightOut);
		} else {
			this.robotOut.setDriveLeftVelocity(this.leftOut);
			this.robotOut.setDriveRightVelocity(this.rightOut);
		}
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		this.robotOut.setDriveLeft(0.0);
		this.robotOut.setDriveRight(0.0);
	}
}
