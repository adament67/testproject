package com.stylist;

import android.content.Context;
import android.os.PowerManager;

public abstract class WakeLocker {
	
	  private static PowerManager.WakeLock wakeLock;

	    @SuppressWarnings("deprecation")
		public static void acquire(Context context) {
	        if (wakeLock != null) wakeLock.release();
	        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
	                PowerManager.ACQUIRE_CAUSES_WAKEUP |
	                PowerManager.ON_AFTER_RELEASE, "WakeLock");
	        wakeLock.acquire();
	    }

	    public static void release() 
	    {
	        if (wakeLock != null) wakeLock.release(); wakeLock = null;
	    }
	
	
//	private static PowerManager.WakeLock wakeLock;
//	static int stayAwake = 0;
//
//	public static void acquire(Context context) {
//		if (wakeLock.isHeld()) {
//			try {
//				wakeLock.release();
//			} catch (Error e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		try {
//			PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//			wakeLock = pm.newWakeLock(
//					PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,
//					"WakeLock");
//			if (stayAwake == 0) {
//				wakeLock.acquire();
//				stayAwake = 1;
//			} else {
//				stayAwake = 0;
//				wakeLock.release();
//			}
//
//		} catch (Error e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	public static void release() {
//		try {
//			if (wakeLock != null)
//				wakeLock.release();
//			wakeLock = null;
//		} catch (Error e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
