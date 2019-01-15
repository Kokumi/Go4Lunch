package com.debruyckere.florian.go4lunch;

import android.content.Context;

import com.debruyckere.florian.go4lunch.Model.Colleague;
import com.debruyckere.florian.go4lunch.Model.FireBaseConnector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    Context mMockContext;

    @Test
    public void ColleagueDatabaseTest(){

        FireBaseConnector fBC = new FireBaseConnector(mMockContext);
        ArrayList<Colleague> toTest = fBC.getColleagues();

        assertSame(toTest.get(0).getName(),"Test");
    }
}