package org.simbotics.frc2018;

import org.simbotics.frc2018.io.Dashboard;
import org.simbotics.frc2018.util.PIDConstants;
import org.simbotics.frc2018.util.ProfileConstants;
import org.simbotics.frc2018.util.TrajectoryConfig;

public class RobotConstants {

	public static final double DRIVE_TICKS_PER_INCH_HIGH = (4096.0) / (Math.PI * 4.23979) * (22 / 15.0); // wheel was
																											// 4.25

	public static final double INTAKE_TICKS_PER_DEGREE = (4096.0) / 360.0; // not plugged into talon so its
																			// 1024
	public static final double ELEVATOR_TICKS_PER_FOOT = (4096.0) / (Math.PI * 1.273) / 2 * 12;

	public static final boolean USING_DASHBOARD = true;
	private static Dashboard dashboard = Dashboard.getInstance();
	
	private static ProfileConstants driveProfile = new ProfileConstants(0.45, 0.0, 0.002, 1.0 / 12.0, 0.02, 0.02, 0,
			0.05, 0.1);
	private static TrajectoryConfig driveProfileTrajectory = new TrajectoryConfig(9.5, 9.5, 11.5);

	public static double ELEVATOR_GRAVITY_OFFSET = 0.075;
	public static double INTAKE_GRAVITY_OFFSET = 0.225;
	public static double INTAKE_CUBE_GRAVITY_OFFSET = 0.5;

	private static PIDConstants driveStraightPID = new PIDConstants(3.5, 0.08, 5, 0.25);
	private static PIDConstants driveTurnPID = new PIDConstants(0.15, 0.0015, 0.3, 0.25);
	private static PIDConstants driveElevatorUpPID = new PIDConstants(1.25, 0.029, 0.6, 0.25);
	private static PIDConstants talonVelocityPID = new PIDConstants(0.5, 0.0, 10, 0.16, 100);
	private static PIDConstants elevatorPIDUp = new PIDConstants(0.15, 0.000005, 4.0, 10.0);
	private static PIDConstants elevatorPIDDown = new PIDConstants(0.08, 0.000005, 7.0, 20.0);
	private static PIDConstants gyroPID = new PIDConstants(0.015, 0.0005, 0.08, 0.5);
	private static PIDConstants intakePID = new PIDConstants(0.04, 0.0005, 0.2, 0.1);
	private static PIDConstants intakeCubePID = new PIDConstants(0.06, 0.0005, 0.3, 0.1);

	public static final double PATH_TURN_P = 7;

	// Drive Size Constants (Feet)
	public static final double DRIVE_WIDTH = 34.0 / 12.0;
	public static final double DRIVE_LENGTH = 39.125 / 12.0;

	// Elevator Presets (Feet)
	public static final double ELEVATOR_GROUND_PRESET = 0.0;
	public static final double ELEVATOR_HAS_CUBE_PRESET = 0.25;
	public static final double ELEVATOR_SWITCH_PRESET = 2.26;
	public static final double ELEVATOR_SWITCH_UP_PRESET = 3.0;
	public static final double ELEVATOR_SCALE_PRESET = 5.0;
	public static final double ELEVATOR_SCALE_BETWEEN_PRESET = 6.1;
	public static final double ELEVATOR_SCALE_UP_PRESET = 6.58;
	public static final double ELEVATOR_HANGING_PRESET = 5.0;
	public static final double ELEVATOR_RAMP_RATE = 0.4;

	// Intake Wrist Presets (Degrees)
	public static final double INTAKE_CLIMBING_PRESET = 70;
	public static final double INTAKE_SHOOTING_SCALE_UP = 55;
	public static final double INTAKE_GROUND_PRESET = 0;
	public static final double INTAKE_SHOOTING_PRESET = 45;
	public static final double INTAKE_SHOOTING_GROUND = 65;
	public static final double INTAKE_STARTING_PRESET = 93;
	public static final double INTAKE_STANDBY = 55;
	public static final double INTAKE_STANDBY_TOP = 25;
	public static final double INTAKE_ACCELERATION_FEEDFORWARD = 1.0 / 250.0;
	public static final double INTAKE_WRIST_ENCODER_OFFSET = 58421; // comp was 3490.0 practice is -3038.0
	
	
	public static final int LED_BLINK_MED_ON_CYCLES = 5;
	public static final int LED_BLINK_MED_OFF_CYCLES = 5;
	
	public static final int LED_BLINK_SLOW_ON_CYCLES = 20;
	public static final int LED_BLINK_SLOW_OFF_CYCLES = 20;
	
	public static final int LED_BLINK_FAST_ON_CYCLES = 1;
	public static final int LED_BLINK_FAST_OFF_CYCLES = 1;
	
	public static final int LED_BLINK_HP_CYCLES = 30;
	public static final int LED_BLINK_HAS_CUBE_CYCLES = 6;
	public static final int LED_BLINK_DROPPING_CYCLES = 2;
	public static final int LED_BLINK_DROPPING_SWITCH_CYCLES = 2;
	
	public static final int intakeDelayCycles = 1;

	public static PIDConstants getDriveStraightPID() {
		if (USING_DASHBOARD) {
			return dashboard.getPIDConstants("NEW_DRIVE_PID", driveStraightPID);
		} else {
			return driveStraightPID;
		}
	}

	public static PIDConstants getTalonVelocityPID() {
		if (USING_DASHBOARD) {
			return dashboard.getPIDConstants("TALON_VELOCITY_PID", talonVelocityPID);
		} else {
			return talonVelocityPID;
		}
	}

	public static PIDConstants getDriveTurnPID() {
		if (USING_DASHBOARD) {
			return dashboard.getPIDConstants("NEW_TURN_PID", driveTurnPID);
		} else {
			return driveTurnPID;
		}
	}

	public static ProfileConstants getDriveProfile() {
		if (USING_DASHBOARD) {
			return dashboard.getProfileConstants("DRIVE_Profile", driveProfile);
		} else {
			return driveProfile;
		}
	}

	public static TrajectoryConfig getDriveProfileTrajectory() {
		if (USING_DASHBOARD) {
			return dashboard.getTrajectoryConfig("DRIVE_Profile_Trajectory", driveProfileTrajectory);
		} else {
			return driveProfileTrajectory;
		}
	}

	public static PIDConstants getDriveElevatorUpPID() {
		if (USING_DASHBOARD) {
			return dashboard.getPIDConstants("DRIVE_ELEVATOR_UP", driveElevatorUpPID);
		} else {
			return driveElevatorUpPID;
		}
	}

	public static PIDConstants getGyroPID() {
		if (USING_DASHBOARD) {
			return dashboard.getPIDConstants("DRIVE_GYRO", gyroPID);
		} else {
			return gyroPID;
		}
	}

	public static PIDConstants getElevatorPIDUp() {
		if (USING_DASHBOARD) {
			return dashboard.getPIDConstants("ELEVATOR_UP", elevatorPIDUp);
		} else {
			return elevatorPIDUp;
		}
	}

	public static PIDConstants getElevatorPIDDown() {
		if (USING_DASHBOARD) {
			return dashboard.getPIDConstants("ELEVATOR_DOWN", elevatorPIDDown);
		} else {
			return elevatorPIDDown;
		}
	}

	public static PIDConstants getIntakePID() {
		if (USING_DASHBOARD) {
			return dashboard.getPIDConstants("INTAKE", intakePID);
		} else {
			return intakePID;
		}
	}
	
	public static PIDConstants getIntakeCubePID() {
		if (USING_DASHBOARD) {
			return dashboard.getPIDConstants("INTAKE_CUBE", intakeCubePID);
		} else {
			return intakeCubePID;
		}
	}

	public static void pushValues() {
		//dashboard.putPIDConstants("INTAKE", intakePID);
		//dashboard.putPIDConstants("INTAKE_CUBE", intakeCubePID);
		// dashboard.putPIDConstants("NEW_DRIVE_PID", driveStraightPID);
		// dashboard.putPIDConstants("NEW_TURN_PID", driveTurnPID);
		// dashboard.putPIDConstants("DRIVE_ELEVATOR_UP", driveElevatorUpPID);
		// dashboard.putPIDConstants("TALON_VELOCITY_PID", talonVelocityPID);
		//dashboard.putPIDConstants("ELEVATOR_UP", elevatorPIDUp);
		//dashboard.putPIDConstants("ELEVATOR_DOWN", elevatorPIDDown);
		// dashboard.putPIDConstants("DRIVE_STRAIGHT", driveStraightPID);
		// dashboard.putPIDConstants("DRIVE_TURN", driveTurnPID);

	}
}
