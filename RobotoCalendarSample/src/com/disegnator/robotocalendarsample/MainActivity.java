/*
 * Copyright (C) 2014 Marco Hernaiz Cao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.disegnator.robotocalendarsample;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.disegnator.robotocalendar.RobotoCalendarView;
import com.disegnator.robotocalendar.RobotoCalendarView.RobotoCalendarListener;

import com.disegnator.robotocalendarsample.R;

/**
 * Sample Activity
 * 
 * @author Marco Hernaiz Cao
 */
public class MainActivity extends Activity implements RobotoCalendarListener {

	private RobotoCalendarView robotoCalendarView;
	private int currentMonthIndex;
	private Calendar currentCalendar;
	private Button markButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Gets the calendar from the view
		robotoCalendarView = (RobotoCalendarView) findViewById(R.id.robotoCalendarPicker);
		markButton = (Button) findViewById(R.id.markButton);
		markButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				markSomeRandomDaysInCalendar();
			}
		});

		// Set listener, in this case, the same activity
		robotoCalendarView.setRobotoCalendarListener(this);

		// Initialize the RobotoCalendarPicker with the current index and date
		currentMonthIndex = 0;
		currentCalendar = Calendar.getInstance(Locale.getDefault());

		// Mark current day
		robotoCalendarView.markDayAsCurrentDay(currentCalendar.getTime());

		// Mark some random days. These days are not fixed, if you change the
		// month they will be cleaned
		markSomeRandomDaysInCalendar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onDateSelected(Date date) {

		// Mark calendar day
		robotoCalendarView.markDayAsSelectedDay(date);

		// Do your own stuff
		// ...
	}

	@Override
	public void onRightButtonClick() {
		currentMonthIndex++;
		updateCalendar();
	}

	@Override
	public void onLeftButtonClick() {
		currentMonthIndex--;
		updateCalendar();
	}

	private void updateCalendar() {
		currentCalendar = Calendar.getInstance(Locale.getDefault());
		currentCalendar.add(Calendar.MONTH, currentMonthIndex);
		robotoCalendarView.initializeCalendar(currentCalendar);
	}

	private void markSomeRandomDaysInCalendar() {
		final Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < 15; i++) {
			final Calendar calendar = Calendar.getInstance(Locale.getDefault());
			calendar.add(Calendar.DAY_OF_MONTH, random.nextInt(20));

			final int style = random.nextInt(3);
			switch (style) {
			case 0:
				robotoCalendarView.markDayWithStyle(RobotoCalendarView.BLUE_CIRCLE, calendar.getTime());
				break;
			case 1:
				robotoCalendarView.markDayWithStyle(RobotoCalendarView.GREEN_CIRCLE, calendar.getTime());
				break;
			case 2:
				robotoCalendarView.markDayWithStyle(RobotoCalendarView.RED_CIRCLE, calendar.getTime());
				break;
			default:
				break;
			}
		}
	}
}
