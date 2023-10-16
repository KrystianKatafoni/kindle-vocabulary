package com.katafoni.kindlevocabulary.core.binarydata;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
class BinaryDataProviderFactory {
    private Map<BinaryDataProviderName, BinaryDataProvider> binaryDataProviderStrategies;

    public BinaryDataProviderFactory(Set<BinaryDataProvider> binaryDataProviders) {
        createStrategy(binaryDataProviders);
    }

    public BinaryDataProvider findBinaryDataProvider(BinaryDataProviderName binaryDataProviderName) {
        return binaryDataProviderStrategies.get(binaryDataProviderName);
    }

    private void createStrategy(Set<BinaryDataProvider> binaryDataProviders) {
        binaryDataProviderStrategies = new HashMap<>();
        binaryDataProviders.forEach(
                strategy -> binaryDataProviderStrategies.put(strategy.getBinaryDataProviderName(), strategy));
    }
}
