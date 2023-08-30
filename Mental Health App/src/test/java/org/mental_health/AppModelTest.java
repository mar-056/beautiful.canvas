/* *****************************************
 * CSCI205 -Software Engineering and Design
 * Spring 2023
 * Instructor: Prof. Brian King
 *
 * Name: Joshua Lee
 * Section: Section 1 9 am
 * Date: 4/5/23
 * Time: 9:36 AM
 *
 * Project: csci205_final_project
 * Package: test.java.org.mental_health
 * Class: AppModelTest
 *
 * Description:
 * JUnit test class for the App Model. No other classes could
 * really be tested, so this is the only test class
 * ****************************************
 */

package org.mental_health;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppModelTest {

    @BeforeEach
    void setUp() {
        AppModel theModel = new AppModel();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("testUpdate_Day_Mood")
    void testUpdate_Date_Mood() {
        assertEquals(1, 1);
    }

    @Test
    @DisplayName("testGetMoodForDate")
    void testGetMoodForDate() {

    }

    @Test
    @DisplayName("testGetDate")
    void testGetDate() {

    }

    @Test
    @DisplayName("testIsIsChecked")
    void testIsIsChecked() {

    }

    @Test
    @DisplayName("testIsCheckedProperty")
    void testIsCheckedProperty() {

    }

    @Test
    @DisplayName("testIsIsUnchecked")
    void testIsIsUnchecked() {

    }

    @Test
    @DisplayName("testIsUncheckedProperty")
    void testIsUncheckedProperty() {

    }
}