package com.examples.gg;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class Dialog_Jsoup extends DialogPreference {

	public Dialog_Jsoup(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setDialogLayoutResource(R.layout.open_source_preference);

	}

	@Override
	protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
		// builder.setTitle(R.string.pin_changepin_title);
		builder.setPositiveButton(null, null);
		builder.setNegativeButton(null, null);
		super.onPrepareDialogBuilder(builder);
	}

	@Override
	public void onBindDialogView(View view) {
		TextView mLink1 = (TextView) view.findViewById(R.id.open_source_link1);
		mLink1.setText(Html
				.fromHtml("<a href=\"http://jsoup.org/license\">Jsoup</a>"));
		mLink1.setMovementMethod(LinkMovementMethod.getInstance());

		TextView mLink2 = (TextView) view.findViewById(R.id.open_source_link2);
		mLink2.setVisibility(View.GONE);

		TextView license = (TextView) view.findViewById(R.id.apache_license);
		license.setText(Html
				.fromHtml("<h3>The MIT License</h3>"
						+ "<p>Copyright &copy; 2009 - 2013 <a href=\"http://jonathanhedley.com\">Jonathan Hedley</a> (<a href=\"mailto:jonathan@hedley.net\">jonathan@hedley.net</a>)</p> "
						+ "<p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the &quot;Software&quot;), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:</p> "
						+ "<p>The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.</p> "
						+ "<p>THE SOFTWARE IS PROVIDED &quot;AS IS&quot;, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.</p> "));
		license.setMovementMethod(LinkMovementMethod.getInstance());

		super.onBindDialogView(view);
	}

}
