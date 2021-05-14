package edu.wpi.teamC;

import static org.testfx.api.FxAssert.verifyThat;

import javafx.scene.Node;
import javafx.scene.text.Text;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;

/**
 * This is an integration test for the entire application. Rather than running a single scene
 * individually, it will run the entire application as if you were running it.
 */
@ExtendWith(ApplicationExtension.class)
public class AppTest extends FxRobot {

  /** Setup test suite. */
  @BeforeAll
  public static void setup() throws Exception {
    FxToolkit.registerPrimaryStage();
    FxToolkit.setupApplication(Capp.class);
  }

  @AfterAll
  public static void cleanup() {}

  @Test
  public void testButton() {

    clickOn("InterpreterRequest");


    clickOn("Back");
    verifyThat("edu.wpi.teamC.views.DefaultScreenController", Node::isVisible);



  }
}
