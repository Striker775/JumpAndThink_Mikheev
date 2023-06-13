package com.mygdx.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int level = getIntent().getIntExtra("level", 0);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		switch(level) {
			case 1:
				initialize(new JumpAndThink(), config);
				break;
			case 2:
				initialize(new JumpAndThink2(), config);
				break;
			case 3:
				initialize(new JumpAndThink3(), config);
				break;
		}
	}
}
