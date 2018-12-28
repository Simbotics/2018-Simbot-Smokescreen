package org.simbotics.frc2018.auton.drive;

import org.simbotics.frc2018.RobotConstants;
import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.io.RobotOutput;
import org.simbotics.frc2018.io.SensorInput;
import org.simbotics.frc2018.util.SimLib;
import org.simbotics.frc2018.util.SimMotionProfile;
import org.simbotics.frc2018.util.SimPID;

public class DriveStraight extends AutonCommand {

	private SensorInput sensorIn;
	private RobotOutput robotOut;
	private SimMotionProfile driveProfile;
	private SimPID gyroPID;
	private double targetDistance;
	private double angle;
	private double endingVel;
	private double posEps;
	private double velEps;

	public DriveStraight(double targetDistance, double angle, long timeoutLength) {
		this(targetDistance, angle, -1, 0, timeoutLength);
	}

	public DriveStraight(double targetDistance, double angle, double maxVel, long timeoutLength) {
		this(targetDistance, angle, maxVel, 0, -1, -1, 0.1, 0.1, timeoutLength);
	}

	public DriveStraight(double targetDistance, double angle, double posEps, double velEps, long timeoutLength) {
		this(targetDistance, angle, -1, 0, -1, -1, posEps, velEps, timeoutLength);
	}

	public DriveStraight(double targetDistance, double angle, double posEps, double velEps, double maxVel,
			long timeoutLength) {
		this(targetDistance, angle, maxVel, 0, -1, -1, posEps, velEps, timeoutLength);
	}

	public DriveStraight(double targetDistance, double angle, double maxVel, double endingVel, double maxAccel,
			double maxDecel, double posEps, double velEps, long timeoutLength) {
		super(RobotComponent.DRIVE, timeoutLength);
		this.targetDistance = targetDistance;
		this.posEps = posEps;
		this.velEps = velEps;
		this.robotOut = RobotOutput.getInstance();
		this.sensorIn = SensorInput.getInstance();
		this.angle = angle;
		this.endingVel = endingVel;
		this.driveProfile = new SimMotionProfile(RobotConstants.getDriveProfile());
		this.driveProfile.setDebug(true);
		this.gyroPID = new SimPID(RobotConstants.getGyroPID());

		double configuredMaxVelocity;
		double configuredMaxAccel;
		double configuredMaxDecel;

		if (maxVel != RobotConstants.getDriveProfileTrajectory().maxVelocity) {
			if (maxVel == -1) { // do default
				configuredMaxVelocity = RobotConstants.getDriveProfileTrajectory().maxVelocity;
			} else {
				configuredMaxVelocity = maxVel;
			}
		} else {
			configuredMaxVelocity = RobotConstants.getDriveProfileTrajectory().maxVelocity;
		}

		if (maxAccel != RobotConstants.getDriveProfileTrajectory().maxAcceleration) {
			if (maxAccel == -1) {
				configuredMaxAccel = RobotConstants.getDriveProfileTrajectory().maxAcceleration;
			} else {
				configuredMaxAccel = maxAccel;
			}
		} else {
			configuredMaxAccel = RobotConstants.getDriveProfileTrajectory().maxAcceleration;
		}

		if (maxDecel != RobotConstants.getDriveProfileTrajectory().maxDeceleration) {
			if (maxDecel == -1) {
				configuredMaxDecel = RobotConstants.getDriveProfileTrajectory().maxDeceleration;
			} else {
				configuredMaxDecel = maxDecel;
			}
		} else {
			configuredMaxDecel = RobotConstants.getDriveProfileTrajectory().maxDeceleration;
		}

		this.driveProfile.configureProfile(this.sensorIn.getDeltaTime(), configuredMaxVelocity, configuredMaxAccel,
				configuredMaxDecel);

		this.driveProfile.setPositionEps(this.posEps);
		this.driveProfile.setVelocityEps(this.velEps);

	}

	@Override
	public void firstCycle() {
		this.driveProfile.setDesiredValue(this.sensorIn.getDrivePositionState(), this.sensorIn.getDriveVelocityState(),
				this.sensorIn.getDriveAccelerationState(), this.sensorIn.getDriveFeet() + targetDistance,
				this.endingVel);
		this.gyroPID.setDesiredValue(this.angle);

	}

	@Override
	public boolean calculate() {
		double x = -this.gyroPID.calcPID(this.sensorIn.getGyroAngle());
		double y = this.driveProfile.calculate(this.sensorIn.getDriveFeet(), this.sensorIn.getDriveSpeedFPS());

		if (this.driveProfile.isDone()) {
			this.robotOut.setDriveLeft(0);
			this.robotOut.setDriveRight(0);
			return true;
		} else {
			this.robotOut.setDriveLeft(SimLib.calcLeftTankDrive(x, y));
			this.robotOut.setDriveRight(SimLib.calcRightTankDrive(x, y));
			return false;
		}
	}

	@Override
	public void override() {
		this.robotOut.setDriveLeft(0);
		this.robotOut.setDriveRight(0);
	}

}
