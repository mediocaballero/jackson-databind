package com.fasterxml.jackson.databind.introspect;

import java.lang.annotation.Annotation;
import java.util.*;

import com.fasterxml.jackson.databind.util.Annotations;

/**
 * Simple helper class used to keep track of collection of
 * Jackson Annotations associated with annotatable things
 * (methods, constructors, classes).
 * Note that only Jackson-owned annotations are tracked (for now?).
 */
public final class AnnotationMap implements Annotations
{
    protected Map<Class<?>,Annotation> _annotations;

    /*
    /**********************************************************
    /* Construction
    /**********************************************************
     */
    
    public AnnotationMap() { }

    public AnnotationMap(Map<Class<?>,Annotation> a) {
        _annotations = a;
    }

    public static AnnotationMap of(Class<?> type, Annotation value) {
        Map<Class<?>,Annotation> ann = new HashMap<Class<?>,Annotation>(4);
        ann.put(type, value);
        return new AnnotationMap(ann);
    }

    public static AnnotationMap of(Collection<Annotation> rawAnnotations) {
        Map<Class<?>,Annotation> map = new HashMap<Class<?>,Annotation>(rawAnnotations.size());
        for (Annotation raw : rawAnnotations) {
            map.put(raw.annotationType(), raw);
        }
        return new AnnotationMap(map);
    }

    /*
    /**********************************************************
    /* Annotations impl
    /**********************************************************
     */
    
    @SuppressWarnings("unchecked")
    @Override
    public <A extends Annotation> A get(Class<A> cls)
    {
        if (_annotations == null) {
            return null;
        }
        return (A) _annotations.get(cls);
    }

    @Override
    public boolean has(Class<?> cls)
    {
        if (_annotations == null) {
            return false;
        }
        return _annotations.containsKey(cls);
    }

    /**
     * Helper method that can be used for a "bulk" check to see if at least
     * one of given annotation types is included within this map.
     */
    @Override
    public boolean hasOneOf(Class<? extends Annotation>[] annoClasses) {
        if (_annotations != null) {
            for (int i = 0, end = annoClasses.length; i < end; ++i) {
                if (_annotations.containsKey(annoClasses[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
    /**********************************************************
    /* Other API
    /**********************************************************
     */

    public Iterable<Annotation> annotations() {
        if (_annotations == null || _annotations.size() == 0) {
            return Collections.emptyList();
        }
        return _annotations.values();
    }
    
    public static AnnotationMap merge(AnnotationMap primary, AnnotationMap secondary)
    {
        if (primary == null || primary._annotations == null || primary._annotations.isEmpty()) {
            return secondary;
        }
        if (secondary == null || secondary._annotations == null || secondary._annotations.isEmpty()) {
            return primary;
        }
        HashMap<Class<?>,Annotation> annotations = new HashMap<Class<?>,Annotation>();
        // add secondary ones first
        for (Annotation ann : secondary._annotations.values()) {
            annotations.put(ann.annotationType(), ann);
        }
        // to be overridden by primary ones
        for (Annotation ann : primary._annotations.values()) {
            annotations.put(ann.annotationType(), ann);
        }
        return new AnnotationMap(annotations);
    }
    
    @Override
    public int size() {
        return (_annotations == null) ? 0 : _annotations.size();
    }

    @Override
    public String toString() {
        if (_annotations == null) {
            return "[null]";
        }
        return _annotations.toString();
    }
}
