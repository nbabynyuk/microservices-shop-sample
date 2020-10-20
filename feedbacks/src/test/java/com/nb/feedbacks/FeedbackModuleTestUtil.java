package com.nb.feedbacks;

import com.nb.feedbacks.model.Feedback;

import java.io.IOException;
import java.io.InputStream;

public class FeedbackModuleTestUtil {

    public static final String TEST_PRODUCT_UUID = "da9169fc-65b5-4708-8895-88af85b423e0";
    public static final String TEST_FEEDBACK_UUID = "da9169fc-65b5-4708-8895-88af85b99999";
    public static final String TEST_FEEDBACK_MESSAGE = "feedback #15";

    public static String loadSampleResource(String path) throws IOException {
        InputStream resourceAsStream = FeedbackModuleTestUtil.class.getClassLoader().getResourceAsStream(path);
        assert resourceAsStream != null;
        return new String(resourceAsStream.readAllBytes());
    }

    public static Feedback createDummyFeedback() {
        Feedback feedback = new Feedback();
        feedback.setFeedbackMessage(TEST_FEEDBACK_MESSAGE);
        feedback.setUserId(5L);
        feedback.setUserDisplayName("Test user");
        return feedback;
    }

}
