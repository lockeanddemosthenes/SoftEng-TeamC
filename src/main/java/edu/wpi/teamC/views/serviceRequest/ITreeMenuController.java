package edu.wpi.teamC.views.serviceRequest;

import com.jfoenix.controls.JFXTreeView;
import edu.wpi.teamC.repository.NodeDB;

public interface ITreeMenuController {
    public void setLocation(String shortName);
    ITreeMenuController getThis();
}
