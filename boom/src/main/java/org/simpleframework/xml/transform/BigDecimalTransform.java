// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import java.math.BigDecimal;

class BigDecimalTransform implements Transform<BigDecimal>
{
    @Override
    public BigDecimal read(final String val) {
        return new BigDecimal(val);
    }
    
    @Override
    public String write(final BigDecimal bigDecimal) {
        return bigDecimal.toString();
    }
}
