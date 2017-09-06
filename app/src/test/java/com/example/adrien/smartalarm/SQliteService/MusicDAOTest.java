package com.example.adrien.smartalarm.SQliteService;

import static junit.framework.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import android.support.constraint.BuildConfig;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class MusicDAOTest {

    private MusicDAO musicDAO= new MusicDAO(RuntimeEnvironment.application);

    @Before
    public void setUp() throws Exception {
        musicDAO.open();
    }

    @After
    public void tearDown() throws Exception {
        musicDAO.close();
    }

    @Test
    public void SelectTest()
    {
        int numberOfQuestionsSelected = 4;
        assertEquals(numberOfQuestionsSelected,musicDAO.select(numberOfQuestionsSelected,"Medium").size());
    }
}
