package com.uauker.apps.transitorio.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.uauker.apps.transitorio.R;

/**
 * 
 * 
 * 
 */
public class TryAgainHelper extends RelativeLayout implements OnClickListener {

	private Context context;

	private OnClickToTryAgain onClickToTryAgain;

	public interface OnClickToTryAgain {
		void tryAgain();
	}

	/*
	 * -------------- Default constructors
	 */
	public TryAgainHelper(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public TryAgainHelper(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		init(context);
	}

	private void init(Context context) {
		Button tryRecoverButton = (Button) this
				.findViewById(R.id.without_internet_button_try_again);

		tryRecoverButton.setOnClickListener(this);
	}

	public void setOnClickToTryAgain(OnClickToTryAgain onClickToTryAgain) {
		this.onClickToTryAgain = onClickToTryAgain;
	}

	@Override
	public void onClick(View v) {
		if (onClickToTryAgain != null) {
			onClickToTryAgain.tryAgain();
		}
	}
}