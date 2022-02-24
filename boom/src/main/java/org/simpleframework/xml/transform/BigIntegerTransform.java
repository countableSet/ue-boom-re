// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import java.math.BigInteger;

class BigIntegerTransform implements Transform<BigInteger>
{
    @Override
    public BigInteger read(final String val) {
        return new BigInteger(val);
    }
    
    @Override
    public String write(final BigInteger bigInteger) {
        return bigInteger.toString();
    }
}
