package com.DataLabelingSystem.labelingMechanism;

import com.DataLabelingSystem.model.Instance;
import com.DataLabelingSystem.model.Label;
import com.DataLabelingSystem.model.User;

public interface LabelingMechanism {

    void label(User user, Instance instance, Label[] label);

}
