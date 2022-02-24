// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import java.util.Currency;

class CurrencyTransform implements Transform<Currency>
{
    @Override
    public Currency read(final String currencyCode) {
        return Currency.getInstance(currencyCode);
    }
    
    @Override
    public String write(final Currency currency) {
        return currency.toString();
    }
}
