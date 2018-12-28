package org.simbotics.frc2018.auton.drive;

import org.simbotics.frc2018.RobotConstants;
import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.io.Dashboard;
import org.simbotics.frc2018.io.RobotOutput;
import org.simbotics.frc2018.io.SensorInput;
import org.simbotics.frc2018.util.SimLib;
import org.simbotics.frc2018.util.SimPID;
import org.simbotics.frc2018.util.SimPoint;

public class DriveTurnToPoint extends AutonCommand {

	private double x;
	private double y;
	private double theta;
	private double minVelocity;
	private double maxVelocity;
	private double eps;
	private SensorInput sensorIn;
	private RobotOutput robotOut;
	private double turnRate;
	private double rampRate;
	private double maxTurn; 

	private SimPID turnPID;
	private SimPID straightPID;
	private SimPID straightElevatorUpPID;

	

	public DriveTurnToPoint(double x, double y, double theta,
			double eps, double turnRate, double maxTurn, long timeout) {
		super(RobotComponent.DRIVE, timeout);
		this.x = x;
		this.y = y;
		this.theta = theta;
		
		this.eps = eps;
		
		this.turnRate = turnRate;
		this.maxTurn = maxTurn;

		this.sensorIn = SensorInput.getInstance();
		this.robotOut = RobotOutput.getInstance();

	}

	private SimPoint getRotatedError() { // rotates the xy coordinates to be relative to the angle of the target
		double currentX = this.sensorIn.getDriveXPos();
		double currentY = this.sensorIn.getDriveYPos();
		double rotation = 90 - this.theta;

		SimPoint currentPosition = new SimPoint(currentX, currentY);
		SimPoint finalPosition = new SimPoint(this.x, this.y);

		currentPosition.rotateByAngleDegrees(rotation);
		finalPosition.rotateByAngleDegrees(rotation);

		double xError = finalPosition.getX() - currentPosition.getX();
		double yError = finalPosition.getY() - currentPosition.getY();

		return new SimPoint(xError, yError);

	}

	@Override
	public void firstCycle() {
		this.straightElevatorUpPID = new SimPID(RobotConstants.getDriveElevatorUpPID());
		this.straightElevatorUpPID.setMinMaxOutput(this.minVelocity, this.maxVelocity);
		this.straightElevatorUpPID.setMinDoneCycles(1);
		this.straightPID = new SimPID(RobotConstants.getDriveStraightPID());
		this.straightPID.setMinMaxOutput(this.minVelocity, this.maxVelocity);
		this.straightPID.setMinDoneCycles(1);
		this.turnPID = new SimPID(RobotConstants.getDriveTurnPID());
		this.turnPID.setMaxOutput(11);
		this.robotOut.configureDrivePID(RobotConstants.getTalonVelocityPID());
		if (this.turnRate < 0) {
			this.turnRate = Dashboard.getInstance().getPathTurnP();
		}

	}

	@Override
	public boolean calculate() {
		this.robotOut.setDriveClosedLoopRamp(0, 20);
		SimPoint error = getRotatedError();
		double targetHeading;

		if (error.getY() < 0) { // flip X if we are going backwards
			error.setX(-error.getX());
		}

		double turningOffset = (error.getX() * this.turnRate); // based on how far we are in x turn more

		if (turningOffset > this.maxTurn) { // limit it to be within 90* from the target angle
			turningOffset = this.maxTurn;
		} else if (turningOffset < -this.maxTurn) {
			turningOffset = -this.maxTurn;
		}

		targetHeading = this.theta - turningOffset;

		double angle = this.sensorIn.getGyroAngle();
		double offset = angle % 360;

		// Corrects the target to work with Gyro position
		if (targetHeading - offset < -180) {
			this.turnPID.setDesiredValue(angle + 360 + targetHeading - offset);
		} else if (targetHeading - offset < 180) {
			this.turnPID.setDesiredValue(angle + targetHeading - offset);
		} else {
			this.turnPID.setDesiredValue(angle - 360 + targetHeading - offset);
		}



		double distanceFromTargetHeading = Math.abs(this.turnPID.getDesiredVal() - this.sensorIn.getAngle());
		
		double xOutput = -this.turnPID.calcPID(this.sensorIn.getAngle());

	
		double leftOut = SimLib.calcLeftTankDrive(xOutput, 0);
		double rightOut = SimLib.calcRightTankDrive(xOutput, 0);

		this.robotOut.setDriveLeftVelocity(leftOut);
		this.robotOut.setDriveRightVelocity(rightOut);

		
		boolean returning = (distanceFromTargetHeading  < this.eps);
		if (returning) {
			override();
			System.out.println("TURN TO POINT DONE!!");
		}

		return returning;

	}

	@Override
	public void override() {

		this.robotOut.setDriveLeft(0);
		this.robotOut.setDriveRight(0);

	}

}
