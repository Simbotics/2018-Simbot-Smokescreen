package org.simbotics.frc2018.io;

import org.simbotics.frc2018.RobotConstants;
import org.simbotics.frc2018.auton.AutonModeSet;
import org.simbotics.frc2018.subsystems.Lift;
import org.simbotics.frc2018.util.DriveControl;
import org.simbotics.frc2018.util.PIDConstants;
import org.simbotics.frc2018.util.ProfileConstants;
import org.simbotics.frc2018.util.TrajectoryConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dashboard {
	private static Dashboard instance;
	private DriverInput driverIn;
	private Lift lift;
	private RobotOutput robotOut;

	private boolean manual = false;

	private Dashboard() {

		this.driverIn = DriverInput.getInstance();
		this.lift = Lift.getInstance();
		this.robotOut = RobotOutput.getInstance();

		SmartDashboard.putNumber("Wrist G", RobotConstants.INTAKE_GRAVITY_OFFSET);
		SmartDashboard.putNumber("Wrist G Cube", RobotConstants.INTAKE_CUBE_GRAVITY_OFFSET);
		SmartDashboard.putNumber("Path Turn P", RobotConstants.PATH_TURN_P);
		SmartDashboard.putNumber("ELEVATOR RAMP RATE", RobotConstants.ELEVATOR_RAMP_RATE);

	}

	public static Dashboard getInstance() {
		if (instance == null) {
			instance = new Dashboard();
		}
		return instance;
	}

	public void updateAll() {
		updateSensorDisplay();
	}

	
	public void updateSensorDisplay() {
		SensorInput sensorInput = SensorInput.getInstance();
		DriveControl driveCont = DriveControl.getInstance();
		RobotOutput robotOut = RobotOutput.getInstance();
		SmartDashboard.putNumber("123_Gyro", sensorInput.getGyroAngle());
		SmartDashboard.putString("123_ALLIANCE", sensorInput.getAllianceColour().toString());
		SmartDashboard.putString("123_MODE: ", sensorInput.getDriverStationMode().toString());
		SmartDashboard.putNumber("123_MATCH TIME: ", sensorInput.getMatchTime());
		SmartDashboard.putNumber("123_X Position: ", sensorInput.getDriveXPos());
		SmartDashboard.putNumber("123_Y Position: ", sensorInput.getDriveYPos());
		SmartDashboard.putNumber("123_Left Encoder: ", sensorInput.getEncoderLeft());
		SmartDashboard.putNumber("123_Right Encoder: ", sensorInput.getEncoderRight());
		SmartDashboard.putNumber("123_Drive Speed FPS: ", sensorInput.getDriveSpeedFPS());
		SmartDashboard.putNumber("123_Elevator Feet", sensorInput.getElevatorHeight());
		SmartDashboard.putNumber("Elevator Enc", sensorInput.getElevatorEnc());
		//SmartDashboard.putNumber("123_ElevatorVelocity", sensorInput.getElevatorVelocity());
		//SmartDashboard.putNumber("123_ElevatorAcceleration", sensorInput.getElevatorAcceleration());
		// SmartDashboard.putNumber("4_Elevator gravity offset: ",
		// RobotConstants.ELEVATOR_GRAVITY_OFFSET);
		SmartDashboard.putNumber("1235_IntakeDegrees", sensorInput.getIntakeDegrees());
		// SmartDashboard.putNumber("5_IntakeEncoder", sensorInput.getIntakePosition());
		SmartDashboard.putBoolean("123_Light Sensor", sensorInput.getIntakeLightSensor());
		SmartDashboard.putBoolean("123_Left Bump", sensorInput.getIntakeLeftBumpSensor());
		SmartDashboard.putBoolean("123_Right Bump", sensorInput.getIntakeRightBumpSensor());
		//SmartDashboard.putString("123_Drive Control Mode: ", driveCont.getMode().toString());
		//SmartDashboard.putNumber("123_Drive Control Mode Key: ", driveCont.getMode().ordinal());
		SmartDashboard.putNumber("456_Wrist_Encoder: ", sensorInput.getIntakePosition());
		SmartDashboard.putNumber("456_Wrist_Encoder Raw: ", sensorInput.getIntakeRawEnc());

		SmartDashboard.putNumber("4_LeftIntakeCurrent", sensorInput.getIntakeLeftCurrent());
		SmartDashboard.putNumber("4_RightIntakeCurrent", sensorInput.getIntakeRightCurrent());

		SmartDashboard.putNumber("5_WristCurrent", sensorInput.getIntakeWristCurrent());
		//SmartDashboard.putNumber("Desired Height" ,this.robotOut.getDesiredElevatorHeight());
		this.lift.pushToDashboard();

	}

	public void updateAutoModes(AutonModeSet set, boolean modified) {
		if (modified) {
			SmartDashboard.putString("1_Auto Set:", "Modified " + set.getName());
		} else {
			SmartDashboard.putString("1_Auto Set:", set.getName());
		}
		SmartDashboard.putString("1_RR Mode", className(set.getRRMode()));
		SmartDashboard.putString("1_RL Mode", className(set.getRLMode()));
		SmartDashboard.putString("1_LR Mode", className(set.getLRMode()));
		SmartDashboard.putString("1_LL Mode", className(set.getLLMode()));
	}

	public void printAutoModes(AutonModeSet set, boolean modified) {
		System.out.println(" ");
		if (modified) {
			System.out.println("1_Auto Set: Modified: " + set.getName());
		} else {
			System.out.println("1_Auto Set: " + set.getName());
		}
		System.out.println("1_RR Mode: " + className(set.getRRMode()));
		System.out.println("1_RL Mode: " + className(set.getRLMode()));
		System.out.println("1_LR Mode: " + className(set.getLRMode()));
		System.out.println("1_LL Mode: " + className(set.getLLMode()));
	}

	public void updateAutoDelay(double delay) {
		SmartDashboard.putNumber("1_Auton Delay: ", delay);
	}

	public void printAutoDelay(double delay) {
		System.out.println("1_Auton Delay: " + delay);
	}

	public double getConstant(String name, double defaultValue) {
		return SmartDashboard.getNumber(name, defaultValue);
	}

	public PIDConstants getPIDConstants(String name, PIDConstants constants) {
		double p = SmartDashboard.getNumber("5_" + name + " - P Value", constants.p);
		double i = SmartDashboard.getNumber("5_" + name + " - I Value", constants.i);
		double d = SmartDashboard.getNumber("5_" + name + " - D Value", constants.d);
		double ff = SmartDashboard.getNumber("5_" + name + " - FF Value", constants.ff);
		double eps = SmartDashboard.getNumber("5_" + name + " - EPS Value", constants.eps);
		return new PIDConstants(p, i, d, ff, eps);
	}

	public void putPIDConstants(String name, PIDConstants constants) {
		SmartDashboard.putNumber("5_" + name + " - P Value", constants.p);
		SmartDashboard.putNumber("5_" + name + " - I Value", constants.i);
		SmartDashboard.putNumber("5_" + name + " - D Value", constants.d);
		SmartDashboard.putNumber("5_" + name + " - FF Value", constants.ff);
		SmartDashboard.putNumber("5_" + name + " - EPS Value", constants.eps);
	}

	public ProfileConstants getProfileConstants(String name, ProfileConstants constants) {
		double p = SmartDashboard.getNumber("5_" + name + " - P Value", constants.p);
		double i = SmartDashboard.getNumber("5_" + name + " - I Value", constants.i);
		double d = SmartDashboard.getNumber("5_" + name + " - D Value", constants.d);
		double vFF = SmartDashboard.getNumber("3_" + name + " - vFF Value", constants.vFF);
		double aFF = SmartDashboard.getNumber("3_" + name + " - aFF Value", constants.aFF);
		double dFF = SmartDashboard.getNumber("3_" + name + " - dFF Value", constants.dFF);
		double gFF = SmartDashboard.getNumber("3_" + name + " - gFF Value", constants.gravityFF);
		double posEps = SmartDashboard.getNumber("3_" + name + " - Pos EPS Value", constants.positionEps);
		double velEps = SmartDashboard.getNumber("3_" + name + " - Vel EPS Value", constants.velocityEps);
		return new ProfileConstants(p, i, d, vFF, aFF, dFF, gFF, posEps, velEps);
	}

	public void putProfileConstants(String name, ProfileConstants constants) {
		SmartDashboard.putNumber("5_" + name + " - P Value", constants.p);
		SmartDashboard.putNumber("5_" + name + " - I Value", constants.i);
		SmartDashboard.putNumber("5_" + name + " - D Value", constants.d);
		SmartDashboard.putNumber("3_" + name + " - vFF Value", constants.vFF);
		SmartDashboard.putNumber("3_" + name + " - aFF Value", constants.aFF);
		SmartDashboard.putNumber("3_" + name + " - dFF Value", constants.dFF);
		SmartDashboard.putNumber("3_" + name + " - gFF Value", constants.gravityFF);
		SmartDashboard.putNumber("3_" + name + " - Pos EPS Value", constants.positionEps);
		SmartDashboard.putNumber("3_" + name + " - Vel EPS Value", constants.velocityEps);
	}

	public TrajectoryConfig getTrajectoryConfig(String name, TrajectoryConfig constants) {
		double maxAccel = SmartDashboard.getNumber("3_" + name + " - Max Accel Value", constants.maxAcceleration);
		double maxDecel = SmartDashboard.getNumber("3_" + name + " - Max Decel Value", constants.maxDeceleration);
		double maxVel = SmartDashboard.getNumber("3_" + name + " - Max Vel Value", constants.maxVelocity);
		return new TrajectoryConfig(maxAccel, maxDecel, maxVel);

	}

	public double getElevatorRampRate() {
		return SmartDashboard.getNumber("ELEVATOR RAMP RATE", RobotConstants.ELEVATOR_RAMP_RATE);
	}

	public double getPathTurnP() {
		return SmartDashboard.getNumber("Path Turn P", RobotConstants.PATH_TURN_P);
	}

	public void putTrajectoryConfig(String name, TrajectoryConfig constants) {
		SmartDashboard.putNumber("3_" + name + " - Max Accel Value", constants.maxAcceleration);
		SmartDashboard.putNumber("3_" + name + " - Max Decel Value", constants.maxDeceleration);
		SmartDashboard.putNumber("3_" + name + " - Max Vel Value", constants.maxVelocity);

	}

	private String className(Object obj) {
		return obj.getClass().getSimpleName();
	}
}
