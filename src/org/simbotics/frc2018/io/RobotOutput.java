package org.simbotics.frc2018.io;

import org.simbotics.frc2018.RobotConstants;
import org.simbotics.frc2018.util.PIDConstants;

import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Solenoid;

public class RobotOutput {
	private static RobotOutput instance;

	private double driveRampRate;

	private Solenoid led;
	private Solenoid intakeOpen;
	private Solenoid intakeClose;
	private Solenoid intakePancakeOpen;
	private Solenoid intakePancakeClose;

	private TalonSRX driveLeft1;
	private VictorSPX driveLeft2;
	private VictorSPX driveLeft3;

	private TalonSRX driveRight1;
	private VictorSPX driveRight2;
	private VictorSPX driveRight3;

	private VictorSPX intakeLeft;
	private VictorSPX intakeRight;
	private TalonSRX intakeWrist;

	private TalonSRX elevatorRight1;
	private VictorSPX elevatorLeft1;
	private VictorSPX elevatorRight2;
	private VictorSPX elevatorLeft2;

	private double leftDriveTarget = 0;
	private double rightDriveTarget = 0;
	private double lastDesired =0;

	private RobotOutput() {
		this.driveRampRate = 0;

		this.driveLeft1 = new TalonSRX(0);
		this.driveLeft2 = new VictorSPX(1);
		this.driveLeft3 = new VictorSPX(2);

		this.driveRight1 = new TalonSRX(3);
		this.driveRight2 = new VictorSPX(4);
		this.driveRight3 = new VictorSPX(5);

		this.intakeLeft = new VictorSPX(6);
		this.intakeRight = new VictorSPX(7);

		this.intakeWrist = new TalonSRX(8);

		this.elevatorRight1 = new TalonSRX(9);
		this.elevatorLeft1 = new VictorSPX(10);
		this.elevatorRight2 = new VictorSPX(11);
		this.elevatorLeft2 = new VictorSPX(12);

		this.led = new Solenoid(0);
		this.intakeOpen = new Solenoid(1);
		this.intakeClose = new Solenoid(2);
		this.intakePancakeOpen = new Solenoid(3);
		this.intakePancakeClose = new Solenoid(4);

		this.configureSpeedControllers();
	}

	public static RobotOutput getInstance() {
		if (instance == null) {
			instance = new RobotOutput();
		}
		return instance;

	}

	// Motor Commands

	public void configureSpeedControllers() {

		this.driveRight1.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		this.driveRight2.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		this.driveRight3.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		this.driveLeft1.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		this.driveLeft2.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		this.driveLeft3.setControlFramePeriod(ControlFrame.Control_3_General, 20);

		this.elevatorRight1.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		this.elevatorRight2.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		this.elevatorLeft1.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		this.elevatorLeft2.setControlFramePeriod(ControlFrame.Control_3_General, 20);

		this.intakeWrist.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		this.intakeRight.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		this.intakeLeft.setControlFramePeriod(ControlFrame.Control_3_General, 20);

		this.driveLeft1.setInverted(true);
		this.driveLeft2.setInverted(true);
		this.driveLeft3.setInverted(true);

		this.driveRight1.setInverted(false);
		this.driveRight2.setInverted(false);
		this.driveRight3.setInverted(false);

		this.elevatorRight1.setInverted(false);
		this.elevatorLeft1.setInverted(false);
		this.elevatorRight2.setInverted(true);// practice is false
		this.elevatorLeft2.setInverted(false);// practice is true

		this.intakeWrist.setInverted(true);

		this.intakeLeft.setInverted(true);
		this.intakeRight.setInverted(true);

		this.driveLeft2.follow(this.driveLeft1);
		this.driveLeft3.follow(this.driveLeft1);

		this.driveRight2.follow(this.driveRight1);
		this.driveRight3.follow(this.driveRight1);

		this.elevatorLeft1.follow(this.elevatorRight1);
		this.elevatorRight2.follow(this.elevatorRight1);
		this.elevatorLeft2.follow(this.elevatorRight1);

		this.driveLeft1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		this.driveRight1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

		this.intakeWrist.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);

		this.elevatorRight1.setSensorPhase(true);
		this.intakeWrist.setSensorPhase(false); // comp is false

		this.driveRight1.enableVoltageCompensation(true);
		this.driveLeft1.enableVoltageCompensation(true);
		this.intakeWrist.enableVoltageCompensation(true);
		this.elevatorRight1.enableVoltageCompensation(true);
		this.intakeLeft.enableVoltageCompensation(true);
		this.intakeRight.enableVoltageCompensation(true);

		this.elevatorRight1.configClosedloopRamp(0.05, 10);
		

		this.driveRight1.configClosedloopRamp(0.0, 10);
		this.driveLeft1.configClosedloopRamp(0.0, 10);

	}

	public void setLed(boolean on) {
		this.led.set(on);
	}

	// Drive
	public void setDriveLeft(double output) {
		this.driveLeft1.set(ControlMode.PercentOutput, output);
	}

	public void setDriveRight(double output) {
		this.driveRight1.set(ControlMode.PercentOutput, output);
	}

	public double getDriveLeftTarget() {
		return this.leftDriveTarget;
	}

	public double getDriveRightTarget() {
		return this.rightDriveTarget;
	}

	public double getDriveLeftError() {
		return this.driveLeft1.getClosedLoopError(0);
	}

	public double getDriveRightError() {
		return this.driveRight1.getClosedLoopError(0);
	}

	public double getDriveLeftOutput() {
		return this.driveLeft1.getMotorOutputPercent();
	}

	public double getDriveRightOutput() {
		return this.driveRight1.getMotorOutputPercent();
	}

	public void setDriveLeftVelocity(double output) {
		this.leftDriveTarget = output;
		output = (output * (RobotConstants.DRIVE_TICKS_PER_INCH_HIGH * 12)) / 10; // output in ticks/100ms
		this.driveLeft1.set(ControlMode.Velocity, output);
	}

	public void setDriveRightVelocity(double output) {
		this.rightDriveTarget = output;
		output = (output * (RobotConstants.DRIVE_TICKS_PER_INCH_HIGH * 12)) / 10; // output in ticks/100ms
		this.driveRight1.set(ControlMode.Velocity, output);
	}

	public void resetDriveEncoders() {
		this.driveLeft1.setSelectedSensorPosition(0, 0, 0);
		this.driveRight1.setSelectedSensorPosition(0, 0, 0);
	}

	public int getDriveLeftSpeed() {
		return this.driveLeft1.getSelectedSensorVelocity(0);
	}

	public int getDriveRightSpeed() {
		return this.driveRight1.getSelectedSensorVelocity(0);
	}

	public int getDriveRightEnc() {
		return this.driveRight1.getSelectedSensorPosition(0);
	}

	public int getDriveLeftEnc() {
		return this.driveLeft1.getSelectedSensorPosition(0);
	}

	public void configureDrivePID(PIDConstants constants) {
		this.driveRight1.selectProfileSlot(0, 0);
		this.driveRight1.config_kP(0, constants.p, 20);
		this.driveRight1.config_kI(0, constants.i, 20);
		this.driveRight1.config_kD(0, constants.d, 20);
		this.driveRight1.configAllowableClosedloopError(0, (int) constants.eps, 20);
		this.driveRight1.config_IntegralZone(0, 4096, 20);
		this.driveRight1.config_kF(0, constants.ff, 20);

		this.driveLeft1.selectProfileSlot(0, 0);
		this.driveLeft1.config_kP(0, constants.p, 20);
		this.driveLeft1.config_kI(0, constants.i, 20);
		this.driveLeft1.config_kD(0, constants.d, 20);
		this.driveLeft1.configAllowableClosedloopError(0, (int) constants.eps, 20);
		this.driveLeft1.config_IntegralZone(0, 4096, 20);
		this.driveLeft1.config_kF(0, constants.ff, 20);

	}

	public int getElevatorEncoderTicks() {
		return this.elevatorRight1.getSelectedSensorPosition(0);
	}

	public int getIntakeWristEnc() {
		return this.intakeWrist.getSelectedSensorPosition(0);
	}

	/*
	 * Ticks per 100 ms
	 */

	public void configureUpElevatorPID(PIDConstants constants) {
		this.elevatorRight1.selectProfileSlot(0, 0);
		this.elevatorRight1.config_kP(0, constants.p, 20);
		this.elevatorRight1.config_kI(0, constants.i, 20);
		this.elevatorRight1.config_kD(0, constants.d, 20);
		this.elevatorRight1.configAllowableClosedloopError(0, (int) constants.eps, 20);
		this.elevatorRight1.config_IntegralZone(0, 8192, 20);
		this.elevatorRight1.config_kF(0, 0, 20);
	}

	public void configureDownElevatorPID(PIDConstants constants) {
		this.elevatorRight1.selectProfileSlot(1, 0);
		this.elevatorRight1.config_kP(1, constants.p, 20);
		this.elevatorRight1.config_kI(1, constants.i, 20);
		this.elevatorRight1.config_kD(1, constants.d, 20);
		this.elevatorRight1.configAllowableClosedloopError(1, (int) constants.eps, 20);
		this.elevatorRight1.config_IntegralZone(1, 8192, 20);
		this.elevatorRight1.config_kF(1, 0, 20);
	}

	public void setElevatorHeight(double height) {

		if (this.getElevatorEncoderTicks() / ((4096.0) / (Math.PI * 1.273) / 2 * 12) - height > 0) { // going down
			this.elevatorRight1.selectProfileSlot(0, 0);
			this.configureUpElevatorPID(RobotConstants.getElevatorPIDDown());
			if (height < 0.001) {
				height = 0.001;
			}
			this.elevatorRight1.config_kF(0, 102.4 / (height * (4096.0) / (Math.PI * 1.273) / 2 * 12), 20);
			
		} else { // going up
			this.elevatorRight1.selectProfileSlot(0, 0);
			this.configureUpElevatorPID(RobotConstants.getElevatorPIDUp());
			if (height < 0.001) {
				height = 0.001;
			}
			this.elevatorRight1.config_kF(0, 102.4 / (height * (4096.0) / (Math.PI * 1.273) / 2 * 12), 20);
			
		}
		// System.out.println("Desired Height: " + height);
		// System.out.println("Desired Enc Pos:" + height * (4096.0) / (Math.PI * 1.273)
		// / 2 * 12);
		this.elevatorRight1.set(ControlMode.Position, height * (4096.0) / (Math.PI * 1.273) / 2 * 12);
	}

	public double getDesiredElevatorHeight() {
		return this.elevatorRight1.getClosedLoopTarget(0);
	}

	public double getElevatorVelocity() {
		return this.elevatorRight1.getSelectedSensorVelocity(0);
	}

	public void resetElevatorEnc() {
		this.elevatorRight1.setSelectedSensorPosition(0, 0, 0);
	}

	public void setElevatorRampRate(double secondsToFull) {
		this.elevatorRight1.configClosedloopRamp(secondsToFull, 40);
	}

	public void setIntakeWrist(double speed) {
		this.intakeWrist.set(ControlMode.PercentOutput, speed);
	}

	public double getIntakeWristCurrent() {
		return this.intakeWrist.getOutputCurrent();
	}

	public void setIntakeLeft(double output) {
		this.intakeLeft.set(ControlMode.PercentOutput, output);
	}

	public void setIntakeRight(double output) {
		this.intakeRight.set(ControlMode.PercentOutput, output);
	}

	public void setIntakeOpen(boolean open) {
		this.intakeOpen.set(!open);
		this.intakeClose.set(open);
	}

	public void setIntakePancakeOpen(boolean open) {
		this.intakePancakeOpen.set(!open);
		this.intakePancakeClose.set(open);
	}

	// Elevator
	public void setElevator(double output) {
		if (this.getElevatorEncoderTicks() <= 50 && output < 0) {
			output = 0;
		} else if (this.getElevatorEncoderTicks() >= 6.6 * (4096.0) / (Math.PI * 1.273) / 2 * 12 && output > 0) {
			output = RobotConstants.ELEVATOR_GRAVITY_OFFSET;
		}

		this.elevatorRight1.set(ControlMode.PercentOutput, output);
	}

	public void setElevatorMaxOutput(double maxOutput) {
		//this.elevatorRight1.configClosedLoopPeakOutput(0, maxOutput, 20);
		//this.elevatorRight1.configClosedLoopPeakOutput(1, maxOutput, 20);
	}

	public void setElevatorManual(double output) {
		this.elevatorRight1.set(ControlMode.PercentOutput, output);
	}

	public void setDriveClosedLoopRamp(double rampRateSecondsToFull, int timeoutMS) {
		this.driveRampRate = rampRateSecondsToFull;
		this.driveLeft1.configClosedloopRamp(rampRateSecondsToFull, timeoutMS);
		this.driveRight1.configClosedloopRamp(rampRateSecondsToFull, timeoutMS);
	}

	public void setDriveRampRate(double rampRateSecondsToFull, int timeoutMS) {
		this.driveRampRate = rampRateSecondsToFull;
		this.driveLeft1.configOpenloopRamp(rampRateSecondsToFull, timeoutMS);
		this.driveRight1.configOpenloopRamp(rampRateSecondsToFull, timeoutMS);
	}

	public double getRampRate() {
		return this.driveRampRate;
	}

	public void stopAll() {
		setDriveLeft(0);
		setDriveRight(0);
		setIntakeLeft(0);
		setIntakeRight(0);
		setIntakeWrist(0);
		setElevator(0);
		setLed(false);
		// shut off things here
	}

}
