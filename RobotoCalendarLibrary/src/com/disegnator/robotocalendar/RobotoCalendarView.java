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
package com.disegnator.robotocalendar;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.disegnator.robotocalendar.font.RobotoTypefaceManager;
import com.disegnator.robotocalendarlibrary.R;

/**
 * The roboto calendar view
 * 
 * @author Marco Hernaiz Cao
 */
public class RobotoCalendarView extends LinearLayout {

	// ************************************************************************************************************************************************************************
	// * Attributes
	// ************************************************************************************************************************************************************************

	// View
	private Context context;
	private TextView dateTitle;
	private ImageView leftButton;
	private ImageView rightButton;
	private View view;

	// Class
	private RobotoCalendarListener robotoCalendarListener;
	private Calendar currentCalendar;
	private Locale locale;

	// Style
	private int monthTitleColor;
	private int monthTitleFont;
	private int dayOfWeekColor;
	private int dayOfWeekFont;
	private int dayOfMonthColor;
	private int dayOfMonthFont;

	public static final int RED_CIRCLE = R.drawable.red_circle;
	public static final int GREEN_CIRCLE = R.drawable.green_circle;
	public static final int BLUE_CIRCLE = R.drawable.blue_circle;

	// ************************************************************************************************************************************************************************
	// * Initialization methods
	// ************************************************************************************************************************************************************************

	public RobotoCalendarView(Context context) {
		super(context);
		this.context = context;
		onCreateView();
	}

	public RobotoCalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		if (isInEditMode()) {
			return;
		}
		getAttributes(context, attrs);
		onCreateView();
	}

	private void getAttributes(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RobotoCalendarView, 0, 0);
		monthTitleColor = typedArray.getColor(R.styleable.RobotoCalendarView_monthTitleColor, R.color.monthTitleColor);
		monthTitleFont = typedArray.getInt(R.styleable.RobotoCalendarView_monthTitleFont, R.string.monthTitleFont);
		dayOfWeekColor = typedArray.getColor(R.styleable.RobotoCalendarView_dayOfWeekColor, R.color.dayOfWeekColor);
		dayOfWeekFont = typedArray.getInt(R.styleable.RobotoCalendarView_dayOfWeekFont, R.string.dayOfWeekFont);
		dayOfMonthColor = typedArray.getColor(R.styleable.RobotoCalendarView_dayOfMonthColor, R.color.dayOfMonthColor);
		dayOfMonthFont = typedArray.getInt(R.styleable.RobotoCalendarView_dayOfMonthFont, R.string.dayOfMonthFont);
		typedArray.recycle();
	}

	public View onCreateView() {

		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflate.inflate(R.layout.roboto_calendar_picker_layout, this, true);

		findViewsById(view);

		initializeEventListeners();

		initializeComponentBehavior();

		return view;
	}

	private void findViewsById(View view) {
		leftButton = (ImageView) view.findViewById(R.id.leftButton);
		rightButton = (ImageView) view.findViewById(R.id.rightButton);
		dateTitle = (TextView) view.findViewWithTag("dateTitle");
	}

	private void initializeEventListeners() {

		leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (robotoCalendarListener == null) {
					throw new IllegalStateException("You must assing a valid RobotoCalendarListener first!");
				}
				robotoCalendarListener.onLeftButtonClick();
			}
		});

		rightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (robotoCalendarListener == null) {
					throw new IllegalStateException("You must assing a valid RobotoCalendarListener first!");
				}
				robotoCalendarListener.onRightButtonClick();
			}
		});
	}

	private void initializeComponentBehavior() {
		// Initialize calendar for current month
		Locale locale = context.getResources().getConfiguration().locale;
		Calendar currentCalendar = Calendar.getInstance(locale);
		initializeCalendar(currentCalendar);
	}

	// ************************************************************************************************************************************************************************
	// * Private auxiliary methods
	// ************************************************************************************************************************************************************************

	@SuppressLint("DefaultLocale")
	private void initializeTitleLayout() {
		// Apply styles
		String font = getResources().getString(monthTitleFont);
		Typeface robotoTypeface = RobotoTypefaceManager.obtaintTypefaceFromString(context, font);
		int color = getResources().getColor(monthTitleColor);
		dateTitle.setTypeface(robotoTypeface);
		dateTitle.setTextColor(color);

		String dateText = new DateFormatSymbols(locale).getMonths()[currentCalendar.get(Calendar.MONTH)].toString();
		dateText = dateText.substring(0, 1).toUpperCase() + dateText.subSequence(1, dateText.length());
		dateTitle.setText(dateText + " " + currentCalendar.get(Calendar.YEAR));
	}

	@SuppressLint("DefaultLocale")
	private void initializeWeekDaysLayout() {

		// Apply styles
		String font = getResources().getString(dayOfWeekFont);
		Typeface robotoTypeface = RobotoTypefaceManager.obtaintTypefaceFromString(context, font);
		int color = getResources().getColor(dayOfWeekColor);

		TextView dayOfWeek;
		String dayOfTheWeekString;
		String[] weekDaysArray = new DateFormatSymbols(locale).getShortWeekdays();
		for (int i = 1; i < weekDaysArray.length; i++) {
			dayOfWeek = (TextView) view.findViewWithTag("dayOfWeek" + getWeekIndex(i, currentCalendar));
			dayOfTheWeekString = weekDaysArray[i];
			
			// Check it for languages with only one week day lenght
			if(dayOfTheWeekString.length() > 1) {
			    dayOfTheWeekString = dayOfTheWeekString.substring(0, 1).toUpperCase() + dayOfTheWeekString.subSequence(1, 2);
			}
			
			dayOfWeek.setText(dayOfTheWeekString);

			// Apply styles
			dayOfWeek.setTypeface(robotoTypeface);
			dayOfWeek.setTextColor(color);
		}
	}

	private void initializeDaysOfMonthLayout() {

		// Apply styles
		String font = getResources().getString(dayOfMonthFont);
		Typeface robotoTypeface = RobotoTypefaceManager.obtaintTypefaceFromString(context, font);
		int color = getResources().getColor(dayOfMonthColor);
		TextView dayOfMonthText;
		ImageView dayOfMonthImage;
		ViewGroup dayOfMonthContainer;

		for (int i = 1; i < 43; i++) {

			dayOfMonthContainer = (ViewGroup) view.findViewWithTag("dayOfMonthContainer" + i);
			dayOfMonthText = (TextView) view.findViewWithTag("dayOfMonthText" + i);
			dayOfMonthImage = (ImageView) view.findViewWithTag("dayOfMonthImage" + i);

			dayOfMonthText.setVisibility(View.INVISIBLE);
			dayOfMonthImage.setVisibility(View.INVISIBLE);

			// Apply styles
			dayOfMonthText.setTypeface(robotoTypeface);
			dayOfMonthText.setTextColor(color);
			dayOfMonthText.setBackgroundResource(android.R.color.transparent);

			dayOfMonthContainer.setBackgroundResource(android.R.color.transparent);
			dayOfMonthContainer.setOnClickListener(null);
		}
	}

	private void setDaysInCalendar() {
		Calendar auxCalendar = Calendar.getInstance(locale);
		auxCalendar.setTime(currentCalendar.getTime());
		auxCalendar.set(Calendar.DAY_OF_MONTH, 1);
		int firstDayOfMonth = auxCalendar.get(Calendar.DAY_OF_WEEK);
		TextView dayOfMonthText;
		ViewGroup dayOfMonthContainer;

		// Calculate dayOfMonthIndex
		int dayOfMonthIndex = getWeekIndex(firstDayOfMonth, auxCalendar);

		for (int i = 1; i <= auxCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++, dayOfMonthIndex++) {
			dayOfMonthContainer = (ViewGroup) view.findViewWithTag("dayOfMonthContainer" + dayOfMonthIndex);
			dayOfMonthText = (TextView) view.findViewWithTag("dayOfMonthText" + dayOfMonthIndex);
			if (dayOfMonthText == null) {
				break;
			}
			dayOfMonthContainer.setOnClickListener(onDayOfMonthClickListener);
			dayOfMonthText.setVisibility(View.VISIBLE);
			dayOfMonthText.setText(String.valueOf(i));
		}

		// If the last week row has no visible days, hide it or show it in case
		ViewGroup weekRow = (ViewGroup) view.findViewWithTag("weekRow6");
		dayOfMonthText = (TextView) view.findViewWithTag("dayOfMonthText36");
		if (dayOfMonthText.getVisibility() == INVISIBLE) {
			weekRow.setVisibility(GONE);
		} else {
			weekRow.setVisibility(VISIBLE);
		}
	}

	private void clearDayOfMonthContainerBackground() {
		ViewGroup dayOfMonthContainer;
		for (int i = 1; i < 43; i++) {
			dayOfMonthContainer = (ViewGroup) view.findViewWithTag("dayOfMonthContainer" + i);
			dayOfMonthContainer.setBackgroundResource(android.R.color.transparent);
		}
	}

	private ViewGroup getDayOfMonthContainer(Calendar currentCalendar) {
		int monthOffset = getMonthOffset(currentCalendar);
		int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
		ViewGroup dayOfMonthContainer = (ViewGroup) view.findViewWithTag("dayOfMonthContainer" + (currentDay + monthOffset));
		return dayOfMonthContainer;
	}

	private TextView getDayOfMonthText(Calendar currentCalendar) {
		int monthOffset = getMonthOffset(currentCalendar);
		int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
		TextView dayOfMonth = (TextView) view.findViewWithTag("dayOfMonthText" + (currentDay + monthOffset));
		return dayOfMonth;
	}

	private ImageView getDayOfMonthImage(Calendar currentCalendar) {
		int monthOffset = getMonthOffset(currentCalendar);
		int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
		ImageView dayOfMonth = (ImageView) view.findViewWithTag("dayOfMonthImage" + (currentDay + monthOffset));
		return dayOfMonth;
	}

	private int getMonthOffset(Calendar currentCalendar) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentCalendar.getTime());
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int firstDayWeekPosition = calendar.getFirstDayOfWeek();
		int dayPosition = calendar.get(Calendar.DAY_OF_WEEK);

		if (firstDayWeekPosition == 1) {
			return dayPosition - 1;
		} else {

			if (dayPosition == 1) {
				return 6;
			} else {
				return dayPosition - 2;
			}
		}
	}

	private int getWeekIndex(int weekIndex, Calendar currentCalendar) {
		int firstDayWeekPosition = currentCalendar.getFirstDayOfWeek();

		if (firstDayWeekPosition == 1) {
			return weekIndex;
		} else {

			if (weekIndex == 1) {
				return 7;
			} else {
				return weekIndex - 1;
			}
		}
	}

	// ************************************************************************************************************************************************************************
	// * Event handler methods
	// ************************************************************************************************************************************************************************

	private OnClickListener onDayOfMonthClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			// Extract day selected
			ViewGroup dayOfMonthContainer = (ViewGroup) view;
			String tagId = (String) dayOfMonthContainer.getTag();
			tagId = tagId.substring(19, tagId.length());
			TextView dayOfMonthText = (TextView) view.findViewWithTag("dayOfMonthText" + tagId);

			// Fire event
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(currentCalendar.getTime());
			calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dayOfMonthText.getText().toString()));

			if (robotoCalendarListener == null) {
				throw new IllegalStateException("You must assing a valid RobotoCalendarListener first!");
			} else {
				robotoCalendarListener.onDateSelected(calendar.getTime());
			}
		}
	};

	// ************************************************************************************************************************************************************************
	// * Public calendar methods
	// ************************************************************************************************************************************************************************

	public interface RobotoCalendarListener {

		public void onDateSelected(Date date);

		public void onRightButtonClick();

		public void onLeftButtonClick();
	}

	public void setRobotoCalendarListener(RobotoCalendarListener robotoCalendarListener) {
		this.robotoCalendarListener = robotoCalendarListener;
	}

	@SuppressLint("DefaultLocale")
	public void initializeCalendar(Calendar currentCalendar) {

		this.currentCalendar = currentCalendar;
		locale = context.getResources().getConfiguration().locale;

		// Set date title
		initializeTitleLayout();

		// Set weeks days titles
		initializeWeekDaysLayout();

		// Initialize days of the month
		initializeDaysOfMonthLayout();

		// Set days in calendar
		setDaysInCalendar();
	}

	public void markDayAsCurrentDay(Date currentDate) {
		Locale locale = context.getResources().getConfiguration().locale;
		Calendar currentCalendar = Calendar.getInstance(locale);
		currentCalendar.setTime(currentDate);
		TextView dayOfMonth = getDayOfMonthText(currentCalendar);

		Typeface robotoTypeface = RobotoTypefaceManager.obtaintTypefaceFromString(context, getResources().getString(R.string.currentDayOfMonthFont));
		dayOfMonth.setTypeface(robotoTypeface);
		dayOfMonth.setTextColor(context.getResources().getColor(R.color.currentDayOfMonthColor));
	}

	public void markDayAsSelectedDay(Date currentDate) {

		// Clear previous marks
		clearDayOfMonthContainerBackground();

		Locale locale = context.getResources().getConfiguration().locale;
		Calendar currentCalendar = Calendar.getInstance(locale);
		currentCalendar.setTime(currentDate);
		ViewGroup dayOfMonthContainer = getDayOfMonthContainer(currentCalendar);
		dayOfMonthContainer.setBackgroundResource(R.drawable.blue_ring);
	}

	public void markDayWithStyle(int style, Date currentDate) {
		Locale locale = context.getResources().getConfiguration().locale;
		Calendar currentCalendar = Calendar.getInstance(locale);
		currentCalendar.setTime(currentDate);
		ImageView dayOfMonthImage = getDayOfMonthImage(currentCalendar);

		// Draw day with style
		dayOfMonthImage.setVisibility(View.VISIBLE);
		dayOfMonthImage.setImageDrawable(null);
		dayOfMonthImage.setBackgroundResource(style);
	}

}
