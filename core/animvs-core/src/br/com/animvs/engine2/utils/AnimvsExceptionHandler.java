package br.com.animvs.engine2.utils;

import java.lang.Thread.UncaughtExceptionHandler;

import br.com.animvs.engine2.saturn.InstallManager;

public final class AnimvsExceptionHandler implements UncaughtExceptionHandler {
	private UncaughtExceptionHandler mDefaultExceptionHandler;
	private ExceptionSender sender;
	private InstallManager install;

	public AnimvsExceptionHandler(UncaughtExceptionHandler pDefaultExceptionHandler, ExceptionSender sender, InstallManager install) {
		mDefaultExceptionHandler = pDefaultExceptionHandler;
	}

	public void uncaughtException(Thread t, Throwable e) {
		sender.sendException(install, t, e);
		
		mDefaultExceptionHandler.uncaughtException(t, e);

		// cleanup, don't know if really required
//		Thread.currentThread().getThreadGroup().destroy();
		t.getThreadGroup().destroy();
	}
}
