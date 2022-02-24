// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.TimeZone;
import java.util.GregorianCalendar;
import java.util.Currency;
import java.util.Locale;
import java.net.URL;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Time;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.io.File;

class PackageMatcher implements Matcher
{
    public PackageMatcher() {
    }
    
    private Transform matchEnum(final Class clazz) {
        final Class superclass = clazz.getSuperclass();
        if (superclass == null) {
            return null;
        }
        EnumTransform enumTransform;
        if (superclass.isEnum()) {
            enumTransform = new EnumTransform(clazz);
        }
        else {
            if (!clazz.isEnum()) {
                return null;
            }
            enumTransform = new EnumTransform(clazz);
        }
        return enumTransform;
        enumTransform = null;
        return enumTransform;
    }
    
    private Transform matchFile(final Class clazz) throws Exception {
        FileTransform fileTransform;
        if (clazz == File.class) {
            fileTransform = new FileTransform();
        }
        else {
            fileTransform = null;
        }
        return fileTransform;
    }
    
    private Transform matchLanguage(final Class clazz) throws Exception {
        Object o;
        if (clazz == Boolean.class) {
            o = new BooleanTransform();
        }
        else if (clazz == Integer.class) {
            o = new IntegerTransform();
        }
        else if (clazz == Long.class) {
            o = new LongTransform();
        }
        else if (clazz == Double.class) {
            o = new DoubleTransform();
        }
        else if (clazz == Float.class) {
            o = new FloatTransform();
        }
        else if (clazz == Short.class) {
            o = new ShortTransform();
        }
        else if (clazz == Byte.class) {
            o = new ByteTransform();
        }
        else if (clazz == Character.class) {
            o = new CharacterTransform();
        }
        else if (clazz == String.class) {
            o = new StringTransform();
        }
        else if (clazz == Class.class) {
            o = new ClassTransform();
        }
        else {
            o = null;
        }
        return (Transform)o;
    }
    
    private Transform matchMath(final Class clazz) throws Exception {
        Object o;
        if (clazz == BigDecimal.class) {
            o = new BigDecimalTransform();
        }
        else if (clazz == BigInteger.class) {
            o = new BigIntegerTransform();
        }
        else {
            o = null;
        }
        return (Transform)o;
    }
    
    private Transform matchSQL(final Class clazz) throws Exception {
        DateTransform dateTransform;
        if (clazz == Time.class) {
            dateTransform = new DateTransform(clazz);
        }
        else if (clazz == Date.class) {
            dateTransform = new DateTransform(clazz);
        }
        else if (clazz == Timestamp.class) {
            dateTransform = new DateTransform(clazz);
        }
        else {
            dateTransform = null;
        }
        return dateTransform;
    }
    
    private Transform matchURL(final Class clazz) throws Exception {
        URLTransform urlTransform;
        if (clazz == URL.class) {
            urlTransform = new URLTransform();
        }
        else {
            urlTransform = null;
        }
        return urlTransform;
    }
    
    private Transform matchUtility(final Class clazz) throws Exception {
        Object o;
        if (clazz == java.util.Date.class) {
            o = new DateTransform(clazz);
        }
        else if (clazz == Locale.class) {
            o = new LocaleTransform();
        }
        else if (clazz == Currency.class) {
            o = new CurrencyTransform();
        }
        else if (clazz == GregorianCalendar.class) {
            o = new GregorianCalendarTransform();
        }
        else if (clazz == TimeZone.class) {
            o = new TimeZoneTransform();
        }
        else if (clazz == AtomicInteger.class) {
            o = new AtomicIntegerTransform();
        }
        else if (clazz == AtomicLong.class) {
            o = new AtomicLongTransform();
        }
        else {
            o = null;
        }
        return (Transform)o;
    }
    
    @Override
    public Transform match(final Class clazz) throws Exception {
        final String name = clazz.getName();
        Transform transform;
        if (name.startsWith("java.lang")) {
            transform = this.matchLanguage(clazz);
        }
        else if (name.startsWith("java.util")) {
            transform = this.matchUtility(clazz);
        }
        else if (name.startsWith("java.net")) {
            transform = this.matchURL(clazz);
        }
        else if (name.startsWith("java.io")) {
            transform = this.matchFile(clazz);
        }
        else if (name.startsWith("java.sql")) {
            transform = this.matchSQL(clazz);
        }
        else if (name.startsWith("java.math")) {
            transform = this.matchMath(clazz);
        }
        else {
            transform = this.matchEnum(clazz);
        }
        return transform;
    }
}
