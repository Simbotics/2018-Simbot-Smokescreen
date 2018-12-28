package org.simbotics.frc2018.io;

import org.simbotics.frc2018.RobotConstants;
import org.simbotics.frc2018.util.RobotPoseEstimator;
import org.simbotics.frc2018.util.SimNavx;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SensorInput {

	private static double DRIVE_TICKS_PER_INCH_HIGH = RobotConstants.DRIVE_TICKS_PER_INCH_HIGH;
	// drive output
	// ratio / (PI*D)

	private static double INTAKE_TICKS_PER_DEGREE = (4096.0) / 360.0; // not plugged into talon so its
																		// 1024
	private static double ELEVATOR_TICKS_PER_FOOT = (4096.0) / (Math.PI * 1.273) / 2 * 12;

	private static final double WRIST_DYING_CURRENT = 12;
	private static SensorInput instance;

	private RobotOutput robotOut;

	// private SimCamera simCam;
	private DriverStation driverStation;
	private PowerDistributionPanel pdp;
	private SimNavx navx;
	private DigitalInput intakeLightSensor;
	private DigitalInput intakeLeftBumpSensor;
	private DigitalInput intakeRightBumpSensor;

	private int leftIntakeJamCounter = 0;
	private int rightIntakeJamCounter = 0;
	private int wristDyingCounter = 0;
	private double lastTime = 0.0;
	private double deltaTime = 20.0;
	private double wristDPS;
	private double lastWristPos;

	private boolean firstCycle = true;

	private DriverStationState driverStationMode = DriverStationState.DISABLED;
	private DriverStation.Alliance alliance;
	private double matchTime;

	private long SystemTimeAtAutonStart = 0;
	private long timeSinceAutonStarted = 0;

	private boolean usingNavX = true;

	private double leftDriveSpeedFPS;
	private double rightDriveSpeedFPS;
	private double lastLeftDriveSpeedFPS = 0;
	private double lastRightDriveSpeedFPS = 0;

	private double leftDriveAccelerationFPSSquared;
	private double rightDriveAccelerationFPSSquared;

	private double xPosition = 0;
	private double yPosition = 0;
	private double autoStartAngle = 90;

	private double drivePositionState = 0;
	private double driveAccelerationState = 0;
	private double driveVelocityState = 0;
	private double gyroPositionState = 0;
	private double gyroAngle;
	private double lastGyroAngle;

	private double prevElevatorVelocity = 0;
	private double elevatorAcceleration = 0;

	// private Thread cameraThread;
	// private boolean cameraThreadRunning = false;

	private SensorInput() {

		this.robotOut = RobotOutput.getInstance();
		// this.simCam = new SimCamera();
		// this.cameraThread = new Thread(this.simCam);
		this.pdp = new PowerDistributionPanel();
		this.driverStation = DriverStation.getInstance();
		this.intakeLightSensor = new DigitalInput(2);
		this.intakeLeftBumpSensor = new DigitalInput(0);
		this.intakeRightBumpSensor = new DigitalInput(1);
		this.navx = new SimNavx();

		if (this.usingNavX) {
			this.navx = new SimNavx();
		}

		this.reset();
	}

	public static SensorInput getInstance() {
		if (instance == null) {
			instance = new SensorInput();
		}
		return instance;
	}

	public void reset() {
		this.firstCycle = true;
		this.navx.reset();
		if (this.usingNavX) {
			this.navx.reset();
		}

		this.robotOut.resetDriveEncoders();
		this.robotOut.resetElevatorEnc();

		this.xPosition = 0;
		this.yPosition = 0;
	}

	public void update() {
		
		if (this.lastTime == 0.0) {
			this.deltaTime = 20;
			this.lastTime = System.currentTimeMillis();
		} else {
			this.deltaTime = System.currentTimeMillis() - this.lastTime;
			this.lastTime = System.currentTimeMillis();
		}

		if (this.driverStation.isAutonomous()) {
			this.timeSinceAutonStarted = System.currentTimeMillis() - this.SystemTimeAtAutonStart;
			SmartDashboard.putNumber("12_Time Since Auto Start:", this.timeSinceAutonStarted);
		}

		if (this.firstCycle) {
			this.firstCycle = false;
			if (this.usingNavX) {
				this.lastGyroAngle = this.navx.getAngle() + (this.autoStartAngle - 90);
			} else {
				this.lastGyroAngle = 0.0;
			}
		}

		if (this.usingNavX) {
			this.navx.update();
		}

		this.alliance = this.driverStation.getAlliance();
		this.matchTime = this.driverStation.getMatchTime();

		if (this.driverStation.isDisabled()) {
			this.driverStationMode = DriverStationState.DISABLED;
		} else if (this.driverStation.isAutonomous()) {
			this.driverStationMode = DriverStationState.AUTONOMOUS;
		} else if (this.driverStation.isOperatorControl()) {
			this.driverStationMode = DriverStationState.TELEOP;
		}

		double leftTicksPerCycle = this.getEncoderLeftSpeed() / DRIVE_TICKS_PER_INCH_HIGH;
		double rightTicksPerCycle = this.getEncoderRightSpeed() / DRIVE_TICKS_PER_INCH_HIGH;
		double wristDegreesPerCycle = this.getIntakeDegrees() - this.lastWristPos;

		this.wristDPS = (wristDegreesPerCycle / (this.deltaTime / 1000.0));
		this.lastWristPos = this.getIntakeDegrees();

		this.leftDriveSpeedFPS = (leftTicksPerCycle / 12) * 10;
		this.rightDriveSpeedFPS = (rightTicksPerCycle / 12) * 10;

		this.leftDriveAccelerationFPSSquared = (this.leftDriveSpeedFPS - this.lastLeftDriveSpeedFPS)
				/ (this.deltaTime / 1000.0);
		this.rightDriveAccelerationFPSSquared = (this.rightDriveSpeedFPS - this.lastRightDriveSpeedFPS)
				/ (this.deltaTime / 1000.0);

		this.lastLeftDriveSpeedFPS = this.leftDriveSpeedFPS;
		this.lastRightDriveSpeedFPS = this.rightDriveSpeedFPS;

		if (this.usingNavX) {
			this.gyroAngle = this.navx.getAngle() + (this.autoStartAngle - 90);
		} else {
			this.gyroAngle = 0.0;
		}

		this.drivePositionState = this.getDriveFeet();
		this.driveVelocityState = this.getDriveSpeedFPS();
		this.driveAccelerationState = this.getDriveAcceleration();

		this.gyroPositionState = this.gyroAngle;

		double driveXSpeed = this.driveVelocityState * Math.cos(Math.toRadians(this.gyroPositionState));
		double driveYSpeed = this.driveVelocityState * Math.sin(Math.toRadians(this.gyroPositionState));

		xPosition += driveXSpeed * this.deltaTime / 1000.0;
		yPosition += driveYSpeed * this.deltaTime / 1000.0;

		this.elevatorAcceleration = (this.getElevatorVelocity() - this.prevElevatorVelocity)
				/ (this.deltaTime / 1000.0);
		this.prevElevatorVelocity = this.getElevatorVelocity();

		double jamCurrent = (8.0 / 5.0 * this.pdp.getVoltage()) + (28.0 / 5.0);// (2 * this.pdp.getVoltage()) -5; //
																				// //(V,
																				// I) = (12, 25),(7,17)

		if (this.getIntakeLeftCurrent() > jamCurrent) {
			this.leftIntakeJamCounter++;
		} else {
			this.leftIntakeJamCounter = 0;
		}

		if (this.getIntakeRightCurrent() > jamCurrent) {
			this.rightIntakeJamCounter++;
		} else {
			this.rightIntakeJamCounter = 0;
		}

		if (this.getIntakeWristCurrent() > WRIST_DYING_CURRENT) {
			this.wristDyingCounter++;
		} else {
			this.wristDyingCounter = 0;
		}

	}
	
	public double getMatchTimeLeft() {
		return this.matchTime;
	}

	public DriverStationState getDriverStationMode() {
		return this.driverStationMode;
	}

	public void resetAutonTimer() {
		this.SystemTimeAtAutonStart = System.currentTimeMillis();
	}

	public void setAutoStartAngle(double angle) {
		this.autoStartAngle = angle;
	}

	public double getDriveSpeedFPS() {
		return (this.leftDriveSpeedFPS + this.rightDriveSpeedFPS) / 2.0;
	}

	public double getDriveLeftFPS() {
		return this.leftDriveSpeedFPS;
	}

	public double getDriveRightFPS() {
		return this.rightDriveSpeedFPS;
	}

	public int getEncoderLeftSpeed() {
		return this.robotOut.getDriveLeftSpeed();
	}

	public int getEncoderRightSpeed() {
		return this.robotOut.getDriveRightSpeed();
	}

	public double getDriveFeet() {
		return this.getDriveInches() / 12.0;
	}

	public void setDriveXPos(double x) {
		this.xPosition = x;
	}

	public void setDriveYPos(double y) {
		this.yPosition = y;
	}

	public double getDriveXPos() {
		return this.xPosition;
	}

	public double getDriveYPos() {
		return this.yPosition;
	}

	public double getGyroPositionState() {
		return this.gyroPositionState;
	}

	public double getAngle() {
		return this.gyroAngle;
	}

	public double getDrivePositionState() {
		return this.drivePositionState;
	}

	public double getDriveVelocityState() {
		return this.driveVelocityState;
	}

	public double getDriveAccelerationState() {
		return this.driveAccelerationState;
	}

	public double getDriveAcceleration() {
		return (this.getLeftDriveAcceleration() + this.getRightDriveAcceleration()) / 2.0;
	}

	public double getLeftDriveAcceleration() {
		return this.leftDriveAccelerationFPSSquared;
	}

	public double getRightDriveAcceleration() {
		return this.rightDriveAccelerationFPSSquared;
	}

	public double getDeltaTime() {
		return this.deltaTime;
	}

	public enum DriverStationState {
		AUTONOMOUS, TELEOP, DISABLED,
	}

	public DriverStation.Alliance getAllianceColour() {
		return this.alliance;
	}

	public double getElevatorEnc() {
		return this.robotOut.getElevatorEncoderTicks();
	}

	public double getElevatorHeight() {
		return this.robotOut.getElevatorEncoderTicks() / ELEVATOR_TICKS_PER_FOOT;
	}

	public double getElevatorVelocity() {
		return this.robotOut.getElevatorVelocity() / ELEVATOR_TICKS_PER_FOOT * 10;
	}

	public double getElevatorAcceleration() {
		return this.elevatorAcceleration;
	}

	public double getMatchTime() {
		return this.matchTime;
	}

	public String getFieldConfig() {
		return this.driverStation.getGameSpecificMessage();
	}

	public long getTimeSinceAutoStarted() {
		return this.timeSinceAutonStarted;
	}

	// PDP //

	public double getVoltage() {
		return this.pdp.getVoltage();
	}

	public double getCurrent(int port) {
		return this.pdp.getCurrent(port);
	}

	// NAVX //
	public double getGyroAngle() {
		if (this.usingNavX) {
			return this.gyroAngle;
		} else {
			return 0;
		}

	}

	public int getEncoderRight() {
		return this.robotOut.getDriveRightEnc();
	}

	public int getEncoderLeft() {
		return this.robotOut.getDriveLeftEnc();
	}

	public double getDriveEncoderAverage() {
		return (this.getEncoderRight() + this.getEncoderLeft()) / 2.0;
	}

	public double getDriveInches() {
		return this.getDriveEncoderAverage() / DRIVE_TICKS_PER_INCH_HIGH;
	}

	public double getIntakePosition() {
		double adjustedWristEncVal = this.robotOut.getIntakeWristEnc() + RobotConstants.INTAKE_WRIST_ENCODER_OFFSET;

		if (adjustedWristEncVal > -1024 && adjustedWristEncVal < 2048) {
			return adjustedWristEncVal;
		} else {
			return adjustedWristEncVal - Math.floor((adjustedWristEncVal + 512.0) / 4096) * 4096;
		}
	}

	public double getIntakeRawEnc() {
		return this.robotOut.getIntakeWristEnc();
	}

	public double getIntakeDegrees() {
		return this.getIntakePosition() / INTAKE_TICKS_PER_DEGREE;
	}

	public boolean getWristIsBack() {
		return (this.getIntakeDegrees()) >= 87.5;
	}

	public double getIntakeWristCurrent() {
		return this.robotOut.getIntakeWristCurrent();
	}

	public boolean isWristDying() {
		return this.wristDyingCounter > 4;
	}

	public double getIntakeWristDPS() {
		return this.wristDPS;
	}

	// intake current
	public double getIntakeRightCurrent() {
		return this.pdp.getCurrent(10);
	}

	public double getIntakeLeftCurrent() {
		return this.pdp.getCurrent(11);
	}

	public boolean isLeftIntakeJammed() {
		return this.leftIntakeJamCounter >= 1;
	}

	public boolean isRightIntakeJammed() {
		return this.rightIntakeJamCounter >= 1;
	}

	public boolean getIntakeLightSensor() {
		return !this.intakeLightSensor.get();
	}

	public boolean getIntakeLeftBumpSensor() {
		return !this.intakeLeftBumpSensor.get();
	}

	public boolean getIntakeRightBumpSensor() {
		return !this.intakeRightBumpSensor.get();
	}

	public boolean getIntakeBumpSensors() {
		return !this.intakeLeftBumpSensor.get() && !this.intakeRightBumpSensor.get();
	}
}
