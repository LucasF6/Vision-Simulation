// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.photonvision.SimVisionSystem;
import org.photonvision.SimVisionTarget;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  Joystick joystick = new Joystick(0);

  Field2d field = new Field2d();
  double x = 0;
  double y = 0;
  double rot = 0;
  Pose2d pose = new Pose2d();

  final String kCamName = "Photon Camera";
  final double kCamDiagFOVDegrees = 170;
  final double kCamPitchDegrees = 0;
  final Transform2d kCameraToRobot = new Transform2d();
  final double kCameraHeightOffGroundMeters = 1;
  final double kMaxLEDRangeMeters = 9000;
  final int kCameraResWidth = 600;
  final int kCameraResHeight = 400;
  final double kMinTargetArea = 2;

  SimVisionSystem visionSystem = new SimVisionSystem(
      kCamName,
      kCamDiagFOVDegrees,
      kCamPitchDegrees,
      kCameraToRobot,
      kCameraHeightOffGroundMeters,
      kMaxLEDRangeMeters,
      kCameraResWidth,
      kCameraResHeight,
      kMinTargetArea
  );

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {

    Pose2d targetPose1 = new Pose2d(new Translation2d(5, 5), new Rotation2d(0));
    Pose2d targetPose2 = new Pose2d(new Translation2d(8, 2), new Rotation2d(Math.PI));

    SimVisionTarget target1 = 
        new SimVisionTarget(
            targetPose1,
            2, // Height
            0.1, // Width of target
            0.1 // Length of target
        );
    SimVisionTarget target2 =
        new SimVisionTarget(
            targetPose2,
            1.5, // Height
            0.1, // Width of target
            0.1 // Length of target
        );

    visionSystem.addSimVisionTarget(target1);
    visionSystem.addSimVisionTarget(target2);

    field.getObject("target1").setPose(targetPose1);
    field.getObject("target2").setPose(targetPose2);

    SmartDashboard.putData("Field", field);
  }

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {
    x += 0.05 * joystick.getX();
    y -= 0.05 * joystick.getY();
    if (joystick.getRawButton(1) && !joystick.getRawButton(2)) rot += 0.03;
    else if (joystick.getRawButton(2) && !joystick.getRawButton(1)) rot -= 0.03;
    pose = new Pose2d(x, y, new Rotation2d(rot));
    field.setRobotPose(pose);
    visionSystem.processFrame(pose);
  }
}
