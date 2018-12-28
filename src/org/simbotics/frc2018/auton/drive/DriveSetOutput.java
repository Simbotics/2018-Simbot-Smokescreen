package org.simbotics.frc2018.auton.drive;

import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.io.RobotOutput;

public class DriveSetOutput extends AutonCommand {

	private double speed;
	private RobotOutput robotOut;

	public DriveSetOutput(double speed) {
		super(RobotComponent.DRIVE);
		this.speed = speed;

		this.robotOut = RobotOutput.getInstance();
	}

	@Override
	public void firstCycle() {
		// nothing!!!
	}

	@Override
	public boolean calculate() {
		this.robotOut.setDriveLeft(this.speed);
		this.robotOut.setDriveRight(this.speed);
		return true;

	}

	@Override
	public void override() {
		this.robotOut.setDriveLeft(0.0);
		this.robotOut.setDriveRight(0.0);
	}

}
