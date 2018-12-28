package org.simbotics.frc2018.auton.drive;

import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.io.RobotOutput;

public class DriveTurnTimer extends AutonCommand {

	private RobotOutput robotOut;
	private double speed;
	private long length;
	private long startTime;

	public DriveTurnTimer(double speed, long howLong) {
		super(RobotComponent.DRIVE);
		this.speed = speed;
		this.length = howLong;

		this.robotOut = RobotOutput.getInstance();
	}

	@Override
	public void firstCycle() {
		this.startTime = System.currentTimeMillis();
	}

	@Override
	public boolean calculate() {
		// motors
		this.robotOut.setDriveLeft(this.speed);
		this.robotOut.setDriveRight(-this.speed);

		long timePassed = System.currentTimeMillis() - this.startTime;

		if (timePassed > this.length) {
			this.robotOut.setDriveLeft(0.0);
			this.robotOut.setDriveRight(0.0);

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void override() {
		this.robotOut.setDriveLeft(0.0);
		this.robotOut.setDriveRight(0.0);

	}

}
