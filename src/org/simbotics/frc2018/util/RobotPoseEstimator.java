package org.simbotics.frc2018.util;

import org.simbotics.frc2018.RobotConstants;

public class RobotPoseEstimator {

	private RobotPose gyroBasedPose;
	private RobotPose encoderBasedPose;
	private RobotPose gyroEncoderFusedPose;

	private double dL; // feet/cycle
	private double dR; // feet/cycle
	private double dW; // Rads
	private double absAngleRads;
	private double prevAbsAngleRads;

	public RobotPoseEstimator() {
		this.gyroBasedPose = new RobotPose();
		this.encoderBasedPose = new RobotPose();
		this.gyroEncoderFusedPose = new RobotPose();

		this.absAngleRads = Math.PI / 2;
		this.prevAbsAngleRads = Math.PI / 2;
	}

	/**
	 * Updates the different pose estimations using a variety of sensor data.
	 * Inputs: Takes distance traveled in feet by the left and right drive trains,
	 * and the absolute angle from the gyro
	 */
	public void updatePose(double dL, double dR, double absAngleDeg) {
		this.dL = dL;
		this.dR = dR;
		this.absAngleRads = Math.toRadians(absAngleDeg);

		updateGyroBasedPose();
		updateEncoderBasedPose();
		updateGyroEncoderFusedPose();

		this.prevAbsAngleRads = this.absAngleRads;
	}

	private void updateGyroBasedPose() {
		double averageVelocity = (dL + dR) / 2;
		this.gyroBasedPose.x += averageVelocity * Math.cos(absAngleRads);
		this.gyroBasedPose.y += averageVelocity * Math.sin(absAngleRads);
		this.gyroBasedPose.theta = absAngleRads;
	}

	private void updateEncoderBasedPose() {
		double deltaTheta = Math.toRadians((dR - dL) / RobotConstants.DRIVE_WIDTH);
		double p = (RobotConstants.DRIVE_WIDTH * dR) / (dR - dL);
		double r = p - RobotConstants.DRIVE_WIDTH / 2;

		double theta = deltaTheta + this.encoderBasedPose.theta;

		double dx = (this.encoderBasedPose.x + r) * Math.sin(theta) + this.encoderBasedPose.y * Math.cos(theta)
				- r * Math.sin(this.encoderBasedPose.theta);
		double dy = -(this.encoderBasedPose.x + r) * Math.cos(theta) + this.encoderBasedPose.y * Math.sin(theta)
				+ r * Math.cos(this.encoderBasedPose.theta);

		this.encoderBasedPose.x += dx;
		this.encoderBasedPose.y += dy;
		this.encoderBasedPose.theta = theta;
	}

	private void updateGyroEncoderFusedPose() {
		double deltaTheta = Math.toRadians((dR - dL) / RobotConstants.DRIVE_WIDTH);
		double p = (RobotConstants.DRIVE_WIDTH * dR) / (dR - dL);
		double r = p - RobotConstants.DRIVE_WIDTH / 2;

		double theta = deltaTheta + this.gyroEncoderFusedPose.theta;

		double dx = (this.gyroEncoderFusedPose.x + r) * Math.sin(theta) + this.gyroEncoderFusedPose.y * Math.cos(theta)
				- r * Math.sin(this.prevAbsAngleRads);
		double dy = -(this.gyroEncoderFusedPose.x + r) * Math.cos(theta) + this.gyroEncoderFusedPose.y * Math.sin(theta)
				+ r * Math.cos(this.prevAbsAngleRads);

		this.gyroEncoderFusedPose.x += dx;
		this.gyroEncoderFusedPose.y += dy;
		this.gyroEncoderFusedPose.theta = this.absAngleRads;
	}

	public RobotPose getGyroBasedRobotPose() {
		return this.gyroBasedPose;
	}

	public RobotPose getEncoderBasedRobotPose() {
		return this.encoderBasedPose;
	}

	public RobotPose getGyroEncoderFusedRobotPose() {
		return this.gyroEncoderFusedPose;
	}

	public void reset() {
		this.gyroBasedPose = new RobotPose();
		this.encoderBasedPose = new RobotPose();
		this.gyroEncoderFusedPose = new RobotPose();
	}

	public class RobotPose {
		public double x;
		public double y;
		public double theta;

		public RobotPose() {
			this.theta = Math.PI / 2;
		}
	}

}
