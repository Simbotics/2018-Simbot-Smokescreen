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

public class DriveToPoint extends AutonCommand {

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
	private boolean slowTurn;
	private double maxTurn; 

	private SimPID turnPID;
	private SimPID straightPID;
	private SimPID straightElevatorUpPID;

	public DriveToPoint(double x, double y, double theta, long timeout) {
		this(x, y, theta, 0, 10, 0.25, -1, 90, true, timeout);
	}

	public DriveToPoint(double x, double y, double theta, double minVelocity, double eps, long timeout) {
		this(x, y, theta, minVelocity, 11, 0, eps, -1,90, true, timeout);
	}

	public DriveToPoint(double x, double y, double theta, double minVelocity, double maxVelocity, double eps,
			long timeout) {
		this(x, y, theta, minVelocity, maxVelocity, 0, eps, -1,90, true, timeout);
	}
	
	public DriveToPoint(double x, double y, double theta, double minVelocity, double maxVelocity, double eps,
			double turnRate, boolean slowTurn, long timeout) {
		this(x, y, theta, minVelocity, maxVelocity, 0, eps, turnRate,90, slowTurn, timeout);
	}


	public DriveToPoint(double x, double y, double theta, double minVelocity, double maxVelocity, double eps,
			double turnRate,double maxTurn, boolean slowTurn, long timeout) {
		this(x, y, theta, minVelocity, maxVelocity, 0, eps, turnRate,maxTurn, slowTurn, timeout);
	}

	public DriveToPoint(double x, double y, double theta, double minVelocity, double maxVelocity, double rampRate,
			double eps, double turnRate, double maxTurn,boolean slowTurn, long timeout) {
		super(RobotComponent.DRIVE, timeout);
		this.x = x;
		this.y = y;
		this.theta = theta;
		this.minVelocity = minVelocity;
		this.maxVelocity = maxVelocity;
		this.eps = eps;
		this.slowTurn = slowTurn;
		this.rampRate = rampRate;
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
		this.robotOut.setDriveClosedLoopRamp(rampRate, 20);
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

		double yError = error.getY();
		double yOutput;

	
		yOutput = this.straightPID.calcPIDError(yError);


		double distanceFromTargetHeading = Math.abs(this.turnPID.getDesiredVal() - this.sensorIn.getAngle());
		if (distanceFromTargetHeading > 90) { // prevents the y output from being reversed in the next calculation
			distanceFromTargetHeading = 90;
		}
		
		if (slowTurn) { // slow down y if we aren't facing the correct angle
			yOutput = yOutput * (((-1 * distanceFromTargetHeading) / 90.0) + 1); 
		}
		
		
		double xOutput = -this.turnPID.calcPID(this.sensorIn.getAngle());

		if(!this.slowTurn) {
			xOutput *= 0.85;
		}
		double leftOut = SimLib.calcLeftTankDrive(xOutput, yOutput);
		double rightOut = SimLib.calcRightTankDrive(xOutput, yOutput);

		this.robotOut.setDriveLeftVelocity(leftOut);
		this.robotOut.setDriveRightVelocity(rightOut);

		

		double dist = (yError);
		if (Math.abs(dist) < this.eps) {
			System.out.println("I have reached the epsilon!");
		}
		boolean returning = Math.abs(dist) < this.eps;
		if (returning && this.minVelocity <= 0.5) {
			override();
		}

		return returning;

	}

	@Override
	public void override() {

		this.robotOut.setDriveLeft(0);
		this.robotOut.setDriveRight(0);

	}

}
