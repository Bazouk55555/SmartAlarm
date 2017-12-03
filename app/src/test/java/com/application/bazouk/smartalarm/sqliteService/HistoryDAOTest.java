package com.application.bazouk.smartalarm.sqliteService;

import static junit.framework.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import android.support.constraint.BuildConfig;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class HistoryDAOTest {

    private HistoryDAO historyDAO= new HistoryDAO(RuntimeEnvironment.application);

    @Before
    public void setUp() throws Exception {
        historyDAO.open();
    }

    @After
    public void tearDown() throws Exception {
        historyDAO.close();
    }

    @Test
    public void selectTest()
    {
        int numberOfQuestionsSelected = 4;
        List<Question> questionSelected = historyDAO.select(numberOfQuestionsSelected, "Easy");
        assertEquals(numberOfQuestionsSelected,questionSelected.size());
        for(Question question: questionSelected)
        {
            assertEquals("Easy", question.getLevel());
        }
    }
}
