/*
 * Copyright (C) 2013 The Android Open Source Project 
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
package com.df.tipcalc;

import java.text.NumberFormat;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/** Calculates tip amount based on the user-selected percentage button or a custom tip percentage
 * A total meal amount is entered by the user. The user can select a percentage button or enter a
 * custom amount. Any time the meal amount or custom tip amount is changed, the app recalculates the
 * tip and total amounts automatically  */

public class TipCalcActivity extends Activity {
	public EditText etMealAmt;
	public EditText etCustomTipAmt;
	public TextView tvTipAmtLabel;
	public TextView tvTotalAmtLabel;
	public View vCurrentTipAmt;
	private double tipPercent = 15;
	private double mealAmount = 0;
	NumberFormat nm = NumberFormat.getCurrencyInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip_calc);
		
		// find the elements
		etMealAmt = (EditText) findViewById(R.id.etMealAmt);
	    etCustomTipAmt = (EditText) findViewById(R.id.etCustomTipAmt);
	    tvTipAmtLabel = (TextView) findViewById(R.id.tvTipAmtLabel);
	    tvTotalAmtLabel = (TextView) findViewById(R.id.tvTotalAmtLabel);
	    
	    // Allows for automatic recalculation based on any change to the meal amount
	    etMealAmt.addTextChangedListener(new TextWatcher() 
	    {
	        @Override
	        public void afterTextChanged(Editable s) 
	        {// Fires right after the text has changed
	            
	        	String strmealAmount = etMealAmt.getText().toString();
	        	mealAmount = Double.parseDouble(strmealAmount);
	        	Toast.makeText(TipCalcActivity.this, "afterTextChanged-meal amt", Toast.LENGTH_SHORT).show();
	        	
	        	if (etCustomTipAmt == null)
	        		{
	        			Log.d("DEBUG", "meal amt changed, calling onDetermineTip!");
	        			onDetermineTip(vCurrentTipAmt);
	        		} else {
	        			Log.d("DEBUG", "meal amt changed, calling onCustomTip!");
	        			onCustomTip(etCustomTipAmt) ;
	        		}
	        }
	        
	        @Override
	        public void onTextChanged(CharSequence s, int start, int before, int count) 
	        {// Fires right as the text is being changed (even supplies the range of text)
	        }

	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count,
	                int after) 
	        {// Fires right before text is changing
	        }
	    });
	    
	 // Allows for automatic recalculation based on any change to the custom tip amount
    etCustomTipAmt.addTextChangedListener(new TextWatcher() 
    {
        @Override
        public void afterTextChanged(Editable s) 
        {// Fires right after the text has changed
        	
        	onCustomTip(etCustomTipAmt);
        }
        
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) 
        {// Fires right as the text is being changed (even supplies the range of text)
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) 
        {// Fires right before text is changing
        }
    });
}
	
	// Will fire when any one of the calculate percentage buttons is clicked
	public void onDetermineTip(View v) 
	{    
		/** Save current selected tip view and it's tag for use when auto-calculating on meal amount change
		 * and re-initialize custom tip  */

		vCurrentTipAmt = v;
		etCustomTipAmt = null;
		
		// Gets the tag (percent amount) associated with the view that was clicked
		tipPercent = Double.parseDouble((String) v.getTag());
		
		calculateAmounts(tipPercent);
		
		Log.d("DEBUG", "onDetermineTip was just called!");
	}
	
	public void onCustomTip(EditText etCustomTipAmt) 
	{// re-initializes the current tip view, since custom tip is now used
		
    	vCurrentTipAmt = null;
    	String strTipAmt = etCustomTipAmt.getText().toString();
    	tipPercent = Double.parseDouble(strTipAmt);
	    Toast.makeText(TipCalcActivity.this, "afterTextChanged-custom tip amt", Toast.LENGTH_SHORT).show();
		calculateAmounts(tipPercent);
	}
	
	public void calculateAmounts(double tipPercent) 
	{
		// Calculate the tip and total amounts

		double tipAmt = mealAmount * (tipPercent / 100);
		tvTipAmtLabel.setText(nm.format(tipAmt));
		
		double totalAmt = mealAmount + tipAmt;
		tvTotalAmtLabel.setText(nm.format(totalAmt));
		
		Log.d("DEBUG", "calculateAmounts was just called!");
	}
}
