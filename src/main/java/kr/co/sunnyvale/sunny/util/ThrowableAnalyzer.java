package kr.co.sunnyvale.sunny.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ThrowableAnalyzer {

    public static final ThrowableCauseExtractor DEFAULT_EXTRACTOR = new ThrowableCauseExtractor() {
    	public Throwable extractCause(Throwable throwable) {
    		return throwable.getCause();
    	}
    };

    public static final ThrowableCauseExtractor INVOCATIONTARGET_EXTRACTOR
        = new ThrowableCauseExtractor() {
            public Throwable extractCause(Throwable throwable) {
                verifyThrowableHierarchy(throwable, InvocationTargetException.class);
                return ((InvocationTargetException) throwable).getTargetException();
            }
        };

    private static final Comparator<Class<? extends Throwable>> CLASS_HIERARCHY_COMPARATOR =
        new Comparator<Class<? extends Throwable>>() {

        public int compare(Class<? extends Throwable> class1, Class<? extends Throwable> class2) {
            if (class1.isAssignableFrom(class2)) {
                return 1;
            } else if (class2.isAssignableFrom(class1)) {
                return -1;
            } else {
                return class1.getName().compareTo(class2.getName());
            }
        }

    };

    private final Map<Class<? extends Throwable>, ThrowableCauseExtractor> extractorMap;

    public ThrowableAnalyzer() {
        this.extractorMap = new TreeMap<Class<? extends Throwable>, ThrowableCauseExtractor>(CLASS_HIERARCHY_COMPARATOR);
        initExtractorMap();
    }

    protected final void registerExtractor(Class<? extends Throwable> throwableType, ThrowableCauseExtractor extractor) {
        Assert.notNull(extractor, "Invalid extractor: null");
        this.extractorMap.put(throwableType, extractor);
    }

    protected void initExtractorMap() {
        registerExtractor(InvocationTargetException.class, INVOCATIONTARGET_EXTRACTOR);
        registerExtractor(Throwable.class, DEFAULT_EXTRACTOR);
    }

    @SuppressWarnings("unchecked")
    final Class<? extends Throwable>[] getRegisteredTypes() {
        Set<Class<? extends Throwable>> typeList = this.extractorMap.keySet();
        return typeList.toArray(new Class[typeList.size()]);
    }

    public final Throwable[] determineCauseChain(Throwable throwable) {
        if (throwable == null) {
            throw new IllegalArgumentException("Invalid throwable: null");
        }

        List<Throwable> chain = new ArrayList<Throwable>();
        Throwable currentThrowable = throwable;

        while (currentThrowable != null) {
            chain.add(currentThrowable);
            currentThrowable = extractCause(currentThrowable);
        }

        return chain.toArray(new Throwable[chain.size()]);
    }

    private Throwable extractCause(Throwable throwable) {
        for (Map.Entry<Class<? extends Throwable>, ThrowableCauseExtractor> entry : extractorMap.entrySet()) {
            Class<? extends Throwable> throwableType = entry.getKey();
            if (throwableType.isInstance(throwable)) {
                ThrowableCauseExtractor extractor = entry.getValue();
                return extractor.extractCause(throwable);
            }
        }

        return null;
    }

    public final Throwable getFirstThrowableOfType(Class<? extends Throwable> throwableType, Throwable[] chain) {
        if (chain != null) {
            for (Throwable t : chain) {
                if ((t != null) && throwableType.isInstance(t)) {
                    return t;
                }
            }
        }

        return null;
    }

    public static void verifyThrowableHierarchy(Throwable throwable, Class<? extends Throwable> expectedBaseType) {
        if (expectedBaseType == null) {
            return;
        }

        if (throwable == null) {
            throw new IllegalArgumentException("Invalid throwable: null");
        }
        Class<? extends Throwable> throwableType = throwable.getClass();

        if (!expectedBaseType.isAssignableFrom(throwableType)) {
            throw new IllegalArgumentException("Invalid type: '"
                    + throwableType.getName()
                    + "'. Has to be a subclass of '" + expectedBaseType.getName() + "'");
        }
    }
}
