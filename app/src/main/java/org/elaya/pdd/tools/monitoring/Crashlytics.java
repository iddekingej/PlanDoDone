package org.elaya.pdd.tools.monitoring;

import androidx.annotation.NonNull;

import com.google.firebase.crashlytics.CustomKeysAndValues;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class Crashlytics {
    static public void recordException(@NonNull Throwable pE)
    {
        FirebaseCrashlytics.getInstance().recordException(pE);

    }

    static public void recordExceptionParam(@NonNull String pKey,@NonNull String pValue,@NonNull Throwable pE)
    {
        CustomKeysAndValues lValues=new CustomKeysAndValues.Builder().putString(pKey,pValue).build();
        FirebaseCrashlytics.getInstance().setCustomKeys(lValues);
        recordException(pE);
    }

    static public void log(@NonNull String pLog){
        FirebaseCrashlytics.getInstance().log(pLog);
    }
}
