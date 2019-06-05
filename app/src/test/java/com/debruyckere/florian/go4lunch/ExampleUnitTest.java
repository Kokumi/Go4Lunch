package com.debruyckere.florian.go4lunch;

import android.content.Context;

import com.debruyckere.florian.go4lunch.Model.Colleague;
import com.debruyckere.florian.go4lunch.Model.Restaurant;
import com.debruyckere.florian.go4lunch.Model.Wish;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    Context mMockContext;

    @Test
    public void ColleagueTest(){
        Colleague test = new Colleague("07","test");

        assertSame(test.getId(),"07");
    }

    @Test
    public void wishTest(){
        Wish test = new Wish(Calendar.getInstance().getTime(),
                new Colleague("07","test"),
                new Restaurant("01","home","12 test","restaurant","notime",0));

        assertSame(test.getRestaurant().getId(),"01");
    }
}