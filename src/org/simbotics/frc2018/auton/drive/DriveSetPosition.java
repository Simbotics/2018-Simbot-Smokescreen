package org.simbotics.frc2018.auton.drive;

import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.io.RobotOutput;
import org.simbotics.frc2018.io.SensorInput;
import org.simbotics.frc2018.util.SimPoint;

public class DriveSetPosition extends AutonCommand {

	private double x;
	private double y;
	private double angle;
	private SensorInput sensorIn;
	private RobotOutput robotOut;

	public DriveSetPosition(SimPoint p, double angle) {
		this(p.getX(), p.getY(), angle);
	}

	public DriveSetPosition(double x, double y, double angle) {
		super(RobotComponent.DRIVE);
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.sensorIn = SensorInput.getInstance();
		this.robotOut = RobotOutput.getInstance();
	}

	@Override
	public void firstCycle() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean calculate() {
		this.sensorIn.setDriveXPos(this.x);
		this.sensorIn.setDriveYPos(this.y);
		this.sensorIn.setAutoStartAngle(this.angle);
		this.robotOut.setIntakeOpen(false);
		this.robotOut.setIntakePancakeOpen(false);
		return true;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub

	}

}
