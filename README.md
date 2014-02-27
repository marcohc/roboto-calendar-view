Roboto Calendar View
==============

Android Roboto Calendar View provides an easy and customizable calendar view with native support for the [Roboto](http://developer.android.com/design/style/typography.html) fonts, includes the brand new [Roboto Slab](http://www.google.com/fonts/specimen/Roboto+Slab) fonts.

Sample
------

Application is available on Google Play:

<a href="https://play.google.com/store/apps/details?id=com.disegnator.robotocalendarsample">
  <img alt="Get it on Google Play"
     src="http://www.android.com/images/brand/get_it_on_play_logo_small.png" />
</a>

![screenshot][1]


Compatibility
-------------

This library is compatible from API 3 (Android 1.5).


Usage
-----

Sample layout with RobotoTextView:

``` xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    tools:context=".MainActivity" >

    <com.disegnator.robotocalendar.RobotoCalendarView
        android:id="@+id/robotoCalendarPicker"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" >
    </com.disegnator.robotocalendar.RobotoCalendarView>

</RelativeLayout>
```

Sample activity with RobotoTextView:

```
public class MainActivity extends Activity implements RobotoCalendarListener {

	private RobotoCalendarView robotoCalendarView;
	private Calendar currentCalendar;
	private int currentMonthIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Gets the calendar from the view
		robotoCalendarView = (RobotoCalendarView) findViewById(R.id.robotoCalendarPicker);

		// Set listener, in this case, the same activity
		robotoCalendarView.setRobotoCalendarListener(this);

		// Initialize the RobotoCalendarPicker with the current index and date
		currentMonthIndex = 0;
		currentCalendar = Calendar.getInstance(Locale.getDefault());
		robotoCalendarView.initializeCalendar(currentCalendar);

		// Mark current day
		robotoCalendarView.markDayAsCurrentDay(currentCalendar.getTime());
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

}
```

Override dimens.xml, colors.xml and strings.xml to modify styles.

These fonts are availables:

* roboto_thin
* roboto_thin_italic
* roboto_light
* roboto_light_italic
* roboto_regular
* roboto_italic
* roboto_medium
* roboto_medium_italic
* roboto_bold
* roboto_bold_italic
* roboto_black
* roboto_black_italic
* roboto_condensed_light
* roboto_condensed_light_italic
* roboto_condensed_regular
* roboto_condensed_italic
* roboto_condensed_bold
* roboto_condensed_bold_italic
* roboto_slab_thin
* roboto_slab_light
* roboto_slab_regular
* roboto_slab_bold


Developed By
------------
* Marco Hernaiz Cao - <marcohernaizcao@gmail.com>


License
-------

    Copyright 2014 Marco Hernaiz Cao
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: http://i58.tinypic.com/1zcl0xz.png
