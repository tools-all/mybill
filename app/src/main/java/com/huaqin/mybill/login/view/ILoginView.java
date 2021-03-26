package com.huaqin.mybill.login.view;

public interface ILoginView {
	void onClearText();
	void onLoginResult(Boolean result, int returncode, long userid);
	void onSetProgressBarVisibility(int visibility);
}
