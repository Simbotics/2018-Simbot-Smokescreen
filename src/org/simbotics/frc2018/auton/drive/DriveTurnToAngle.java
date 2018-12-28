package org.simbotics.frc2018.auton.drive;

import org.simbotics.frc2018.RobotConstants;
import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.io.RobotOutput;
import org.simbotics.frc2018.io.SensorInput;
import org.simbotics.frc2018.util.SimLib;
import org.simbotics.frc2018.util.SimPID;

public class DriveTurnToAngle extends AutonCommand {

	private SensorInput sensorIn;
	private RobotOutput robotOut;
	private double targetAngle;
	private double eps;
	private SimPID turnPID;
	private double maxOutput;
	private double rampRate;

	public DriveTurnToAngle(double targetAngle, double eps, long timeoutLength) {
		this(targetAngle, 11, eps, timeoutLength);
	}

	public DriveTurnToAngle(double targetAngle, double maxOutput, double eps, long timeoutLength) {
		this(targetAngle, maxOutput, 0, eps, timeoutLength);
	}

	public DriveTurnToAngle(double targetAngle, double maxOutput, double rampRate, double eps, long timeoutLength) {
		super(RobotComponent.DRIVE, timeoutLength);
		this.targetAngle = targetAngle;

		this.maxOutput = maxOutput;
		this.eps = eps;
		this.rampRate = rampRate;
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();

	}

	@Override
	public void firstCycle() {
		this.robotOut.setDriveClosedLoopRamp(rampRate, 20);
		double angle = this.sensorIn.getGyroAngle();
		double offset = angle % 360;
		this.turnPID = new SimPID(RobotConstants.getDriveTurnPID());
		this.turnPID.setMaxOutput(11);
		this.turnPID.setFinishedRange(this.eps);
		this.robotOut.configureDrivePID(RobotConstants.getTalonVelocityPID());

		if (this.targetAngle - offset < -180) {
			this.turnPID.setDesiredValue(angle + 360 + this.targetAngle - offset);
		} else if (this.targetAngle - offset < 180) {
			this.turnPID.setDesiredValue(angle + this.targetAngle - offset);
		} else {
			this.turnPID.setDesiredValue(angle - 360 + this.targetAngle - offset);
		}
	}

	@Override
	public boolean calculate() {
		double x = -this.turnPID.calcPID(this.sensorIn.getGyroAngle());
		if (x > this.maxOutput) {
			x = this.maxOutput;
		} else if (x < -this.maxOutput) {
			x = -this.maxOutput;
		}

		if (this.turnPID.isDone()) {
			this.robotOut.setDriveLeft(0);
			this.robotOut.setDriveRight(0);
			return true;
		} else {
			double leftOut = SimLib.calcLeftTankDrive(x, 0);
			double rightOut = SimLib.calcRightTankDrive(x, 0);

			this.robotOut.setDriveLeftVelocity(leftOut);
			this.robotOut.setDriveRightVelocity(rightOut);
			return false;
		}

	}

	@Override
	public void override() {
		this.robotOut.setDriveLeft(0);
		this.robotOut.setDriveRight(0);
	}

}
