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
@Config(constants = BuildConfig.class)
public class CinemaDAOTest {

    private CinemaDAO cinemaDAO= new CinemaDAO(RuntimeEnvironment.application);

    @Before
    public void setUp() throws Exception {
        cinemaDAO.open();
    }

    @After
    public void tearDown() throws Exception {
        cinemaDAO.close();
    }

    @Test
    public void SelectTest()
    {
        assertEquals(cinemaDAO.select(4).size(),cinemaDAO.getNumberOfQuestionInFile());
    }
}
